package socketed.common.socket.gem.effect.activatable.activator.passive;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;

import java.util.List;

public class PassiveAOEActivator extends PassiveActivator {
	
	public static final String TYPE_NAME = "Passive AOE";
	
	@SerializedName("Block Range")
	protected final Integer blockRange;
	
	public PassiveAOEActivator(int activationRate, int blockRange) {
		super(activationRate);
		this.blockRange = blockRange;
	}
	
	/**
	 * @return the radius in blocks this activator will check for entities to affect
	 */
	public int getBlockRange() {
		return this.blockRange;
	}
	
	@Override
	public void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player) {
		List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(this.getBlockRange()));
		for(EntityLivingBase entity : entitiesNearby) {
			if(entity != player) effect.performEffect(entity);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.passive_aoe", this.getBlockRange());
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * BlockRange: Required
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.blockRange == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be defined");
			else if(this.blockRange < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, block range must be greater than 0");
			else return true;
		}
		return false;
	}
}
