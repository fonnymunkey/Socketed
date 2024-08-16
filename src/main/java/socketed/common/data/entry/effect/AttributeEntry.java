package socketed.common.data.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

import java.util.UUID;

public class AttributeEntry extends EffectEntry {
    private static final AttributeModifier INVALID = new AttributeModifier(new UUID(1,1), "INVALID", 0, 0);

    public static final String FILTER_NAME = "Attribute";

    @SerializedName("Unique Modifier Name")
    private final String name;

    @SerializedName("Attribute Name")
    private final String attribute;

    @SerializedName("Modifier Amount")
    private final double amount;

    @SerializedName("Modifier Operation")
    private final int operation;

    private transient AttributeModifier modifier;

    public AttributeEntry(String name, String attribute, double amount, int operation) {
        this.name = name;
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.type = FILTER_NAME;
    }

    public AttributeModifier getModifier() {
        if(!this.isValid()) return INVALID;
        return this.modifier;
    }

    public String getAttribute() {
        if(!this.isValid()) return "INVALID";
        return this.attribute.trim();
    }

    @Override
    protected void validate() {
        this.valid = false;
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Attribute Effect entry, name null or empty");
        else if(this.attribute == null || this.attribute.trim().isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Attribute Effect entry, " + this.name + ", attribute null or empty");
        else if(this.operation < 0 || this.operation > 2) Socketed.LOGGER.log(Level.WARN, "Invalid Attribute Effect entry, " + this.name + ", operation must be 0, 1, or 2");
        else {
            this.modifier = new AttributeModifier(new UUID(1, 1), this.name, this.amount, this.operation);
            this.valid = true;
        }
        this.parsed = true;
    }
}