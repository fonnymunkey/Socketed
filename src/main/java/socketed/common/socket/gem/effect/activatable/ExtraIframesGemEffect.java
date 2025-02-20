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

public class ExtraIframesGemEffect extends ActivatableGemEffect {

	public static final String TYPE_NAME = "Extra Iframes";

	@SerializedName("Tick Amount")
	private final Integer ticks;

	public ExtraIframesGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, int ticks, boolean setToAmount) {
		super(slotType, activator, targets);
		this.ticks = ticks;
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			effectTarget.hurtResistantTime = effectTarget.maxHurtResistantTime + this.ticks;
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
	 * Tick Amount: Required
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.ticks == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, tick amount must be defined");
			else return true;
		}
		return false;
	}
}