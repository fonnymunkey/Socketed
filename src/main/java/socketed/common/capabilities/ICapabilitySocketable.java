package socketed.common.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ICapabilitySocketable {
    
    // -----     SOCKET SECTION     -----
    // ----------------------------------

    /**
     * @return the amount of sockets this item has
     */
    int getSocketCount();
    
    /**
     * @return the ordered list of sockets this item has
     */
    @Nonnull
    List<GenericSocket> getSockets();
    
    /**
     * @return the GenericSocket instance at the specified socket index. null if index out of range
     */
    @Nullable
    GenericSocket getSocketAt(int socketIndex);

    /**
     * Adds a socket at the end of the list of this item's sockets
     */
    void addSocket(GenericSocket socket);

    /**
     * Creates and adds a socket instance inheriting from GenericSocket, built with the given nbt tags
     * @return true if successful
     */
    void addSocketFromNBT(String socketType, NBTTagCompound tags);

    /**
     * Replaces the socket at the specified position with the supplied socket
     * Does not change socket count, use setSocketCount for that
     * Will attempt to place the existing gem into the new socket if the new socket accepts it
     * @return the gem that was in the old socket, if the new socket is full or doesn't accept that gem
     */
    @Nullable
    GemInstance replaceSocketAt(GenericSocket socket, int socketIndex);
    
    
    // -----     GEM SECTION     -----
    // -------------------------------

    /**
     * @return the amount of socketed gems this item has
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
     * @param includeDisabled whether to also return gems in disabled sockets
     * @return List of non-null gems, can be size 0 if there are no gems
     */
    @Nonnull
    List<GemInstance> getAllGems(boolean includeDisabled);

    /**
     * Adds a gem to the first available empty socket
     * @return true if gem was added, false if no available empty socket
     */
    boolean addGem(GemInstance gem);

    /**
     * Replaces the gem in the specified socket with the provided gem.
     * Will not add the gem and return null if socketIndex is out of range or if the provided gem is null
     * @return the gem that was in the same socket before, can be null if socket was empty.
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
     * Gathers and returns a list of (instantiated) effects of all socketed gems that match this item's slot
     */
    @Nonnull
    List<GenericGemEffect> getAllEffectsForStackSlot();


    // -----     GEM COMBINATION SECTION     -----
    // -------------------------------------------

    /**
     * Returns the gem combinations on this item
     */
    @Nonnull
    List<GemCombinationInstance> getGemCombinations();

    /**
     * Adds a gem combination from NBT to this item (quick write without check)
     */
    void addCombinationFromNBT(NBTTagCompound nbt);

    /**
     * Refreshes the gem combinations when gems are added/removed
     */
    void refreshCombinations();
}