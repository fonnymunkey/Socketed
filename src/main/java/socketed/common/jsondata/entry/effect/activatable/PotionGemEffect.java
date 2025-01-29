package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

import javax.annotation.Nullable;

public class PotionGemEffect extends ActivatableGemEffect {

    public static final String TYPE_NAME = "Potion";

    @SerializedName("Potion Name")
    private final String potionName;

    @SerializedName("Amplifier")
    private final int amplifier;

    @SerializedName("Duration")
    private final int duration;

    private transient Potion potion;

    public PotionGemEffect(ISlotType slotType, String potionName, int amplifier, int duration, SocketedActivationTypes activation) {
        super(slotType);
        this.potionName = potionName;
        this.amplifier = amplifier;
        this.duration = duration;
        this.activation = activation;
        this.type = TYPE_NAME;
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
        if(!this.isValid()) return 0;
        return this.duration;
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        Potion potion = this.getPotion();
        if(potion == null) return "";
        String tooltip = I18n.format("socketed.tooltip.activationtype." + this.getActivationType().getTooltipKey(), I18n.format(potion.getName()));
        int potionLvl = this.getAmplifier() + 1;
        if(potionLvl == 1) return tooltip;
        else if(potionLvl > 1 && potionLvl <= 10) return tooltip + " " + I18n.format("enchantment.level." + potionLvl);
        else return tooltip + " " + potionLvl;
    }
}