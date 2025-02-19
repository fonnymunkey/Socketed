package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.GenericEventCallback;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class AttackingActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacking";
	
	public AttackingActivator(@Nullable GenericCondition condition, boolean directlyActivated) {
		super(condition, directlyActivated);
	}
	
	@Override
	protected void attemptAttackActivation(ActivatableGemEffect effect, GenericEventCallback<LivingAttackEvent> callback, EntityPlayer player, EntityLivingBase target, boolean directlyActivated) {
		//Check if direct activation is required
		if(directlyActivated != this.directlyActivated) return;
		if(this.testCondition(callback, player, target)) {
			effect.affectTargets(callback, player, target);
		}
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