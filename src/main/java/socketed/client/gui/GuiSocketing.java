package socketed.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.capabilities.CapabilityHasSockets;
import socketed.common.capabilities.ICapabilityHasSockets;
import socketed.common.container.ContainerSocketing;

@SideOnly(Side.CLIENT)
public class GuiSocketing extends GuiContainer {
    private static final ResourceLocation SOCKETING_GUI_TEXTURE = new ResourceLocation(Socketed.MODID, "textures/gui/container/socketing.png");
    private final InventoryPlayer playerInventory;

    public GuiSocketing(InventoryPlayer playerInv) {
        super(new ContainerSocketing(playerInv));
        this.playerInventory = playerInv;
        this.ySize = 222; //same height as double chest
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
        //technically checking for cap isn't needed, as slot only accepts items with the cap, just safety against desyncs maybe
        boolean hasSocketable = !socketable.isEmpty() && socketable.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null);
        if(hasSocketable) {
            ICapabilityHasSockets itemSockets = this.inventorySlots.getSlot(0).getStack().getCapability(CapabilityHasSockets.HAS_SOCKETS,null);
            int socketCount = itemSockets.getSocketCount();
            int xCent = ContainerSocketing.xCent - 1;
            int yCent = ContainerSocketing.yCent - 1;
            double angleIncr = 2. * Math.PI / Math.max(1,socketCount);
            for (int socketIndex = 0; socketIndex < socketCount; socketIndex++) {
                int x = this.guiLeft + xCent + (int) (ContainerSocketing.radius * Math.sin(socketIndex * angleIncr));
                int y = this.guiTop + yCent - (int) (ContainerSocketing.radius * Math.cos(socketIndex * angleIncr));

                this.drawTexturedModalRect(x, y, 0, 222, 18, 18);
            }
        }
        //TODO: draw socket tiers
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}