package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

public abstract class GenericGemEffect {
    
    public static final String TYPE_FIELD = "Effect Type";

    @SerializedName(TYPE_FIELD)
    protected final String type = this.getTypeName();
    @SerializedName("Equipment Slot Type")
    protected final ISlotType slotType;
    
    protected GenericGemEffect(ISlotType slotType) {
        this.slotType = slotType;
    }

    public ISlotType getSlotType() {
        return this.slotType;
    }
    
    @SideOnly(Side.CLIENT)
    public abstract String getTooltipString(boolean onItem);
    
    /**
     * @return the user readable type name for the subclass used for deserialization
     */
    public abstract String getTypeName();
    
    /**
     * Instantiates this effect for application to sockets
     * Allows for storing variable values such as RandomValueRange if needed
     * @return an instantiated copy of this, or this itself if instantiation is not needed
     */
    public GenericGemEffect instantiate() {
        return this;
    }
    
    /**
     * Attempts to validate this effect and setup caches from parsed values, such as Potion references
     * @return false if any required value is invalid, which should result in discarding this effect
     */
    public boolean validate() {
        if(this.slotType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid slot type");
        else return true;
        return false;
    }
    
    /**
     * Writes this effect to NBT to allow for storing values from instantiated effects
     * @return null if this effect is non-instantiated
     */
    public NBTTagCompound writeToNBT() {
        return new NBTTagCompound();
    }
    
    /**
     * Reads this effect from NBT to allow for storing values from instantiated effects
     */
    public void readFromNBT(NBTTagCompound nbt) {
        /*noop*/
    }
}