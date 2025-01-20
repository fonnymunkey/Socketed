package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

import java.util.List;
import java.util.UUID;

public class AttributeGemEffect extends GenericGemEffect {
    private static final AttributeModifier INVALID = new AttributeModifier(new UUID(1,1), "INVALID", 0, 0);

    public static final String FILTER_NAME = "Attribute";

    @SerializedName("Attribute Name")
    private final String attribute;

    @SerializedName("Modifier Amount")
    private final double amount;

    @SerializedName("Modifier Operation")
    private final int operation;

    private transient AttributeModifier modifier = INVALID;

    public AttributeGemEffect(List<EntityEquipmentSlot> slots, String attribute, double amount, int operation) {
        super(slots);
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.type = FILTER_NAME;
    }

    public AttributeGemEffect(AttributeGemEffect effect){
        super(effect.getSlots());
        this.attribute = effect.attribute;
        this.amount = effect.amount;
        this.operation = effect.operation;

        //Only instantiated AttributeGemEffects have valid modifiers
        this.modifier = new AttributeModifier(UUID.randomUUID(),Socketed.MODID+"GemEffect", this.amount, this.operation);
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
        if(this.attribute == null || this.attribute.trim().isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Attribute Effect entry, attribute null or empty");
        else if(this.operation < 0 || this.operation > 2) Socketed.LOGGER.log(Level.WARN, "Invalid Attribute Effect entry, operation must be 0, 1, or 2");
        else {
            this.valid = true;
        }
        this.parsed = true;
    }

    @Override
    public AttributeGemEffect instantiate(){
        return new AttributeGemEffect(this);
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("UUID",this.modifier.getID().toString());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("UUID"))
            this.modifier = new AttributeModifier(UUID.fromString(nbt.getString("UUID")),Socketed.MODID+"GemEffect", this.amount, this.operation);
        else
            this.modifier = INVALID;
    }
}