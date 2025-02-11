package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nonnull;

public class PotionGemEffect extends ActivatableGemEffect {
    
    public static final String TYPE_NAME = "Potion";
    
    @SerializedName("Potion Name")
    private final String potionName;

    @SerializedName("Amplifier")
    private final Integer amplifier;

    @SerializedName("Duration")
    private final Integer duration;

    private transient Potion potion;
    
    public PotionGemEffect(ISlotType slotType, GenericActivator activator, String potionName, int amplifier, int duration) {
        super(slotType, activator);
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
    public void performEffect(EntityPlayer player, EntityLivingBase entity) {
        if(entity != null && !entity.world.isRemote) {
            entity.addPotionEffect(new PotionEffect(this.getPotion(), this.getDuration(), this.getAmplifier()));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        String tooltip = this.getActivatorType().getTooltipString() + " " + I18n.format(this.getPotion().getName());
        int potionLvl = this.getAmplifier() + 1;
        if(potionLvl == 1) return tooltip;
        else if(potionLvl > 1 && potionLvl <= 10) return tooltip + " " + I18n.format("enchantment.level." + potionLvl);
        else return tooltip + " " + potionLvl;
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    /**
     * PotionName: Required
     * Amplifier: Required
     * Duration: Required
     */
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.potionName == null || this.potionName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, potion name null or empty");
            else {
                this.potion = Potion.getPotionFromResourceLocation(this.potionName);
                if(this.potion == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.potionName + ", potion does not exist");
                else if(this.amplifier == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.potionName + ", amplifier must be defined");
                else if(this.amplifier < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.potionName + ", amplifier can not be less than 0");
                else if(this.duration == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.potionName + ", duration must be defined");
                else if(this.duration < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.potionName + ", duration can not be less than 0");
                else return true;
            }
        }
        return false;
    }
}