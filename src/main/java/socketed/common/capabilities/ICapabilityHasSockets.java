package socketed.common.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ICapabilityHasSockets {
    /**
     * Returns the amount of sockets this item has
     */
    int getSocketCount();
    /**
     * Sets the socket amount
     * @return a list of gems that have been removed if socket count has been reduced
     */
    @Nonnull
    List<GemInstance> setSocketCount(int socketCount);

    /**
     * Sets the socket at the specified position to be a specific socket (use this if you use special types of sockets)
     * does not change socket count, use setSocketCount for that
     * Will place the current gem (if not empty) in the new socket if the new socket accepts it
     * @return the gem that was in that socket, if the new socket doesn't accept that gem
     */
    @Nullable
    GemInstance setSocketAt(GenericSocket socket, int socketIndex);

    /**
     * Creates a socket instance inheriting from GenericSocket, built with the given nbt tags
     */
    @Nullable
    GenericSocket createSocketFromNBT(String socketType, NBTTagCompound tags);

    /**
     * @return the GenericSocket instance at the specified socket index. null if index out of range
     */
    @Nullable
    GenericSocket getSocketAt(int socketIndex);

    /**
     * Returns the amount of socketed gems this item has
     */
    int getGemCount();

    /**
     * Adds a gem to the first available empty socket
     * @return true if gem was added, false if no available empty socket
     */
    boolean addGem(GemInstance gem);

    /**
     * Adds a gem to the specified socket. Will not add the gem and return null if socketIndex is out of range
     * @return gem that was in the same socket before, can be null if socket was empty.
     *
     */
    @Nullable
    GemInstance setGemAt(GemInstance gem, int socketIndex);

    /**
     * Returns all gems this item has in its sockets
     * @return List of non-null gems, can be size 0 if there are no gems
     */
    @Nonnull
    List<GemInstance> getAllGems();

    /**
     * Removes all gems from the sockets of this item and returns a List of them
     * @return List of non-null gems, can be size 0 if there are no gems
     */
    @Nonnull
    List<GemInstance> removeAllGems();

    /**
     * Returns gems in specified socket
     * @return gem in specified socket, null if socketIndex is out of range
     */
    @Nullable
    GemInstance getGemAt(int socketIndex);

    /**
     * Gathers and returns a list of effect entries of all socketed gems
     */
    @Nonnull
    List<GenericGemEffect> getAllEffectsFromAllSockets();
}
