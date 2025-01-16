package socketed.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import socketed.common.capabilities.CapabilityHasSockets;
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilityHasSockets;
import socketed.common.data.GemType;
import socketed.common.item.ItemSocketTool;

import javax.annotation.Nonnull;
import java.util.*;

public class SocketAddRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private int toolSlot = -1;
    private int recipientSlot = -1;
    private int gemSlot = -1;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        List<Integer> remainingOccupiedSlots = new ArrayList<>();

        //Prevent this from running multiple times until getCraftingResult is called
        if(toolSlot!=-1 && recipientSlot != -1 && gemSlot != -1) return true;

        //Iterate crafting grid, check first for tool and 3 filled slots in total
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            if(!inv.getStackInSlot(i).isEmpty()) {
                if(inv.getStackInSlot(i).getItem() instanceof ItemSocketTool)
                    if(toolSlot==-1)
                        toolSlot = i;
                    else {
                        //More than one socketing tool
                        resetSlotIndices();
                        return false;
                    }
                else remainingOccupiedSlots.add(i);
            }
        }
        if(toolSlot == -1 || remainingOccupiedSlots.size() != 2){
            resetSlotIndices();
            return false;
        }

        //Iterate occupied slots, get recipient item and gem
        for(int i : remainingOccupiedSlots) {
            ItemStack itemStack = inv.getStackInSlot(i);
            boolean hasSockets = itemStack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null);
            boolean isGem = GemType.getGemTypeFromItemStack(itemStack) != null;
            if (hasSockets && !isGem)
                recipientSlot = i;
            if (!hasSockets && isGem)
                gemSlot = i;
        }
        if(recipientSlot == -1 || gemSlot == -1){
            resetSlotIndices();
            return false;
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        //Output is copy of item with sockets
        ItemStack returnStack = inv.getStackInSlot(recipientSlot).copy();

        ICapabilityHasSockets socketSlots = returnStack.getCapability(CapabilityHasSockets.HAS_SOCKETS,null);
        ItemStack gemStack = inv.getStackInSlot(gemSlot).copy();
        gemStack.setCount(1);
        GemInstance gem = new GemInstance(gemStack);

        //Reset search values
        resetSlotIndices();

        //Try to add Gem to sockets, return Empty Stack (crafting not possible) if no empty sockets available
        if(!socketSlots.addGem(gem))
            return ItemStack.EMPTY;
        return returnStack;
    }

    @Override
    public boolean isDynamic()
    {
        //Should guarantee that getCraftingResult() always runs right after matches() if recipe matches
        //But getRemainingItems() also runs after matches, so we have to reset search values there as well
        return true;
    }

    private void resetSlotIndices(){
        toolSlot = -1;
        recipientSlot = -1;
        gemSlot = -1;
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
        resetSlotIndices();
        return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}