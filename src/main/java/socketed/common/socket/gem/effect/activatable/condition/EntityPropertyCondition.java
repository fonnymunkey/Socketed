package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class EntityPropertyCondition extends GenericCondition {
	@SerializedName("Check For Player")
	private Boolean checkForPlayer;

	public EntityPropertyCondition(boolean checkForPlayer) {
		super();
		this.checkForPlayer = checkForPlayer;
	}

	protected EntityLivingBase determineAffectedEntity(EntityPlayer player, EntityLivingBase target){
		return this.checkForPlayer ? player : target;
	}

	/**
	 * Check For Player: default false (check target)
	 */
	@Override
	public boolean validate() {
		if(this.checkForPlayer == null) this.checkForPlayer = false;
		return true;
	}
}