package socketed.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = socketed.Socketed.MODID, name = socketed.Socketed.MODNAME, category = "")
public class ForgeConfig {

    @Config.Name("Common")
    public static Common COMMON = new Common();

    @Config.Name("Client")
    public static Client CLIENT = new Client();

    @Config.Name("Socketable Items")
    public static SocketableConfig SOCKETABLES = new SocketableConfig();

    @Config.Name("Default Socket Acquisition")
    public static AddSocketsConfig ADD_SOCKETS = new AddSocketsConfig();

    public static class Common {
        @Config.Comment("Maximum amount of sockets any item can have")
        @Config.Name("Max Sockets")
        @Config.RangeInt(min = 1, max = 8)
        public int maxSockets = 8;

        @Config.Comment("Set to false to disable removing gems from sockets")
        @Config.Name("Gems are removable")
        public boolean gemsAreRemovable = true;

        @Config.Comment("Set to true to destroy gems when removing them from sockets")
        @Config.Name("Destroy gems on removal")
        public boolean destroyGemsOnRemoval = false;
    }

    public static class Client {

    }

    @Mod.EventBusSubscriber(modid = socketed.Socketed.MODID)
    private static class EventHandler {
        
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(socketed.Socketed.MODID)) {
                ConfigManager.sync(socketed.Socketed.MODID, Config.Type.INSTANCE);
                ForgeConfig.SOCKETABLES.reset();
            }
        }
    }
}