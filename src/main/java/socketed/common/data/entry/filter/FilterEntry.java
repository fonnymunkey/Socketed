package socketed.common.data.entry.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;

public abstract class FilterEntry {

    public static final String FILTER_NAME = "Filter Type";

    @SerializedName(FILTER_NAME)
    public String type;

    protected transient boolean parsed;

    protected transient boolean valid;

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    protected abstract void validate();

    public abstract boolean matches(ItemStack input);

}