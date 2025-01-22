package socketed.client.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.capabilities.CapabilityHasSockets;
import socketed.common.capabilities.GemCombinationInstance;
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilityHasSockets;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.RandomValueRange;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.EnumSlots;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.PotionGemEffect;

import java.util.List;

@Mod.EventBusSubscriber
public class TooltipHandler {
    private static int tooltipIndex = -1;
    private static List<String> tooltips;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        boolean hasSockets = stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null);
        GemType gemType = GemType.getGemTypeFromItemStack(stack);
        boolean isGem = gemType != null;

        if(!hasSockets && !isGem) return;

        tooltips = event.getToolTip();
        for(int i = tooltips.size()-1; i>=0; i--){
            if(tooltips.get(i).equals(TextFormatting.DARK_GRAY + (Item.REGISTRY.getNameForObject(event.getItemStack().getItem())).toString())) {
                tooltipIndex = i;
                break;
            }
        }
        if(tooltipIndex!=-1 && stack.isItemDamaged())
            //Shift one farther up to have durability below socket tooltips
            tooltipIndex--;

        if(hasSockets) {
            ICapabilityHasSockets sockets = stack.getCapability(CapabilityHasSockets.HAS_SOCKETS, null);

            List<EntityEquipmentSlot> slots = CapabilityHasSockets.getSlotsForItemStack(stack);

            //Sockets (x/y) Tooltip
            int socketCount = sockets.getSocketCount();
            int gemCount = sockets.getGemCount();
            putBeforeItemId(TextFormatting.BOLD + I18n.format("socketed.tooltip.socket", gemCount, socketCount, TextFormatting.RESET));

            //Gem Combinations
            for (GemCombinationInstance combination : sockets.getGemCombinations()){
                GemCombinationType combinationType = combination.getGemCombinationType();
                //Display Name Tooltip
                putBeforeItemId(" " + TextFormatting.ITALIC + combinationType.getColor() + I18n.format(combinationType.getDisplayName()) + TextFormatting.RESET);

                //Effect Tooltips
                for (GenericGemEffect effect : combination.getGemEffectsForSlots(slots)) {
                    putBeforeItemId("  " + combinationType.getColor() + getTooltipString(effect, true) + TextFormatting.RESET);
                }
            }
            //Gems
            for (GemInstance gemInstance : sockets.getAllGems(false)) {
                gemType = gemInstance.getGemType();
                //Display Name Tooltip
                putBeforeItemId(" " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

                //Effect Tooltips
                for (GenericGemEffect effect : gemInstance.getGemEffectsForSlots(slots)) {
                    putBeforeItemId("  " + gemType.getColor() + getTooltipString(effect, true) + TextFormatting.RESET);
                }
            }
        } else {
            //In socket Tooltip
            putBeforeItemId(TextFormatting.BOLD + I18n.format("socketed.tooltip.gem", TextFormatting.RESET));
            //Display Name Tooltip
            putBeforeItemId(" " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

            //Effect Tooltips
            for (GenericGemEffect effect : gemType.getEffects()) {
                String tooltip = "  " + gemType.getColor() + getTooltipString(effect,false);
                tooltip += EnumSlots.getSlotTooltip(effect.getEnumSlots());
                putBeforeItemId(tooltip + TextFormatting.RESET);
            }
        }
    }

    private static String getTooltipString(GenericGemEffect entry, boolean onItem) {
        if(entry instanceof AttributeGemEffect) return getAttributeString((AttributeGemEffect)entry, onItem);
        else if(entry instanceof PotionGemEffect) return getPotionString((PotionGemEffect)entry, onItem);
        else return "";
    }

    private static String getRandomRangeString(RandomValueRange range, int operation, String attributeString){
        if(range.getMax()==range.getMin()) {
            double amount = range.getMin() * (operation == 0 ? 1.0D : 100.0D);
            if (amount > 0.0D) return I18n.format("attribute.modifier.plus." + operation, ItemStack.DECIMALFORMAT.format(amount), attributeString);
            else if (amount < 0.0D) return I18n.format("attribute.modifier.take." + operation, ItemStack.DECIMALFORMAT.format(amount), attributeString);
            else return "";
        } else {
            double min = range.getMin() * (operation == 0 ? 1.0D : 100.0D);
            double max = range.getMax() * (operation == 0 ? 1.0D : 100.0D);
            if(min >= 0.0D) return I18n.format("socketed.modifier.plus.plus." + operation, ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), attributeString);
            else if(min < 0.0D && max >=0.0D) return I18n.format("socketed.modifier.take.plus." + operation, ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), attributeString);
            else if(min < 0.0D && max < 0.0D) return I18n.format("socketed.modifier.take.take." + operation, ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), attributeString);
            return "";
        }
    }

    private static String getAttributeString(AttributeGemEffect entry, boolean onItem) {
        if(onItem) {
            AttributeModifier modifier = entry.getModifier();
            double amount = modifier.getAmount() * (modifier.getOperation() == 0 ? 1.0D : 100.0D);
            if (amount > 0.0D)
                return I18n.format("attribute.modifier.plus." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
            else if (amount < 0.0D)
                return I18n.format("attribute.modifier.take." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
            else return "";
        } else
            return getRandomRangeString(entry.getAmountRange(), entry.getOperation(), I18n.format("attribute.name." + entry.getAttribute()));
    }

    private static String getPotionString(PotionGemEffect entry, boolean onItem) {
        Potion potion = entry.getPotion();
        if (potion == null) return "";
        String tooltip = I18n.format("socketed.tooltip.activationtype." + entry.getActivationType().getToolTipKey(), I18n.format(potion.getName()));
        int potionLvl = entry.getAmplifier() + 1;
        if (potionLvl == 1) return tooltip;
        else if (potionLvl > 1 && potionLvl <= 10) return tooltip + " " + I18n.format("enchantment.level." + potionLvl);
        else return tooltip + " " + potionLvl;
    }

    private static void putBeforeItemId(String tooltip) {
        if (tooltipIndex < 0 || tooltipIndex >= tooltips.size())
            tooltips.add(tooltip);
        else {
            tooltips.add(tooltipIndex, tooltip);
            tooltipIndex++;
        }
    }
}