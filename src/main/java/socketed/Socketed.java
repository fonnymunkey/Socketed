package socketed;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import socketed.common.capabilities.CapabilityHasSockets;
import socketed.common.capabilities.ICapabilityHasSockets;
import socketed.common.config.JsonConfig;
import socketed.common.container.GuiHandlerSocketing;
import socketed.common.init.ModItems;
import socketed.common.init.ModRecipes;
import socketed.common.proxy.IProxy;
import socketed.common.socket.AddSocketCommand;
import socketed.common.lootfunctions.SocketLootFunction;

import java.util.Random;

@Mod(modid = Socketed.MODID, name = Socketed.MODNAME, version = Socketed.VERSION)
public class Socketed {

    public static final String MODID = "socketed";
    public static final String MODNAME = "Socketed";
    public static final String VERSION = "1.0.0";
    public static Logger LOGGER;

    @Mod.Instance(MODID)
    public static Socketed instance;

    public static Random RAND = new Random();

    @SidedProxy(serverSide = "socketed.common.proxy.ServerProxy", clientSide = "socketed.client.proxy.ClientProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        JsonConfig.preInit(event.getModConfigurationDirectory());

        CapabilityManager.INSTANCE.register(ICapabilityHasSockets.class, new CapabilityHasSockets.Storage(), CapabilityHasSockets.GenericHasSockets::new);
        LootFunctionManager.registerFunction(new SocketLootFunction.Serializer());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandlerSocketing());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new AddSocketCommand());
    }

    @Mod.EventHandler
    public static void postInit(FMLInitializationEvent event) {
        JsonConfig.postInit();
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
    }
}