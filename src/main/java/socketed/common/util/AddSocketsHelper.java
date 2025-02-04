package socketed.common.util;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.config.ForgeConfig;
import socketed.common.socket.TieredSocket;

import java.util.Random;

public class AddSocketsHelper {
    public static final Random RAND = new Random();

    public static void addSockets(ItemStack stack, EnumItemCreationContext context) {
        ICapabilitySocketable itemSockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(itemSockets == null) return;
        //Only add sockets to items that don't have sockets yet
        //Adding alreadyBeenChecked tag *shouldn't* be needed currently, at least not with the given contexts yet
        if(itemSockets.getSocketCount() != 0) return;

        // default behavior
        // - creation context (merchant, loot, drops) gives base chance and max sockets that can be rolled
        // - distribution between tiers is fixed in config (for example 1 5 2 1 for tiers 0-3)
        // - item type gives amount of rolls
        // - and item material enchantability gives chance for the rolls
        // - example helmet: 3 rolls, diamond: each 50% chance

        if(RAND.nextFloat() >= context.getChance()) return;

        float rollChance = getRollChance(stack);
        int rollAmount = getRollAmount(stack);

        addSocketsRandomly(stack, context.getMaxSockets(), rollAmount, rollChance);
    }

    public static void addSocketsRandomly(ItemStack socketable, int maxSockets, int rollAmount, float rollChance){
        ICapabilitySocketable itemSockets = socketable.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(itemSockets == null) return;

        for(int i = 0; i < rollAmount; i++){
            if(itemSockets.getSocketCount() >= maxSockets) break;
            if(RAND.nextFloat() < rollChance)
                itemSockets.addSocket(new TieredSocket(getRandomTierForSocket()));
        }
    }

    private static int getRollAmount(ItemStack stack) {
        String socketableType = ForgeConfig.SOCKETABLES.getSocketableType(stack);
        return ForgeConfig.ADD_SOCKETS.getSocketRollCount(socketableType);
    }

    private static float getRollChance(ItemStack stack) {
        Item item = stack.getItem();

        //Override by item id
        ResourceLocation itemReg = item.getRegistryName();
        if(itemReg != null){
            float chance = ForgeConfig.ADD_SOCKETS.getSocketRollChance(itemReg.toString());
            if(chance >= 0) return chance;
        }

        //Vanilla enchanting always uses enchantability divided by 4, so we do the same.
        //Items without a material will have tier 0 and use base chance only
        int enchantabilityTier = item.getItemEnchantability(stack) / 4;

        return ForgeConfig.ADD_SOCKETS.socketRollBaseChance + enchantabilityTier * ForgeConfig.ADD_SOCKETS.socketRollChancePerEnchantabilityTier;
    }

    private static int getRandomTierForSocket() {
        //Weighted roll
        int totalWeights = 0;
        for(int weight : ForgeConfig.ADD_SOCKETS.socketTierWeights)
            totalWeights += weight;

        int randWeight = RAND.nextInt(totalWeights);
        for(int i = 0; i < ForgeConfig.ADD_SOCKETS.socketTierWeights.length; i++){
            randWeight -= ForgeConfig.ADD_SOCKETS.socketTierWeights[i];
            if(randWeight < 0)
                return i;
        }
        return 0;
    }

    public enum EnumItemCreationContext implements IAddSocketsCreationContext {
        MERCHANT{
            @Override public float getChance() {
                return ForgeConfig.ADD_SOCKETS.chanceOnMerchant;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.ADD_SOCKETS.maxSocketsOnMerchant;
            }
        },
        LOOT {
            @Override public float getChance() {
                return ForgeConfig.ADD_SOCKETS.chanceOnLootGen;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.ADD_SOCKETS.maxSocketsOnLootGen;
            }
        },
        MOB_DROP {
            @Override public float getChance() {
                return ForgeConfig.ADD_SOCKETS.chanceOnMobDrop;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.ADD_SOCKETS.maxSocketsOnMobDrop;
            }
        }
    }
}
