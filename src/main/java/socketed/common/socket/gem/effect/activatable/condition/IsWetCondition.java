package socketed.common.socket.gem.effect.activatable.condition;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class IsWetCondition extends GenericCondition {

	public static final String TYPE_NAME = "Is Wet";

	public IsWetCondition() {
		super();
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return effectTarget.isWet();
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