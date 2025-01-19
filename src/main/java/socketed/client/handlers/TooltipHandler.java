package socketed.client.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import socketed.common.data.GemType;
import socketed.common.data.entry.effect.AttributeGemEffect;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.data.entry.effect.activatable.PotionGemEffect;

import java.util.List;

@Mod.EventBusSubscriber
public class TooltipHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void ItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null)) return;
        ICapabilityHasSockets sockets = stack.getCapability(CapabilityHasSockets.HAS_SOCKETS, null);

        List<EntityEquipmentSlot> slots = CapabilityHasSockets.getSlotsForItemStack(stack);

        int socketCount = sockets.getSocketCount();
        int gemCount = sockets.getGemCount();
        event.getToolTip().add(TextFormatting.BOLD + I18n.format("socketed.tooltip.socket", gemCount, socketCount, TextFormatting.RESET));
        for (GemInstance gem : sockets.getAllGems()) {
            GemType gemType = gem.getGemType();
            List<GenericGemEffect> effectsForSlot = gem.getGemEffectsForSlots(slots);
            event.getToolTip().add(" " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);
            for (GenericGemEffect effect : effectsForSlot) {
                event.getToolTip().add("  " + gemType.getColor() + getTooltipString(effect) + TextFormatting.RESET);
            }
        }
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
}