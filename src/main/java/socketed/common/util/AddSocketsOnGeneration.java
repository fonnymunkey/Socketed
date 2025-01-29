package socketed.common.util;

import net.minecraft.item.ItemStack;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.socket.GenericSocket;

public class AddSocketsOnGeneration {
    public static void addSockets(ItemStack stack, EnumItemCreationContext context) {
        //TODO: customisable by context and item type via config
        // amount, chance and tiers of sockets should be configable
        // somanyconfigs...
        if (!stack.hasCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null)) return;
        ICapabilitySocketable itemSockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        //Only add sockets to items that don't have sockets yet
        if (itemSockets.getReceivedInitialSockets()) return;
        if (itemSockets.getSocketCount() != 0) return;

        //Just for testing:
        switch (context) {
            case CRAFTING:
                itemSockets.addSocket(new GenericSocket());
                break;
            case MERCHANT:
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                break;
            case LOOT:
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                break;
            case MOB_DROP:
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                itemSockets.addSocket(new GenericSocket());
                break;
        }

        itemSockets.setReceivedInitialSockets();
    }

    public enum EnumItemCreationContext {
        CRAFTING,
        MERCHANT,
        LOOT,
        MOB_DROP
    }
}
