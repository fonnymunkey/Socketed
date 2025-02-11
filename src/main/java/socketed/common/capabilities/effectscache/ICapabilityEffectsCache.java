package socketed.common.capabilities.effectscache;

import socketed.common.socket.gem.effect.GenericGemEffect;

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