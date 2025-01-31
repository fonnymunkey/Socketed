package socketed.common.socket;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.capabilities.GemInstance;

import javax.annotation.Nonnull;

public class TieredSocket extends GenericSocket {

    public static final String TYPE_NAME = "Tiered";
    
    private final int tier;
    
    /**
     * Constructs an empty socket with the provided tier
     */
    public TieredSocket(int tier) {
        this.tier = tier;
    }

    /**
     * Constructs a socket holding the provided gem with the provided tier
     */
    public TieredSocket(GemInstance gem, int tier) {
        super(gem);
        this.tier = tier;
    }
    
    /**
     * Constructs a socket from provided nbt
     */
    public TieredSocket(NBTTagCompound nbt) {
        super(nbt);
        this.tier = nbt.getInteger("Tier");
    }

    /**
     * Whether this socket accepts the given gem
     */
    @Override
    public boolean acceptsGem(GemInstance gem) {
        return super.acceptsGem(gem) && gem.getGemType().getTier() <= this.tier;
    }

    /**
     * @return NBTTagCompound of the socket including its gem NBT if not empty
     */
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = super.writeToNBT();
        nbt.setString("SocketType", TieredSocket.TYPE_NAME);
        nbt.setInteger("Tier", this.tier);
        return nbt;
    }
}