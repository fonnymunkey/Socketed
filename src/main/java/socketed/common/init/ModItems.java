package socketed.common.init;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import socketed.common.config.ForgeConfig;
import socketed.common.item.ItemSocketGeneric;
import socketed.common.item.ItemSocketTier;
import socketed.common.item.ItemSocketTool;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    
    public static final ItemSocketTool ITEM_SOCKET_TOOL = new ItemSocketTool("socket_tool");
    public static final List<ItemSocketGeneric> socketItems = new ArrayList<>();

    static {
        if(ForgeConfig.ADD_SOCKETS.registerSocketItems) {
            socketItems.add(new ItemSocketGeneric("socket_generic"));
            socketItems.add(new ItemSocketTier("socket_tier_0", 0));
            socketItems.add(new ItemSocketTier("socket_tier_1", 1));
            socketItems.add(new ItemSocketTier("socket_tier_2", 2));
            socketItems.add(new ItemSocketTier("socket_tier_3", 3));
        }
    }
    
    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(ITEM_SOCKET_TOOL);
        for(ItemSocketGeneric socket : socketItems) {
            registry.register(socket);
        }
    }

    public static void registerModels() {
        ITEM_SOCKET_TOOL.registerModel();
        for(ItemSocketGeneric socket : socketItems) {
            socket.registerModel();
        }
    }
}