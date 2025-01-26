package socketed.common.init;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import socketed.common.item.ItemSocketTool;

public class ModItems {
    
    public static final ItemSocketTool SOCKET_TOOL = new ItemSocketTool("socket_tool");

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(SOCKET_TOOL);
    }

    public static void registerModels() {
        SOCKET_TOOL.registerModel();
    }
}