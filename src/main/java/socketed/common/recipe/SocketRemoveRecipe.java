package socketed.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import socketed.common.config.CustomConfig;
import socketed.common.data.RecipientGroup;
import socketed.common.item.ItemSocketTool;
import socketed.common.util.SocketedUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SocketRemoveRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return validInput(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        InputReturnable returnable = validInput(inv);
        if(returnable==null) return ItemStack.EMPTY;

        return returnable.stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nullable
    private InputReturnable validInput(InventoryCrafting inv) {
        int toolSlot = -1;
        int recipientSlot = -1;

        for(int i = 0; i < inv.getSizeInventory(); i++) {
            if(!inv.getStackInSlot(i).isEmpty()) {
                if(inv.getStackInSlot(i).getItem() instanceof ItemSocketTool) {
                    if(toolSlot != -1) return null;
                    toolSlot = i;
                }
                else {
                    if(recipientSlot != -1) return null;
                    recipientSlot = i;
                }
            }
        }
        if(toolSlot == -1 || recipientSlot == -1) return null;

        ItemStack stack = inv.getStackInSlot(recipientSlot).copy();
        List<String> recipientKeys = getRecipientKeys(stack);
        if(recipientKeys.isEmpty()) return null;
        /*
        int maxSlots = 0;
        for(String key : recipientKeys) {
            int tmpMax = CustomConfig.getRecipientData().get(key).getMaxSockets();
            if(tmpMax > maxSlots) maxSlots = tmpMax;
        }
        if(maxSlots <= 0) return null;
        NBTTagCompound newTag = new NBTTagCompound();
        newTag.setInteger("MaxSlots", maxSlots);
         */

        if(stack.getTagCompound() == null || !SocketedUtil.isSocketed(stack)) return null;
        SocketedUtil.clearSocket(stack);

        InputReturnable returnable = new InputReturnable(toolSlot, recipientSlot, stack);
        return returnable.isValid() ? returnable : null;
    }

    @Nonnull
    private static List<String> getRecipientKeys(ItemStack stack) {
        if(stack.getMaxStackSize() != 1) return Collections.emptyList();//TODO: add some early validation warning for stacksize
        List<String> recipients = new ArrayList<>();
        for(Map.Entry<String, RecipientGroup> entry : CustomConfig.getRecipientData().entrySet()) {
            if(entry.getValue().matches(stack)) {
                recipients.add(entry.getKey());
            }
        }
        return recipients;
    }

    private static class InputReturnable {
        int toolSlot;
        int recipientSlot;
        ItemStack stack;

        private InputReturnable(int toolSlot, int recipientSlot, ItemStack stack) {
            this.toolSlot = toolSlot;
            this.recipientSlot = recipientSlot;
            this.stack = stack;
        }

        private boolean isValid() {
            return toolSlot != -1 && recipientSlot != -1 && stack != null && !stack.isEmpty();
        }
    }
}