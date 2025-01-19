package socketed.common.handlers;

import net.minecraft.item.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.common.capabilities.CapabilityHasSockets;

@Mod.EventBusSubscriber
public class AttachSocketCapabilityHandler {
    @SubscribeEvent
    public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.isEmpty()) return;
        if (stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null)) return;
        if (stack.getMaxStackSize() > 1) return;
        Item item = stack.getItem();
        //Allowed item types that can get sockets
        //TODO: add custom config to add more items to this
        if (item instanceof ItemArmor || item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword)
            event.addCapability(CapabilityHasSockets.CAPABILITY_KEY, new CapabilityHasSockets.Provider(stack));
    }
}
