package socketed.common.jsondata.entry.effect.activatable.activator.passive;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.activatable.ActivatableGemEffect;
import socketed.common.jsondata.entry.effect.activatable.activator.GenericActivator;

public abstract class PassiveActivator extends GenericActivator {
	
	@SerializedName("Activation Rate")
	protected final int activationRate;
	
	public PassiveActivator(int activationRate) {
		super();
		this.activationRate = activationRate;
	}
	
	/**
	 * @return the tick rate of calling attemptPassiveActivation
	 */
	public int getActivationRate() {
		return this.activationRate;
	}
	
	/**
	 * Called every x ticks as defined by the activation rate
	 * Only called on the server thread
	 * @param effect the effect to be performed if this activator is successful
	 * @param player the player that is the source of the effect
	 */
	public abstract void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player);
	
	@Override
	public boolean validate() {
		if(this.activationRate < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, activation rate must be greater than 0");
		else return true;
		return false;
	}
}
