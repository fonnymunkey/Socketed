package socketed.common.handlers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.config.CustomConfig;
import socketed.common.data.EffectGroup;
import socketed.common.data.entry.effect.activatable.ActivatableEntry;
import socketed.common.data.entry.effect.AttributeEntry;
import socketed.common.data.entry.effect.EffectEntry;
import socketed.common.util.SocketedUtil;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class EffectHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityHit(LivingAttackEvent event) {
        /*
        if(event.getEntity().world.isRemote) return;
        boolean indirect =
        if(event.getEntity() instanceof EntityPlayer) {

        }
            handleHitEffects((EntityPlayer)event.getEntity(), event.getSource().getImmediateSource(), true, true);
        if(event.getSource().getImmediateSource() instanceof EntityPlayer)//TODO: add config for ranged vs melee
            handleHitEffects((EntityPlayer)event.getSource().getImmediateSource(), event.getEntity(), false);

         */
    }
/*
    private static void handleHitEffects(EntityPlayer player, @Nullable Entity other, boolean received) {
        Socketed.LOGGER.log(Level.INFO, "Handling hit effects");
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {//Iterate active slots
            ItemStack stack = player.getItemStackFromSlot(slot);
            String effectName = SocketedUtil.getSocketEffectName(stack);
            if(!effectName.isEmpty()) {
                Socketed.LOGGER.log(Level.INFO, "Effect item found: " + effectName);
                EffectGroup group = CustomConfig.getEffectData().get(effectName);//Get effect group of slot
                if(group == null) continue;
                for(EffectEntry effect : group.getEffectEntries()) {//Iterate socket effects
                    //No attribute from hit effect currently
                    //Potion effect
                    if(effect instanceof PotionEntry) {
                        Socketed.LOGGER.log(Level.INFO, "Potion entry found");
                        PotionEntry potEffect = (PotionEntry)effect;
                        if(potEffect.getPotion() == null) continue;
                        if(received) {
                            if(potEffect.getActivationType() == PotionEntry.ActivationType.DAMAGE_RECEIVED_SELF) {
                                Socketed.LOGGER.log(Level.INFO, "Applying " + potEffect.getPotion().getName() + " to " + player.getName());
                                player.addPotionEffect(new PotionEffect(potEffect.getPotion(), potEffect.getDuration(), potEffect.getAmplifier()));
                            }
                            else if(potEffect.getActivationType() == PotionEntry.ActivationType.DAMAGE_RECEIVED_ATTACKER && other instanceof EntityLivingBase) {
                                Socketed.LOGGER.log(Level.INFO, "Applying " + potEffect.getPotion().getName() + " to " + other.getName());
                                ((EntityLivingBase)other).addPotionEffect(new PotionEffect(potEffect.getPotion(), potEffect.getDuration(), potEffect.getAmplifier()));
                            }
                        }
                        else {
                            if(potEffect.getActivationType() == PotionEntry.ActivationType.DAMAGE_GIVEN_SELF) {
                                Socketed.LOGGER.log(Level.INFO, "Applying " + potEffect.getPotion().getName() + " to " + player.getName());
                                player.addPotionEffect(new PotionEffect(potEffect.getPotion(), potEffect.getDuration(), potEffect.getAmplifier()));
                            }
                            else if(potEffect.getActivationType() == PotionEntry.ActivationType.DAMAGE_GIVEN_TARGET && other instanceof EntityLivingBase) {
                                Socketed.LOGGER.log(Level.INFO, "Applying " + potEffect.getPotion().getName() + " to " + other.getName());
                                ((EntityLivingBase)other).addPotionEffect(new PotionEffect(potEffect.getPotion(), potEffect.getDuration(), potEffect.getAmplifier()));
                            }
                        }
                    }
                }
            }
        }
    }

 */

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving().world.isRemote || !(event.getEntityLiving() instanceof EntityPlayer) || event.getEntityLiving().ticksExisted%20 != 0) return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();
        Multimap<String, AttributeModifier> previousModifiers = HashMultimap.create();
        player.getAttributeMap().getAllAttributes()
                .forEach(a -> a.getModifiers().stream()
                        .filter(m -> m.getName().startsWith("socketed."))
                        .forEach(m -> previousModifiers.put(a.getAttribute().getName(), m)));//Get all active socketed modifiers
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {//Iterate active slots
            ItemStack stack = player.getItemStackFromSlot(slot);
            String effectName = SocketedUtil.getSocketEffectName(stack);
            if(!effectName.isEmpty()) {
                EffectGroup group = CustomConfig.getEffectData().get(effectName);//Get effect group of slot
                if(group == null) continue;
                for(EffectEntry effect : group.getEffectEntries()) {//Iterate socket effects
                    //Attribute effect
                    if(effect instanceof AttributeEntry) {
                        AttributeEntry attrEffect = (AttributeEntry)effect;
                        boolean active = false;
                        //Skip damage/speed/reach attributes for offhand, let 2 hand mods like RLCombat handle the compat properly
                        if(slot == EntityEquipmentSlot.OFFHAND && (
                                attrEffect.getAttribute().contains("attackDamage") ||
                                attrEffect.getAttribute().contains("attackSpeed") ||
                                attrEffect.getAttribute().contains("reachDistance"))) {
                            continue;
                        }
                        //Iterate active modifiers, remove from previous list if it should stay
                        for(Iterator<AttributeModifier> iter = previousModifiers.get(attrEffect.getAttribute()).iterator(); iter.hasNext();) {
                            if(iter.next().getName().startsWith(
                                    "socketed." +
                                    slot.getName() +
                                    "." +
                                    attrEffect.getModifier().getName())) {
                                iter.remove();
                                active = true;
                                break;
                            }
                        }
                        //Apply new modifier if not already active
                        if(!active) {
                            IAttributeInstance inst = player.getAttributeMap().getAttributeInstanceByName((attrEffect.getAttribute()));
                            AttributeModifier newMod = new AttributeModifier(
                                    "socketed." +
                                            slot.getName() +
                                            "." +
                                            attrEffect.getModifier().getName(),
                                    attrEffect.getModifier().getAmount(),
                                    attrEffect.getModifier().getOperation());
                            if(inst != null && !inst.hasModifier(newMod)) {
                                inst.applyModifier(newMod);
                            }
                        }
                    }
                    //Activated effect
                    else if(effect instanceof ActivatableEntry) {
                        ActivatableEntry actEffect = (ActivatableEntry)effect;
                        actEffect.getActivationType().triggerOnSecondEffect(actEffect, player);
                    }
                }
            }
        }
        //Remove any leftover effects that shouldn't be active
        player.getAttributeMap().removeAttributeModifiers(previousModifiers);
    }
}