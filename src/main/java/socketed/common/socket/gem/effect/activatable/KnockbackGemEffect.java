package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.target.GenericTarget;
import socketed.api.socket.gem.effect.slot.ISlotType;

import javax.annotation.Nullable;
import java.util.List;

public class KnockbackGemEffect extends ActivatableGemEffect {
	
	public static final String TYPE_NAME = "Knockback";
	
	@SerializedName("Strength")
	private final Float strength;
	
	@SerializedName("Inverted")
	private Boolean inverted;
	
	public KnockbackGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, float strength, boolean inverted) {
		super(slotType, activator, targets);
		this.strength = strength;
		this.inverted = inverted;
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote && effectTarget != playerSource) {
			if(this.inverted) knockBackIgnoreKBRes(effectTarget, 0.5F + this.strength, effectTarget.posX - playerSource.posX, effectTarget.posZ - playerSource.posZ);
			else knockBackIgnoreKBRes(effectTarget, 0.5F + this.strength, playerSource.posX - effectTarget.posX, playerSource.posZ - effectTarget.posZ);
		}
	}
	
	/**
	 * https://github.com/fonnymunkey/SoManyEnchantments/blob/master/src/main/java/com/shultrea/rin/util/EnchantUtil.java
	 */
	private static void knockBackIgnoreKBRes(EntityLivingBase entityIn, float strength, double xRatio, double zRatio) {
		entityIn.isAirBorne = true;
		float f = MathHelper.sqrt(Math.max(0.1F, xRatio * xRatio + zRatio * zRatio));
		entityIn.motionX /= 2.0;
		entityIn.motionZ /= 2.0;
		entityIn.motionX -= xRatio / (double)f * (double)strength;
		entityIn.motionZ -= zRatio / (double)f * (double)strength;
		//Protection from non-finite XZ
		if(!Double.isFinite(entityIn.motionX)) entityIn.motionX = 0;
		if(!Double.isFinite(entityIn.motionZ)) entityIn.motionZ = 0;
		if(entityIn.onGround) {
			entityIn.motionY /= 2.0;
			entityIn.motionY += strength;
			if(entityIn.motionY > 0.4) {
				entityIn.motionY = 0.4;
			}
		}
		//Protection from non-finite Y
		if(!Double.isFinite(entityIn.motionY)) entityIn.motionY = 0;
		entityIn.velocityChanged = true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return this.getActivator().getTooltipString() + " " + (this.inverted ? I18n.format("socketed.tooltip.effect.knockback_inverted") : I18n.format("socketed.tooltip.effect.knockback"));
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * Strength: Required
	 * Inverted: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.inverted == null) this.inverted = false;
		
		if(super.validate()) {
			if(this.strength == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, strength must be defined");
			else if(this.strength <= 0.0F) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, strength must be greater than 0");
			else return true;
		}
		return false;
	}
}