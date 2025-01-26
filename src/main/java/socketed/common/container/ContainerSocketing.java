package socketed.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.GemInstance;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.config.ForgeConfig;

import javax.annotation.Nonnull;

public class ContainerSocketing extends Container {
    
    private final IInventory socketingInventory;
    public static final int xCent = 80;
    public static final int yCent = 63;
    public static final double radius = 45.;

    public ContainerSocketing(InventoryPlayer playerInventory) {
        this.socketingInventory = new InventoryBasic("Socketing", false, 1 + ForgeConfig.COMMON.maxSockets) {
            public int getInventoryStackLimit() {
                return 1;
            }
        };

        //Socket slot
        this.addSlotToContainer(new SlotSocketable(this.socketingInventory, 0, xCent, yCent));

        //Gem slots
        for(int i = 0; i < ForgeConfig.COMMON.maxSockets; i++) {
            this.addSlotToContainer(new SlotGem(this.socketingInventory, 1 + i, Integer.MAX_VALUE, Integer.MAX_VALUE));
        }

        //Player inventory slots
        for(int k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 140 + k * 18));
            }
        }
        //Player hotbar slots
        for(int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 198));
        }
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.socketingInventory);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return this.socketingInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer player) {
        super.onContainerClosed(player);
        if(!player.world.isRemote) this.clearContainer(player, player.world, this.socketingInventory);
    }

    @Override
    protected void clearContainer(EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull IInventory inventoryIn) {
        //We don't give gems back, as they are stored in the socketed item
        if(!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected()) {
            playerIn.dropItem(inventoryIn.removeStackFromSlot(0), false);
        }
        else {
            playerIn.inventory.placeItemBackInInventory(worldIn, inventoryIn.removeStackFromSlot(0));
        }
    }

    private final int indexPlayerSlotsStart = 1 + ForgeConfig.COMMON.maxSockets;
    private final int indexPlayerSlotsEnd = 1 + ForgeConfig.COMMON.maxSockets + 36;
    
    /**
     * Player shift clicks (quick move) on a slot fromIndex. this function tries to move the stack in that slot to the correct container slot or back
     * @return the itemstack that is left over after this move (will be done multiple times in Container.slotClick to potentially move to multiple slots)
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int fromIndex) {
        ItemStack stackFromCopy = ItemStack.EMPTY;
        Slot slotFrom = this.inventorySlots.get(fromIndex);

        if(slotFrom != null && slotFrom.getHasStack()) {
            ItemStack stackFrom = slotFrom.getStack();
            stackFromCopy = stackFrom.copy();

            if(fromIndex == 0) {
                //Take from socketable item slot, merge into player inventory if possible
                //Fail if can't put in player inventory
                if(!this.mergeItemStack(stackFrom, indexPlayerSlotsStart, indexPlayerSlotsEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(isGemSlot(fromIndex)) {
                //Take from gem socket slots, merge into player inventory if possible
                //Fail if can't put in player inventory
                if(!this.mergeItemStack(stackFrom, indexPlayerSlotsStart, indexPlayerSlotsEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(GemInstance.stackIsGem(stackFrom)) {
                //Take gem from player inventory, merge into a socket (todo: this should probably be harder to do to have no accidents)
                //Fail if cant put gem item in gem slots
                if(!this.mergeItemStack(stackFrom, 1, indexPlayerSlotsStart, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else { //index is in player inventory and item is not gem
                //Take socketable item from player inventory, put into socketable item slot

                //Fail if socketable slot is already full or if item not socketable
                if(this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(stackFrom)) {
                    return ItemStack.EMPTY;
                }

                //Forge Special move if item has custom NBT to not have it be deleted
                this.inventorySlots.get(0).putStack(stackFrom.splitStack(1));
            }

            //Clear old slot if all items were moved
            if(stackFrom.isEmpty()) slotFrom.putStack(ItemStack.EMPTY);
            //or mark dirty if amount changed
            else slotFrom.onSlotChanged();

            //if moving failed, return empty stack
            if(stackFrom.getCount() == stackFromCopy.getCount()) return ItemStack.EMPTY;

            slotFrom.onTake(playerIn, stackFrom);
        }
        //no early returns/fails, return original stack amount (why not new amount? i guess it doesnt really matter since on next call it gets the slot contents anyway. only matters if the whole slotclicked thing fails)
        return stackFromCopy;
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull EntityPlayer player) {
        if(isGemSlot(slotId) && !this.inventorySlots.get(slotId).getStack().isEmpty()) {
            if(!ForgeConfig.COMMON.gemsAreRemovable) {
                return ItemStack.EMPTY;
            }
            else if(ForgeConfig.COMMON.destroyGemsOnRemoval) {
                this.putStackInSlot(slotId, ItemStack.EMPTY);
                return ItemStack.EMPTY;
            }
        }
        return super.slotClick(slotId,dragType,clickType,player);
    }

    private static boolean isGemSlot(int slotId) {
        return slotId > 0 && slotId <= ForgeConfig.COMMON.maxSockets;
    }

    public class SlotSocketable extends Slot {
        
        public SlotSocketable(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }
        
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public void onSlotChanged() {
            if(this.getHasStack()){
                ICapabilitySocketable sockets = this.getStack().getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
                int socketCount = sockets.getSocketCount();

                //Gem slots
                double angleIncr = 2.0*Math.PI/Math.max(1, socketCount);
                for(int socketIndex = 0; socketIndex < socketCount; socketIndex++) {
                    int x = xCent + (int)(radius*Math.sin(socketIndex*angleIncr));
                    int y = yCent - (int)(radius*Math.cos(socketIndex*angleIncr));

                    inventorySlots.get(socketIndex+1).xPos = x;
                    inventorySlots.get(socketIndex+1).yPos = y;
                }

                for(int i = 0; i < socketCount; i++) {
                    GemInstance gem = sockets.getGemAt(i);
                    if(gem != null) {
                        this.inventory.setInventorySlotContents(i + 1, gem.getItemStack());
                    }
                }
            }
            else {
                for(int socketIndex = 0; socketIndex < ForgeConfig.COMMON.maxSockets; socketIndex++){
                    this.inventory.setInventorySlotContents(socketIndex + 1, ItemStack.EMPTY);
                    inventorySlots.get(socketIndex+1).xPos = Integer.MAX_VALUE;
                    inventorySlots.get(socketIndex+1).yPos = Integer.MAX_VALUE;
                }
            }
            this.inventory.markDirty();
        }
    }

    public static class SlotGem extends Slot {
        
        public SlotGem(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            ItemStack socketable = this.inventory.getStackInSlot(0);
            if(socketable.isEmpty()) return false;
            ICapabilitySocketable itemSockets = socketable.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
            if(itemSockets.getSocketCount() < this.getSlotIndex()) return false;
            GemInstance gem = new GemInstance(stack);
            return itemSockets.getSocketAt(this.getSlotIndex() - 1).acceptsGem(gem);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public void onSlotChanged() {
            if(!this.inventory.getStackInSlot(0).isEmpty()) {
                ICapabilitySocketable sockets = this.inventory.getStackInSlot(0).getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
                if(this.getHasStack()) sockets.replaceGemAt(new GemInstance(this.getStack()),this.getSlotIndex() - 1);
                else sockets.removeGemAt(this.getSlotIndex() - 1);
            }
            this.inventory.markDirty();
        }
    }
}