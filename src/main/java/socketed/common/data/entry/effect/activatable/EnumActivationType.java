package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import socketed.Socketed;

public enum EnumActivationType implements IActivationType {
    @SerializedName(Socketed.MODID + ":"+"INVALID")
    INVALID {
        @Override
        public String getToolTipKey() {
            return "invalid";
        }
    },

    @SerializedName(Socketed.MODID + ":"+"PASSIVE")
    PASSIVE {
        @Override
        public void triggerPerSecondEffect(ActivatableGemEffect entry, EntityPlayer player) {
            entry.performEffect(player);
        }
        @Override
        public String getToolTipKey() {
            return "passive";
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
    };
}
    
    
    