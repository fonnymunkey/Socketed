package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;

public class IsSpecificEntityCondition extends GenericCondition {

	public static final String TYPE_NAME = "Entity";
	@SerializedName("Entity")
	private final String entityName;

	public IsSpecificEntityCondition(String entityName) {
		super();
		this.entityName = entityName;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		ResourceLocation loc = EntityList.getKey(effectTarget);
		return loc != null && loc.toString().equals(entityName);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public boolean validate() {
		if(entityName == null || entityName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define entity mob id");
		else return true;
		return false;
	}
}