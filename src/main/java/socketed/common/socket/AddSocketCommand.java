package socketed.common.socket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
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
        return "socketed addsocket <tier>(0-3)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] argString) throws WrongUsageException {
        if (sender.getEntityWorld().isRemote) return;
        if (sender.getCommandSenderEntity() == null) return;
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)) return;
        if(argString.length < 2) return;
        if (argString[0].equals("addsocket")) {
            try {
                EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                ItemStack mainhand = player.getHeldItemMainhand().copy();
                if (!mainhand.hasCapability(CapabilityHasSockets.HAS_SOCKETS, null)) return;
                mainhand.getCapability(CapabilityHasSockets.HAS_SOCKETS, null).addSocket(new TieredSocket(Integer.parseInt(argString[1])));
                player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
            } catch(NumberFormatException exception) {
                throw new WrongUsageException("Invalid Usage of Socketed addsocket command, must provide tier (0-3)", new Object[]{argString});
            }
        }
    }
}
