package socketed.common.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import socketed.common.attributes.Attributes;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean isItemStackDamageable();
    @Shadow public abstract int getMaxDamage();

    @ModifyVariable(
            method = "attemptDamageItem",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private int socketed_itemStack_changeDurabilityDamage(int amount, @Local(argsOnly = true) Random rand, @Local(argsOnly = true) EntityPlayerMP player) {
        if (player == null) return amount;
        if (!this.isItemStackDamageable()) return amount;
        IAttributeInstance instance = player.getAttributeMap().getAttributeInstance(Attributes.DURABILITY);
        if (instance == null) return amount;
        float durabilityAttribute = (float) instance.getAttributeValue();

        //Avoid math problems. With -100% durability and lower the item just breaks gg
        if (durabilityAttribute <= 0) return this.getMaxDamage();

        //With low integer values of amount (amount is always 1 for tools/weapons), this calc can fail bc of truncation
        float theoreticalNewAmount = amount / durabilityAttribute;
        int actualNewAmount = (int) theoreticalNewAmount;
        //So we just do some rng magic to fix it (same method as furnace xp calculation, way faster than what Unbreaking does)
        // (this is bernoulli, unbreaking does an approximately normal distribution)
        if (rand.nextFloat() < theoreticalNewAmount - actualNewAmount)
            actualNewAmount++;
        //Note: different to unbreaking we don't differentiate between armor and tools/weapons
        return Math.max(0, actualNewAmount);
    }
}
