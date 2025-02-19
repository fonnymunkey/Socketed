package socketed.common.socket.gem.effect.activatable.condition;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class IsBossCondition extends GenericCondition {

	public static final String TYPE_NAME = "Is Boss";

	public IsBossCondition() {
		super();
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return !effectTarget.isNonBoss();
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public boolean validate() {
		return true;
	}
}