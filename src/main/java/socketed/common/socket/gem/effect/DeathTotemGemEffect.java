package socketed.common.socket.gem.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.socket.gem.effect.slot.ISlotType;

public class DeathTotemGemEffect extends GenericGemEffect {
	
	public static final String TYPE_NAME = "Death Totem";
	
	@SerializedName("Save Chance")
	private Float saveChance;
	
	public DeathTotemGemEffect(ISlotType slotType, float saveChance) {
		super(slotType);
		this.saveChance = saveChance;
	}
	
	public float getSaveChance() {
		return this.saveChance;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString(boolean onItem) {
		return I18n.format("socketed.tooltip.effect.deathtotem", String.format("%.1f%%", this.getSaveChance() * 100));
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * SaveChance: Optional, default 0.1
	 */
	@Override
	public boolean validate() {
		if(this.saveChance == null) this.saveChance = 0.1F;
		
		if(super.validate()) {
			if(this.saveChance <= 0.0F) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Effect, save chance must be greater than 0");
			else return true;
		}
		return false;
	}
}