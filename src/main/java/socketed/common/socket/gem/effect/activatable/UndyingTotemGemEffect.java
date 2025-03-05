package socketed.common.socket.gem.effect.activatable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class UndyingTotemGemEffect extends ActivatableGemEffect {
	
	public static final String TYPE_NAME = "Undying Totem";
	
	public UndyingTotemGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets) {
		super(slotType, activator, targets);
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(effectTarget != null && !effectTarget.world.isRemote) {
			effectTarget.setHealth(1.0F);
			effectTarget.clearActivePotions();
			effectTarget.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
			effectTarget.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
			if(effectTarget instanceof EntityPlayer) effectTarget.world.setEntityState(effectTarget, (byte)35);
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