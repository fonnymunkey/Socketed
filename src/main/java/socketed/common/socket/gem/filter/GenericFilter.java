package socketed.common.socket.gem.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;

public abstract class GenericFilter {

    public static final String TYPE_FIELD = "Filter Type";

    @SerializedName(TYPE_FIELD)
    protected final String type = this.getTypeName();
    
    protected GenericFilter() {
    
    }
    
    /**
     * Checks a given itemstack against this filter
     * @param input the itemstack to be checked against the filter
     * @return true if the given itemstack is valid for this filter
     */
    public abstract boolean matches(ItemStack input);
    
    /**
     * @return the user readable type name for the subclass used for deserialization
     */
    public abstract String getTypeName();
    
    /**
     * Attempts to validate this filter and setup caches from parsed values, such as Item references
     * @return false if any required value is invalid, which should result in discarding this filter
     */
    public abstract boolean validate();
}