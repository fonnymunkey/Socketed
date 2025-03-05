package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import socketed.Socketed;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class BiomeTypeCondition extends GenericCondition {
	public static final String TYPE_NAME = "Biome Type";

	@SerializedName("Biome Type")
	protected final String biomeTypeName;

	private transient BiomeDictionary.Type biomeType;

	public BiomeTypeCondition(String biomeTypeName) {
		super();
		this.biomeTypeName = biomeTypeName;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		Biome biome = effectTarget.world.getBiome(effectTarget.getPosition());
		return BiomeDictionary.hasType(biome, this.biomeType);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(this.biomeTypeName == null || this.biomeTypeName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, biome type name null or empty");
		else {
			if(BiomeDictionary.Type.getAll().stream().noneMatch(type -> type.getName().equals(this.biomeTypeName)))
				Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, biome type does not exist");
			else {
				this.biomeType = BiomeDictionary.Type.getType(this.biomeTypeName);
				return true;
			}
		}
		return false;
	}
}