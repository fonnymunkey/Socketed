package socketed.client.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.GemCombinationInstance;
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class TooltipHandler {

    private static int toolTipIndex = -1;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        boolean hasSockets = stack.hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        GemType gemType = GemType.getGemTypeFromItemStack(stack);
        boolean isGem = gemType != null;

        if(!hasSockets && !isGem) return;

        List<String> tooltips = event.getToolTip();
        toolTipIndex = -1;
        ResourceLocation itemLoc = Item.REGISTRY.getNameForObject(event.getItemStack().getItem());
        if(itemLoc != null) {
            String itemName = itemLoc.toString();
            for(int i = tooltips.size() - 1; i >= 0; i--) {
                if(tooltips.get(i).contains(itemName)) {
                    toolTipIndex = i;
                    break;
                }
            }
        }
        if(toolTipIndex > 0 && stack.isItemDamaged()) {
            //Shift one farther up to have durability below socket tooltips
            toolTipIndex--;
        }

        if(hasSockets) {
            ICapabilitySocketable sockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
            if(sockets == null) return;

            //Sockets (x/y) Tooltip
            int socketCount = sockets.getSocketCount();
            int gemCount = sockets.getGemCount();
            if(socketCount <= 0) return;
            insertTooltip(tooltips, TextFormatting.BOLD + I18n.format("socketed.tooltip.socket", gemCount, socketCount, TextFormatting.RESET));

            //Gem Combinations
            for(GemCombinationInstance combination : sockets.getGemCombinations()) {
                GemCombinationType combinationType = combination.getGemCombinationType();
                //Display Name Tooltip
                insertTooltip(tooltips, " " + TextFormatting.ITALIC + combinationType.getColor() + I18n.format(combinationType.getDisplayName()) + TextFormatting.RESET);

                //Effect Tooltips
                for(GenericGemEffect effect : combination.getGemEffectsForStack(stack)) {
                    insertTooltip(tooltips, "  " + combinationType.getColor() + effect.getTooltipString(true) + TextFormatting.RESET);
                }
            }
            //Gems
            for(GemInstance gemInstance : sockets.getAllGems(false)) {
                gemType = gemInstance.getGemType();
                //Display Name Tooltip
                insertTooltip(tooltips, " " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

                //Effect Tooltips
                for(GenericGemEffect effect : gemInstance.getGemEffectsForStack(stack)) {
                    insertTooltip(tooltips, "  " + gemType.getColor() + effect.getTooltipString(true) + TextFormatting.RESET);
                }
            }
        }
        else {
            //In socket Tooltip
            insertTooltip(tooltips, TextFormatting.BOLD + I18n.format("socketed.tooltip.gem", TextFormatting.RESET));
            //Display Name Tooltip
            insertTooltip(tooltips, " " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + TextFormatting.RESET);

            //Effect Tooltips
            for(GenericGemEffect effect : gemType.getEffects()) {
                String tooltip = "  " + gemType.getColor() + effect.getTooltipString(false);
                tooltip += " " + ISlotType.getSlotTooltip(effect.getSlotType());
                insertTooltip(tooltips, tooltip + TextFormatting.RESET);
            }
        }
    }

    private static void insertTooltip(List<String> tooltips, String tooltip) {
        if(toolTipIndex < 0 || toolTipIndex >= tooltips.size()) tooltips.add(tooltip);
        else {
            tooltips.add(toolTipIndex, tooltip);
            toolTipIndex++;
        }
    }
}