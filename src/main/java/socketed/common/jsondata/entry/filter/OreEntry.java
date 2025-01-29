package socketed.common.jsondata.entry.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

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
        this.name = name;
        this.strict = strict;
        this.type = TYPE_NAME;
    }

    public String getName() {
        if(!this.isValid()) return "INVALID";
        return this.name.trim();
    }

    @Override
    public boolean matches(ItemStack input) {
        if(!this.isValid()) return false;
        if(input == null || input.isEmpty()) return false;
        NonNullList<ItemStack> list = OreDictionary.getOres(this.name.trim(), false);
        for(ItemStack stack : list) {
            if(stack.getItem().equals(input.getItem()) && (!this.strict || stack.getMetadata() == input.getMetadata())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void validate() {
        this.valid = false;
        if(this.name == null || this.name.trim().isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Ore Dictionary entry, name null or empty");
        else if(!OreDictionary.doesOreNameExist(this.name.trim())) Socketed.LOGGER.log(Level.WARN, "Invalid Ore Dictionary entry, " + this.name.trim() + ", does not exist");
        else if(OreDictionary.getOres(this.name.trim(), false).isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Ore Dictionary entry, " + this.name.trim() + ", exists but is empty");
        else this.valid = true;
        this.parsed = true;
    }
}