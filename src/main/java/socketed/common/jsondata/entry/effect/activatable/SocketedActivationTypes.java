package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public enum SocketedActivationTypes implements IActivationType {
    
    @SerializedName("INVALID")
    INVALID {
        
        @Override
        public String getTooltipKey() {
            return "invalid";
        }
    },

    @SerializedName("PASSIVE_SELF")
    PASSIVE_SELF {
        
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            entry.performEffect(player);
        }
        
        @Override
        public String getTooltipKey() {
            return "passive_self";
        }
    },
    @SerializedName("PASSIVE_NEARBY")
    PASSIVE_NEARBY {
        
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(8.0D));
            for(EntityLivingBase entity : entitiesNearby) {
                if(entity != player) entry.performEffect(entity);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "passive_nearby";
        }
    },
    @SerializedName("PASSIVE_FAR")
    PASSIVE_FAR {
        
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(16.0D));
            for(EntityLivingBase entity : entitiesNearby) {
                if(entity != player) entry.performEffect(entity);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "passive_far";
        }
    },

    @SerializedName("ON_ATTACKED_SELF")
    ON_ATTACKED_SELF {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_self";
        }
    },
    @SerializedName("ON_ATTACKED_SELF_MELEE")
    ON_ATTACKED_SELF_MELEE {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received && SocketedActivationTypes.isDamageSourceMelee(source)) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_self_melee";
        }
    },
    @SerializedName("ON_ATTACKED_SELF_RANGED")
    ON_ATTACKED_SELF_RANGED {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received && SocketedActivationTypes.isDamageSourceRanged(source)) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_self_ranged";
        }
    },

    @SerializedName("ON_ATTACKED_ATTACKER")
    ON_ATTACKED_ATTACKER {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received && source.getTrueSource() instanceof EntityLivingBase) {
                entry.performEffect((EntityLivingBase)source.getTrueSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_attacker";
        }
    },
    @SerializedName("ON_ATTACKED_ATTACKER_MELEE")
    ON_ATTACKED_ATTACKER_MELEE {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received && SocketedActivationTypes.isDamageSourceMelee(source)) {
                entry.performEffect((EntityLivingBase)source.getImmediateSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_attacker_melee";
        }
    },
    @SerializedName("ON_ATTACKED_ATTACKER_RANGED")
    ON_ATTACKED_ATTACKER_RANGED {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(received && SocketedActivationTypes.isDamageSourceRanged(source) && source.getTrueSource() instanceof EntityLivingBase) {
                entry.performEffect((EntityLivingBase)source.getTrueSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacked_attacker_ranged";
        }
    },

    @SerializedName("ON_ATTACKING_SELF")
    ON_ATTACKING_SELF {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received && source.getTrueSource() instanceof EntityLivingBase) {
                entry.performEffect((EntityLivingBase)source.getTrueSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_self";
        }
    },
    @SerializedName("ON_ATTACKING_SELF_MELEE")
    ON_ATTACKING_SELF_MELEE {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received && SocketedActivationTypes.isDamageSourceMelee(source)) {
                entry.performEffect((EntityLivingBase)source.getImmediateSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_self_melee";
        }
    },
    @SerializedName("ON_ATTACKING_SELF_RANGED")
    ON_ATTACKING_SELF_RANGED {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received && SocketedActivationTypes.isDamageSourceRanged(source) && source.getTrueSource() instanceof EntityLivingBase) {
                entry.performEffect((EntityLivingBase)source.getTrueSource());
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_self_ranged";
        }
    },

    @SerializedName("ON_ATTACKING_TARGET")
    ON_ATTACKING_TARGET {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_target";
        }
    },
    @SerializedName("ON_ATTACKING_TARGET_MELEE")
    ON_ATTACKING_TARGET_MELEE {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received && SocketedActivationTypes.isDamageSourceMelee(source)) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_target_melee";
        }
    },
    @SerializedName("ON_ATTACKING_TARGET_RANGED")
    ON_ATTACKING_TARGET_RANGED {
        
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase victim, DamageSource source, boolean received) {
            if(!received && SocketedActivationTypes.isDamageSourceRanged(source)) {
                entry.performEffect(victim);
            }
        }
        
        @Override
        public String getTooltipKey() {
            return "on_attacking_target_ranged";
        }
    };
    
    private static boolean isDamageSourceMelee(DamageSource source) {
        return source.getImmediateSource() instanceof EntityLivingBase && !(source instanceof EntityDamageSourceIndirect);
    }
    
    private static boolean isDamageSourceRanged(DamageSource source) {
        return !(source.getImmediateSource() instanceof EntityLivingBase) && source instanceof EntityDamageSourceIndirect;
    }
}