package socketed.common.jsondata.entry.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import socketed.Socketed;

import javax.annotation.Nonnull;

public class OreEntry extends FilterEntry {

    public static final String TYPE_NAME = "Ore Dictionary";

    @SerializedName("Ore Dictionary Name")
    private final String name;

    @SerializedName("Strict Metadata")
    private final boolean strict;

    public OreEntry(String name) {
        this(name, false);
    }

    public OreEntry(String name, boolean strict) {
        super();
        this.name = name;
        this.strict = strict;
    }
    
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override
    public boolean matches(ItemStack input) {
        if(input == null || input.isEmpty()) return false;
        NonNullList<ItemStack> list = OreDictionary.getOres(this.name, false);
        for(ItemStack stack : list) {
            if(stack.getItem().equals(input.getItem()) && (!this.strict || stack.getMetadata() == input.getMetadata())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public boolean validate() {
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter entry, name null or empty");
        else if(!OreDictionary.doesOreNameExist(this.name)) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter entry, " + this.name + ", dictionary does not exist");
        else if(OreDictionary.getOres(this.name, false).isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter entry, " + this.name.trim() + ", dictionary exists but is empty");
        else return true;
        return false;
    }
}