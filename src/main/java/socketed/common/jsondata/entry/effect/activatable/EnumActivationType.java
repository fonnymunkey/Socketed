package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import socketed.Socketed;

import java.util.List;

public enum EnumActivationType implements IActivationType {
    @SerializedName(Socketed.MODID + ":"+"INVALID")
    INVALID {
        @Override
        public String getToolTipKey() {
            return "invalid";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"PASSIVE_SELF")
    PASSIVE_SELF {
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            entry.performEffect(player);
        }
        @Override
        public String getToolTipKey() {
            return "passive_self";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"PASSIVE_NEARBY")
    PASSIVE_NEARBY{
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(player.getPosition()).grow(8.));
            for(EntityLivingBase entity : entitiesNearby)
                if(entity != player)
                    entry.performEffect(entity);
        }
        @Override
        public String getToolTipKey() {
            return "passive_nearby";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"PASSIVE_FAR")
    PASSIVE_FAR{
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            List<EntityLivingBase> entitiesNearby = player.world.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(player.getPosition()).grow(32.));
            for(EntityLivingBase entity : entitiesNearby)
                if(entity != player)
                    entry.performEffect(entity);
        }
        @Override
        public String getToolTipKey() {
            return "passive_far";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"ON_ATTACKED_SELF")
    ON_ATTACKED_SELF {
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase effectTarget, DamageSource source) {
            entry.performEffect(effectTarget);
        }
        @Override
        public String getToolTipKey() {
            return "on_attacked_self";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"ON_ATTACKED_ATTACKER")
    ON_ATTACKED_ATTACKER {
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase effectTarget, DamageSource source) {
            entry.performEffect(effectTarget);
        }
        @Override
        public String getToolTipKey() {
            return "on_attacked_attacker";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"ON_ATTACKING_SELF")
    ON_ATTACKING_SELF {
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase effectTarget, DamageSource source) {
            entry.performEffect(effectTarget);
        }
        @Override
        public String getToolTipKey() {
            return "on_attacking_self";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"ON_ATTACKING_TARGET")
    ON_ATTACKING_TARGET {
        @Override
        public void triggerOnAttackEffect(ActivatableGemEffect entry, EntityLivingBase effectTarget, DamageSource source) {
            entry.performEffect(effectTarget);
        }
        @Override
        public String getToolTipKey() {
            return "on_attacking_target";
        }
    }
}
    
    
    