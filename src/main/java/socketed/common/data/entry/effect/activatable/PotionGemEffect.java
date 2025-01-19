package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import socketed.Socketed;

import javax.annotation.Nullable;
import java.util.List;

public class PotionGemEffect extends ActivatableGemEffect {

    public static final String FILTER_NAME = "Potion";

    @SerializedName("Potion Name")
    private final String potionName;

    @SerializedName("Amplifier")
    private final int amplifier;

    @SerializedName("Duration")
    private final int duration;

    private transient Potion potion;

    public PotionGemEffect(List<EntityEquipmentSlot> slots, String potionName, int amplifier, int duration, EnumActivationType activation) {
        super(slots);
        this.potionName = potionName;
        this.amplifier = amplifier;
        this.duration = duration;
        this.activation = activation;
        this.type = FILTER_NAME;
    }

    @Nullable
    public Potion getPotion() {
        if(!this.isValid()) return null;
        return this.potion;
    }

    public int getAmplifier() {
        if(!this.isValid()) return 0;
        return this.amplifier;
    }

    public int getDuration() {
        if(!this.isValid() || this.getActivationType() == EnumActivationType.PASSIVE) return 21;
        return this.amplifier;
    }

    @Override
    protected void performEffect(EntityLivingBase entity) {
        if(this.getPotion() != null) {
            entity.addPotionEffect(new PotionEffect(this.getPotion(), this.getDuration(), this.getAmplifier()));
        }
    }

    @Override
    protected void validate() {
        this.valid = false;
        if(this.potionName == null || this.potionName.isEmpty()) Socketed.LOGGER.warn("Invalid Potion Effect entry, name null or empty");
        else if(Potion.getPotionFromResourceLocation(this.potionName) == null) Socketed.LOGGER.warn("Invalid Potion Effect entry, " + this.potionName + ", potion does not exist");
        else if(this.amplifier < 0) Socketed.LOGGER.warn("Invalid Potion Effect entry, " + this.potionName + ", amplifier can not be less than 0");
        else if(this.duration < 0) Socketed.LOGGER.warn("Invalid Potion Effect entry, " + this.potionName + ", duration can not be less than 0");
        else if(this.activation == null) Socketed.LOGGER.warn("Invalid Potion Effect entry, " + this.potionName + ", invalid activation type");
        else {
            this.potion = Potion.getPotionFromResourceLocation(this.potionName);
            this.valid = true;
        }
        this.parsed = true;
    }

}