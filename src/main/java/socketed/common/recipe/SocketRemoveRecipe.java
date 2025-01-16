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
import socketed.common.item.ItemSocketTool;

import javax.annotation.Nonnull;
import java.util.List;

public class SocketRemoveRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private int toolSlot = -1;
    private int recipientSlot = -1;
    List<GemInstance> gemsInItem;

    @Override
    public boolean matches(InventoryCrafting inv, @Nonnull World worldIn) {
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            if(!inv.getStackInSlot(i).isEmpty()) {
                if(inv.getStackInSlot(i).getItem() instanceof ItemSocketTool) {
                    if(toolSlot==-1)
                        toolSlot = i;
                    else{
                        resetSlotIndices();
                        return false;
                    }
                }
                if(inv.getStackInSlot(i).hasCapability(CapabilityHasSockets.HAS_SOCKETS,null))
                    recipientSlot = i;
            }
        }
        if(toolSlot == -1 || recipientSlot == -1){
            resetSlotIndices();
            return false;
        }

        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        resetSlotIndices();
        ItemStack returnStack = inv.getStackInSlot(recipientSlot).copy();
        ICapabilityHasSockets sockets = returnStack.getCapability(CapabilityHasSockets.HAS_SOCKETS,null);
        gemsInItem = sockets.removeAllGems();

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

    private void resetSlotIndices(){
        toolSlot = -1;
        recipientSlot = -1;
    }

    @Override
    public boolean isDynamic()
    {
        //Should guarantee that getCraftingResult() always runs right after matches() if recipe matches
        //But getRemainingItems() also runs after matches, so we have to reset search values there as well
        return true;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv)
    {
        resetSlotIndices();
        NonNullList<ItemStack> remainingItems = net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
        for(int i=0; i<remainingItems.size(); i++){
            if(!remainingItems.get(i).isEmpty())
                //This will fail to give back more than 8 gems in a normal crafting grid
                remainingItems.set(i,gemsInItem.remove(0).getItemStack());
        }
        gemsInItem = null;
        return remainingItems;
    }
}