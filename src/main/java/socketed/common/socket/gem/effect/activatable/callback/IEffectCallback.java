package socketed.common.socket.gem.effect.activatable.callback;

/**
 * Denotes an arbitrary Callback info container
 * Used for additional custom data manipulation such as;
 * Calling back to cancel the event that started the effect chain (use an ICancellableCallback)
 * Passing forward additional activator specific info such as mined blocks or loot lists
 */
public interface IEffectCallback {}