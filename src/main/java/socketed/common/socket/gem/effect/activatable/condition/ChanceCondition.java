package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;

public class ChanceCondition extends GenericCondition {
	
	public static final String TYPE_NAME = "Chance";
	
	@SerializedName("Chance")
	protected final Float chance;
	
	public ChanceCondition(float chance) {
		super();
		this.chance = chance;
	}
	
	@Override
	public boolean testCondition(EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return playerSource.getRNG().nextFloat() < this.chance;
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(this.chance == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define chance");
		else if(this.chance < 0.0F || this.chance > 1.0F) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, chance must be between 0.0 and 1.0");
		else return true;
		return false;
	}
}