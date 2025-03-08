package socketed.api.socket.gem.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.api.socket.gem.effect.GenericGemEffect;
import socketed.api.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.api.socket.gem.effect.activatable.callback.IEffectCallback;
import socketed.api.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public abstract class GenericActivator {
	
	public static final String TYPE_FIELD = "Activator Type";
	
	@SerializedName(TYPE_FIELD)
	protected final String type = this.getTypeName();
	
	@SerializedName("Condition")
	protected final GenericCondition condition;
	
	protected GenericActivator(@Nullable GenericCondition condition) {
		this.condition = condition;
	}
	
	/**
	 * Tests if this activator should attempt to activate
	 * @return true if there is no additional condition, or if the condition tests true
	 */
	protected boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return this.condition == null || this.condition.testCondition(callback, playerSource, effectTarget);
	}
	
	//TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
	@SideOnly(Side.CLIENT)
	public abstract String getTooltipString();
	
	/**
	 * @return the user readable type name for the subclass used for deserialization
	 */
	public abstract String getTypeName();
	
	/**
	 * Attempts to validate this activator
	 * @return false if any required value is invalid, which should result in discarding this activator
	 * Condition: Optional
	 */
	public boolean validate() {
		if(this.condition != null && !this.condition.validate()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, invalid condition");
		else return true;
		return false;
	}
}