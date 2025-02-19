package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class PotionActiveCondition extends EntityPropertyCondition {

	public static final String TYPE_NAME = "Potion Active";

	@SerializedName("Potion Name")
	private final String potionName;

	@SerializedName("Minimum Amplifier")
	protected Integer minAmplifier;

	@SerializedName("Minimum Duration")
	protected Integer minDuration;

	private transient Potion potion;

	public PotionActiveCondition(String potionName, Integer minAmplifier, Integer minDuration, boolean checkForPlayer) {
		super(checkForPlayer);
		this.potionName = potionName;
		this.minAmplifier = minAmplifier;
		this.minDuration = minDuration;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		PotionEffect effect = determineAffectedEntity(playerSource, effectTarget).getActivePotionEffect(potion);
		if(effect == null) return false;
		if(this.minAmplifier != null && effect.getAmplifier() < this.minAmplifier) return false;
		if(this.minDuration != null && effect.getDuration() < this.minDuration) return false;
		return true;
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Potion Name: Required
	 * Min Amplifier: Default null (always pass)
	 * Min Duration: Default null (always pass)
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if (this.potionName == null || this.potionName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, potion name null or empty");
			else {
				this.potion = Potion.getPotionFromResourceLocation(this.potionName);
				if (this.potion == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, " + this.potionName + ", potion does not exist");
				else return true;
			}
		}
		return false;
	}
}