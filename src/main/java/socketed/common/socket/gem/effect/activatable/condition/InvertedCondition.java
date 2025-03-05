package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class InvertedCondition extends GenericCondition {

	public static final String TYPE_NAME = "Inverted";

	@SerializedName("Condition")
	private final GenericCondition condition;

	public InvertedCondition(GenericCondition condition) {
		super();
		this.condition = condition;
	}

	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return !condition.testCondition(callback,playerSource,effectTarget);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Condition: required, any
	 */
	@Override
	public boolean validate() {
		if(this.condition == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define inverted condition");
		else if(!this.condition.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, inverted condition invalid");
		else return true;
		return false;
	}
}