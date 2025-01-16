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
import socketed.Socketed;
import socketed.common.capabilities.CapabilityHasSockets;
import socketed.common.data.entry.effect.activatable.ActivatableGemEffect;
import socketed.common.data.entry.effect.AttributeGemEffect;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.data.entry.effect.activatable.EnumActivationTypes;
import socketed.common.data.entry.effect.activatable.IActivationType;
import socketed.common.data.entry.effect.activatable.PotionGemEffect;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class EffectHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityHit(LivingAttackEvent event) {
        if(event.getEntity().world.isRemote) return;
        //TODO: add activation types for ranged vs melee
        //TODO: same for indirect dmg sources
        if(event.getEntity() instanceof EntityPlayer)
            handleHitEffects((EntityPlayer)event.getEntityLiving(), (EntityPlayer)event.getSource().getTrueSource(), event.getSource(), true);
        if(event.getSource().getTrueSource() instanceof EntityPlayer)
            handleHitEffects((EntityPlayer)event.getSource().getTrueSource(), event.getEntityLiving(), event.getSource(), false);
    }

    private static void handleHitEffects(EntityPlayer player, EntityLivingBase other, DamageSource source, boolean received) {
        //Iterate active slots
        for (ItemStack stack : player.getEquipmentAndArmor()) {
            if(!stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null)) continue;

            List<GenericGemEffect> effects = stack.getCapability(CapabilityHasSockets.HAS_SOCKETS,null).getAllEffectsFromAllSockets();
            for(GenericGemEffect effect : effects){
                //No attribute from hit effect currently
                //Potion effect
                if (effect instanceof PotionGemEffect) {
                    PotionGemEffect potEffect = (PotionGemEffect) effect;
                    if (potEffect.getPotion() == null) continue;
                    IActivationType activationType = potEffect.getActivationType();
                    if(received) {
                        if(activationType == EnumActivationTypes.ON_ATTACKED_SELF)
                            potEffect.getActivationType().triggerOnAttackEffect(potEffect, player, source);
                        if(activationType == EnumActivationTypes.ON_ATTACKED_ATTACKER)
                            potEffect.getActivationType().triggerOnAttackEffect(potEffect, other, source);
                    } else {
                        if(activationType == EnumActivationTypes.ON_ATTACKING_SELF)
                            potEffect.getActivationType().triggerOnAttackEffect(potEffect, player, source);
                        if(activationType == EnumActivationTypes.ON_ATTACKING_TARGET)
                            potEffect.getActivationType().triggerOnAttackEffect(potEffect, other, source);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving().world.isRemote ||
                !(event.getEntityLiving() instanceof EntityPlayer) ||
                event.getEntityLiving().ticksExisted%20 != 0
        ) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {//Iterate active slots
            ItemStack stack = player.getItemStackFromSlot(slot);
            if(!stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null)) continue;

            List<GenericGemEffect> effects = stack.getCapability(CapabilityHasSockets.HAS_SOCKETS,null).getAllEffectsFromAllSockets();
            for(GenericGemEffect effect : effects){
                //Activated effect
                if(effect instanceof ActivatableGemEffect) {
                    ActivatableGemEffect actEffect = (ActivatableGemEffect)effect;
                    if(actEffect.getActivationType() == EnumActivationTypes.PASSIVE)
                        actEffect.getActivationType().triggerPerSecondEffect(actEffect, player);
                }
            }
        }
    }

    private static final List<IAttribute> offhandSkipAttributes = Arrays.asList(SharedMonsterAttributes.ATTACK_DAMAGE, SharedMonsterAttributes.ATTACK_SPEED);


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        if(event.getEntityLiving().world.isRemote) return;
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        EntityEquipmentSlot slot = event.getSlot();
        ItemStack stackOld = event.getFrom();
        ItemStack stackNew = event.getTo();
        //Reskillable changes the value behind the "to" itemstack to empty if requirements are not met, but it shouldn't matter
        //ItemStack stackNew = player.getItemStackFromSlot(slot);

        //Remove all modifiers that were on removed item
        if(stackOld.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null)){
            List<GenericGemEffect> effectsOld = stackOld.getCapability(CapabilityHasSockets.HAS_SOCKETS, null).getAllEffectsFromAllSockets();
            for (GenericGemEffect effect : effectsOld) {
                if (effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect) effect;

                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if (attrInstance == null) continue;

                    //Skip damage/speed/reach attributes for offhand, let 2 hand mods like RLCombat handle the compat properly
                    if (slot == EntityEquipmentSlot.OFFHAND && (offhandSkipAttributes.contains(attrInstance.getAttribute()) || attribute.contains("reachDistance")))
                        continue;

                    AttributeModifier modifier = attrEffect.getModifier();
                    if (attrInstance.hasModifier(modifier))
                        attrInstance.removeModifier(modifier);
                }
            }
        }

        //Apply new modifiers from new item
        if(stackNew.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null)) {
            List<GenericGemEffect> effectsNew = stackNew.getCapability(CapabilityHasSockets.HAS_SOCKETS, null).getAllEffectsFromAllSockets();
            for (GenericGemEffect effect : effectsNew) {
                if (effect instanceof AttributeGemEffect) {
                    AttributeGemEffect attrEffect = (AttributeGemEffect) effect;

                    String attribute = attrEffect.getAttribute();
                    IAttributeInstance attrInstance = player.getAttributeMap().getAttributeInstanceByName(attribute);
                    if (attrInstance == null) continue;

                    //Skip damage/speed/reach attributes for offhand, let 2 hand mods like RLCombat handle the compat properly
                    if (slot == EntityEquipmentSlot.OFFHAND && (offhandSkipAttributes.contains(attrInstance.getAttribute()) || attribute.contains("reachDistance")))
                        continue;

                    AttributeModifier modifier = attrEffect.getModifier();
                    if (!attrInstance.hasModifier(modifier))
                        attrInstance.applyModifier(modifier);
                }
            }
        }
    }
}