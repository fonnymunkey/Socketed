package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class BiomeCondition extends GenericCondition {
	public static final String TYPE_NAME = "Biome";

	@SerializedName("Biome Id")
	protected final String biomeId;

	private transient Biome biome;

	public BiomeCondition(String biomeId) {
		super();
		this.biomeId = biomeId;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return effectTarget.world.getBiome(effectTarget.getPosition()).equals(this.biome);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(this.biomeId == null || this.biomeId.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, biome id null or empty");
		else {
			this.biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(this.biomeId));
			if(this.biome == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, biome does not exist");
			else return true;
		}
		return false;
	}
}