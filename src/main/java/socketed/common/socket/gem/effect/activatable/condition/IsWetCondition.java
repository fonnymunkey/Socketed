package socketed.common.socket.gem.effect.activatable.condition;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.EntityPropertyCondition;

import javax.annotation.Nullable;

public class IsWetCondition extends EntityPropertyCondition {

	public static final String TYPE_NAME = "Is Wet";

	public IsWetCondition(boolean checkForPlayer) {
		super(checkForPlayer);
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return determineAffectedEntity(playerSource,effectTarget).isWet();
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}