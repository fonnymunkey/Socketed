package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;

public abstract class ActivatableGemEffect extends GenericGemEffect {

    @SerializedName("Activation Type")
    protected final IActivationType activationType;
    
    protected ActivatableGemEffect(ISlotType slotType, IActivationType activationType) {
        super(slotType);
        this.activationType = activationType;
    }
    
    @Nonnull
    public IActivationType getActivationType() {
        return this.activationType;
    }

    public abstract void performEffect(EntityLivingBase entity);
    
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.activationType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, invalid activation type");
            else return true;
        }
        return false;
    }
}