package socketed.common.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import socketed.common.config.CustomConfig;
import socketed.common.data.GemType;
import socketed.common.data.RecipientGroup;
import socketed.common.data.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GemInstance {

    @Nullable private final ItemStack stack;
    @Nullable private final GemType gemType;

    public GemInstance(@Nullable ItemStack stack) {
        this.stack = stack;
        this.gemType = GemType.getGemTypeFromItemStack(stack);;
    }

    public GemInstance(NBTTagCompound nbt){
        if(nbt.hasKey("ItemStack")) {
            NBTTagCompound itemStackNBT = nbt.getCompoundTag("ItemStack");
            this.stack = new ItemStack(itemStackNBT);
        } else
            this.stack = null;
        this.gemType = GemType.getGemTypeFromItemStack(stack);;
    }

    @Nullable
    public ItemStack getItemStack() {
        return stack;
    }

    public boolean canApplyOn(ItemStack stack) {
        if(this.gemType==null)
            return false;

        for(String recipientString : this.gemType.getRecipientEntries()) {
            RecipientGroup recipientGroup = CustomConfig.getRecipientData().get(recipientString);
            if(recipientGroup!=null && recipientGroup.matches(stack))
                return true;
        }
        return false;
    }

    @Nonnull
    public List<GenericGemEffect> getGemEffects() {
        if(this.gemType!=null)
            return this.gemType.getEffects();
        else
            return Collections.emptyList();
    }

    @Nullable
    public GemType getGemType() {
        return this.gemType;
    }
    
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (this.stack != null)
            nbt.setTag("ItemStack", this.stack.writeToNBT(new NBTTagCompound()));
        return nbt;
    }
}