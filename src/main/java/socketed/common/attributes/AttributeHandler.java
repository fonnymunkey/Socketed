package socketed.common.attributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.village.MerchantTradeOffersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class AttributeHandler {
    @SubscribeEvent
    public static void onEntityConstruction(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityPlayer) {
            ((EntityPlayer)event.getEntity()).getAttributeMap().registerAttribute(SocketedAttributes.DURABILITY);
            ((EntityPlayer)event.getEntity()).getAttributeMap().registerAttribute(SocketedAttributes.XP);
            ((EntityPlayer)event.getEntity()).getAttributeMap().registerAttribute(SocketedAttributes.TRADECOST);
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event){
        EntityPlayer player = event.getAttackingPlayer();
        if(player == null) return;
        EntityLivingBase victim = event.getEntityLiving();
        if(victim == null) return;
        double amount = event.getDroppedExperience();
        if(amount <= 0) return;

        IAttributeInstance xpAttribute = player.getEntityAttribute(SocketedAttributes.XP);
        xpAttribute.setBaseValue(amount);
        event.setDroppedExperience((int) xpAttribute.getAttributeValue());
        xpAttribute.setBaseValue(0);
    }

    @SubscribeEvent
    public static void onTradeOffers(MerchantTradeOffersEvent event){
        EntityPlayer player = event.getPlayer();
        if(player == null) return;
        MerchantRecipeList trades = event.getList();
        if (trades == null) return;
        //TODO: this doesnt work cause it fires multiple times for whatever reason
        IAttributeInstance tradeAttribute = player.getEntityAttribute(SocketedAttributes.TRADECOST);
        for (MerchantRecipe recipe : event.getList()){
            if(recipe.getItemToBuy().getCount() > 1)
                reduceCost(recipe.getItemToBuy(), tradeAttribute);
            if(recipe.hasSecondItemToBuy() && recipe.getSecondItemToBuy().getCount() > 1)
                reduceCost(recipe.getItemToBuy(), tradeAttribute);
        }
    }

    private static void reduceCost(ItemStack itemToBuy, IAttributeInstance tradeAttribute){
        tradeAttribute.setBaseValue(itemToBuy.getCount());
        itemToBuy.setCount((int) Math.round(tradeAttribute.getAttributeValue()));
        tradeAttribute.setBaseValue(0);
    }
}
