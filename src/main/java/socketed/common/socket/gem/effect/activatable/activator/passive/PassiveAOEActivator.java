package socketed.common.socket.gem.effect.activatable.activator.passive;

import com.google.gson.annotations.SerializedName;
import socketed.Socketed;

public abstract class PassiveAOEActivator extends PassiveActivator {
	
	@SerializedName("Block Range")
	protected final Integer blockRange;
	
	@SerializedName("Affects Self")
	protected Boolean affectsSelf;
	
	public PassiveAOEActivator(int activationRate, int blockRange, boolean affectsSelf) {
		super(activationRate);
		this.blockRange = blockRange;
		this.affectsSelf = affectsSelf;
	}
	
	/**
	 * @return the radius in blocks this activator will check for entities to affect
	 */
	public int getBlockRange() {
		return this.blockRange;
	}
	
	/**
	 * @return if this activator should affect the player source of the effect
	 */
	public boolean getAffectsSelf() {
		return this.affectsSelf;
	}
	
	/**
	 * BlockRange: Required
	 * AffectsSelf: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.affectsSelf == null) this.affectsSelf = false;
		
		if(super.validate()) {
			if(this.blockRange == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be defined");
			else if(this.blockRange < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be greater than 0");
			else return true;
		}
		return false;
	}
}