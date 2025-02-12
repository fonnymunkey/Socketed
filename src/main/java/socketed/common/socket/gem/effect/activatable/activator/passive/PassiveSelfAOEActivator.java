package socketed.common.socket.gem.effect.activatable.activator.passive;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;

import java.util.List;

public class PassiveSelfAOEActivator extends PassiveAOEActivator {
	
	public static final String TYPE_NAME = "Passive AOE";
	
	public PassiveSelfAOEActivator(int activationRate, int blockRange, boolean affectsSelf) {
		super(activationRate, blockRange, affectsSelf);
	}
	
	@Override
	public void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player) {
		if(player.ticksExisted % this.getActivationRate() == 0) {
			if(this.getAffectsSelf()) effect.performEffect(player, player);
			List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(this.getBlockRange()));
			for(EntityLivingBase entity : entitiesNearby) {
				if(entity != player) effect.performEffect(player, entity);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.passive_self_aoe", this.getBlockRange());
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}