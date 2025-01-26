package socketed.common.socket;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.capabilities.GemInstance;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GenericSocket {
    
    private GemInstance gem = null;
    private boolean disabled = false;

    public GenericSocket() {
    
    }

    /**
     * create socket holding the provided gem
     */
    public GenericSocket(GemInstance gem){
        this.gem = gem;
    }

    /**
     * Create socket from nbt
     */
    public GenericSocket(NBTTagCompound nbt) {
        if(nbt.hasKey("Gem")) this.gem = new GemInstance(nbt.getCompoundTag("Gem"));
        if(nbt.hasKey("Disabled")) this.disabled = nbt.getBoolean("Disabled");
    }

    /**
     * Gems in disabled sockets will not have effects (used for gem combinations overwriting gem effects)
     * @return Disabled state of socket
     */
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * Disable the socket (used for gem combinations that overwrite the original effects of their gems)
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Whether this socket accepts the given gem. Default socket accepts all gems
     */
    public boolean acceptsGem(@Nonnull GemInstance gem) {
        return true;
    }
    
    /**
     * Whether this socket has space for a gem or is already filled
     */
    public boolean isEmpty() {
        return gem == null;
    }

    /**
     * @return The gem this socket is filled with
     */
    @Nullable
    public GemInstance getGem() {
        return gem;
    }

    /**
     * Fills the socket with the given gem.
     * @param gem The gem this socket should get filled with
     * @return true if socket accepts the gem, false if not. Will not set the gem if it doesn't accept it
     */
    public boolean setGem(@Nullable GemInstance gem) {
        if(gem == null || this.acceptsGem(gem)) {
            this.gem = gem;
            return true;
        }
        return false;
    }

    /**
     * Get all effect entries of this socket's gem. Override this method if you want to make a special socket vary the effects of its gem
     * Return an empty list if socket is empty
     */
    @Nonnull
    public List<GenericGemEffect> getEffects() {
        if(gem != null) return gem.getGemEffects();
        else return Collections.emptyList();
    }

    /**
     * @return NBTTagCompound of the socket including its gem NBT if not empty
     */
    @Nonnull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("SocketType", "Generic");
        if(this.gem != null) nbt.setTag("Gem", this.gem.writeToNBT());
        nbt.setBoolean("Disabled", this.disabled);
        return nbt;
    }
}