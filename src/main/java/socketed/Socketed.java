package socketed;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import socketed.common.IProxy;
import socketed.common.config.CustomConfig;

@Mod(modid = Socketed.MODID, name = Socketed.MODNAME, version = Socketed.VERSION)
public class Socketed {

    public static final String MODID = "socketed";
    public static final String MODNAME = "Socketed";
    public static final String VERSION = "1.0.0";
    public static Logger LOGGER;

    @Mod.Instance(MODID)
    public static Socketed instance;

    @SidedProxy(serverSide = "socketed.common.ServerProxy", clientSide = "socketed.client.ClientProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        CustomConfig.preInit(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public static void postInit(FMLInitializationEvent event) {
        CustomConfig.postInit();
    }
/*
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

 */
}