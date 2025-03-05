package socketed.common.socket.gem.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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

public class ExplosionGemEffect extends ActivatableGemEffect {

	public static final String TYPE_NAME = "Explosion";

	@SerializedName("Strength")
	private final Float strength;
	@SerializedName("Damages Terrain")
	private Boolean damagesTerrain;

	public ExplosionGemEffect(ISlotType slotType, GenericActivator activator, List<GenericTarget> targets, float strength, boolean damagesTerrain) {
		super(slotType, activator, targets);
		this.strength = strength;
		this.damagesTerrain = damagesTerrain;
	}
	
	@Override
	public void performEffect(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(playerSource != null && effectTarget != null && !playerSource.world.isRemote) {
			BlockPos pos = effectTarget.getPosition();
			effectTarget.world.createExplosion(effectTarget,pos.getX(), pos.getY(), pos.getZ(), this.strength, this.damagesTerrain);
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

	/**
	 * Strength: Required, positive float
	 * Damages Terrain: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.damagesTerrain == null) this.damagesTerrain = false;

			if(this.strength == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, strength must be defined");
			else if(this.strength <= 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, strength must be positive");
			else return true;
		}
		return false;
	}
}