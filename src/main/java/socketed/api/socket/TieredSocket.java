package socketed.api.socket;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.api.util.SocketedUtil;
import socketed.api.socket.gem.GemInstance;
import socketed.api.socket.gem.GemType;

import javax.annotation.Nonnull;

public class TieredSocket extends GenericSocket {

    public static final String TYPE_NAME = "Tiered";
    
    private final int tier;
    
    /**
     * Constructs an empty socket with the provided tier
     */
    public TieredSocket(int tier) {
        this.tier = Math.max(0, Math.min(SocketedUtil.getMaxSocketTier(), tier));
    }

    /**
     * Constructs a socket holding the provided gem with the provided tier
     */
    public TieredSocket(GemInstance gem, int tier) {
        super(gem);
        this.tier = Math.max(0, Math.min(SocketedUtil.getMaxSocketTier(), tier));
    }
    
    /**
     * Constructs a socket from provided nbt
     */
    public TieredSocket(NBTTagCompound nbt) {
        super(nbt);
        this.tier = Math.max(0, Math.min(SocketedUtil.getMaxSocketTier(), nbt.getInteger("Tier")));
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
    
    protected static final ResourceLocation TIER_0_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_0_empty.png");
    protected static final ResourceLocation TIER_0_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_0_filled.png");
    protected static final ResourceLocation TIER_1_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_1_empty.png");
    protected static final ResourceLocation TIER_1_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_1_filled.png");
    protected static final ResourceLocation TIER_2_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_2_empty.png");
    protected static final ResourceLocation TIER_2_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_2_filled.png");
    protected static final ResourceLocation TIER_3_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_3_empty.png");
    protected static final ResourceLocation TIER_3_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_3_filled.png");
    protected static final ResourceLocation TIER_4_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_4_empty.png");
    protected static final ResourceLocation TIER_4_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_4_filled.png");
    protected static final ResourceLocation TIER_5_EMPTY_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_5_empty.png");
    protected static final ResourceLocation TIER_5_FILLED_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socket_tier_5_filled.png");
    
    /**
     * @return The resourcelocation of the texture to render in the socketing gui
     */
    @SideOnly(Side.CLIENT)
    @Nonnull
    public ResourceLocation getSocketTexture() {
        switch(this.getTier()) {
            case 0: return this.isEmpty() ? TIER_0_EMPTY_TEXTURE : TIER_0_FILLED_TEXTURE;
            case 1: return this.isEmpty() ? TIER_1_EMPTY_TEXTURE : TIER_1_FILLED_TEXTURE;
            case 2: return this.isEmpty() ? TIER_2_EMPTY_TEXTURE : TIER_2_FILLED_TEXTURE;
            case 3: return this.isEmpty() ? TIER_3_EMPTY_TEXTURE : TIER_3_FILLED_TEXTURE;
            case 4: return this.isEmpty() ? TIER_4_EMPTY_TEXTURE : TIER_4_FILLED_TEXTURE;
            case 5: return this.isEmpty() ? TIER_5_EMPTY_TEXTURE : TIER_5_FILLED_TEXTURE;
        }
        return super.getSocketTexture();
    }
    
    /**
     * @return the tooltip to be displayed when hovering over this socket in gui while empty
     */
    @SideOnly(Side.CLIENT)
    @Nonnull
    public String getSocketTooltip() {
        switch(this.getTier()) {
            case 0: return I18n.format("socketed.tooltip.tier_0");
            case 1: return I18n.format("socketed.tooltip.tier_1");
            case 2: return I18n.format("socketed.tooltip.tier_2");
            case 3: return I18n.format("socketed.tooltip.tier_3");
            case 4: return I18n.format("socketed.tooltip.tier_4");
            case 5: return I18n.format("socketed.tooltip.tier_5");
        }
        return super.getSocketTooltip();
    }
}