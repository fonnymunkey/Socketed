package socketed.api.socket.gem.effect.activatable.target;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public abstract class GenericTarget {
	
	public static final String TYPE_FIELD = "Target Type";
	
	@SerializedName(TYPE_FIELD)
	protected final String type = this.getTypeName();
	
	@SerializedName("Condition")
	protected final GenericCondition condition;
	
	protected GenericTarget(@Nullable GenericCondition condition) {
		this.condition = condition;
	}
	
	/**
	 * Determines each specific entity to apply the given effect to, checks the conditions for each entity, and applies the effect
	 * @param effect the effect to be applied to each entity
	 * @param callback callback info container for additional effect manipulation for use with specific activators
	 * @param playerSource the player that is the source of the effect
	 * @param effectTarget the entity involved in the effect being triggered, may be the same as the player
	 */
	public abstract void affectTarget(ActivatableGemEffect effect, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget);
	
	/**
	 * Tests if this target should attempt to activate for each specific entity
	 * @return true if there is no additional condition, or if the condition tests true
	 */
	protected boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return this.condition == null || this.condition.testCondition(callback, playerSource, effectTarget);
	}
	
	/**
	 * @return the user readable type name for the subclass used for deserialization
	 */
	public abstract String getTypeName();
	
	/**
	 * Attempts to validate this target type
	 * @return false if any required value is invalid, which should result in discarding this target
	 * Condition: Optional
	 */
	public boolean validate() {
		if(this.condition != null && !this.condition.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Target, invalid condition");
		else return true;
		return false;
	}
}