package socketed.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.container.ContainerSocketing;
import socketed.common.jsondata.GemType;
import socketed.common.socket.GenericSocket;
import socketed.common.socket.TieredSocket;

@SideOnly(Side.CLIENT)
public class GuiSocketing extends GuiContainer {
    
    private static final ResourceLocation SOCKETING_GUI_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socketing.png");
    private final InventoryPlayer playerInventory;
    
    //Same height as double chest
    private static final int guiHeight = 222;
    private static final Tuple<Integer, Integer> texSlotActive = new Tuple<>(0, 222);
    private static final Tuple<Integer, Integer> texSlotInactive = new Tuple<>(18, 222);
    private static final Tuple<Integer, Integer> texTier0 = new Tuple<>(192, 0);
    private static final Tuple<Integer, Integer> texTier1 = new Tuple<>(192, 16);
    private static final Tuple<Integer, Integer> texTier2 = new Tuple<>(192, 32);
    private static final Tuple<Integer, Integer> texTier3 = new Tuple<>(192, 48);
    private static final Tuple<Integer, Integer> texGeneric = new Tuple<>(208, 48);

    public GuiSocketing(InventoryPlayer playerInv) {
        super(new ContainerSocketing(playerInv));
        this.playerInventory = playerInv;
        this.ySize = guiHeight;
    }

    public void initGui() {
        super.initGui();
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("socketed.socketinggui.displayname");
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(SOCKETING_GUI_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        ItemStack socketable = this.inventorySlots.getSlot(0).getStack();
        if(!socketable.isEmpty()) {
            ICapabilitySocketable itemSockets = socketable.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
            if(itemSockets != null && itemSockets.getSocketCount() > 0) {
                int socketCount = itemSockets.getSocketCount();
                int xCent = ContainerSocketing.xCent - 1;
                int yCent = ContainerSocketing.yCent - 1;
                double angleIncr = 2.0D * Math.PI / Math.max(1, socketCount);
                
                ItemStack cursorStack = this.playerInventory.getItemStack();
                GemType gemType = GemType.getGemTypeFromItemStack(cursorStack);
                if(gemType != null) {
                    int tier = gemType.getTier();
                    switch(tier) {
                        case 0: this.drawTexturedModalRect(this.guiLeft + xCent + 1, this.guiTop + yCent - 17, texTier0.getFirst(), texTier0.getSecond(), 16, 16); break;
                        case 1: this.drawTexturedModalRect(this.guiLeft + xCent + 1, this.guiTop + yCent - 17, texTier1.getFirst(), texTier1.getSecond(), 16, 16); break;
                        case 2: this.drawTexturedModalRect(this.guiLeft + xCent + 1, this.guiTop + yCent - 17, texTier2.getFirst(), texTier2.getSecond(), 16, 16); break;
                        case 3: this.drawTexturedModalRect(this.guiLeft + xCent + 1, this.guiTop + yCent - 17, texTier3.getFirst(), texTier3.getSecond(), 16, 16); break;
                    }
                }
                
                for(int socketIndex = 0; socketIndex < socketCount; socketIndex++) {
                    GenericSocket socket = itemSockets.getSocketAt(socketIndex);
                    if(socket == null) continue;
                    int x = this.guiLeft + xCent + (int)(ContainerSocketing.radius * Math.sin(socketIndex * angleIncr));
                    int y = this.guiTop + yCent - (int)(ContainerSocketing.radius * Math.cos(socketIndex * angleIncr));
                    
                    if(cursorStack.isEmpty() || (gemType != null && socket.acceptsGemType(GemType.getGemTypeFromItemStack(cursorStack)) && gemType.hasEffectsForStack(socketable))) {
                        this.drawTexturedModalRect(x, y, texSlotActive.getFirst(), texSlotActive.getSecond(), 18, 18);
                    }
                    else {
                        this.drawTexturedModalRect(x, y, texSlotInactive.getFirst(), texSlotInactive.getSecond(), 18, 18);
                    }
                    
                    if(socket instanceof TieredSocket) {
                        int tier = ((TieredSocket)socket).getTier();
                        switch(tier) {
                            case 0: this.drawTexturedModalRect(x + 1, y + 1, texTier0.getFirst(), texTier0.getSecond(), 16, 16); break;
                            case 1: this.drawTexturedModalRect(x + 1, y + 1, texTier1.getFirst(), texTier1.getSecond(), 16, 16); break;
                            case 2: this.drawTexturedModalRect(x + 1, y + 1, texTier2.getFirst(), texTier2.getSecond(), 16, 16); break;
                            case 3: this.drawTexturedModalRect(x + 1, y + 1, texTier3.getFirst(), texTier3.getSecond(), 16, 16); break;
                        }
                    }
                    else {
                        this.drawTexturedModalRect(x + 1, y + 1, texGeneric.getFirst(), texGeneric.getSecond(), 16, 16);
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}