package socketed.client.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.config.CustomConfig;
import socketed.common.data.EffectGroup;
import socketed.common.data.entry.effect.AttributeEntry;
import socketed.common.data.entry.effect.EffectEntry;
import socketed.common.util.SocketedUtil;

@Mod.EventBusSubscriber
public class TooltipHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void ItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(!SocketedUtil.isSocketed(stack)) return;
        String effectName = SocketedUtil.getSocketEffectName(stack);
        EffectGroup group = CustomConfig.getEffectData().get(effectName);
        event.getToolTip().add(TextFormatting.BOLD + I18n.format("socketed.tooltip.socket", TextFormatting.RESET + ((group == null || effectName.isEmpty()) ? I18n.format("socketed.tooltip.empty") : group.getColor() + I18n.format(group.getDisplayName()))));
        if(group == null || effectName.isEmpty()) return;
        for(EffectEntry entry : group.getEffectEntries()) {
            String tooltipString = getTooltipString(entry);
            if(!tooltipString.isEmpty()) event.getToolTip().add("  " + group.getColor() + tooltipString);
        }
    }

    private static String getTooltipString(EffectEntry entry) {
        if(entry instanceof AttributeEntry) return getAttributeString((AttributeEntry)entry);
        //else if(entry instanceof PotionEntry) return getPotionString((PotionEntry)entry);
        else return "";
    }

    private static String getAttributeString(AttributeEntry entry) {
        AttributeModifier modifier = entry.getModifier();
        double amount = modifier.getAmount() * (modifier.getOperation() == 0 ? 1.0D : 100.0D);
        if(amount > 0.0D) return I18n.format("attribute.modifier.plus." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
        else if(amount < 0.0D) return I18n.format("attribute.modifier.take." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + entry.getAttribute()));
        else return "";
    }
/*
    private static String getPotionString(PotionEntry entry) {
        Potion potion = entry.getPotion();
        if(potion == null) return "";
        String tooltip = I18n.format("socketed.tooltip.potiontype." + entry.getActivationType().getKey(), I18n.format(potion.getName()));
        if(entry.getAmplifier() < 10) return tooltip + " " + I18n.format("enchantment.level." + (entry.getAmplifier() + 1));
        else return tooltip + " " + (entry.getAmplifier() + 1);
    }

 */
}