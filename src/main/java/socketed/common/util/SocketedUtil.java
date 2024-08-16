package socketed.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import socketed.common.config.CustomConfig;
import socketed.common.data.entry.effect.EffectEntry;
import socketed.common.data.entry.effect.activatable.IActivationType;
import socketed.common.data.entry.filter.FilterEntry;

public abstract class SocketedUtil {

    public static boolean isSocketed(ItemStack stack) {
        return !getSocketEffectName(stack).isEmpty();
    }

    public static void clearSocket(ItemStack stack) {
        if(stack.isEmpty() || stack.getTagCompound() == null) return;
        setSocketEffectName(stack, "");
    }

    public static String getSocketEffectName(ItemStack stack) {
        if(stack.isEmpty() || stack.getTagCompound() == null) return "";
        if(!stack.getTagCompound().hasKey("Socketed")) return "";
        return stack.getTagCompound().getCompoundTag("Socketed").getString("SocketEffect");
    }

    public static void setSocketEffectName(ItemStack stack, String name) {
        if(stack.isEmpty()) return;
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("Socketed")) stack.getTagCompound().setTag("Socketed", new NBTTagCompound());
        NBTTagCompound socketTag = stack.getTagCompound().getCompoundTag("Socketed");
        socketTag.setString("SocketEffect", name);
    }

    public static void registerFilterDeserializer(String ENTRY_FILTER, Class<? extends FilterEntry> typeClass) {
        CustomConfig.filterDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerEffectDeserializer(String ENTRY_FILTER, Class<? extends EffectEntry> typeClass) {
        CustomConfig.effectDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerActivationTypeDeserializer(String MODID, Class<? extends IActivationType> typeClass) {
        CustomConfig.activationDeserializerMap.put(MODID, typeClass);
    }
}