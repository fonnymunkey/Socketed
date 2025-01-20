package socketed.common.capabilities;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GemInstance {

    private final String itemId;
    private final int metadata;
    @Nullable private final GemType gemType;

    //Instantiated effects, do not use gemType.getEffects() to perform effects
    private final List<GenericGemEffect> effects = new ArrayList<>();

    public GemInstance(@Nullable ItemStack stack) {
        if (stack != null) {
            this.itemId = stack.getItem().getRegistryName().toString();
            this.metadata = stack.getMetadata();
            this.gemType = GemType.getGemTypeFromItemStack(stack);
            if (this.gemType != null)
                for (int i = 0; i < this.gemType.getEffects().size(); i++)
                    this.effects.add(this.gemType.getEffects().get(i).instantiate());
        } else {
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

        if(nbt.hasKey("GemType")) {
            this.gemType = GemType.getGemTypeFromName(nbt.getString("GemType"));
            if (this.gemType != null && nbt.hasKey("Effects")) {
                NBTTagList effectsNBT = nbt.getTagList("Effects",10);
                for (int i = 0; i < this.gemType.getEffects().size(); i++) {
                    this.effects.add(this.gemType.getEffects().get(i).instantiate());
                    this.effects.get(i).readFromNBT((NBTTagCompound) effectsNBT.get(i));
                }
            }
        }
        else
            this.gemType = null;
    }

    @Nonnull
    public ItemStack getItemStack() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.itemId));
        if(item==null) return ItemStack.EMPTY;
        return new ItemStack(item,1,this.metadata);
    }

    public boolean hasEffectsForStackDefaultSlot(ItemStack stack) {
        if (this.gemType == null || this.effects.isEmpty())
            return false;

        for (GenericGemEffect effect : this.gemType.getEffects()) {
            for (EntityEquipmentSlot itemslot : CapabilityHasSockets.getSlotsForItemStack(stack))
                if (effect.getSlots().contains(itemslot))
                    return true;
        }
        return false;
    }

    @Nonnull
    public List<GenericGemEffect> getGemEffects() {
        return this.effects;
    }

    public List<GenericGemEffect> getGemEffectsForSlots(List<EntityEquipmentSlot> slots) {
        List<GenericGemEffect> effectsForSlot = new ArrayList<>();
        for(GenericGemEffect effect : effects)
            for(EntityEquipmentSlot slot : slots)
                if(effect.getSlots().contains(slot)) {
                    effectsForSlot.add(effect);
                    break; //don't count the same effect multiple times
                }
        return effectsForSlot;
    }

    @Nullable
    public GemType getGemType() {
        return this.gemType;
    }
    
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (!this.itemId.isEmpty())
            nbt.setString("ItemId", this.itemId);
        if (this.metadata != 0)
            nbt.setInteger("Metadata", this.metadata);

        if (this.gemType != null)
            nbt.setString("GemType", this.gemType.getName());

        NBTTagList effectsNBT = new NBTTagList();
        for (GenericGemEffect effect : this.effects)
            effectsNBT.appendTag(effect.writeToNBT());
        nbt.setTag("Effects", effectsNBT);

        return nbt;
    }

    public static boolean stackIsGem(ItemStack stack){
        GemInstance gem = new GemInstance(stack);
        return gem.getGemType() != null;
    }
}