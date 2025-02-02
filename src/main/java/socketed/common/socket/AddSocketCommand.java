package socketed.common.socket;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;

public class AddSocketCommand extends CommandBase {
    
    @Override
    public String getName() {
        return "socketed";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/socketed add <type> <optional:tier>" + "\n" +
                "/socketed replace <index> <type> <optional:tier>" + "\n" +
                "/socketed lock <index>" + "\n" +
                "/socketed disable <index>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] argString) throws WrongUsageException {
        if(!(sender.getCommandSenderEntity() instanceof EntityPlayer)) return;
        if(sender.getEntityWorld().isRemote) return;
        if(argString.length < 1) return;
        EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
        ItemStack mainhand = player.getHeldItemMainhand().copy();
        ICapabilitySocketable cap = mainhand.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(cap == null) return;
		switch(argString[0]) {
			case "add": {
                if(argString.length > 1) {
                    if(argString[1].equals("generic")) {
                        cap.addSocket(new GenericSocket());
                        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                        sender.sendMessage(new TextComponentString("Added generic socket"));
                        return;
                    }
                    else if(argString[1].equals("tiered")) {
                        int tier = 0;
                        try {
                            tier = Math.min(3, Math.max(0, Integer.parseInt(argString[2])));
                        }
                        catch(Exception ignored) {}
                        cap.addSocket(new TieredSocket(tier));
                        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                        sender.sendMessage(new TextComponentString("Added tier " + tier + " socket"));
                        return;
                    }
                    throw new WrongUsageException("Invalid command usage, must specify socket type <generic/tiered>");
                }
                throw new WrongUsageException("Invalid command usage", new Object[]{argString});
            }
			case "replace": {
                if(argString.length > 2) {
                    int index;
                    try {
                        index = Integer.parseInt(argString[1]);
                    }
                    catch(Exception ignored) {
                        index = -1;
                    }
                    if(cap.getSocketAt(index) == null) {
                        throw new WrongUsageException("Invalid command usage, specified index has no socket");
                    }
                    
                    if(argString[2].equals("generic")) {
                        cap.replaceSocketAt(new GenericSocket(), index);
                        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                        sender.sendMessage(new TextComponentString("Replaced index " + index + " with generic socket"));
                        return;
                    }
                    else if(argString[2].equals("tiered")) {
                        int tier = 0;
                        try {
                            tier = Math.min(3, Math.max(0, Integer.parseInt(argString[3])));
                        }
                        catch(Exception ignored) {}
                        cap.replaceSocketAt(new TieredSocket(tier), index);
                        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                        sender.sendMessage(new TextComponentString("Replaced index " + index + " with tier " + tier + " socket"));
                        return;
                    }
                    throw new WrongUsageException("Invalid command usage, must specify socket type <generic/tiered>");
                }
                throw new WrongUsageException("Invalid command usage", new Object[]{argString});
            }
			case "lock": {
                if(argString.length > 1) {
                    int index;
                    try {
                        index = Integer.parseInt(argString[1]);
                    }
                    catch(Exception ignored) {
                        index = -1;
                    }
                    GenericSocket socket = cap.getSocketAt(index);
                    if(socket == null) {
                        throw new WrongUsageException("Invalid command usage, specified index has no socket");
                    }
                    
                    socket.setLocked(!socket.isLocked());
                    player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                    sender.sendMessage(new TextComponentString("Toggled locked at index " + index));
                    return;
                }
                throw new WrongUsageException("Invalid command usage", new Object[]{argString});
            }
			case "disable": {
                if(argString.length > 1) {
                    int index;
                    try {
                        index = Integer.parseInt(argString[1]);
                    }
                    catch(Exception ignored) {
                        index = -1;
                    }
                    GenericSocket socket = cap.getSocketAt(index);
                    if(socket == null) {
                        throw new WrongUsageException("Invalid command usage, specified index has no socket");
                    }
                    
                    socket.setDisabled(!socket.isDisabled());
                    player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainhand);
                    sender.sendMessage(new TextComponentString("Toggled disabled at index " + index));
                    return;
                }
                throw new WrongUsageException("Invalid command usage", new Object[]{argString});
            }
			default: {
                throw new WrongUsageException("Invalid command usage" + "\n" + getUsage(sender));
            }
		}
    }
}