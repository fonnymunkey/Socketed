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

public class IgniteGemEffect extends ActivatableGemEffect {
	
	public static final String TYPE_NAME = "Ignite";
	
	@SerializedName("Duration")
	private final Integer duration;
	
	public IgniteGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, int duration) {
		super(slotType, activator, targets);
		this.duration = duration;
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			effectTarget.setFire(this.duration);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return this.getActivator().getTooltipString() + " " + I18n.format("socketed.tooltip.effect.ignite", this.duration);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * Duration: Required
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.duration == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, duration must be defined");
			else if(this.duration < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, duration can not be less than 1");
			else return true;
		}
		return false;
	}
}