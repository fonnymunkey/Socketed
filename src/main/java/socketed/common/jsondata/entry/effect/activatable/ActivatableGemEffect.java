package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.activator.GenericActivator;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

import javax.annotation.Nonnull;

public abstract class ActivatableGemEffect extends GenericGemEffect {

    @SerializedName("Activator")
    protected final GenericActivator activatorType;
    
    protected ActivatableGemEffect(ISlotType slotType, GenericActivator activatorType) {
        super(slotType);
        this.activatorType = activatorType;
    }
    
    @Nonnull
    public GenericActivator getActivatorType() {
        return this.activatorType;
    }

    public abstract void performEffect(EntityLivingBase entity);
    
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.activatorType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, invalid activator");
            else if(!this.activatorType.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect entry, invalid activator");
            else return true;
        }
        return false;
    }
}