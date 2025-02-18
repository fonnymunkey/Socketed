package socketed.common.socket.gem.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.CancelEventCallback;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class AttackingActivator extends AttackActivator {
	
	public static final String TYPE_NAME = "Attacking";
	
	@SerializedName("Directly Activated")
	protected Boolean directlyActivated;
	
	public AttackingActivator(@Nullable GenericCondition condition, boolean allowsMelee, boolean allowsRanged, boolean directlyActivated) {
		super(condition, allowsMelee, allowsRanged);
		this.directlyActivated = directlyActivated;
	}
	
	@Override
	protected void attemptAttackActivation(ActivatableGemEffect effect, CancelEventCallback callback, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean isMelee, boolean isRanged, boolean directlyActivated, DamageSource source) {
		//Check if direct activation is required
		if(directlyActivated != this.directlyActivated) return;
		//Player is the attacker
		if(player == attacker) {
			if((this.allowsMelee && isMelee) || this.allowsRanged && isRanged) {
				if(this.testCondition(callback, player, target)) {
					effect.affectTargets(callback, player, target);
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
	
	/**
	 * DirectlyActivated: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.directlyActivated == null) this.directlyActivated = false;
		
		return super.validate();
	}
}