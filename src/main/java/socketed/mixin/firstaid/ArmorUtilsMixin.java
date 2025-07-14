package socketed.mixin.firstaid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ichttt.mods.firstaid.common.util.ArmorUtils;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import socketed.Socketed;

import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@Mixin(ArmorUtils.class)
public abstract class ArmorUtilsMixin {
    @Unique private static boolean socketed$includeSocketModifiers = true;

    @Inject(
            method = {"getArmor","getArmorToughness"},
            at = @At("HEAD"),
            remap = false
    )
    private static void socketed_firstAidArmorUtils_getArmor_Toughness(ItemStack stack, EntityEquipmentSlot slot, boolean includeQualityTools, CallbackInfoReturnable<Double> cir){
        socketed$includeSocketModifiers = includeQualityTools;
    }

    @WrapOperation(
            method = "getValueFromAttributes",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;mapToDouble(Ljava/util/function/ToDoubleFunction;)Ljava/util/stream/DoubleStream;"),
            remap = false
    )
    private static DoubleStream socketed_firstAidArmorUtils_getValueFromAttributes(Stream<AttributeModifier> instance, ToDoubleFunction<AttributeModifier> toDoubleFunction, Operation<DoubleStream> original){
        if(!socketed$includeSocketModifiers) {
            socketed$includeSocketModifiers = true;
            return original.call(instance.filter(mod -> !mod.getName().equals(Socketed.MODID + "GemEffect")), toDoubleFunction);
        } else
            return original.call(instance, toDoubleFunction);
    }
}
