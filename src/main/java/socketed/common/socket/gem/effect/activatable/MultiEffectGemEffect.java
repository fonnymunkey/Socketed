package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.activatable.activator.MultiEffectActivator;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.slot.ISlotType;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MultiEffectGemEffect extends ActivatableGemEffect {
    
    public static final String TYPE_NAME = "Multi Effect";
    
    @SerializedName("Sub-Effects")
    private final List<GenericGemEffect> effects;
    
    public MultiEffectGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, List<GenericGemEffect> effects) {
        super(slotType, activator, targets);
        this.effects = effects;
    }

    public MultiEffectGemEffect(MultiEffectGemEffect multiEffect) {
        super(multiEffect.slotType, multiEffect.activator, multiEffect.targets);
        this.effects = new ArrayList<>();
        for (GenericGemEffect effect : multiEffect.effects)
            this.effects.add(effect.instantiate());
    }

    @Override
    public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
        if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
            for(GenericGemEffect effect : this.effects) {
                //Shouldn't be possible after validation, but sanity check
                if(!(effect instanceof ActivatableGemEffect)) continue;
                if(!(((ActivatableGemEffect)effect).getActivator() instanceof MultiEffectActivator)) continue;
                ((MultiEffectActivator)((ActivatableGemEffect)effect).getActivator()).attemptMultiEffectActivation((ActivatableGemEffect)effect, callback, playerSource, effectTarget);
            }
        }
    }
    
    //TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
    @SideOnly(Side.CLIENT)
    @Override
    public String getTooltipString(boolean onItem) {
        return "";
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public MultiEffectGemEffect instantiate() {
        return new MultiEffectGemEffect(this);
    }

    /**
     * Effects: Required, atleast two, must use MultiEffectActivator for activators and ALL for ISlotType
     */
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.effects == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, sub-effects list invalid");
            else if(this.effects.size() < 2) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, must have at least two sub-effects");
            else {
                for(GenericGemEffect effect : this.effects) {
                    if(effect == null || !effect.validate()) {
                        Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, sub-effect invalid");
                        return false;
                    }
                    else if(!(effect instanceof ActivatableGemEffect)) {
                        Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, sub-effect must be activatable");
                        return false;
                    }
                    else if(!(((ActivatableGemEffect)effect).getActivator() instanceof MultiEffectActivator)) {
                        Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, sub-effect activator must be a Multi Effect activator");
                        return false;
                    }
                    else if(effect.getSlotType() != SocketedSlotTypes.ALL) {
                        Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, sub-effect slot type must be ALL");
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList effectList = new NBTTagList();
        if (!this.effects.isEmpty()) {
            for (GenericGemEffect effect : this.effects)
                effectList.appendTag(effect.writeToNBT());
            nbt.setTag("Sub-Effects", effectList);
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Sub-Effects")) {
            NBTTagList effectList = nbt.getTagList("Sub-Effects", 10);
            //todo: somewhat lazy check, will reroll any instantiation if somehow the saving/reading fails due to for example config changes
            Socketed.LOGGER.info("this effects size {}, nbt size {}", this.effects.size(), effectList.tagCount());
            if(this.effects.size() == effectList.tagCount())
                for (int i = 0; i < effectList.tagCount(); i++)
                    this.effects.get(i).readFromNBT(effectList.getCompoundTagAt(i));
        }
    }
}