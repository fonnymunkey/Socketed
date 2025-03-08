package socketed.common.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class SocketedAttributes {
    public static final IAttribute DURABILITY = (new RangedAttribute(null, "socketed.durability", 1.0D, 0.0D, 1000.0)).setDescription("Durability").setShouldWatch(true);
    public static final IAttribute XP = (new RangedAttribute(null, "socketed.experience", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE)).setDescription("Experience").setShouldWatch(true);
    public static final IAttribute TRADECOST = (new RangedAttribute(null, "socketed.tradecost", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE)).setDescription("Trade Costs").setShouldWatch(true);
}