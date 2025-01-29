package socketed.common.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import javax.annotation.Nullable;

public class GemCombinationInstance extends GemInstance {
    
    private final GemCombinationType gemCombinationType;

    public GemCombinationInstance(@Nullable GemCombinationType combination) {
        this.gemCombinationType = combination;
        if(this.gemCombinationType != null) {
            for(int i = 0; i < this.gemCombinationType.getEffects().size(); i++) {
                this.effects.add(this.gemCombinationType.getEffects().get(i).instantiate());
            }
        }
    }

    public GemCombinationInstance(NBTTagCompound nbt) {
        if(nbt.hasKey("GemCombinationType")) {
            this.gemCombinationType = JsonConfig.getGemCombinationData().get(nbt.getString("GemCombinationType"));
            if(this.gemCombinationType != null && nbt.hasKey("Effects")) {
                NBTTagList effectsNBT = nbt.getTagList("Effects",10);
                for(int i = 0; i < this.gemCombinationType.getEffects().size(); i++) {
                    this.effects.add(this.gemCombinationType.getEffects().get(i).instantiate());
                    this.effects.get(i).readFromNBT((NBTTagCompound) effectsNBT.get(i));
                }
            }
        }
        else this.gemCombinationType = null;
    }

    @Override
    public boolean hasGemEffectsForStack(ItemStack stack) {
        if(this.gemCombinationType == null) return false;

        for(GenericGemEffect effect : this.effects) {
            if(effect.getSlotType().isStackValid(stack)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public GemCombinationType getGemCombinationType() {
        return this.gemCombinationType;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        if(this.gemCombinationType != null) {
            nbt.setString("GemCombinationType", this.gemCombinationType.getName());
        }

        NBTTagList effectsNBT = new NBTTagList();
        for(GenericGemEffect effect : this.effects) {
            effectsNBT.appendTag(effect.writeToNBT());
        }
        nbt.setTag("Effects", effectsNBT);

        return nbt;
    }
}