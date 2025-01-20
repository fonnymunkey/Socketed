package socketed.common.jsondata.entry.effect.activatable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public interface IActivationType {
    default void triggerPerTickEffect(ActivatableGemEffect entry, EntityPlayer player) { /*noop*/ }
    default void triggerPerSecondEffect(ActivatableGemEffect actEffect, EntityPlayer player) { /*noop*/ }
    default void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase effectTarget, DamageSource source) { /*noop*/ }

    String getToolTipKey();
}