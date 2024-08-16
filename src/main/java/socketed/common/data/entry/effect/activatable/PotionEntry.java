package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

import javax.annotation.Nullable;

public class PotionEntry extends ActivatableEntry {

    public static final String FILTER_NAME = "Potion";

    @SerializedName("Potion Name")
    private final String name;

    @SerializedName("Amplifier")
    private final int amplifier;

    @SerializedName("Duration")
    private final int duration;

    private transient Potion potion;

    public PotionEntry(String name, int amplifier, int duration, IActivationType activation) {
        this.name = name;
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
        if(!this.isValid() || this.getActivationType() == SocketedActivationTypes.PASSIVE) return 21;
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
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Potion Effect entry, name null or empty");
        else if(Potion.getPotionFromResourceLocation(this.name) == null) Socketed.LOGGER.log(Level.WARN, "Invalid Potion Effect entry, " + this.name + ", potion does not exist");
        else if(this.amplifier < 0) Socketed.LOGGER.log(Level.WARN, "Invalid Potion Effect entry, " + this.name + ", amplifier can not be less than 0");
        else if(this.duration < 0) Socketed.LOGGER.log(Level.WARN, "Invalid Potion Effect entry, " + this.name + ", duration can not be less than 0");
        else if(this.activation == null) Socketed.LOGGER.log(Level.WARN, "Invalid Potion Effect entry, " + this.name + ", invalid activation type");
        else {
            this.potion = Potion.getPotionFromResourceLocation(this.name);
            this.valid = true;
        }
        this.parsed = true;
    }

}