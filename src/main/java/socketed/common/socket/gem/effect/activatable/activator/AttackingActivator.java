package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class AttackingActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacking";
	
	public AttackingActivator(@Nullable GenericCondition condition, boolean directlyActivated, EventType type) {
		super(condition, directlyActivated, type);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.attacking");
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}