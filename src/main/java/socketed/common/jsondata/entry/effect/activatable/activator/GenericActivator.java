package socketed.common.jsondata.entry.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GenericActivator {
	
	public static final String TYPE_FIELD = "Activator Type";
	
	@SerializedName(TYPE_FIELD)
	protected final String type = this.getTypeName();
	
	@SideOnly(Side.CLIENT)
	public abstract String getTooltipString();
	
	/**
	 * @return the user readable type name for the subclass used for deserialization
	 */
	public abstract String getTypeName();
	
	/**
	 * Attempts to validate this activator type and cache any parsed values
	 * @return false if any required value is invalid, which should result in discarding this activator
	 */
	public abstract boolean validate();
}