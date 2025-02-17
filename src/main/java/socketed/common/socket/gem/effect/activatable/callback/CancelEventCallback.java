package socketed.common.socket.gem.effect.activatable.callback;

public class CancelEventCallback implements IEffectCallback {
	
	private boolean cancelled;
	
	public CancelEventCallback(boolean val) {
		this.cancelled = val;
	}
	
	public void setCancelled(boolean val) {
		this.cancelled = val;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
}