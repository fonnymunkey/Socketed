package socketed.common.capabilities;

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
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CapabilityHasSockets {
    @CapabilityInject(ICapabilityHasSockets.class)
    public static Capability<ICapabilityHasSockets> HAS_SOCKETS;

    public static final ResourceLocation CAPABILITY_KEY = new ResourceLocation(Socketed.MODID, "has_sockets");

    public static class GenericHasSockets implements ICapabilityHasSockets {
        //Copy of the itemstack these sockets belong to
        private final ItemStack itemStack;

        //Ordered list of sockets, default size of 0 sockets
        //private final List<GenericSocket> sockets = new ArrayList<>();
        private final List<GenericSocket> sockets = Collections.singletonList(new GenericSocket());

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
        public int getGemCount() {
            int counter = 0;
            for(GenericSocket socket : sockets)
                if(!socket.isEmpty())
                    counter++;
            return counter;
        }

        @Override
        @Nonnull
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
        @Nullable
        public GemInstance setSocketAt(GenericSocket socket, int socketIndex) {
            Socketed.LOGGER.info("Set Socket at");
            if(socketIndex < 0 || socketIndex >= getSocketCount())
                return null;
            GemInstance gem = sockets.get(socketIndex).getGem();
            if(socket.setGem(gem))
                return null;
            return gem;
        }

        @Override
        public GenericSocket createSocketFromNBT(String socketType, NBTTagCompound tags) {
            return new GenericSocket(tags);
        }

        @Override
        @Nullable
        public GenericSocket getSocketAt(int socketIndex) {
            if(socketIndex<0 || socketIndex>=sockets.size())
                return null;
            return sockets.get(socketIndex);
        }

        @Override
        public boolean addGem(GemInstance gem) {
            Socketed.LOGGER.info("Add Gem");
            if(!gem.canApplyOn(this.itemStack)) return false;
            for (GenericSocket socket : sockets) {
                if (socket.isEmpty() && socket.setGem(gem)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        @Nullable
        public GemInstance setGemAt(GemInstance gem, int socketIndex) {
            Socketed.LOGGER.info("Set Gem at");
            if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
            if(!gem.canApplyOn(this.itemStack)) return null;
            GemInstance oldGem = sockets.get(socketIndex).getGem();
            sockets.get(socketIndex).setGem(gem);
            return oldGem;
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
        @Nonnull
        public List<GemInstance> removeAllGems() {
            Socketed.LOGGER.info("Remove All Gems");
            List<GemInstance> gems = getAllGems();
            for(GenericSocket socket : sockets){
                socket.setGem(null);
            }
            return gems;
        }

        @Override
        @Nullable
        public GemInstance getGemAt(int socketIndex) {
            if (socketIndex < 0 || socketIndex >= sockets.size())
                return null;
            return this.sockets.get(socketIndex).getGem();
        }

        @Nonnull
        @Override
        public List<GenericGemEffect> getAllEffectsFromAllSockets() {
            List<GenericGemEffect> effects = new ArrayList<>();
            for(GenericSocket socket : sockets){
                effects.addAll(socket.getEffects());
            }
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

            int socketCount = instance.getSocketCount();
            nbt.setInteger("SocketCount", socketCount);

            NBTTagList socketTagList = new NBTTagList();
            for (int socketIndex = 0; socketIndex < socketCount; socketIndex++)
                socketTagList.appendTag(instance.getSocketAt(socketIndex).writeToNBT());
            nbt.setTag("Sockets", socketTagList);

            return nbt;
        }

        @Override
        public void readNBT(Capability<ICapabilityHasSockets> capability, ICapabilityHasSockets instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;

            int socketCount = tags.getInteger("SocketCount");
            instance.setSocketCount(socketCount);

            NBTTagList socketsNBT = tags.getTagList("Sockets",10);
            for (int socketIndex = 0; socketIndex < socketCount; socketIndex++) {
                NBTTagCompound socketNBT = socketsNBT.getCompoundTagAt(socketIndex);
                String socketType = socketNBT.getString("SocketType");
                instance.setSocketAt(instance.createSocketFromNBT(socketType,socketNBT),socketIndex);
            }
        }
    }
}