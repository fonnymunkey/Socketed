package socketed.mixin.firstaid;

import com.llamalad7.mixinextras.sugar.Local;
import ichttt.mods.firstaid.client.ClientEventHandler;
import ichttt.mods.firstaid.common.util.ArmorUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import socketed.api.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.api.common.capabilities.socketable.ICapabilitySocketable;
import socketed.api.util.SocketedUtil;
import socketed.common.socket.gem.effect.AttributeGemEffect;

import java.util.List;

@Mixin(ClientEventHandler.class)
public abstract class ClientEventHandlerMixin {
    @Shadow(remap = false) private static String buildOriginalText(double val, String attribute){ return "";}
    @Shadow(remap = false) private static <T> void replaceOrAppend(List<T> list, T search, T replace){}
    @Shadow(remap = false) private static String makeArmorMsg(double value){ return "";}
    @Shadow(remap = false) private static String makeToughnessMsg(double value){ return "";}

    @Inject(
            method = "tooltipItems",
            at = @At(value = "FIELD", target = "Lichttt/mods/firstaid/common/util/ArmorUtils;QUALITY_TOOLS_PRESENT:Z"),
            remap = false
    )
    private static void socketed_firstAidClientEventHandler_tooltipItems(ItemTooltipEvent event, CallbackInfo ci, @Local ItemStack stack, @Local ItemArmor armor, @Local List<String> tooltip){
        ICapabilitySocketable sockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(sockets == null) return;

        SocketedUtil.filterForEffectType(sockets.getAllPossibleEffects(), AttributeGemEffect.class)
                .filter(effect -> effect.getAttribute().equals(SharedMonsterAttributes.ARMOR.getName()))
                .forEach(effect -> {
                    double amount = effect.getModifier().getAmount();
                    int operation = effect.getOperation();
                    String original = buildOriginalText(amount, "armor");
                    if(operation == 0) amount *= ArmorUtils.getArmorMultiplier(armor.armorType);
                    replaceOrAppend(tooltip, original, makeArmorMsg(amount));
                });

        SocketedUtil.filterForEffectType(sockets.getAllPossibleEffects(), AttributeGemEffect.class)
                .filter(effect -> effect.getAttribute().equals(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName()))
                .forEach(effect -> {
                    double amount = effect.getModifier().getAmount();
                    int operation = effect.getOperation();
                    String original = buildOriginalText(amount, "armorToughness");
                    if(operation == 0) amount *= ArmorUtils.getToughnessMultiplier(armor.armorType);
                    replaceOrAppend(tooltip, original, makeToughnessMsg(amount));
                });
    }
}
