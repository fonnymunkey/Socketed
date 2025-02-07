package socketed.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.socket.GenericSocket;
import socketed.common.socket.TieredSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSocketTier extends ItemSocketGeneric {
	
	protected final int tier;
	
	public ItemSocketTier(String name, int tier) {
		super(name);
		this.tier = tier;
	}
	
	@Override
	@Nonnull
	public GenericSocket getNewSocket() {
		return new TieredSocket(this.tier);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		switch(this.tier) {
			case 0: tooltip.add(I18n.format("item.socketed.socket.socket_tier_0.tooltip")); break;
			case 1: tooltip.add(I18n.format("item.socketed.socket.socket_tier_1.tooltip")); break;
			case 2: tooltip.add(I18n.format("item.socketed.socket.socket_tier_2.tooltip")); break;
			case 3: tooltip.add(I18n.format("item.socketed.socket.socket_tier_3.tooltip")); break;
		}
	}
}