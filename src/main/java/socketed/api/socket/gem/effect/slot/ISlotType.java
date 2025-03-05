package socketed.api.socket.gem.effect.slot;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public interface ISlotType {
	
	/**
	 * Note: Use SocketedUtil::isStackValidForSlot if possible to allow for config override compatibility
	 * @return true if the provided ItemStack is valid for this ISlotType
	 */
	boolean isStackValid(ItemStack stack);
	
	/**
	 * @return true if the provided ISlotType is valid for this ISlotType (Equals or inherits)
	 */
	boolean isSlotValid(ISlotType type);
	
	/**
	 * @return the ItemStack(s) equipped on the provided EntityPlayer for this ISlotType
	 */
	Set<ItemStack> getEquippedStacks(EntityPlayer player);
	
	/**
	 * @return the tooltip key for this ISlotType
	 */
	String getTooltipKey();
	
	/**
	 * @return the translated tooltip for the given ISlotType
	 */
	@SideOnly(Side.CLIENT)
	static String getSlotTooltip(ISlotType slotType) {
		return I18n.format("socketed.tooltip.slottype." + slotType.getTooltipKey());
	}
}