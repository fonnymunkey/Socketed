package socketed.common.socket.gem.effect.activatable.activator.attack;

import com.google.gson.annotations.SerializedName;
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

public class AttackingAOEActivator extends AttackAOEActivator {
	
	public static final String TYPE_NAME = "Attacking AOE";
	
	@SerializedName("Directly Activated")
	protected Boolean directlyActivated;
	
	public AttackingAOEActivator(boolean affectsSelf, boolean affectsAttacker, boolean allowsMelee, boolean allowsRanged, boolean aoeAroundSelf, boolean aoeAroundAttacker, int blockRange, boolean directlyActivated) {
		super(affectsSelf, affectsAttacker, allowsMelee, allowsRanged, aoeAroundSelf, aoeAroundAttacker, blockRange);
		this.directlyActivated = directlyActivated;
	}
	
	/**
	 * @return if this activator must be triggered directly from the used weapon
	 */
	public boolean getDirectlyActivated() {
		return this.directlyActivated;
	}
	
	@Override
	public void attemptAttackActivation(ActivatableGemEffect effect, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean isMelee, boolean isRanged, boolean directlyActivated, DamageSource source) {
		//Check if direct activation is required
		if(directlyActivated != this.getDirectlyActivated()) return;
		//Player is the attacker
		if(player == attacker) {
			if((this.getAllowsMelee() && isMelee) || this.getAllowsRanged() && isRanged) {
				if(this.getAffectsSelf()) effect.performEffect(player, attacker);
				if(this.getAffectsOther()) effect.performEffect(player, target);
				if(this.getAOEAroundSelf()) {
					List<EntityLivingBase> entitiesNearby = attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(attacker.getPosition()).grow(this.getBlockRange()));
					for(EntityLivingBase entity : entitiesNearby) {
						if(entity != target && entity != attacker) effect.performEffect(player, entity);
					}
				}
				if(this.getAOEAroundOther()) {
					List<EntityLivingBase> entitiesNearby = target.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(target.getPosition()).grow(this.getBlockRange()));
					for(EntityLivingBase entity : entitiesNearby) {
						if(entity != target && entity != attacker) effect.performEffect(player, entity);
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.attacking_aoe",
						   (this.getAllowsMelee() ?
							this.getAllowsRanged() ?
							"" :
							I18n.format("socketed.tooltip.attack.melee") + "/" :
							I18n.format("socketed.tooltip.attack.ranged") + "/") +
							(this.getAOEAroundSelf() ?
							 this.getAOEAroundOther() ?
							 I18n.format("socketed.tooltip.attack.both") :
							 I18n.format("socketed.tooltip.attack.self") :
							 I18n.format("socketed.tooltip.attack.target")),
						   this.getBlockRange()
						  );
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * DirectlyActivated: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.directlyActivated == null) this.directlyActivated = false;
		
		if(super.validate()) {
			if(!this.allowsMelee && !this.allowsRanged) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must allow at least either melee or ranged");
			else if(!this.aoeAroundSelf && !this.aoeAroundOther) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must affect aoe around at least either self or attacker");
			else return true;
		}
		return false;
	}
}