package socketed.common.socket.gem.effect.activatable.activator.attack;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;

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
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		
		/**
		 * Event handling for AttackActivators
		 */
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void onEntityHit(LivingAttackEvent event) {
			if(event.getEntityLiving() == null) return;
			if(event.getEntityLiving().world.isRemote) return;
			if(event.getSource() == null) return;
			DamageSource source = event.getSource();
			
			//Prioritize checking the direct attacker, prevent ranged pet attacks from triggering attacker effects such as lycanites
			boolean isMelee = isDamageSourceMelee(source);
			boolean isRanged = !isMelee && isDamageSourceRanged(source);
			
			EntityLivingBase target = event.getEntityLiving();
			EntityLivingBase attacker;
			if(isMelee) attacker = (EntityLivingBase)source.getImmediateSource();
			else if(isRanged) attacker = (EntityLivingBase)source.getTrueSource();
			else return;
			
			//Dont trigger on self damage if that manages to happen
			if(target == attacker) return;
			
			//AttackedActivator handling
			if(target instanceof EntityPlayer) {
				handleAttacked((EntityPlayer)target, attacker, isMelee, isRanged, source);
			}
			//AttackingActivator handling
			if(attacker instanceof EntityPlayer) {
				handleAttacking((EntityPlayer)attacker, target, isMelee, isRanged, source);
			}
		}
		
		private static void handleAttacked(EntityPlayer player, EntityLivingBase attacker, boolean isMelee, boolean isRanged, DamageSource source) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivatorType() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivatorType());
						if((attackActivator.getAllowsMelee() && isMelee) || (attackActivator.getAllowsRanged() && isRanged)) {
							attackActivator.attemptAttackActivation(activatableGemEffect, player, player, attacker, false, source);
						}
					}
				}
			}
		}
		
		private static void handleAttacking(EntityPlayer player, EntityLivingBase target, boolean isMelee, boolean isRanged, DamageSource source) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivatorType() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivatorType());
						if((attackActivator.getAllowsMelee() && isMelee) || (attackActivator.getAllowsRanged() && isRanged)) {
							attackActivator.attemptAttackActivation(activatableGemEffect, player, target, player, false, source);
						}
					}
				}
			}
			
			//Handle direct activation effects
			//RLCombat swaps offhand into mainhand before posting LivingAttack for offhand attacks
			ItemStack weaponStack = player.getHeldItemMainhand();
			if(weaponStack.isEmpty()) return;
			
			ICapabilitySocketable sockets = weaponStack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
			if(sockets == null) return;
			
			//TODO: RLCombat compat to get whether the attack is being posted from mainhand or offhand for more accurate slot type check
			for(GenericGemEffect effect : sockets.getAllActiveEffects(SocketedSlotTypes.HAND)) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivatorType() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivatorType());
						if((attackActivator.getAllowsMelee() && isMelee) || (attackActivator.getAllowsRanged() && isRanged)) {
							attackActivator.attemptAttackActivation(activatableGemEffect, player, target, player, true, source);
						}
					}
				}
			}
		}
		
		private static boolean isDamageSourceMelee(DamageSource source) {
			return source.getImmediateSource() instanceof EntityLivingBase;
		}
		
		private static boolean isDamageSourceRanged(DamageSource source) {
			return !(source.getImmediateSource() instanceof EntityLivingBase) && source.getTrueSource() instanceof EntityLivingBase;
		}
	}
}