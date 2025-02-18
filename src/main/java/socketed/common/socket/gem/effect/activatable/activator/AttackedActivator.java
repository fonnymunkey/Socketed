package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.CancelEventCallback;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class AttackedActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacked";
	
	public AttackedActivator(@Nullable GenericCondition condition, boolean allowsMelee, boolean allowsRanged) {
		super(condition, allowsMelee, allowsRanged);
	}
	
	@Override
	protected void attemptAttackActivation(ActivatableGemEffect effect, CancelEventCallback callback, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean isMelee, boolean isRanged, boolean directlyActivated, DamageSource source) {
		//Attacked activators can not be directly activated
		if(directlyActivated) return;
		//Player is being attacked
		if(player == target) {
			if((this.allowsMelee && isMelee) || this.allowsRanged && isRanged) {
				if(this.testCondition(callback, player, attacker)) {
					effect.affectTargets(callback, player, attacker);
				}
			}
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