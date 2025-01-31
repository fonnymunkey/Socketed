package socketed.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.item.ItemSocketTool;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SocketAddRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private int toolSlot = -1;
    private int recipientSlot = -1;
    private int gemSlot = -1;
    GemInstance gem = null;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        //Prevent this from running multiple times until getCraftingResult is called
        if(toolSlot!=-1 && recipientSlot != -1 && gemSlot != -1) return true;

        List<Integer> occupiedSlots = new ArrayList<>();

        //Quick check if there's more than 3 items in crafting, return false if so
        int itemCounter = 0;
        for(int i = 0; i< inv.getSizeInventory(); i++) {
            if(!inv.getStackInSlot(i).isEmpty()) {
                itemCounter++;
                occupiedSlots.add(i);
            } if(itemCounter > 3)
                return false;
        }

        //Iterate crafting grid, check first for tool and 3 filled slots in total
        for(int i : occupiedSlots) {
            if (inv.getStackInSlot(i).getItem() instanceof ItemSocketTool) {
                if (toolSlot == -1)
                    toolSlot = i;
                else {
                    //More than one socketing tool
                    resetTempValues();
                    return false;
                }
            }
        }
        if(toolSlot == -1){
            resetTempValues();
            return false;
        }

        //Iterate occupied slots, get recipient item and gem
        for(int i : occupiedSlots) {
            ItemStack itemStack = inv.getStackInSlot(i);
            boolean hasSockets = itemStack.hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);

            boolean isGem = false;
            if (gem == null){
                gem = new GemInstance(itemStack);
                isGem = gem.validate();
                if(!isGem)
                    gem = null;
            }

            if (hasSockets && !isGem)
                recipientSlot = i;
            if (!hasSockets && isGem)
                gemSlot = i;
        }
        if(recipientSlot == -1 || gemSlot == -1){
            resetTempValues();
            return false;
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        //Output is copy of item with sockets
        ItemStack returnStack = inv.getStackInSlot(recipientSlot).copy();

        ICapabilitySocketable recipientSockets = returnStack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);

        //Try to add Gem to sockets, return Empty Stack (crafting not possible) if no empty sockets available
        if(!recipientSockets.addGem(gem)) {
            resetTempValues();
            return ItemStack.EMPTY;
        }

        resetTempValues();
        return returnStack;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    /**
     * These temporary search values assume that either getCraftingResult() or getRemainingItems() will always run after matches() if recipe does match.
     * Which should be guaranteed by isDynamic = true, but other mods might change that behavior
     */
    private void resetTempValues(){
        toolSlot = -1;
        recipientSlot = -1;
        gemSlot = -1;
        gem = null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 3;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv)
    {
        resetTempValues();
        return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}