package socketed.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import socketed.Socketed;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;
import socketed.common.socket.TieredSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CapabilityHasSockets {
    @CapabilityInject(ICapabilityHasSockets.class)
    public static Capability<ICapabilityHasSockets> HAS_SOCKETS;

    public static final ResourceLocation CAPABILITY_KEY = new ResourceLocation(Socketed.MODID, "has_sockets");

    public static List<EntityEquipmentSlot> getSlotsForItemStack(ItemStack stack){
        //TODO: customisable via config
        return Collections.singletonList(EntityLiving.getSlotForItemStack(stack));
    }

    //Default implementation of the capability
    public static class GenericHasSockets implements ICapabilityHasSockets {
        //Copy of the itemstack these sockets belong to
        private final ItemStack itemStack;

        //Ordered list of sockets, default size of 0 sockets
        private final List<GenericSocket> sockets = new ArrayList<>();

        public GenericHasSockets() {
            this(null);
        }
        public GenericHasSockets(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public int getSocketCount() {
            return this.sockets.size();
        }

        @Override
        @Nullable
        public GenericSocket getSocketAt(int socketIndex) {
            if(socketIndex<0 || socketIndex>=sockets.size())
                return null;
            return sockets.get(socketIndex);
        }

        @Override
        @Nonnull
        @Deprecated
        public List<GemInstance> setSocketCount(int newSocketCount) {
            //If setSocketCount increases socketCount, add empty sockets in socketedGems at end of list
            while (newSocketCount > this.sockets.size())
                this.sockets.add(new GenericSocket());
            //Remove sockets if setSocketCount decreases socketCount, return List of gems that have been removed
            List<GemInstance> removedGems = new ArrayList<>();
            while (newSocketCount < this.sockets.size()) {
                int lastIndex = this.sockets.size() - 1;
                if (!this.sockets.get(lastIndex).isEmpty()) {
                    removedGems.add(this.sockets.get(lastIndex).getGem());
                    sockets.remove(lastIndex);
                }
            }
            return removedGems;
        }

        @Override
        public void addSocket(GenericSocket socket) {
            if(socket != null)
                this.sockets.add(socket);
        }

        public boolean addSocketFromNBT(String socketType, NBTTagCompound tags) {
            if(socketType.equals("Generic")) {
                addSocket(new GenericSocket(tags));
                return true;
            }
            if(socketType.equals("Tiered")) {
                addSocket(new TieredSocket(tags));
                return true;
            }
            return false;
        }

        @Override
        @Nullable
        public GemInstance replaceSocketAt(GenericSocket newSocket, int socketIndex) {
            if (socketIndex < 0 || socketIndex >= getSocketCount())
                return null;

            GemInstance gemInOldSocket = sockets.get(socketIndex).getGem();
            GemInstance returnGem = null;
            //Put old gem in new socket if possible
            if (newSocket.isEmpty() && gemInOldSocket!=null && !newSocket.setGem(gemInOldSocket))
                returnGem = gemInOldSocket;

            //Put new socket in old socket slot, deleting the old socket
            this.sockets.set(socketIndex, newSocket);
            return returnGem;
        }

        @Override
        public int getGemCount() {
            int counter = 0;
            for(GenericSocket socket : sockets)
                if(!socket.isEmpty())
                    counter++;
            return counter;
        }

        @Override
        @Nullable
        public GemInstance getGemAt(int socketIndex) {
            if (socketIndex < 0 || socketIndex >= sockets.size())
                return null;
            return this.sockets.get(socketIndex).getGem();
        }

        @Override
        @Nonnull
        public List<GemInstance> getAllGems() {
            List<GemInstance> gems = new ArrayList<>();
            for(GenericSocket socket : this.sockets)
                if(socket.getGem() != null)
                    gems.add(socket.getGem());
            return gems;
        }

        @Override
        public boolean addGem(@Nonnull GemInstance gem) {
            if(!gem.hasEffectsForStackDefaultSlot(this.itemStack)) return false;
            for (GenericSocket socket : sockets)
                if (socket.isEmpty() && socket.setGem(gem))
                    return true;
            return false;
        }

        @Override
        @Nullable
        public GemInstance replaceGemAt(@Nonnull GemInstance gem, int socketIndex) {
            if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
            if(!gem.hasEffectsForStackDefaultSlot(this.itemStack)) return null;
            GemInstance oldGem = sockets.get(socketIndex).getGem();
            sockets.get(socketIndex).setGem(gem);
            return oldGem;
        }

        @Nullable
        @Override
        public GemInstance removeGemAt(int socketIndex) {
            if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
            GenericSocket socket = sockets.get(socketIndex);
            if(socket.isEmpty()) return null;
            GemInstance returnGem = socket.getGem();
            socket.setGem(null);
            return returnGem;
        }

        @Override
        @Nonnull
        public List<GemInstance> removeAllGems() {
            List<GemInstance> gems = getAllGems();
            for(GenericSocket socket : sockets){
                socket.setGem(null);
            }
            return gems;
        }

        @Override
        @Nonnull
        public List<GenericGemEffect> getAllEffects() {
            List<GenericGemEffect> effects = new ArrayList<>();
            for(GenericSocket socket : sockets){
                effects.addAll(socket.getEffects());
            }
            return effects;
        }

        @Override
        @Nonnull
        public List<GenericGemEffect> getAllEffectsForSlot(EntityEquipmentSlot slot) {
            List<GenericGemEffect> effects = new ArrayList<>();
            for(GenericGemEffect effect : getAllEffects())
                if(effect.getSlots().contains(slot))
                    effects.add(effect);
            return effects;
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        final ICapabilityHasSockets instance;

        public Provider(ItemStack stack) {
            this.instance = new GenericHasSockets(stack);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == HAS_SOCKETS;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == HAS_SOCKETS ? HAS_SOCKETS.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) HAS_SOCKETS.getStorage().writeNBT(HAS_SOCKETS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            HAS_SOCKETS.getStorage().readNBT(HAS_SOCKETS, instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilityHasSockets> {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityHasSockets> capability, ICapabilityHasSockets instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();

            NBTTagList socketTagList = new NBTTagList();
            for (int socketIndex = 0; socketIndex < instance.getSocketCount(); socketIndex++)
                socketTagList.appendTag(instance.getSocketAt(socketIndex).writeToNBT());
            nbt.setTag("Sockets", socketTagList);

            return nbt;
        }

        @Override
        public void readNBT(Capability<ICapabilityHasSockets> capability, ICapabilityHasSockets instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;

            NBTTagList socketsNBT = tags.getTagList("Sockets",10);
            for (int socketIndex = 0; socketIndex < socketsNBT.tagCount(); socketIndex++) {
                NBTTagCompound socketNBT = socketsNBT.getCompoundTagAt(socketIndex);
                String socketType = socketNBT.getString("SocketType");
                instance.addSocketFromNBT(socketType,socketNBT);
            }
        }
    }
}