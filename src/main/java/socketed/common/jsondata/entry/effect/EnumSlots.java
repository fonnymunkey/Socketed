package socketed.common.jsondata.entry.effect;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.common.config.ForgeConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EnumSlots {
    @SerializedName("NONE") NONE,
    @SerializedName("ALL") ALL,

    @SerializedName("BODY") BODY,
    @SerializedName("HEAD") HEAD,
    @SerializedName("CHEST") CHEST,
    @SerializedName("LEGS") LEGS,
    @SerializedName("FEET") FEET,

    @SerializedName("HAND") HAND,
    @SerializedName("MAINHAND") MAINHAND,
    @SerializedName("OFFHAND") OFFHAND;
    
    public static List<EntityEquipmentSlot> getSlots(EnumSlots enumSlots){
        switch(enumSlots){
            case NONE : return  Collections.emptyList();
            case ALL : return  Arrays.asList(EntityEquipmentSlot.values());
            case BODY : return  Arrays.asList(EntityEquipmentSlot.HEAD,EntityEquipmentSlot.CHEST,EntityEquipmentSlot.LEGS,EntityEquipmentSlot.FEET);
            case HEAD : return  Collections.singletonList(EntityEquipmentSlot.HEAD);
            case CHEST : return  Collections.singletonList(EntityEquipmentSlot.CHEST);
            case LEGS : return  Collections.singletonList(EntityEquipmentSlot.LEGS);
            case FEET : return  Collections.singletonList(EntityEquipmentSlot.FEET);
            case HAND : return  Arrays.asList(EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);
            case MAINHAND : return  Collections.singletonList(EntityEquipmentSlot.MAINHAND);
            case OFFHAND : return  Collections.singletonList(EntityEquipmentSlot.OFFHAND);
        }
        return Collections.emptyList();
    }

    @SideOnly(Side.CLIENT)
    public static String getSlotTooltip(EnumSlots slots) {
        String tooltip;
        switch(slots){
            case ALL: return "";
            case BODY: tooltip = I18n.format("socketed.tooltip.slotsbody"); break;
            case HAND: tooltip = I18n.format("socketed.tooltip.slotshands"); break;
            case HEAD: tooltip = I18n.format("socketed.tooltip.slotshead"); break;
            case CHEST: tooltip = I18n.format("socketed.tooltip.slotschest"); break;
            case LEGS: tooltip = I18n.format("socketed.tooltip.slotslegs"); break;
            case FEET: tooltip = I18n.format("socketed.tooltip.slotsfeet"); break;
            case MAINHAND: tooltip = I18n.format("socketed.tooltip.slotsmainhand"); break;
            case OFFHAND: tooltip = I18n.format("socketed.tooltip.slotsoffhand"); break;
            default: return "()";
        }
        return " ("+tooltip + ")";
    }
}