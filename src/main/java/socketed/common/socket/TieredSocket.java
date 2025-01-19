package socketed.common.socket;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.capabilities.GemInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TieredSocket extends GenericSocket {

    private final int tier;

    public TieredSocket(int tier){
        this.tier = tier;
    }

    /**
     * create socket holding the provided gem
     */
    public TieredSocket(GemInstance gem, int tier){
        super(gem);
        this.tier = tier;
    }

    /**
     * Create socket from nbt
     */
    public TieredSocket(NBTTagCompound nbt) {
        super(nbt);
        this.tier = nbt.getInteger("Tier");
    }

    /**
     * Whether this socket accepts the given gem.
     */
    public boolean acceptsGem(@Nonnull GemInstance gem) {
        return gem.getGemType().getTier() <= this.tier;
    }

    /**
     * @return NBTTagCompound of the socket including its gem NBT if not empty
     */
    @Nonnull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = super.writeToNBT();
        nbt.setString("SocketType","Tiered");
        nbt.setInteger("Tier",this.tier);
        return nbt;
    }
}
