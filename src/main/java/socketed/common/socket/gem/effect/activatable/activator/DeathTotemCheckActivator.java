package socketed.common.socket.gem.effect.activatable.activator;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.CancelEventCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class DeathTotemCheckActivator extends GenericActivator {
	
	public static final String TYPE_NAME = "Death Totem Check";
	
	public DeathTotemCheckActivator(@Nullable GenericCondition condition) {
		super(condition);
	}
	
	/**
	 * Called from EntityLivingBase::checkTotemDeathProtection for players if the player is not already saved by a totem
	 * If the death is prevented from callback, only sets the player health to 1.0, see UndyingTotemGemEffect for performing the rest of the normal totem effects
	 * @param effect the effect parent of this activator
	 * @param callback the Cancellable callback to allow for the effect cancelling the death
	 * @param player the player that is the source of the effect
	 */
	public void attemptDeathTotemCheckActivation(ActivatableGemEffect effect, CancelEventCallback callback, EntityPlayer player) {
		if(this.testCondition(callback, player, player)) {
			effect.affectTargets(callback, player, player);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.activator.deathtotemcheck");
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}