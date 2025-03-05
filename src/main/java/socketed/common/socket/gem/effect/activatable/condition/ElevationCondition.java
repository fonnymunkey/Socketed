package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.ComparingCondition;

import javax.annotation.Nullable;

public class ElevationCondition extends ComparingCondition {

	public static final String TYPE_NAME = "Elevation";

	@SerializedName("Y Level")
	protected final Integer yLvl;

	public ElevationCondition(int yLvl, ConditionComparisonType comparisonType, boolean checkForPlayer) {
		super(comparisonType, checkForPlayer);
		this.yLvl = yLvl;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		int currYLvl = determineAffectedEntity(playerSource, effectTarget).getPosition().getY();
		return comparisonType.test(currYLvl, this.yLvl);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(super.validate()) {
            if (this.yLvl == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define y level");
            else return true;
		}
		return false;
	}
}