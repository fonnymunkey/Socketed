package socketed.common.socket.gem.effect.activatable.callback;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GenericEventCallback implements IEffectCallback {

    private final Class<? extends Event> event;

    public GenericEventCallback(Class<? extends Event> event) {
        this.event = event;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }
}
