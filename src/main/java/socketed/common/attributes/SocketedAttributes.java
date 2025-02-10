package socketed.common.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SocketedAttributes {
    
    public static final IAttribute DURABILITY = (new RangedAttribute(null, "socketed.durability", 1.0D, 0.0D, 1000.0)).setDescription("Durability").setShouldWatch(true);

    @SubscribeEvent
    public static void onEntityConstruction(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityPlayer) {
            ((EntityPlayer)event.getEntity()).getAttributeMap().registerAttribute(DURABILITY);
        }
    }
}