package socketed.common.capabilities.effectscache;

import net.minecraft.entity.player.EntityPlayer;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import java.util.List;

public interface ICapabilityEffectsCache {

    /**
     *
     */
    List<GenericGemEffect> getActiveEffects();

    /**
     *
     */
    void removeEffects(List<GenericGemEffect> oldEffects);

    /**
     *
     */
    void addEffects(List<GenericGemEffect> newEffects);
}