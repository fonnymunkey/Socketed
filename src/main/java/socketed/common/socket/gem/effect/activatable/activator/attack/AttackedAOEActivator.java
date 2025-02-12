package socketed.common.socket.gem.effect.activatable.activator.attack;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;

import java.util.List;

public class AttackedAOEActivator extends AttackAOEActivator {
	
	public static final String TYPE_NAME = "Attacked AOE";
	
	public AttackedAOEActivator(boolean affectsSelf, boolean affectsAttacker, boolean allowsMelee, boolean allowsRanged, boolean aoeAroundSelf, boolean aoeAroundAttacker, int blockRange) {
		super(affectsSelf, affectsAttacker, allowsMelee, allowsRanged, aoeAroundSelf, aoeAroundAttacker, blockRange);
	}
	
	@Override
	public void attemptAttackActivation(ActivatableGemEffect effect, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean isMelee, boolean isRanged, boolean directlyActivated, DamageSource source) {
		//Attacked activators can not be directly activated
		if(directlyActivated) return;
		//Player is being attacked
		if(player == target) {
			if((this.getAllowsMelee() && isMelee) || this.getAllowsRanged() && isRanged) {
				if(this.getAffectsSelf()) effect.performEffect(player, target);
				if(this.getAffectsOther()) effect.performEffect(player, attacker);
				if(this.getAOEAroundSelf()) {
					List<EntityLivingBase> entitiesNearby = target.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(target.getPosition()).grow(this.getBlockRange()));
					for(EntityLivingBase entity : entitiesNearby) {
						if(entity != target && entity != attacker) effect.performEffect(player, entity);
					}
				}
				if(this.getAOEAroundOther()) {
					List<EntityLivingBase> entitiesNearby = attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(attacker.getPosition()).grow(this.getBlockRange()));
					for(EntityLivingBase entity : entitiesNearby) {
						if(entity != target && entity != attacker) effect.performEffect(player, entity);
					}
				}
			}
		}
	}
	
	//TODO
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.attacked",
						   (this.getAllowsMelee() ?
							this.getAllowsRanged() ?
							I18n.format("socketed.tooltip.attack.melee_ranged") :
							I18n.format("socketed.tooltip.attack.melee") :
							I18n.format("socketed.tooltip.attack.ranged")),
						   (this.getAffectsSelf() ?
							this.getAffectsOther() ?
							I18n.format("socketed.tooltip.attack.self_attacker") :
							I18n.format("socketed.tooltip.attack.self") :
							I18n.format("socketed.tooltip.attack.attacker"))
						  );
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(!this.allowsMelee && !this.allowsRanged) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must allow at least either melee or ranged");
			else if(!this.aoeAroundSelf && !this.aoeAroundOther) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must affect aoe around at least either self or attacker");
			else return true;
		}
		return false;
	}
}