package socketed.common.data.entry.effect.activatable;

import net.minecraft.entity.player.EntityPlayer;

public interface IActivationType {
    default String getKey() { return ""; }
    default void triggerTickEffect(ActivatableEntry entry, EntityPlayer player) { /*noop*/ }
    default void triggerAttackEffect(ActivatableEntry entry, EntityPlayer player) { /*noop*/ }
}