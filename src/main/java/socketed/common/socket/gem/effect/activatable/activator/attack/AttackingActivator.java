package socketed.common.socket.gem.effect.activatable.activator.attack;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;

public class AttackingActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacking";
	
	@SerializedName("Affects Target")
	protected final Boolean affectsTarget;
	
	@SerializedName("Directly Activated")
	protected Boolean directlyActivated;
	
	public AttackingActivator(boolean affectsSelf, boolean affectsTarget, boolean allowsMelee, boolean allowsRanged, boolean directlyActivated) {
		super(affectsSelf, allowsMelee, allowsRanged);
		this.affectsTarget = affectsTarget;
		this.directlyActivated = directlyActivated;
	}
	
	/**
	 * @return if this activator should affect the target
	 */
	public boolean getAffectsTarget() {
		return this.affectsTarget;
	}
	
	/**
	 * @return if this activator must be triggered directly from the used weapon
	 */
	public boolean getDirectlyActivated() {
		return this.directlyActivated;
	}
	
	@Override
	public void attemptAttackActivation(ActivatableGemEffect effect, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean directlyActivated, DamageSource source) {
		//Check if direct activation is required
		if(directlyActivated != this.getDirectlyActivated()) return;
		//Player is the attacker
		if(player == attacker) {
			if(this.getAffectsSelf()) effect.performEffect(player, attacker);
			if(this.getAffectsTarget()) effect.performEffect(player, target);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format(this.getDirectlyActivated() ? "socketed.tooltip.activator.attacking_direct" : "socketed.tooltip.activator.attacking_indirect",
						   (this.getAllowsMelee() ?
						   this.getAllowsRanged() ?
						   I18n.format("socketed.tooltip.attack.melee_ranged") :
						   I18n.format("socketed.tooltip.attack.melee") :
						   I18n.format("socketed.tooltip.attack.ranged")),
						   (this.getAffectsSelf() ?
						   this.getAffectsTarget() ?
						   I18n.format("socketed.tooltip.attack.self_target") :
						   I18n.format("socketed.tooltip.attack.self") :
						   I18n.format("socketed.tooltip.attack.target"))
						  );
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * AffectsTarget: Required
	 * DirectlyActivated: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.directlyActivated == null) this.directlyActivated = false;
		
		if(super.validate()) {
			if(this.affectsTarget == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must define if it affects target");
			else if(!this.affectsSelf && !this.affectsTarget) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must affect at least either self or target");
			else return true;
		}
		return false;
	}
}
