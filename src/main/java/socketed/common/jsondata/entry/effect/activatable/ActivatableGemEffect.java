package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import java.util.List;

public abstract class ActivatableGemEffect extends GenericGemEffect {
    public static final String FILTER_NAME = "Activation Type";

    @SerializedName(ActivatableGemEffect.FILTER_NAME)
    protected IActivationType activation;

    public ActivatableGemEffect(List<EntityEquipmentSlot> slots) {
        super(slots);
    }

    public IActivationType getActivationType() {
        if(!this.isValid()) return EnumActivationType.INVALID;
        return this.activation;
    }

    protected abstract void performEffect(EntityLivingBase entity);
}