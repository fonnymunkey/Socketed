package socketed.common.capabilities;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ICapabilityHasSockets {
    // -----     SOCKET SECTION     -----
    // ----------------------------------

    /**
     * Returns the amount of sockets this item has
     */
    int getSocketCount();

    /**
     * @return the GenericSocket instance at the specified socket index. null if index out of range
     */
    @Nullable
    GenericSocket getSocketAt(int socketIndex);

    /**
     * Sets the socket amount
     * @return a list of gems that have been removed if socket count has been reduced
     */
    @Nonnull
    @Deprecated //TODO: replace lootfunction with tiered version via addSocket
    List<GemInstance> setSocketCount(int socketCount);

    /**
     * Adds a socket at the end of the list of this item's sockets
     */
    void addSocket(GenericSocket socket);

    /**
     * Creates and adds a socket instance inheriting from GenericSocket, built with the given nbt tags
     * @return true if successful
     */
    boolean addSocketFromNBT(String socketType, NBTTagCompound tags);

    /**
     * Replaces the socket at the specified position with a different socket
     * does not change socket count, use setSocketCount for that
     * Will place the current gem (if not empty) into the new socket if the new socket accepts it
     * @return the gem that was in the old socket, if the new socket doesn't accept that gem
     */
    @Nullable
    GemInstance replaceSocketAt(GenericSocket socket, int socketIndex);


    // -----     GEM SECTION     -----
    // -------------------------------

    /**
     * Returns the amount of socketed gems this item has
     */
    int getGemCount();

    /**
     * Returns gem in specified socket
     * @return gem in specified socket, null if socketIndex is out of range or if socket is empty
     */
    @Nullable
    GemInstance getGemAt(int socketIndex);

    /**
     * Returns a list of all gems this item has in its sockets
     * @return List of non-null gems, can be size 0 if there are no gems
     */
    @Nonnull
    List<GemInstance> getAllGems();

    /**
     * Adds a gem to the first available empty socket
     * @return true if gem was added, false if no available empty socket
     */
    boolean addGem(@Nonnull GemInstance gem);

    /**
     * Replaces the gem in the specified socket with the provided gem.
     * Will not add the gem and return null if socketIndex is out of range or if the provided gem is null
     * @return the gem that was in the same socket before, can be null if socket was empty.
     *
     */
    @Nullable
    GemInstance replaceGemAt(GemInstance gem, int socketIndex);

    /**
     * Removes the gem in the specified socket
     * Will return null if socketIndex is out of range or if the socket is empty
     * @return the gem that was in the socket before, if not empty
     */
    @Nullable
    GemInstance removeGemAt(int socketIndex);

    /**
     * Removes all gems from the sockets of this item and returns a List of them
     * @return List of non-null gems, can be size 0 if there are no gems
     */
    @Nonnull
    List<GemInstance> removeAllGems();


    // -----     EFFECT SECTION     -----
    // ----------------------------------

    /**
     * Gathers and returns a list of (instantiated) effects of all socketed gems
     */
    @Nonnull
    List<GenericGemEffect> getAllEffects();

    /**
     * Gathers and returns a list of (instantiated) effects of all socketed gems that match the given equipment slot
     */
    @Nonnull
    List<GenericGemEffect> getAllEffectsForSlot(EntityEquipmentSlot slot);
}
