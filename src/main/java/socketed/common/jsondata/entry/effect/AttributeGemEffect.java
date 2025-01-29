package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.jsondata.entry.RandomValueRange;

import java.util.Random;
import java.util.UUID;

public class AttributeGemEffect extends GenericGemEffect {
    
    public static final Random RAND = new Random();
    
    private static final AttributeModifier INVALID = new AttributeModifier(new UUID(1,1), "INVALID", 0, 0);

    public static final String TYPE_NAME = "Attribute";

    @SerializedName("Attribute Name")
    private final String attribute;

    @SerializedName("Modifier Amount")
    private final RandomValueRange amountRange;
    private transient float amount;

    @SerializedName("Modifier Operation")
    private final int operation;

    private transient AttributeModifier modifier = INVALID;

    public AttributeGemEffect(ISlotType slotType, String attribute, RandomValueRange amountRange, int operation) {
        super(slotType);
        this.attribute = attribute;
        this.amountRange = amountRange;
        this.operation = operation;
        this.type = TYPE_NAME;
    }

    public AttributeGemEffect(AttributeGemEffect effect) {
        super(effect.getSlotType());
        this.attribute = effect.attribute;
        this.amountRange = effect.amountRange;
        this.operation = effect.operation;

        //Only instantiated AttributeGemEffects have valid modifiers and amounts
        this.amount = this.amountRange.generateValue(RAND);
        this.modifier = new AttributeModifier(UUID.randomUUID(),Socketed.MODID+"GemEffect", this.amount, this.operation);
    }

    public RandomValueRange getAmountRange(){
        return amountRange;
    }

    public int getOperation() {
        return this.operation;
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
        nbt.setFloat("Amount",this.amount);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("Amount"))
            this.amount = nbt.getFloat("Amount");
        else
            this.amount = 0;
        if(nbt.hasKey("UUID"))
            this.modifier = new AttributeModifier(UUID.fromString(nbt.getString("UUID")),Socketed.MODID+"GemEffect", this.amount, this.operation);
        else
            this.modifier = INVALID;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        if(onItem) {
            AttributeModifier modifier = this.getModifier();
            double amount = modifier.getAmount() * (modifier.getOperation() == 0 ? 1.0D : 100.0D);
            if(amount > 0.0D) return I18n.format("attribute.modifier.plus." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + this.getAttribute()));
            else if (amount < 0.0D) return I18n.format("attribute.modifier.take." + modifier.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + this.getAttribute()));
            else return "";
        }
        else {
            if(this.getAmountRange().getMax() == this.getAmountRange().getMin()) {
                double amount = this.getAmountRange().getMin() * (this.getOperation() == 0 ? 1.0D : 100.0D);
                if(amount > 0.0D) return I18n.format("attribute.modifier.plus." + this.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + this.getAttribute()));
                else if(amount < 0.0D) return I18n.format("attribute.modifier.take." + this.getOperation(), ItemStack.DECIMALFORMAT.format(amount), I18n.format("attribute.name." + this.getAttribute()));
                else return "";
            }
            else {
                double min = this.getAmountRange().getMin() * (this.getOperation() == 0 ? 1.0D : 100.0D);
                double max = this.getAmountRange().getMax() * (this.getOperation() == 0 ? 1.0D : 100.0D);
                if(min >= 0.0D) return I18n.format("socketed.modifier.plus.plus." + this.getOperation(), ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), I18n.format("attribute.name." + this.getAttribute()));
                else if(min < 0.0D && max >=0.0D) return I18n.format("socketed.modifier.take.plus." + this.getOperation(), ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), I18n.format("attribute.name." + this.getAttribute()));
                else if(min < 0.0D && max < 0.0D) return I18n.format("socketed.modifier.take.take." + this.getOperation(), ItemStack.DECIMALFORMAT.format(min), ItemStack.DECIMALFORMAT.format(max), I18n.format("attribute.name." + this.getAttribute()));
                return "";
            }
        }
    }
}