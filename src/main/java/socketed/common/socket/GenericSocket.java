package socketed.common.socket;

import net.minecraft.nbt.NBTTagCompound;
import socketed.Socketed;
import socketed.common.capabilities.GemInstance;
import socketed.common.data.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GenericSocket{
    public GenericSocket(){
    }
    
    public GenericSocket(GemInstance gem){
        this.gem = gem;
    }

    public GenericSocket(NBTTagCompound nbt) {
        this.gem = new GemInstance(nbt.getCompoundTag("Gem"));
    }

    private GemInstance gem = null;

    /**
     * Whether this socket accepts the given gem. Default socket accepts all gems
     */
    public boolean acceptsGem(GemInstance gem){
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
    public GemInstance getGem() {
        return gem;
    }

    /**
     * Fills the socket with the given gem
     * @param gem The gem this socket should get filled with
     * @return true if socket accepts the gem, false if not. Will not set the gem if it doesn't accept it
     */
    public boolean setGem(GemInstance gem) {
        if(this.acceptsGem(gem)) {
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
        if (gem != null)
            return gem.getGemEffects();
        else
            return Collections.emptyList();
    }

    /**
     * @return NBTTagCompound of the socket including its gem NBT if not empty
     */
    @Nonnull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("SocketType","Generic");
        if (this.gem != null)
            nbt.setTag("Gem", this.gem.writeToNBT());
        else
            nbt.setTag("Gem", new NBTTagCompound());
        return nbt;
    }
}
