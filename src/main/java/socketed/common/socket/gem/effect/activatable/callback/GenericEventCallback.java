package socketed.common.socket.gem.effect.activatable.callback;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GenericEventCallback<T extends Event> implements IEffectCallback {

    private final T event;

    public GenericEventCallback(T event) {
        this.event = event;
    }

    public T getEvent() {
        return event;
    }
}
