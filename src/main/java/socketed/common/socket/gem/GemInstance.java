package socketed.common.socket.gem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.util.SocketedUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GemInstance {

    private final String itemId;
    private final int metadata;
    private final GemType gemType;
    
    /**
     * Instantiated effects for actual usage
     */
    private final List<GenericGemEffect> effects = new ArrayList<>();
    
    private Item item = null;
    
    /**
     * Instantiate a new instance from an ItemStack
     */
    public GemInstance(ItemStack stack) {
        this.itemId = stack.getItem().getRegistryName().toString();
        this.metadata = stack.getMetadata();
        this.gemType = SocketedUtil.getGemTypeFromItemStack(stack);
        if(this.gemType != null) {
            for(GenericGemEffect effect : this.gemType.getEffects()) {
                this.effects.add(effect.instantiate());
            }
        }
    }
    
    /**
     * Instantiate a new instance from NBT
     */
    public GemInstance(NBTTagCompound nbt) {
        this.itemId = nbt.getString("ItemId");
        this.metadata = nbt.getInteger("Metadata");
        this.gemType = SocketedUtil.getGemTypeFromName(nbt.getString("GemType"));
        if(this.gemType != null) {
            NBTTagList effectsNBT = nbt.getTagList("Effects", 10);
            //As effects are stored ordered, only read the effects if the stored NBT size is as expected
            //Otherwise re-instantiate effects as the configuration was changed
            //TODO: Handling for if effects are reordered in config/only ranges changed
            if(this.gemType.getEffects().size() == effectsNBT.tagCount()) {
                for(int i = 0; i < this.gemType.getEffects().size(); i++) {
                    this.effects.add(this.gemType.getEffects().get(i).instantiate());
                    this.effects.get(i).readFromNBT((NBTTagCompound)effectsNBT.get(i));
                }
            }
            else {
                for(GenericGemEffect effect : this.gemType.getEffects()) {
                    this.effects.add(effect.instantiate());
                }
            }
        }
    }

    @Nonnull
    public ItemStack getItemStack() {
        return new ItemStack(this.item, 1, this.metadata);
    }

    @Nonnull
    public List<GenericGemEffect> getGemEffects() {
        return this.effects;
    }
    
    @Nonnull
    public List<GenericGemEffect> getGemEffectsForStack(ItemStack stack) {
        List<GenericGemEffect> effectsForSlot = new ArrayList<>();
        
        for(GenericGemEffect effect : this.effects) {
            if(SocketedUtil.isStackValidForSlot(stack, effect.getSlotType())) {
                effectsForSlot.add(effect);
            }
        }
        return effectsForSlot;
    }

    @Nonnull
    public GemType getGemType() {
        return this.gemType;
    }
    
    /**
     * Attempts to validate this instance and caches the stored Item reference
     * @return false if any required value is invalid, which should result in discarding this instance
     */
    //TODO: Debug log output instead of warns like other validation, as warn may spam logs when existing worlds are loaded after updates?
    public boolean validate() {
        if(this.itemId == null || this.itemId.isEmpty()) return false;
        else if(this.metadata < 0 || this.metadata > OreDictionary.WILDCARD_VALUE) return false;
        //Gem Type does not need to be validated as it is a reference to an existing validated Gem Type from Json
        else if(this.gemType == null) return false;
        else if(this.effects.isEmpty()) return false;
        this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.itemId));
		return this.item != null;
    }
    
    /**
     * Writes this instance to NBT for storing on an ItemStack Capability
     */
    @Nonnull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("ItemId", this.itemId);
        nbt.setInteger("Metadata", this.metadata);
        nbt.setString("GemType", this.gemType.getName());
        
        NBTTagList effectsNBT = new NBTTagList();
        for(GenericGemEffect effect : this.effects) {
            effectsNBT.appendTag(effect.writeToNBT());
        }
        nbt.setTag("Effects", effectsNBT);

        return nbt;
    }
}