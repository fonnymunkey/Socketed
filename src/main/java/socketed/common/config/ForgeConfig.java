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
        
        @Config.Comment(
                "Maximum tier possible for tiered sockets" + "\n" +
                "Warning: If you change this value from default you will want to supply your own textures and language files")
        @Config.Name("Max Socket Tier")
        @Config.RangeInt(min = 0, max = 5)
        @Config.RequiresMcRestart
        public int maxSocketTier = 3;

        @Config.Comment("If set to false, gems can only be placed into sockets, not removed")
        @Config.Name("Removable Gems")
        public boolean gemsAreRemovable = true;

        @Config.Comment("If Removable Gems is enabled, destroys the gem on removal instead of returning it to the player")
        @Config.Name("Destructive Gem Removal")
        public boolean destroyGemsOnRemoval = false;
        
        @Config.Comment("If enabled, Socketed will attempt to prevent items with sockets from being enchanted, and enchanted items from getting sockets")
        @Config.Name("Enchantment Lock")
        public boolean enchantmentLock = false;
        
        @Config.Comment("Only loads gem effects from built-in default effects including from addons, disable this when creating your own effects")
        @Config.Name("Only Use Default Effects")
        public boolean onlyUseDefaults = true;
    }

    public static class Client {
        //Unused currently
    }
    
    public static void reset() {
        ForgeConfig.SOCKETABLES.reset();
        ForgeConfig.ADD_SOCKETS.reset();
    }

    @Mod.EventBusSubscriber(modid = socketed.Socketed.MODID)
    private static class EventHandler {
        
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(socketed.Socketed.MODID)) {
                ConfigManager.sync(socketed.Socketed.MODID, Config.Type.INSTANCE);
                ForgeConfig.reset();
            }
        }
    }
}