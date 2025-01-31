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

import javax.annotation.Nonnull;

public class PotionGemEffect extends ActivatableGemEffect {
    
    public static final String TYPE_NAME = "Potion";
    
    @SerializedName("Potion Name")
    private final String potionName;

    @SerializedName("Amplifier")
    private final int amplifier;

    @SerializedName("Duration")
    private final int duration;

    private transient Potion potion;

    public PotionGemEffect(ISlotType slotType, IActivationType activationType, String potionName, int amplifier, int duration) {
        super(slotType, activationType);
        this.potionName = potionName;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    @Nonnull
    public Potion getPotion() {
        return this.potion;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public void performEffect(EntityLivingBase entity) {
        if(entity != null && !entity.world.isRemote) {
            entity.addPotionEffect(new PotionEffect(this.getPotion(), this.getDuration(), this.getAmplifier()));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        String tooltip = I18n.format("socketed.tooltip.activationtype." + this.getActivationType().getTooltipKey(), I18n.format(this.getPotion().getName()));
        int potionLvl = this.getAmplifier() + 1;
        if(potionLvl == 1) return tooltip;
        else if(potionLvl > 1 && potionLvl <= 10) return tooltip + " " + I18n.format("enchantment.level." + potionLvl);
        else return tooltip + " " + potionLvl;
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.potionName == null || this.potionName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, potion name null or empty");
            else {
                this.potion = Potion.getPotionFromResourceLocation(this.potionName);
                if(this.potion == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, " + this.potionName + ", potion does not exist");
                else if(this.amplifier < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, " + this.potionName + ", amplifier can not be less than 0");
                else if(this.duration < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, " + this.potionName + ", duration can not be less than 0");
                else return true;
            }
        }
        return false;
    }
}