package socketed.api.socket.gem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import socketed.api.socket.gem.effect.GenericGemEffect;
import socketed.api.util.SocketedUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GemCombinationInstance {
    
    private final GemCombinationType gemCombinationType;
    
    /**
     * Instantiated effects for actual usage
     */
    private final List<GenericGemEffect> effects = new ArrayList<>();
    
    /**
     * Instantiate a new instance from a GemCombinationType
     */
    public GemCombinationInstance(GemCombinationType combination) {
        this.gemCombinationType = combination;
        for(GenericGemEffect effect : this.gemCombinationType.getEffects()) {
            this.effects.add(effect.instantiate());
        }
    }
    
    /**
     * Instantiate a new instance from NBT
     */
    public GemCombinationInstance(NBTTagCompound nbt) {
        this.gemCombinationType = SocketedUtil.getGemCombinationTypeFromName(nbt.getString("GemCombinationType"));
        if(this.gemCombinationType != null) {
            NBTTagList effectsNBT = nbt.getTagList("Effects",10);
            //As effects are stored ordered, only read the effects if the stored NBT size is as expected
            //Otherwise re-instantiate effects as the configuration was changed
            //TODO: Handling for if effects are reordered in config/only ranges changed
            if(this.gemCombinationType.getEffects().size() == effectsNBT.tagCount()) {
                for(int i = 0; i < this.gemCombinationType.getEffects().size(); i++) {
                    this.effects.add(this.gemCombinationType.getEffects().get(i).instantiate());
                    this.effects.get(i).readFromNBT((NBTTagCompound)effectsNBT.get(i));
                }
            }
            else {
                for(GenericGemEffect effect : this.gemCombinationType.getEffects()) {
                    this.effects.add(effect.instantiate());
                }
            }
        }
    }

    public boolean hasGemEffectsForStack(ItemStack stack) {
        for(GenericGemEffect effect : this.effects) {
            if(SocketedUtil.isStackValidForSlot(stack, effect.getSlotType())) {
                return true;
            }
        }
        return false;
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
    public GemCombinationType getGemCombinationType() {
        return this.gemCombinationType;
    }
    
    /**
     * Attempts to validate this instance
     * @return false if any required value is invalid, which should result in discarding this instance
     */
    //TODO: Debug log output instead of warns like other validation, as warn may spam logs when existing worlds are loaded after updates?
    public boolean validate() {
        //Gem Combination Type does not need to be validated as it is a reference to an existing validated Gem Combination Type from Json
        if(this.gemCombinationType == null) return false;
        return !this.effects.isEmpty();
	}
    
    /**
     * Writes this instance to NBT for storing on an ItemStack Capability
     */
    @Nonnull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        
        nbt.setString("GemCombinationType", this.gemCombinationType.getName());

        NBTTagList effectsNBT = new NBTTagList();
        for(GenericGemEffect effect : this.effects) {
            effectsNBT.appendTag(effect.writeToNBT());
        }
        nbt.setTag("Effects", effectsNBT);

        return nbt;
    }
}