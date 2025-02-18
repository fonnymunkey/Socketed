package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public abstract class GenericCondition {
	
	public static final String TYPE_FIELD = "Condition Type";
	
	@SerializedName(TYPE_FIELD)
	protected final String type = this.getTypeName();
	
	/**
	 * @return true if this condition tests true
	 */
	public abstract boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget);
	
	/**
	 * @return the user readable type name for the subclass used for deserialization
	 */
	public abstract String getTypeName();
	
	/**
	 * Attempts to validate this condition type and cache any parsed values
	 * @return false if any required value is invalid, which should result in discarding this condition
	 */
	public abstract boolean validate();
}