package socketed.common.socket.gem.effect.activatable.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class SelfTarget extends GenericTarget {
	
	public static final String TYPE_NAME = "Self";
	
	public SelfTarget(@Nullable GenericCondition condition) {
		super(condition);
	}
	
	@Override
	public void affectTarget(ActivatableGemEffect effect, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(this.testCondition(playerSource, playerSource)) {
			effect.performEffect(callback, playerSource, playerSource);
		}
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}