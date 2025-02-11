package socketed.common.capabilities.effectscache;

import socketed.common.socket.gem.effect.GenericGemEffect;

import java.util.ArrayList;
import java.util.List;

public class CapabilityEffectsCache implements ICapabilityEffectsCache {
    private final List<GenericGemEffect> activeEffects = new ArrayList<>();

    public CapabilityEffectsCache(){
    }

    @Override
    public List<GenericGemEffect> getActiveEffects() {
        return activeEffects;
    }

    @Override
    public void removeEffects(List<GenericGemEffect> oldEffects) {
        activeEffects.removeAll(oldEffects);
    }

    @Override
    public void addEffects(List<GenericGemEffect> newEffects) {
        activeEffects.addAll(newEffects);
    }
}