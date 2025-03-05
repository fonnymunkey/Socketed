package socketed.common.socket.gem.effect.activatable.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;

import javax.annotation.Nullable;

public class OtherTarget extends GenericTarget {
	
	public static final String TYPE_NAME = "Other";
	
	public OtherTarget(@Nullable GenericCondition condition) {
		super(condition);
	}
	
	@Override
	public void affectTarget(ActivatableGemEffect effect, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(this.testCondition(callback, playerSource, effectTarget)) {
			effect.performEffect(callback, playerSource, effectTarget);
		}
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}