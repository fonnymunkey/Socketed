package socketed.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = socketed.Socketed.MODID, name = socketed.Socketed.MODNAME, category = "")
public class ForgeConfig {

    public static General general = new General();

    public static Client client = new Client();

    public static class General {
        @Config.Comment("Maximum amount of sockets an item can have")
        @Config.Name("Max Sockets")
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
            if(event.getModID().equals(socketed.Socketed.MODID)) ConfigManager.sync(socketed.Socketed.MODID, Config.Type.INSTANCE);
        }
    }
}
