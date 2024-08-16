package socketed.common.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import socketed.Socketed;
import socketed.common.recipe.SocketAddRecipe;
import socketed.common.recipe.SocketRemoveRecipe;

public class ModRecipes {
    public static void registerRecipes(IForgeRegistry<IRecipe> registry) {
        registry.register(new SocketAddRecipe().setRegistryName(new ResourceLocation(Socketed.MODID, "socket_add")));
        registry.register(new SocketRemoveRecipe().setRegistryName(new ResourceLocation(Socketed.MODID, "socket_remove")));
    }
}