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

import javax.annotation.Nullable;
import java.util.List;

public class ItemSocketTool extends Item {

    private final String name;

    public ItemSocketTool(String name) {
        super();
        this.name = name;
        this.setRegistryName(name);
        this.setTranslationKey(Socketed.MODID + "." + name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    public void registerModel() {
        Socketed.proxy.registerItemRenderer(this, 0, this.name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.socketed.socket_tool.tooltip"));
    }
}