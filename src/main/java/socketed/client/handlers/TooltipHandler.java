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
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilityHasSockets;
import socketed.common.config.DefaultCustomConfig;
import socketed.common.data.GemType;
import socketed.common.data.entry.effect.AttributeGemEffect;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.data.entry.effect.activatable.PotionGemEffect;

import java.util.HashSet;
import java.util.List;

@Mod.EventBusSubscriber
public class TooltipHandler {
    private static int tooltipIndex = -1;
    private static List<String> tooltips;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        boolean hasSockets = stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null);
        GemInstance gem = new GemInstance(stack);
        GemType gemType = gem.getGemType();
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

            for (GemInstance gemInstance : sockets.getAllGems()) {
                gemType = gemInstance.getGemType();
                //Display Name Tooltip
                putBeforeItemId(" " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

                //Effect Tooltips
                for (GenericGemEffect effect : gemInstance.getGemEffectsForSlots(slots)) {
                    putBeforeItemId("  " + gemType.getColor() + getTooltipString(effect) + TextFormatting.RESET);
                }
            }
        } else {
            //In socket Tooltip
            putBeforeItemId(TextFormatting.BOLD + I18n.format("socketed.tooltip.gem", TextFormatting.RESET));
            //Display Name Tooltip
            putBeforeItemId(" " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

            //Effect Tooltips
            for (GenericGemEffect effect : gem.getGemEffects()) {
                List<EntityEquipmentSlot> slots = effect.getSlots();

                String tooltip = "  " + gemType.getColor() + getTooltipString(effect);
                tooltip += getSlotTooltip(slots);
                putBeforeItemId(tooltip + TextFormatting.RESET);
            }
        }
    }

    private static String getSlotTooltip(List<EntityEquipmentSlot> slots) {
        HashSet<EntityEquipmentSlot> slotsSet = new HashSet<>(slots);
        if(slotsSet.containsAll(DefaultCustomConfig.allSlots))
            return "";

        String tooltip = "";
        if(slotsSet.containsAll(DefaultCustomConfig.body))
            tooltip = I18n.format("socketed.tooltip.slotsbody");
        else if(slotsSet.containsAll(DefaultCustomConfig.hands))
            tooltip = I18n.format("socketed.tooltip.slotshands");
        else if(slots.size()==1) {
            EntityEquipmentSlot slot = slots.get(0);
            switch(slot){
                case HEAD: tooltip = I18n.format("socketed.tooltip.slotshead"); break;
                case CHEST: tooltip = I18n.format("socketed.tooltip.slotschest"); break;
                case LEGS: tooltip = I18n.format("socketed.tooltip.slotslegs"); break;
                case FEET: tooltip = I18n.format("socketed.tooltip.slotsfeet"); break;
                case MAINHAND: tooltip = I18n.format("socketed.tooltip.slotsmainhand"); break;
                case OFFHAND: tooltip = I18n.format("socketed.tooltip.slotsoffhand"); break;
            }
        } else return "";
        return " ("+tooltip + ")";
    }

    private static String getTooltipString(GenericGemEffect entry) {
        if(entry instanceof AttributeGemEffect) return getAttributeString((AttributeGemEffect)entry);
        else if(entry instanceof PotionGemEffect) return getPotionString((PotionGemEffect)entry);
        else return "";
    }

    private static String getAttributeString(AttributeGemEffect entry) {
        AttributeModifier modifier = entry.getModifier();
        double amount = modifier.getAmount() * (modifier.getOperation() == 0 ? 1.0D : 100.0D);
        if(amount > 0.0D) return I18n.format("attribute.modifier.plus." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
        else if(amount < 0.0D) return I18n.format("attribute.modifier.take." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
        else return "";
    }

    private static String getPotionString(PotionGemEffect entry) {
        Potion potion = entry.getPotion();
        if(potion == null) return "";
        String tooltip = I18n.format("socketed.tooltip.activationtype." + entry.getActivationType().getToolTipKey(), I18n.format(potion.getName()));
        if(entry.getAmplifier() < 10) return tooltip + " " + I18n.format("enchantment.level." + (entry.getAmplifier() + 1));
        else return tooltip + " " + (entry.getAmplifier() + 1);
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