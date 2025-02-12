package socketed.common.socket.gem.effect.activatable.activator.attack;

import com.google.gson.annotations.SerializedName;
import socketed.Socketed;

public abstract class AttackAOEActivator extends AttackActivator {
	
	@SerializedName("AOE Around Self")
	protected final Boolean aoeAroundSelf;
	
	@SerializedName("AOE Around Other")
	protected final Boolean aoeAroundOther;
	
	@SerializedName("Block Range")
	protected final Integer blockRange;
	
	public AttackAOEActivator(boolean affectsSelf, boolean affectsOther, boolean allowsMelee, boolean allowsRanged, boolean aoeAroundSelf, boolean aoeAroundOther, int blockRange) {
		super(affectsSelf, affectsOther, allowsMelee, allowsRanged);
		this.aoeAroundSelf = aoeAroundSelf;
		this.aoeAroundOther = aoeAroundOther;
		this.blockRange = blockRange;
	}
	
	/**
	 * @return if this activator should affect entities around the player source of the effect
	 */
	public boolean getAOEAroundSelf() {
		return this.aoeAroundSelf;
	}
	
	/**
	 * @return if this activator should affect entities around the other entity involved (ex. Attacker/Target)
	 */
	public boolean getAOEAroundOther() {
		return this.aoeAroundOther;
	}
	
	/**
	 * @return the radius in blocks this activator will check for entities to affect
	 */
	public int getBlockRange() {
		return this.blockRange;
	}
	
	/**
	 * AOEAroundSelf: Required
	 * AOEAroundAttacker: Required
	 * BlockRange: Required
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.aoeAroundSelf == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must define if aoe affects around self");
			else if(this.aoeAroundOther == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must define if aoe affects around other entity");
			else if(this.blockRange == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be defined");
			else if(this.blockRange < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be greater than 0");
			else return true;
		}
		return false;
	}
}