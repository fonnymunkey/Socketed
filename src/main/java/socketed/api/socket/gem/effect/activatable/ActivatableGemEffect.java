package socketed.api.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.api.socket.gem.effect.GenericGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;
import socketed.api.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ActivatableGemEffect extends GenericGemEffect {

    @SerializedName("Activator")
    protected final GenericActivator activator;
    
    @SerializedName("Targets")
    protected final List<GenericTarget> targets;
    
    protected ActivatableGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets) {
        super(slotType);
        this.activator = activator;
        this.targets = targets;
    }
    
    @Nonnull
    public GenericActivator getActivator() {
        return this.activator;
    }
    
    @Nonnull
    public List<GenericTarget> getTargets() {
        return this.targets;
    }
    
    /**
     * Handles allowing targets to apply this effect to specific entities
     * @param callback callback info container for additional effect manipulation for use with specific activators
     * @param playerSource the player that is the source of this effect
     * @param effectTarget the entity involved in this effect being triggered, may be the same as the player
     */
    public void affectTargets(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
        for(GenericTarget target : this.targets) {
            target.affectTarget(this, callback, playerSource, effectTarget);
        }
    }
    
    /**
     * Handles applying this effect to a specific entity
     * @param callback callback info container for additional effect manipulation for use with specific activators
     * @param playerSource the player that is the source of this effect
     * @param effectTarget the entity that the effect should be performed on, may be the same as the player
     */
    public abstract void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget);
    
    /**
     * Activator: Required
     * Targets: Required, at least one
     */
    @Override
    public boolean validate() {
        if(super.validate()) {
            if(this.activator == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid activator");
            else if(!this.activator.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid activator");
            else if(this.targets == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, invalid targets list");
            else if(this.targets.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, must define at least one target");
            else {
                for(GenericTarget target : this.targets) {
                    if(!target.validate()) {
                        Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, target invalid");
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}