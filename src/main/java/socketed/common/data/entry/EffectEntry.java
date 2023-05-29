package socketed.common.data.entry;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

public class EffectEntry {
    private static final AttributeModifier INVALID = new AttributeModifier("INVALID", 0, 0);


    @SerializedName("Unique Modifier Name")
    private final String name;
    @SerializedName("Attribute Name")
    private final String attribute;
    @SerializedName("Modifier Amount")
    private final double amount;
    @SerializedName("Modifier Operation")
    private final int operation;

    private transient AttributeModifier modifier;
    private transient boolean parsed;
    private transient boolean valid;

    public EffectEntry(String name, String attribute, double amount, int operation) {
        this.name = name;
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier getModifier() {
        if(!this.isValid()) return INVALID;
        return this.modifier;
    }

    public String getAttribute() {
        if(!this.isValid()) return "INVALID";
        return this.attribute.trim();
    }

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    private void validate() {
        this.valid = false;
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Effect entry, name null or empty");
        else if(this.attribute == null || this.attribute.trim().isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Effect entry, " + this.name + ", attribute null or empty");
        else if(this.operation < 0 || this.operation > 2) Socketed.LOGGER.log(Level.WARN, "Invalid Effect entry, " + this.name + ", operation must be 0, 1, or 2");
        else {
            this.modifier = new AttributeModifier(this.name, this.amount, this.operation);
            this.valid = true;
        }
        this.parsed = true;
    }
}