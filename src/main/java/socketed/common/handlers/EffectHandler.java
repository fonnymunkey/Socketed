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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.ActivatableGemEffect;

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
        
        if(event.getEntityLiving() instanceof EntityPlayer) {
            handleHitEffects((EntityPlayer)event.getEntityLiving(), event.getEntityLiving(), event.getSource(), true);
        }
        if(event.getSource().getTrueSource() instanceof EntityPlayer) {
            handleHitEffects((EntityPlayer)event.getSource().getTrueSource(), event.getEntityLiving(), event.getSource(), false);
        }
    }

    private static void handleHitEffects(EntityPlayer player, EntityLivingBase victim, DamageSource source, boolean received) {
        //TODO: handle iteration automatically from expanded SlotTypes?
        ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
        if(cachedEffects == null) return;
        for (GenericGemEffect effect : cachedEffects.getActiveEffects()) {
            if (effect instanceof ActivatableGemEffect) {
                ActivatableGemEffect activatableEffect = (ActivatableGemEffect) effect;
                activatableEffect.getActivationType().triggerOnAttackEffect(activatableEffect, victim, source, received);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        if (event.getEntityLiving().world.isRemote) return;
        //TODO: allow for sub-20 tick triggers/cache active effects as player capability for performance
        if (event.getEntityLiving().ticksExisted % 20 != 0) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        //TODO: handle iteration automatically from expanded SlotTypes?
        ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
        if (cachedEffects == null) return;
        for (GenericGemEffect effect : cachedEffects.getActiveEffects()) {
            if (effect instanceof ActivatableGemEffect) {
                ActivatableGemEffect activatableEffect = (ActivatableGemEffect) effect;
                activatableEffect.getActivationType().triggerPerSecondEffect(activatableEffect, player);
            }
        }
    }

    private static final List<IAttribute> offhandSkipAttributes = Arrays.asList(SharedMonsterAttributes.ATTACK_DAMAGE, SharedMonsterAttributes.ATTACK_SPEED);

    //This handling assumes that gems cant be added or removed while wearing the item
    //This doesn't fire when swapping from one item to another of same item type in creative mode for whatever reason
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        if(event.getEntityLiving().world.isRemote) return;
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();

        EntityEquipmentSlot slot = event.getSlot();
        ItemStack stackOld = event.getFrom();
        ItemStack stackNew = event.getTo();
        //Reskillable changes the value behind the "to" itemstack to empty if requirements are not met, but it shouldn't matter
        // As its unequipping the armor only on next tick, so event gets triggered twice (equip, unequip)
        //ItemStack stackNew = player.getItemStackFromSlot(slot);

        ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
        if (cachedEffects == null) return;
        List<GenericGemEffect> effectsToCache = new ArrayList<>();
        List<GenericGemEffect> effectsToUncache = new ArrayList<>();

        //Remove all modifiers that were on removed item
        ICapabilitySocketable capOld = stackOld.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(capOld != null) {
            List<GenericGemEffect> effectsOld = capOld.getAllEffectsValidForSlot(slot);
            for(GenericGemEffect effect : effectsOld) {
                if(effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect) effect;

                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if (attrInstance == null) continue;

                    //TODO RLCombat compat
                    //Skip damage/speed/reach attributes for offhand, let 2 hand mods like RLCombat handle the compat properly
                    if (slot == EntityEquipmentSlot.OFFHAND && (offhandSkipAttributes.contains(attrInstance.getAttribute()) || attribute.contains("reachDistance"))) {
                        continue;
                    }

                    AttributeModifier modifier = attrEffect.getModifier();
                    if (attrInstance.hasModifier(modifier)) {
                        attrInstance.removeModifier(modifier);
                    }
                } else {
                    effectsToUncache.add(effect);
                }
            }
            cachedEffects.removeEffects(effectsToUncache);
        }

        //Apply new modifiers from new item
        ICapabilitySocketable capNew = stackNew.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(capNew != null) {
            List<GenericGemEffect> effectsNew = capNew.getAllEffectsValidForSlot(slot);
            for(GenericGemEffect effect : effectsNew) {
                if(effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect)effect;

                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if(attrInstance == null) continue;
                    
                    //TODO RLCombat compat
                    //Skip damage/speed/reach attributes for offhand, let 2 hand mods like RLCombat handle the compat properly
                    if(slot == EntityEquipmentSlot.OFFHAND && (offhandSkipAttributes.contains(attrInstance.getAttribute()) || attribute.contains("reachDistance"))) {
                        continue;
                    }

                    AttributeModifier modifier = attrEffect.getModifier();
                    if(!attrInstance.hasModifier(modifier)) {
                        attrInstance.applyModifier(modifier);
                    }
                } else {
                    //Attribute effects are cached in player attributes and only added+removed here, so don't need to be cached in player cap
                    effectsToCache.add(effect);
                }
            }
            cachedEffects.addEffects(effectsToCache);
        }
    }
}