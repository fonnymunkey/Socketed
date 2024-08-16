package socketed.common.data.entry.effect;

import com.google.gson.annotations.SerializedName;

public abstract class EffectEntry {
    public static final String FILTER_NAME = "Effect Type";

    @SerializedName(FILTER_NAME)
    protected String type;

    protected transient boolean valid;

    protected transient boolean parsed;

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    protected abstract void validate();
    
}