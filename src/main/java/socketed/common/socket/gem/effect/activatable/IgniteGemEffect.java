package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.slot.ISlotType;

public class IgniteGemEffect extends ActivatableGemEffect {
	
	public static final String TYPE_NAME = "Ignite";
	
	@SerializedName("Duration")
	private final Integer duration;
	
	public IgniteGemEffect(ISlotType slotType, GenericActivator activator, int duration) {
		super(slotType, activator);
		this.duration = duration;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	@Override
	public void performEffect(EntityPlayer player, EntityLivingBase entity) {
		if(entity != null && !entity.world.isRemote) {
			entity.setFire(this.getDuration());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return this.getActivatorType().getTooltipString() + " " + I18n.format("socketed.tooltip.effect.ignite", this.getDuration());
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