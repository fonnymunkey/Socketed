package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class LightLevelCondition extends ComparingCondition {
	public static final String TYPE_NAME = "Light Level";

	@SerializedName("Light Level")
	protected final Integer lightLvl;

	public LightLevelCondition(int lightLvl, ConditionComparisonType comparisonType) {
		super(comparisonType);
		this.lightLvl = lightLvl;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		int currLightLvl = effectTarget.world.getLight(effectTarget.getPosition());
		return comparisonType.test(currLightLvl, this.lightLvl, callback, playerSource, effectTarget);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Light Level: Required, range 0-15
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
            if (this.lightLvl == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define light level");
			else if (this.lightLvl < 0 || this.lightLvl >= 16) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, light level has to be between 0 and 15");
            else return true;
		}
		return false;
	}
}