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

public class AttackedActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacked";
	
	public AttackedActivator(@Nullable GenericCondition condition, boolean directlyActivated) {
		super(condition, directlyActivated);
	}
	
	@Override
	protected void attemptAttackActivation(ActivatableGemEffect effect, GenericEventCallback<LivingAttackEvent> callback, EntityPlayer player, EntityLivingBase attacker, boolean directlyActivated) {
		//Check if direct activation is required
		//Attacked activators can only be directly activated if First Aid is loaded
		if (directlyActivated != this.directlyActivated) return;
		if (this.testCondition(callback, player, attacker)) {
			effect.affectTargets(callback, player, attacker);
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