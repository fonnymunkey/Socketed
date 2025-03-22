package socketed.common.socket.gem.effect.activatable.target;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;

import javax.annotation.Nullable;

public class OtherTarget extends GenericTarget {
	
	public static final String TYPE_NAME = "Other";
	
	public OtherTarget(@Nullable GenericCondition condition) {
		super(condition);
	}
	
	@Override
	public void affectTarget(ActivatableGemEffect effect, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(this.testCondition(callback, playerSource, effectTarget)) {
			effect.performEffect(callback, playerSource, effectTarget);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return I18n.format("socketed.tooltip.target.other");
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
}