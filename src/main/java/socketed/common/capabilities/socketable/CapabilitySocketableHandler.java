package socketed.common.capabilities.socketable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;
import socketed.common.instances.GemCombinationInstance;
import socketed.common.config.ForgeConfig;
import socketed.common.socket.GenericSocket;
import socketed.common.util.AddSocketsOnGeneration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitySocketableHandler {
    
    public static final ResourceLocation CAP_SOCKETABLE_KEY = new ResourceLocation(Socketed.MODID, "socketable");
    
    @CapabilityInject(ICapabilitySocketable.class)
    public static Capability<ICapabilitySocketable> CAP_SOCKETABLE;
    
    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ICapabilitySocketable.class, new Storage(), CapabilitySocketable::new);
    }
    
    @Mod.EventBusSubscriber
    public static class AttachCapabilityHandler {
        
        @SubscribeEvent
        public static void attachCapabilitiesEventItemStack(AttachCapabilitiesEvent<ItemStack> event) {
            ItemStack stack = event.getObject();
            if(stack.isEmpty()) return;
            if(stack.hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null)) return;
            if(stack.getMaxStackSize() > 1) return;

            //Allowed item types that can get sockets
            if(ForgeConfig.SOCKETABLES.canSocket(stack))
                event.addCapability(CapabilitySocketableHandler.CAP_SOCKETABLE_KEY, new CapabilitySocketableHandler.Provider(stack));
        }

        //TODO: Mixin into LootTable::generateLootForPools instead to apply to all loot only? Allows for context checks and avoids affecting equipment
        @SubscribeEvent
        public static void onLivingDrops(LivingDropsEvent event) {
            //TODO: stop socket farm where players just let mobs pick up unsocketed items until they get sockets
            for(EntityItem itemDrop : event.getDrops()) {
                AddSocketsOnGeneration.addSockets(itemDrop.getItem(), AddSocketsOnGeneration.EnumItemCreationContext.MOB_DROP);
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        
        private final ICapabilitySocketable instance;

        public Provider(ItemStack stack) {
            this.instance = new CapabilitySocketable(stack);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAP_SOCKETABLE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAP_SOCKETABLE ? CAP_SOCKETABLE.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound)CAP_SOCKETABLE.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAP_SOCKETABLE.readNBT(instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilitySocketable> {
        
        //TODO: Why does this get called every tick for every item in player inventory?
        //TODO: Syncing to client for servers for tooltip rendering
        @Override
        public NBTBase writeNBT(Capability<ICapabilitySocketable> capability, ICapabilitySocketable instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();

            NBTTagList socketTagList = new NBTTagList();
            for(GenericSocket socket : instance.getSockets()) {
                socketTagList.appendTag(socket.writeToNBT());
            }
            //Don't store NBT to stacks that could get sockets but don't have anything yet
            if(!socketTagList.isEmpty()) nbt.setTag("Sockets", socketTagList);

            NBTTagList gemCombinationTagList = new NBTTagList();
            for(GemCombinationInstance gemCombination : instance.getGemCombinations()) {
                gemCombinationTagList.appendTag(gemCombination.writeToNBT());
            }
            //Don't store NBT to stacks that could get sockets but don't have anything yet
            if(!gemCombinationTagList.isEmpty()) nbt.setTag("GemCombinations", gemCombinationTagList);

            return nbt;
        }

        @Override
        public void readNBT(Capability<ICapabilitySocketable> capability, ICapabilitySocketable instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound)nbt;
            
            boolean read = false;
            if(tags.hasKey("Sockets")) {
                NBTTagList socketsNBT = tags.getTagList("Sockets",10);
                for(int socketIndex = 0; socketIndex < socketsNBT.tagCount(); socketIndex++) {
                    NBTTagCompound socketNBT = socketsNBT.getCompoundTagAt(socketIndex);
                    String socketType = socketNBT.getString("SocketType");
                    instance.addSocketFromNBT(socketType, socketNBT);
                    read = true;
                }
            }
            if(tags.hasKey("GemCombinations")) {
                NBTTagList combinationsNBT = tags.getTagList("GemCombinations",10);
                for(int combinationIndex = 0; combinationIndex < combinationsNBT.tagCount(); combinationIndex++) {
                    instance.addCombinationFromNBT(combinationsNBT.getCompoundTagAt(combinationIndex));
                    read = true;
                }
            }
            //Refresh combinations after reading to account for combination changes on update
            if(read) instance.refreshCombinations();
        }
    }
}