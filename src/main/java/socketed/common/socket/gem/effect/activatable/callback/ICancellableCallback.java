package socketed.common.socket.gem.effect.activatable.callback;

public interface ICancellableCallback {
    void setCancelled(boolean val);
    boolean isCancelled();
}