package socketed.common.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.ActivatableGemEffect;
import socketed.common.jsondata.entry.effect.activatable.activator.attack.AttackActivator;
import socketed.common.jsondata.entry.effect.activatable.activator.passive.PassiveActivator;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.jsondata.entry.effect.slot.SocketedSlotTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class EffectHandler {

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

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        if(event.getEntityLiving().world.isRemote) return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();

        ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
        if(cachedEffects == null) return;
        
        for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
            if(effect instanceof ActivatableGemEffect) {
                ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
                if(activatableGemEffect.getActivatorType() instanceof PassiveActivator) {
                    PassiveActivator passiveActivator = (PassiveActivator)activatableGemEffect.getActivatorType();
                    if(player.ticksExisted%passiveActivator.getActivationRate() == 0) {
                        passiveActivator.attemptPassiveActivation(activatableGemEffect, player);
                    }
                }
            }
        }
    }

    private static final List<IAttribute> handSkipAttributes = Arrays.asList(SharedMonsterAttributes.ATTACK_DAMAGE, SharedMonsterAttributes.ATTACK_SPEED, EntityPlayer.REACH_DISTANCE);
    private static final List<IAttribute> bodySkipAttributes = Arrays.asList(SharedMonsterAttributes.ARMOR, SharedMonsterAttributes.ARMOR_TOUGHNESS);

    //This handling assumes that gems cant be added or removed while wearing the item
    //This doesn't fire when swapping from one item to another of same item type in creative mode for whatever reason
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        if(event.getEntityLiving().world.isRemote) return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();

        EntityEquipmentSlot slot = event.getSlot();
        ItemStack stackOld = event.getFrom();
        ItemStack stackNew = event.getTo();
        //Reskillable changes the value behind the "to" itemstack to empty if requirements are not met, but it shouldn't matter
        //As its unequipping the armor only on next tick, so event gets triggered twice (equip, unequip)
        
        //As this event is only posted related to EntityEquipmentSlot, switching based on it doesnt matter much
        //Mods that add slots will need their own equipment change handling
        ISlotType slotType;
        switch(slot) {
            case MAINHAND: slotType = SocketedSlotTypes.MAINHAND; break;
            case OFFHAND: slotType = SocketedSlotTypes.OFFHAND; break;
            case HEAD: slotType = SocketedSlotTypes.HEAD; break;
            case CHEST: slotType = SocketedSlotTypes.CHEST; break;
            case LEGS: slotType = SocketedSlotTypes.LEGS; break;
            case FEET: slotType = SocketedSlotTypes.FEET; break;
            default: return;
        }
        
        //TODO: Retest and ensure attributes applied server-side-only are synced automatically to client
        ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
        if(cachedEffects == null) return;

        //Remove all modifiers that were from removed item and uncache previous active non-attribute effects
        List<GenericGemEffect> effectsToUncache = new ArrayList<>();
        ICapabilitySocketable capOld = stackOld.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(capOld != null) {
            List<GenericGemEffect> effectsOld = capOld.getAllActiveEffects(slotType);
            for(GenericGemEffect effect : effectsOld) {
                if(effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect)effect;
                    AttributeModifier modifier = attrEffect.getModifier();
                    if(modifier == null) continue;
                    
                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if(attrInstance == null) continue;

                    //Damage/Speed/Reach Attributes applied on weapons themselves are handled in ItemMixin for proper handling/compat with offhand mods like RLCombat
                    if(slotType.isSlotValid(SocketedSlotTypes.HAND) && handSkipAttributes.contains(attrInstance.getAttribute())) {
                        continue;
                    }
                    
                    //Armor/Armor Toughness Attributes applied on armor themselves are handled in ItemMixin for proper handling/compat with slot-specific mods like FirstAid
                    if(slotType.isSlotValid(SocketedSlotTypes.BODY) && bodySkipAttributes.contains(attrInstance.getAttribute())) {
                        continue;
                    }

                    if(attrInstance.hasModifier(modifier)) {
                        attrInstance.removeModifier(modifier);
                    }
                }
                else {
                    //Attribute effects are handled above, so only non-attribute effects are cached in the player capability
                    effectsToUncache.add(effect);
                }
            }
            cachedEffects.removeEffects(effectsToUncache);
        }

        //Apply new modifiers from new item and cache active non-attribute effects
        List<GenericGemEffect> effectsToCache = new ArrayList<>();
        ICapabilitySocketable capNew = stackNew.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(capNew != null) {
            List<GenericGemEffect> effectsNew = capNew.getAllActiveEffects(slotType);
            for(GenericGemEffect effect : effectsNew) {
                if(effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect)effect;
                    AttributeModifier modifier = attrEffect.getModifier();
                    if(modifier == null) continue;

                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if(attrInstance == null) continue;
                    
                    //Damage/Speed/Reach Attributes applied on weapons themselves are handled in ItemMixin for proper handling/compat with offhand mods like RLCombat
                    if(slotType.isSlotValid(SocketedSlotTypes.HAND) && handSkipAttributes.contains(attrInstance.getAttribute())) {
                        continue;
                    }
                    
                    //Armor/Armor Toughness Attributes applied on armor themselves are handled in ItemMixin for proper handling/compat with slot-specific mods like FirstAid
                    if(slotType.isSlotValid(SocketedSlotTypes.BODY) && bodySkipAttributes.contains(attrInstance.getAttribute())) {
                        continue;
                    }

                    if(!attrInstance.hasModifier(modifier)) {
                        attrInstance.applyModifier(modifier);
                    }
                }
                else {
                    //Attribute effects are handled above, so only non-attribute effects are cached in the player capability
                    effectsToCache.add(effect);
                }
            }
            cachedEffects.addEffects(effectsToCache);
        }
    }
}