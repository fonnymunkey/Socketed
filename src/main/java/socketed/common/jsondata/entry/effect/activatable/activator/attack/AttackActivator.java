package socketed.common.jsondata.entry.effect.activatable.activator.attack;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.activatable.ActivatableGemEffect;
import socketed.common.jsondata.entry.effect.activatable.activator.GenericActivator;

public abstract class AttackActivator extends GenericActivator {
	
	@SerializedName("Affects Self")
	protected final boolean affectsSelf;
	
	@SerializedName("Allows Melee")
	protected final boolean allowsMelee;
	
	@SerializedName("Allows Ranged")
	protected final boolean allowsRanged;
	
	public AttackActivator(boolean affectsSelf, boolean allowsMelee, boolean allowsRanged) {
		super();
		this.affectsSelf = affectsSelf;
		this.allowsMelee = allowsMelee;
		this.allowsRanged = allowsRanged;
	}
	
	/**
	 * @return if this activator should affect the player source of the effect
	 */
	public boolean getAffectsSelf() {
		return this.affectsSelf;
	}
	
	/**
	 * @return if this activator should trigger on melee attacks
	 */
	public boolean getAllowsMelee() {
		return this.allowsMelee;
	}
	
	/**
	 * @return if this activator should trigger on ranged attacks
	 */
	public boolean getAllowsRanged() {
		return this.allowsRanged;
	}
	
	/**
	 * Called during LivingAttackEvent depending on if melee/ranged options match
	 * Only called on the server thread
	 * Target and attacker can not be the same entity
	 * @param effect the effect to be performed if this activator is successful
	 * @param player the player that is the source of the effect
	 * @param target the target of the attack, may be the player
	 * @param attacker the attacker performing the attack, may be the player
	 * @param directlyActivated if the source of the effect is the attacker's weapon rather than the player's cache
	 * @param source the damagesource of the attack for additional context
	 */
	public abstract void attemptAttackActivation(ActivatableGemEffect effect, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean directlyActivated, DamageSource source);
	
	@Override
	public boolean validate() {
		if(!this.allowsMelee && !this.allowsRanged) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must allow at least either melee or ranged");
		else return true;
		return false;
	}
}