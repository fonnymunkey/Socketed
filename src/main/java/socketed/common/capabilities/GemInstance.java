package socketed.common.capabilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import socketed.Socketed;
import socketed.common.config.CustomConfig;
import socketed.common.data.GemType;
import socketed.common.data.RecipientGroup;
import socketed.common.data.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GemInstance {

    private final String itemId;
    private final int metadata;
    @Nullable private final GemType gemType;

    public GemInstance(@Nullable ItemStack stack) {
        if(stack!=null) {
            this.itemId = stack.getItem().getRegistryName().toString();
            this.metadata = stack.getMetadata();
            this.gemType = GemType.getGemTypeFromItemStack(stack);
        } else{
            this.itemId = "";
            this.metadata = 0;
            this.gemType = null;
        }
    }

    public GemInstance(NBTTagCompound nbt){
        if(nbt.hasKey("ItemId"))
            this.itemId = nbt.getString("ItemId");
        else
            this.itemId = "";
        if(nbt.hasKey("Metadata"))
            this.metadata = nbt.getInteger("Metadata");
        else
            this.metadata = 0;

        Item item = Item.getByNameOrId(itemId);
        if(item!=null)
            this.gemType = GemType.getGemTypeFromItemStack(new ItemStack(item,1,metadata));
        else
            this.gemType = null;
    }

    @Nullable
    public ItemStack getItemStack() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.itemId));
        if(item==null) return null;
        return new ItemStack(item,1,this.metadata);
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
        if(!this.itemId.isEmpty())
            nbt.setTag("ItemId", new NBTTagString(this.itemId));
        if(this.metadata!=0)
            nbt.setTag("Metadata", new NBTTagInt(this.metadata));
        return nbt;
    }
}