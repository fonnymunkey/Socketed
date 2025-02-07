package socketed.common.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import socketed.Socketed;
import socketed.common.config.ForgeConfig;
import socketed.common.recipe.SocketAddRecipe;

public class ModRecipes {
    public static void registerRecipes(IForgeRegistry<IRecipe> registry) {
        if(ForgeConfig.ADD_SOCKETS.registerSocketItems) registry.register(new SocketAddRecipe().setRegistryName(new ResourceLocation(Socketed.MODID, "socket_add")));
    }
}