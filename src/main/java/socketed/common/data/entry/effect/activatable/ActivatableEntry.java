package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import socketed.common.data.entry.effect.EffectEntry;

public abstract class ActivatableEntry extends EffectEntry {
    public static final String FILTER_NAME = "Activation Type";

    @SerializedName(ActivatableEntry.FILTER_NAME)
    protected IActivationType activation;

    public IActivationType getActivationType() {
        if(!this.isValid()) return SocketedActivationTypes.INVALID;
        return this.activation;
    }

    protected abstract void performEffect(EntityLivingBase entity);
}