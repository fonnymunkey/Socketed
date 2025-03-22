package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;
import socketed.api.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class ExtraIFrameGemEffect extends ActivatableGemEffect {

	public static final String TYPE_NAME = "Extra IFrame";

	@SerializedName("Tick Amount")
	private final Integer ticks;

	public ExtraIFrameGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, int ticks, String tooltipKey) {
		super(slotType, activator, targets, tooltipKey);
		this.ticks = ticks;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.effect.extraiframe", this.ticks);
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			effectTarget.hurtResistantTime = effectTarget.maxHurtResistantTime + this.ticks;
		}
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