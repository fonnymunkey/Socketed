package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class HurtResistantTimeGemEffect extends ActivatableGemEffect {

	public static final String TYPE_NAME = "Hurt Resistant Time";

	@SerializedName("Tick Amount")
	private final Integer ticks;

	@SerializedName("Set To Amount")
	protected Boolean setToAmount;

	public HurtResistantTimeGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, int ticks, boolean setToAmount) {
		super(slotType, activator, targets);
		this.ticks = ticks;
		this.setToAmount = setToAmount;
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			if(this.setToAmount) effectTarget.hurtResistantTime = this.ticks;
			else effectTarget.hurtResistantTime += this.ticks;
		}
	}
	
	//TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return "";
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Tick Amount: Required, non-negative
	 * Set To Amount: Optional, default false (adding, not setting)
	 */
	@Override
	public boolean validate() {
		if(this.setToAmount == null) this.setToAmount = false;

		if(super.validate()) {
			if(this.ticks == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, tick amount must be defined");
			else if(this.ticks < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, tick amount can not be negative");
			else return true;
		}
		return false;
	}
}