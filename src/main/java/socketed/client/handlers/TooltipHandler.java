package socketed.client.handlers;

import net.minecraft.client.gui.GuiScreen;
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
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.config.JsonConfig;
import socketed.common.socket.gem.GemCombinationInstance;
import socketed.common.socket.gem.GemInstance;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.socket.gem.GemCombinationType;
import socketed.common.socket.gem.GemType;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.util.SocketedUtil;

import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class TooltipHandler {

    private static int toolTipIndex = -1;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(stack.isEmpty()) return;
        
        if(!JsonConfig.hasCompletedLoading()) return;
        
        ICapabilitySocketable sockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        GemType gemType = null;
        if(sockets == null) gemType = GemType.getGemTypeFromItemStack(stack);
        if(sockets == null && gemType == null) return;

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

        //Socketed item tooltip
        if(sockets != null) {
            //Sockets (x/y) Tooltip
            int socketCount = sockets.getSocketCount();
            if(socketCount <= 0) return;
            int gemCount = sockets.getGemCount();
            insertTooltip(tooltips, TextFormatting.BOLD + I18n.format("socketed.tooltip.socket", gemCount, socketCount) + TextFormatting.RESET);

            if(gemCount > 0) {
                if(!GuiScreen.isShiftKeyDown()) {
                    insertTooltip(tooltips, TextFormatting.BOLD + "" + TextFormatting.GOLD + I18n.format("socketed.tooltip.holdshift") + TextFormatting.RESET);
                }
                else {
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
            }
        }
        //Gem tooltip
        else {
            //In socket Tooltip
            insertTooltip(tooltips, TextFormatting.BOLD + I18n.format("socketed.tooltip.gem") + TextFormatting.RESET);
            //Display Name Tooltip
            insertTooltip(tooltips, " " + gemType.getColor() + I18n.format(gemType.getDisplayName()) + " " + I18n.format("socketed.tooltip.tier_" + gemType.getTier()) + TextFormatting.RESET);
            
            if(!GuiScreen.isShiftKeyDown()) {
                insertTooltip(tooltips, TextFormatting.BOLD + "" + TextFormatting.GOLD + I18n.format("socketed.tooltip.holdshift") + TextFormatting.RESET);
            }
            else {
                //Effect Tooltips
                for(GenericGemEffect effect : gemType.getEffects()) {
                    String tooltip = "  " + gemType.getColor() + effect.getTooltipString(false);
                    tooltip += " " + SocketedUtil.getSlotTooltip(effect.getSlotType());
                    insertTooltip(tooltips, tooltip + TextFormatting.RESET);
                }
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