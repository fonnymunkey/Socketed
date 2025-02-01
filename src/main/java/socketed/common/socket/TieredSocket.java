package socketed.common.socket;

import net.minecraft.nbt.NBTTagCompound;
import socketed.common.capabilities.GemInstance;
import socketed.common.jsondata.GemType;

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
     * @return the tier of this socket
     */
    public int getTier() {
        return this.tier;
    }

    /**
     * Whether this socket accepts the given gem
     */
    @Override
    public boolean acceptsGem(GemInstance gem, boolean ignoreLocked) {
        return super.acceptsGem(gem, ignoreLocked) && gem.getGemType().getTier() <= this.tier;
    }
    
    /**
     * Whether this socket accepts the given gem type
     * Note: prefer only using this for quick checks such as gui rendering and not actual socketing
     * Currently it is fine but there may be cases where the GemType on a GemInstance ends up different than expected
     */
    @Override
    public boolean acceptsGemType(GemType gemType, boolean ignoreLocked) {
        return super.acceptsGemType(gemType, ignoreLocked) && gemType.getTier() <= this.tier;
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