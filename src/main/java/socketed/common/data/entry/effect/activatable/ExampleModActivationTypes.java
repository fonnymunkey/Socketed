/*package socketed.common.data.entry.effect.activatable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.EntityPlayer;

public enum ExampleModActivationTypes implements IActivationType {
    //Serialized format must be "MODID:NAME"
    @SerializedName("<ExampleMod.MODID>" + ":" + "EXAMPLE_NAME")
    EXAMPLE_NAME {
        //Do periodic effect with different requirements, timing, etc
        @Override
        public void triggerOnSecondEffect(ActivatableGemEffect entry, EntityPlayer player) { }
        //Lang key for tooltip translating (return empty to skip tooltip)
        @Override
        public String getKey() { return "example_name"; }
    };
    //Required for reflection
    public static IActivationType fromValue(String value) { return valueOf(value); }
}
 */