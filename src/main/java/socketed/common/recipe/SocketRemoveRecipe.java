package socketed.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.instances.GemInstance;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.item.ItemSocketTool;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SocketRemoveRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private int toolSlot = -1;
    private int recipientSlot = -1;

    @Override
    public boolean matches(InventoryCrafting inv, @Nonnull World worldIn) {
        //Prevent this from running multiple times until getCraftingResult is called
        if(toolSlot!=-1 && recipientSlot != -1) return true;

        List<Integer> occupiedSlots = new ArrayList<>();

        //Quick check if there's more than 2 items in crafting, return false if so
        int itemCounter = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                itemCounter++;
                occupiedSlots.add(i);
            }
            if (itemCounter > 2)
                return false;
        }

        for (int i : occupiedSlots) {
            if (inv.getStackInSlot(i).getItem() instanceof ItemSocketTool) {
                if (toolSlot == -1)
                    toolSlot = i;
                else {
                    //More than one SocketTool
                    resetTempValues();
                    return false;
                }
            }
            if (inv.getStackInSlot(i).hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null)) {
                recipientSlot = i;
            }
        }
        if (toolSlot == -1 || recipientSlot == -1) {
            resetTempValues();
            return false;
        }
        if(inv.getStackInSlot(recipientSlot).getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null).getSocketCount()<1){
            resetTempValues();
            return false;
        }
        if(inv.getStackInSlot(recipientSlot).getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null).getGemCount()<1){
            resetTempValues();
            return false;
        }

        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack returnStack = inv.getStackInSlot(recipientSlot).copy();
        ICapabilitySocketable sockets = returnStack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        sockets.removeAllGems();

        resetTempValues();
        return returnStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    /**
     * These temporary search values assume that either getCraftingResult() or getRemainingItems() will always run after matches() if recipe does match.
     * Which should be guaranteed by isDynamic = true, but other mods might change that behavior
     */
    private void resetTempValues() {
        toolSlot = -1;
        recipientSlot = -1;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        ItemStack oldGemmedItem = inv.getStackInSlot(recipientSlot).copy();
        List<GemInstance> gemsInItem = oldGemmedItem.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null).removeAllGems();

        resetTempValues();
        NonNullList<ItemStack> remainingItems = net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
        for (int i = 0; i < remainingItems.size(); i++) {
            if (remainingItems.get(i).isEmpty() && !gemsInItem.isEmpty()) {
                //This will fail to give back more than 8 gems in a normal crafting grid
                ItemStack gemStack = gemsInItem.remove(0).getItemStack();
                remainingItems.set(i, gemStack);
            }
        }
        return remainingItems;
    }
}