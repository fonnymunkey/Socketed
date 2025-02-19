package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class HealthPercentCondition extends ComparingCondition {

	public static final String TYPE_NAME = "Health Percent";

	@SerializedName("Health Percent")
	protected final Float healthPercent;

	public HealthPercentCondition(float healthPercent, ConditionComparisonType comparisonType, boolean checkForPlayer) {
		super(comparisonType, checkForPlayer);
		this.healthPercent = healthPercent;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		EntityLivingBase entityToTestFor = determineAffectedEntity(playerSource, effectTarget);
		float currHealthPercent = entityToTestFor.getHealth() / entityToTestFor.getMaxHealth();
		if (!Float.isFinite(currHealthPercent)) return false;
		return comparisonType.test(currHealthPercent, this.healthPercent);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(super.validate()) {
			if (this.healthPercent == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define health percent");
			else if (this.healthPercent < 0.0F || this.healthPercent > 1.0F) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, health percent must be between 0.0 and 1.0");
			else return true;
		}
		return false;
	}
}