package socketed.mixin.vanilla;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface IGuiContainerMixin {
	
	@Accessor("hoveredSlot")
	Slot getHoveredSlot();
}