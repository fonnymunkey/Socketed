package socketed;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import socketed.common.capabilities.*;
import socketed.common.config.CustomConfig;
import socketed.common.data.RecipientGroup;
import socketed.common.init.ModItems;
import socketed.common.init.ModRecipes;
import socketed.common.proxy.IProxy;
import socketed.common.socket.SocketLootFunction;

@Mod(modid = Socketed.MODID, name = Socketed.MODNAME, version = Socketed.VERSION)
public class Socketed {

    public static final String MODID = "socketed";
    public static final String MODNAME = "Socketed";
    public static final String VERSION = "1.0.0";
    public static Logger LOGGER;

    @Mod.Instance(MODID)
    public static Socketed instance;

    @SidedProxy(serverSide = "socketed.common.proxy.ServerProxy", clientSide = "socketed.client.proxy.ClientProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        CustomConfig.preInit(event.getModConfigurationDirectory());

        CapabilityManager.INSTANCE.register(ICapabilityHasSockets.class, new CapabilityHasSockets.Storage(), CapabilityHasSockets.GenericHasSockets::new);
        LootFunctionManager.registerFunction(new SocketLootFunction.Serializer());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public static void postInit(FMLInitializationEvent event) {
        CustomConfig.postInit();
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.registerItems(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
            ModRecipes.registerRecipes(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItemModels(ModelRegistryEvent event) {
            ModItems.registerModels();
        }

        @SubscribeEvent
        public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
            ItemStack stack = event.getObject();
            if (stack.isEmpty()) return;
            if (stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null)) return;

            for (RecipientGroup recipientGroup : CustomConfig.getRecipientData().values()) {
                if (recipientGroup.matches(stack)) {
                    event.addCapability(CapabilityHasSockets.CAPABILITY_KEY, new CapabilityHasSockets.Provider(stack));
                    return;
                }
            }
        }
    }
}