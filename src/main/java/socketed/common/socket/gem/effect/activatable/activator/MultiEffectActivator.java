package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class MultiEffectActivator extends GenericActivator {
	
	public static final String TYPE_NAME = "Multi Effect";
	
	public MultiEffectActivator(@Nullable GenericCondition condition) {
		super(condition);
	}
	
	/**
	 * Called from MultiEffectGemEffect::performEffect
	 * Passes handling through new targets and conditions to allow for performing different effects to different groups of entities from a single original activator
	 * @param effect the sub-effect to be performed
	 * @param callback the callback passed through from the original MultiEffectGemEffect's activator
	 * @param playerSource the player that is the source of the original MultiEffectGemEffect
	 * @param effectTarget the entity that is the target of the original MultiEffectGemEffect's targets
	 */
	public void attemptMultiEffectActivation(ActivatableGemEffect effect, IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(this.testCondition(callback, playerSource, effectTarget)) {
			effect.affectTargets(callback, playerSource, effectTarget);
		}
	}
	
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