package socketed.common.util;

import net.minecraft.item.ItemStack;
import socketed.common.capabilities.CapabilitySocketableHandler;
import socketed.common.capabilities.ICapabilitySocketable;
import socketed.common.config.ForgeConfig;
import socketed.common.socket.TieredSocket;

import java.util.Random;

public class AddSocketsOnGeneration {
    private static final Random RAND = new Random();

    public static void addSockets(ItemStack stack, EnumItemCreationContext context) {
        ICapabilitySocketable itemSockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(itemSockets == null) return;
        //Only add sockets to items that don't have sockets yet
        //Adding alreadyBeenChecked tag *shouldn't* be needed currently, at least not with the given contexts yet
        if(itemSockets.getSocketCount() != 0) return;

        //IDEA
        // - creation context (merchant, loot, drops) gives base chance and max sockets that can be rolled
        // - distribution between tiers is fixed in config (for example 10 5 2 1 for tiers 0-3)
        // - item type gives amount of rolls
        // - and item material gives chance for the rolls
        // - example helmet: 3 rolls, diamond: each 50% chance

        float chance = context.getChance();
        if(RAND.nextFloat() >= chance) return;

        int maxSockets = context.getMaxSockets();

        float rollsChance = getRollChance(stack);
        for(int i = 0; i < getRollAmount(stack); i++){
            if(itemSockets.getSocketCount() >= maxSockets) break;
            if(RAND.nextFloat() < rollsChance)
                itemSockets.addSocket(new TieredSocket(getRandomTierForSocket()));
        }
    }

    private static int getRollAmount(ItemStack stack) {
        //Not fully sure yet how to do this
        return 10;
    }

    private static float getRollChance(ItemStack stack) {
        //Not fully sure yet how to do this
        return 1F;
    }

    private static int getRandomTierForSocket() {
        //Weighted roll
        int totalWeights = 0;
        for(int weight : ForgeConfig.COMMON.socketTierWeights)
            totalWeights += weight;

        int randWeight = RAND.nextInt(totalWeights);
        for(int i = 0; i < ForgeConfig.COMMON.socketTierWeights.length; i++){
            randWeight -= ForgeConfig.COMMON.socketTierWeights[i];
            if(randWeight < 0)
                return i;
        }
        return 0;
    }

    public enum EnumItemCreationContext implements IGivesSockets {
        MERCHANT{
            @Override public float getChance() {
                return ForgeConfig.COMMON.chanceOnMerchant;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.COMMON.maxSocketsOnMerchant;
            }
        },
        LOOT {
            @Override public float getChance() {
                return ForgeConfig.COMMON.chanceOnLootGen;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.COMMON.maxSocketsOnLootGen;
            }
        },
        MOB_DROP {
            @Override public float getChance() {
                return ForgeConfig.COMMON.chanceOnMobDrop;
            }
            @Override public int getMaxSockets() {
                return ForgeConfig.COMMON.maxSocketsOnMobDrop;
            }
        };
    }
}
