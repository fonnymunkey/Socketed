package socketed.common.socket.gem.effect.activatable.condition;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class IsBurningCondition extends EntityPropertyCondition {

	public static final String TYPE_NAME = "Is Burning";

	public IsBurningCondition(boolean checkForPlayer) {
		super(checkForPlayer);
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return determineAffectedEntity(playerSource, effectTarget).isBurning();
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}