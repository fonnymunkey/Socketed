package socketed.common.socket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import socketed.common.capabilities.CapabilityHasSockets;

public class AddSocketCommand extends CommandBase {
    @Override
    public String getName() {
        return "socketed";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "socketed addsocket";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] argString) {
        if (sender.getEntityWorld().isRemote) return;
        if (sender.getCommandSenderEntity() == null) return;
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)) return;
        if (argString[0].equals("addsocket")) {
            EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
            ItemStack mainhand = player.getHeldItemMainhand().copy();
            if(!mainhand.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null)) return;
            mainhand.getCapability(CapabilityHasSockets.HAS_SOCKETS,null).addSocket(new GenericSocket());
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,mainhand);
        }
    }
}
