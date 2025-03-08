package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class AttackedActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacked";
	
	public AttackedActivator(@Nullable GenericCondition condition, boolean directlyActivated, EventType type) {
		super(condition, directlyActivated, type);
	}
	
	//TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return "";
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}