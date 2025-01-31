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
        
        ICapabilitySocketable itemSockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(itemSockets == null) return;
        //Only add sockets to items that don't have sockets yet
        //Adding alreadyBeenChecked tag *shouldn't* be needed currently, at least not with the given contexts yet
        if(itemSockets.getSocketCount() != 0) return;

        //Just for testing:
        switch (context) {
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
    }

    public enum EnumItemCreationContext {
        MERCHANT,
        LOOT,
        MOB_DROP
    }
}
