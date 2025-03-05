package socketed.api.socket.gem.effect.activatable.callback;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GenericEventCallback<T extends Event> implements IEffectCallback, ICancellableCallback {

    private final T event;

    public GenericEventCallback(T event) {
        this.event = event;
    }

    public T getEvent() {
        return event;
    }

    @Override
    public void setCancelled(boolean val) {
        if(event.isCancelable()) event.setCanceled(val);
    }

    @Override
    public boolean isCancelled() {
        return event.isCanceled();
    }
}
