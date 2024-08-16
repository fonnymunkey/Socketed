package socketed.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import socketed.common.config.CustomConfig;
import socketed.common.data.EffectGroup;
import socketed.common.data.RecipientGroup;
import socketed.common.item.ItemSocketTool;
import socketed.common.util.SocketedUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SocketAddRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

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
        return width * height >= 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    //God help me
    @Nullable
    private InputReturnable validInput(InventoryCrafting inv) {
        int toolSlot = -1;
        int recipientSlot = -1;
        int effectSlot = -1;
        List<RecipientGroup> recipients = new ArrayList<>();
        List<EffectGroup> tmpEffects = new ArrayList<>();
        List<Integer> dataSlots = new ArrayList<>();

        for(int i = 0; i < inv.getSizeInventory(); i++) {//Iterate crafting grid, check first for tool
            if(!inv.getStackInSlot(i).isEmpty()) {
                if(inv.getStackInSlot(i).getItem() instanceof ItemSocketTool) {
                    if(toolSlot != -1) return null;
                    toolSlot = i;
                }
                else dataSlots.add(i);
            }
        }
        if(toolSlot == -1 || dataSlots.size() != 2) return null;//Require tool and 2 items

        for(int i : dataSlots) {//Iterate occupied slots, get recipients and effects
            ItemStack itemStack = inv.getStackInSlot(i);
            if(recipientSlot == -1) {
                List<RecipientGroup> tmpRecip = getRecipients(itemStack);
                if(!tmpRecip.isEmpty()) {
                    recipientSlot = i;
                    recipients = tmpRecip;
                    continue;
                }
            }
            if(effectSlot == -1) {
                List<EffectGroup> tmpEffect = getEffects(itemStack);
                if(!tmpEffect.isEmpty()) {
                    effectSlot = i;
                    tmpEffects = tmpEffect;
                    continue;
                }
            }
            return null;
        }
        if(recipients.isEmpty() || tmpEffects.isEmpty()) return null;//Require both recipients and effects to be populated

        ItemStack returnStack = inv.getStackInSlot(recipientSlot).copy();
        if(SocketedUtil.isSocketed(returnStack)) return null;//Effect already socketed

        EffectGroup effect = null;
        for(EffectGroup eff : tmpEffects) {//Iterate possible effects
            List<String> validRecipients = eff.getRecipientEntries();
            for(RecipientGroup recip : recipients) {
                if(validRecipients.contains(recip.getName())) effect = eff;//Check collision between possible effects possible recipients, and current recipients
                if(effect != null) break;//TODO: Either add early warning for single effectgroup per recipient per item, or add ability for multiple
            }
            if(effect != null) break;
        }
        if(effect == null) return null;//Require single valid effect group

        /*
        int maxSlots = 0;
        for(RecipientGroup recip : recipients) {//Get max slot count
            int tmpMax = recip.getMaxSockets();
            if(tmpMax > maxSlots) maxSlots = tmpMax;
        }
        if(maxSlots <= 0) return null;//Require non-zero max slots
         */

        SocketedUtil.setSocketEffectName(returnStack, effect.getName());//Store effect name

        InputReturnable returnable = new InputReturnable(toolSlot, recipientSlot, effectSlot, returnStack);
        return returnable.isValid() ? returnable : null;
    }

    @Nonnull
    private static List<RecipientGroup> getRecipients(ItemStack stack) {
        if(stack.getMaxStackSize() != 1) return Collections.emptyList();//TODO: add some early validation warning for stacksize
        List<RecipientGroup> recipients = new ArrayList<>();
        for(Map.Entry<String, RecipientGroup> entry : CustomConfig.getRecipientData().entrySet()) {
            if(entry.getValue().matches(stack)) {
                recipients.add(entry.getValue());
            }
        }
        return recipients;
    }

    @Nonnull
    private static List<EffectGroup> getEffects(ItemStack stack) {
        List<EffectGroup> effects = new ArrayList<>();
        for(Map.Entry<String, EffectGroup> entry : CustomConfig.getEffectData().entrySet()) {
            if(entry.getValue().matches(stack)) {
                effects.add(entry.getValue());
            }
        }
        return effects;
    }

    private static class InputReturnable {
        int toolSlot;
        int recipientSlot;
        int effectSlot;
        ItemStack stack;

        private InputReturnable(int toolSlot, int recipientSlot, int effectSlot, ItemStack stack) {
            this.toolSlot = toolSlot;
            this.recipientSlot = recipientSlot;
            this.effectSlot = effectSlot;
            this.stack = stack;
        }

        private boolean isValid() {
            return toolSlot != -1 && recipientSlot != -1 && effectSlot != -1 && stack != null && !stack.isEmpty();
        }
    }
}