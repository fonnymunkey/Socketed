package socketed.common.socket.gem.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.GenericEventCallback;
import socketed.common.socket.gem.effect.activatable.condition.DamageSourceCondition;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;

import javax.annotation.Nullable;

public abstract class AttackActivator extends GenericActivator {

	@SerializedName("Directly Activated")
	protected Boolean directlyActivated;

	public AttackActivator(@Nullable GenericCondition condition, boolean directlyActivated) {
		super(condition);
		this.directlyActivated = directlyActivated;
	}

	/**
	 * DirectlyActivated: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.directlyActivated == null) this.directlyActivated = false;

		return super.validate();
	}
	
	/**
	 * Called during LivingAttackEvent
	 * Target and attacker can not be the same entity
	 * If the callback is cancelled, cancels the LivingAttackEvent
	 * @param effect the effect parent of this activator
	 * @param callback the GenericEvent callback to allow for the effect to cancel the LivingAttackEvent and conditions to check the DamageSource
	 * @param player the player that is the source of the effect
	 * @param other the other entity involved in the attack event, can be attacker (Attacked) or target (Attacking)
	 * @param directlyActivated if the source of the effect is the attacker's weapon rather than the player's effect cache
	 */
	protected void attemptAttackActivation(ActivatableGemEffect effect, GenericEventCallback<? extends Event> callback, EntityPlayer player, EntityLivingBase other, boolean directlyActivated) {
		//Check if direct activation is required
		if(directlyActivated != this.directlyActivated) return;
		if(this.testCondition(callback, player, other)) {
			effect.affectTargets(callback, player, other);
		}
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
			boolean isMelee = DamageSourceCondition.isDamageSourceMelee(source);
			boolean isRanged = !isMelee && DamageSourceCondition.isDamageSourceRanged(source);
			
			EntityLivingBase target = event.getEntityLiving();
			EntityLivingBase attacker;
			if(isMelee) attacker = (EntityLivingBase)source.getImmediateSource();
			else if(isRanged) attacker = (EntityLivingBase)source.getTrueSource();
			else return;
			
			//Dont trigger on self damage if that manages to happen
			if(target == attacker) return;
			
			//Allow for cancelling the attack from effects
			GenericEventCallback<LivingAttackEvent> callback = new GenericEventCallback<>(event);
			
			//AttackedActivator handling
			if(target instanceof EntityPlayer) {
				handleAttacked(callback, (EntityPlayer)target, attacker);
			}
			
			//Return before offensive effects if defensive effects cancelled attack
			if(callback.getEvent().isCanceled()) {
				return;
			}
			
			//AttackingActivator handling
			if(attacker instanceof EntityPlayer) {
				handleAttacking(callback, (EntityPlayer)attacker, target);
			}
		}
		
		private static void handleAttacked(GenericEventCallback<LivingAttackEvent> callback, EntityPlayer player, EntityLivingBase attacker) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			GenericActivator.filterForActivator(cachedEffects.getActiveEffects(), AttackedActivator.class)
					.forEach(effect -> {
						AttackedActivator activator = (AttackedActivator) effect.getActivator();
						activator.attemptAttackActivation(effect, callback, player, attacker, false);
					});

			//can't do direct activation via first aid since that is only calculated right before living damage
			//TODO: could do direct activation of active shield
		}
		
		private static void handleAttacking(GenericEventCallback<LivingAttackEvent> callback, EntityPlayer player, EntityLivingBase target) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			GenericActivator.filterForActivator(cachedEffects.getActiveEffects(), AttackingActivator.class)
					.forEach(effect ->{
						AttackingActivator activator = (AttackingActivator) effect.getActivator();
						activator.attemptAttackActivation(effect, callback, player, target, false);
					});
			
			//Handle direct activation effects
			//RLCombat swaps offhand into mainhand before posting LivingAttack for offhand attacks
			ItemStack weaponStack = player.getHeldItemMainhand();
			if(weaponStack.isEmpty()) return;
			
			ICapabilitySocketable sockets = weaponStack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
			if(sockets == null) return;
			
			//TODO: RLCombat compat to get whether the attack is being posted from mainhand or offhand for more accurate slot type check
			GenericActivator.filterForActivator(sockets.getAllActiveEffects(SocketedSlotTypes.HAND), AttackingActivator.class)
					.forEach(effect -> {
						AttackingActivator activator = (AttackingActivator) effect.getActivator();
						activator.attemptAttackActivation(effect, callback, player, target, true);
					});
		}
	}
}