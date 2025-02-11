package socketed.common.socket.gem.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.slot.ISlotType;
import socketed.common.socket.gem.util.RandomValueRange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class AttributeGemEffect extends GenericGemEffect {

    public static final String TYPE_NAME = "Attribute";
    
    @SerializedName("Attribute Name")
    private final String attributeName;

    @SerializedName("Modifier Amount")
    private final RandomValueRange amountRange;

    @SerializedName("Modifier Operation")
    private final int operation;
    
    private transient AttributeModifier modifier = null;

    public AttributeGemEffect(ISlotType slotType, String attribute, RandomValueRange amountRange, int operation) {
        super(slotType);
        this.attributeName = attribute;
        this.amountRange = amountRange;
        this.operation = operation;
    }

    public AttributeGemEffect(AttributeGemEffect effect) {
        super(effect.getSlotType());
        this.attributeName = effect.attributeName;
        this.amountRange = effect.amountRange;
        this.operation = effect.operation;

        //Only instantiated AttributeGemEffects have a valid modifier
        this.modifier = new AttributeModifier(UUID.randomUUID(), Socketed.MODID + "GemEffect", this.amountRange.generateValue(), this.operation);
    }
    
    @Nonnull
    public String getAttribute() {
        return this.attributeName;
    }
    
    @Nonnull
    public RandomValueRange getAmountRange() {
        return this.amountRange;
    }

    public int getOperation() {
        return this.operation;
    }

    @Nullable
    public AttributeModifier getModifier() {
        return this.modifier;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        if(onItem) {
            AttributeModifier modifier = this.getModifier();
            if(modifier == null) return "";
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
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public AttributeGemEffect instantiate() {
        return new AttributeGemEffect(this);
    }
    
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.attributeName == null || this.attributeName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, attribute name null or empty");
            else if(this.amountRange == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.attributeName + ", amount range invalid");
            else if(this.operation < 0 || this.operation > 2) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, " + this.attributeName + ", operation must be 0, 1, or 2");
            else return true;
        }
        return false;
    }
    
    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if(this.modifier != null) {
            nbt.setString("UUID", this.modifier.getID().toString());
            nbt.setDouble("Amount", this.modifier.getAmount());
        }
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("UUID")) {
            this.modifier = new AttributeModifier(UUID.fromString(nbt.getString("UUID")),Socketed.MODID + "GemEffect", nbt.getDouble("Amount"), this.operation);
        }
    }
}