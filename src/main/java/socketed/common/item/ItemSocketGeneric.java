package socketed.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.api.socket.GenericSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSocketGeneric extends Item {
	
	public ItemSocketGeneric(String name) {
		super();
		this.setRegistryName(Socketed.MODID + ":" + name);
		this.setTranslationKey(Socketed.MODID + "." + name);
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	
	@Nonnull
	public GenericSocket getNewSocket() {
		return new GenericSocket();
	}
	
	public void registerModel() {
		Socketed.proxy.registerItemRenderer(this, 0);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("item.socketed.socket.socket_generic.tooltip"));
	}
}