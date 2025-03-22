package socketed.common.socket.gem.effect.activatable;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;
import socketed.api.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class RemoveTargetGemEffect extends ActivatableGemEffect {

	public static final String TYPE_NAME = "Remove Target";

	public RemoveTargetGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, String tooltipKey) {
		super(slotType, activator, targets, tooltipKey);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.effect.removetarget");
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(effectTarget != null && !effectTarget.world.isRemote && effectTarget instanceof EntityLiving) {
			((EntityLiving)effectTarget).setAttackTarget(null);
		}
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}