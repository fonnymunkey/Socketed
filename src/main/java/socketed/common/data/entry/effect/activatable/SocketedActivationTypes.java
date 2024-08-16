package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;

public enum SocketedActivationTypes implements IActivationType {
    @SerializedName(Socketed.MODID + ":" + "INVALID")
    INVALID,
    @SerializedName(Socketed.MODID + ":" + "PASSIVE")
    PASSIVE {
        @Override
        public void triggerTickEffect(ActivatableEntry entry, EntityPlayer player) {
            entry.performEffect(player);
        }

        @Override
        public String getKey() { return "passive"; }
    },
    ATTACKED,
    ATTACKED_REFLECTIVE,
    ATTACKING,
    ATTACKIN_REFLECTIVE;

    public static IActivationType fromValue(String value) { return valueOf(value); }
}
    /*
    public static class ActivationType {
        @SerializedName("INVALID")
        public static final ActivationType INVALID = new ActivationType("invalid");
        @SerializedName("PASSIVE")
        public static final ActivationType PASSIVE = new ActivationType("passive") {
            @Override
            public void triggerSecondEffect(ActivatableEntry entry, EntityPlayer player) {
                Socketed.LOGGER.log(Level.INFO, "Called second passive");
                if(player != null) entry.performEffect(player);
            }
        };
        //public static final ActivationType DAMAGE_RECEIVED_SELF = new ActivationType("invalid");
        //public static final ActivationType DAMAGE_RECEIVED_ATTACKER = new ActivationType("invalid");
        //public static final ActivationType DAMAGE_GIVEN_SELF = new ActivationType("invalid");
        //public static final ActivationType DAMAGE_GIVEN_TARGET = new ActivationType("invalid");

        private final String translateKey;

        public ActivationType(String translateKey) { this.translateKey = translateKey; }

        public String getTranslateKey() { return this.translateKey; }

        public void triggerSecondEffect(ActivatableEntry entry, EntityPlayer player) {
            Socketed.LOGGER.log(Level.INFO, "Called second abstract");
        }

        public void triggerHitEffect(ActivatableEntry entry, EntityLivingBase target, DamageSource source) { }

    }
    */