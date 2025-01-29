package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

public class GenericGemEffect {
    
    public static final String TYPE_FIELD = "Effect Type";

    @SerializedName(TYPE_FIELD)
    protected String type;
    @SerializedName("Equipment Slots")
    protected ISlotType slotType;

    protected transient boolean valid;

    protected transient boolean parsed;

    public GenericGemEffect(ISlotType slotType) {
        this.slotType = slotType;
    }

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    public ISlotType getSlotType() {
        return this.slotType;
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
    
    @SideOnly(Side.CLIENT)
    public String getTooltipString(boolean onItem) {
        return "";
    }
}