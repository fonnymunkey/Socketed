package socketed.common.jsondata.entry.effect.activatable.activator.passive;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.jsondata.entry.effect.activatable.ActivatableGemEffect;

public class PassiveSelfActivator extends PassiveActivator {
	
	public static final String TYPE_NAME = "Passive Self";
	
	public PassiveSelfActivator(int activationRate) {
		super(activationRate);
	}
	
	@Override
	public void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player) {
		effect.performEffect(player);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.passive_self");
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}
