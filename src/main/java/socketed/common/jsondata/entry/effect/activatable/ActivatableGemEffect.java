package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

public abstract class ActivatableGemEffect extends GenericGemEffect {
    
    public static final String FILTER_NAME = "Activation Type";

    @SerializedName(ActivatableGemEffect.FILTER_NAME)
    protected IActivationType activation;

    public ActivatableGemEffect(ISlotType slotType) {
        super(slotType);
    }

    public IActivationType getActivationType() {
        if(!this.isValid()) return SocketedActivationTypes.INVALID;
        return this.activation;
    }

    protected abstract void performEffect(EntityLivingBase entity);
}