package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public abstract class EntityPropertyCondition extends GenericCondition {
	public static final String TYPE_NAME = "Entity Property";

	@SerializedName("Check For Player")
	private Boolean checkForPlayer;

	public EntityPropertyCondition(boolean checkForPlayer) {
		super();
		this.checkForPlayer = checkForPlayer;
	}

	protected EntityLivingBase determineAffectedEntity(EntityPlayer player, EntityLivingBase target){
		return this.checkForPlayer ? player : target;
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Check For Player: default false (check target)
	 */
	@Override
	public boolean validate() {
		if(this.checkForPlayer == null) this.checkForPlayer = false;
		return true;
	}
}