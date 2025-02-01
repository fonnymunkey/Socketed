package socketed.common.jsondata.entry.effect.slot;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISlotType {
	
	boolean isStackValid(ItemStack stack);
	
	//TODO: Better method of handling this for non-equipmentslot slots
	boolean isSlotValid(EntityEquipmentSlot slot);
	
	String getTooltipKey();
	
	//TODO: allow for config override?
	static boolean isStackValidForSlot(ItemStack stack, ISlotType slotType) {
		return slotType.isStackValid(stack);
	}
	
	@SideOnly(Side.CLIENT)
	static String getSlotTooltip(ISlotType slotType) {
		return I18n.format("socketed.tooltip.slottype." + slotType.getTooltipKey());
	}
}