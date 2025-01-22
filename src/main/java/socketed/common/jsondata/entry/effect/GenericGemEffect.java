package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class GenericGemEffect {
    public static final String FILTER_NAME = "Effect Type";

    @SerializedName(FILTER_NAME)
    protected String type;
    @SerializedName("Equipment Slots")
    protected EnumSlots slots;

    protected transient boolean valid;

    protected transient boolean parsed;

    public GenericGemEffect(EnumSlots slots){
        this.slots = slots;
    }

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    public EnumSlots getEnumSlots(){
        return slots;
    }
    public List<EntityEquipmentSlot> getSlots(){
        return EnumSlots.getSlots(this.slots);
    }

    protected void validate() {
        //noop, slots are already auto validated
    }

    /**
     * Lazy instantiation. Returns the default instance if subclass doesn't overwrite this
     */
    public GenericGemEffect instantiate() {
        return this;
    }

    public NBTTagCompound writeToNBT() {
        return new NBTTagCompound();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //noop
    }
}