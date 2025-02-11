package socketed.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;

@Config(modid = Socketed.MODID, name = Socketed.MODNAME, category = "")
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
        @Config.Comment(
                "Maximum amount of sockets any item can have" + "\n" +
                "Warning: Lowering this value can cause already existing items to lose excess sockets")
        @Config.Name("Max Sockets")
        @Config.RangeInt(min = 1, max = 8)
        @Config.RequiresMcRestart
        public int maxSockets = 8;

        @Config.Comment("If set to false, gems can only be placed into sockets, not removed")
        @Config.Name("Removable Gems")
        public boolean gemsAreRemovable = true;

        @Config.Comment("If Removable Gems is enabled, destroys the gem on removal instead of returning it to the player")
        @Config.Name("Destructive Gem Removal")
        public boolean destroyGemsOnRemoval = false;
    }

    public static class Client {
        //Unused currently
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