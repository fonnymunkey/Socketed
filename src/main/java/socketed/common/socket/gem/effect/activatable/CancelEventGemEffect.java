package socketed.common.socket.gem.effect.activatable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.activatable.callback.CancelEventCallback;
import socketed.common.socket.gem.effect.activatable.callback.GenericEventCallback;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class CancelEventGemEffect extends ActivatableGemEffect {
	
	public static final String TYPE_NAME = "Cancel Event";
	
	public CancelEventGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets) {
		super(slotType, activator, targets);
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			if(callback instanceof CancelEventCallback) ((CancelEventCallback)callback).setCancelled(true);
			else if(callback instanceof GenericEventCallback){
				Event event = ((GenericEventCallback<?>) callback).getEvent();
				if(event.isCancelable())
					event.setCanceled(true);
			}
		}
	}
	
	//TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return "";
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}