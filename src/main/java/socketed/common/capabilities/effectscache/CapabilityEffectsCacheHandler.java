package socketed.common.capabilities.effectscache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityEffectsCacheHandler {
    
    public static final ResourceLocation CAP_EFFECTS_CACHE_KEY = new ResourceLocation(Socketed.MODID, "effects_cache");
    
    @CapabilityInject(ICapabilityEffectsCache.class)
    public static Capability<ICapabilityEffectsCache> CAP_EFFECTS_CACHE;
    
    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ICapabilityEffectsCache.class, new Storage(), CapabilityEffectsCache::new);
    }
    
    @Mod.EventBusSubscriber
    public static class AttachCapabilityHandler {
        
        @SubscribeEvent
        public static void attachCapabilitiesEventPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(!(event.getObject() instanceof EntityPlayer)) return;
            if(event.getObject().hasCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null)) return;
            event.addCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE_KEY, new CapabilityEffectsCacheHandler.Provider());
        }

        //No need to subscribe to PlayerEvent.Clone (death/end portal) because the new player entity will fire living equipment change events anyway
    }

    public static class Provider implements ICapabilityProvider {
        
        private final ICapabilityEffectsCache instance;

        public Provider() {
            this.instance = new CapabilityEffectsCache();
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAP_EFFECTS_CACHE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAP_EFFECTS_CACHE ? CAP_EFFECTS_CACHE.cast(instance) : null;
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilityEffectsCache> {
        //Unused

        @Override
        public NBTBase writeNBT(Capability<ICapabilityEffectsCache> capability, ICapabilityEffectsCache instance, EnumFacing side) {
            return new NBTTagCompound();
        }

        @Override
        public void readNBT(Capability<ICapabilityEffectsCache> capability, ICapabilityEffectsCache instance, EnumFacing side, NBTBase nbt) {
            //no op, player fires living equipment change event on join anyway
        }
    }
}