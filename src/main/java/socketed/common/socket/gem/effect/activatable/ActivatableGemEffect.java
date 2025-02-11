package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import socketed.Socketed;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.slot.ISlotType;

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
    
    /**
     * ActivatorType: Required
     */
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.activatorType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid activator");
            else if(!this.activatorType.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid activator");
            else return true;
        }
        return false;
    }
}