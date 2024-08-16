package socketed.client.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import socketed.Socketed;
import socketed.common.proxy.IProxy;

public class ClientProxy implements IProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Socketed.MODID + ":" + name, "inventory"));
    }

}