package socketed.common.jsondata.entry.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import socketed.Socketed;

public class ItemEntry extends FilterEntry {

    public static final String FILTER_NAME = "Item";

    @SerializedName("Item Name")
    private final String name;

    @SerializedName("Item Metadata")
    private final int metadata;

    @SerializedName("Strict Metadata")
    private final boolean strict;

    private transient ItemStack stack;

    public ItemEntry(String name) {
        this(name, OreDictionary.WILDCARD_VALUE, false);
    }

    public ItemEntry(String name, int meta, boolean strict) {
        this.name = name;
        this.metadata = meta;
        this.strict = strict;
        this.type = FILTER_NAME;
    }

    public ItemStack getItemStack() {
        if(!this.isValid()) return ItemStack.EMPTY;
        return this.stack;
    }

    @Override
    public boolean matches(ItemStack input) {
        if(!this.isValid()) return false;
        if(input == null || input.isEmpty()) return false;
        return this.stack.getItem().equals(input.getItem()) && (!this.strict || this.stack.getMetadata() == input.getMetadata());
    }

    @Override
    protected void validate() {
        this.valid = false;
        if(this.name == null || this.name.trim().isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Item entry, name null or empty");
        else {
            String[] in = this.name.split(":");
            ResourceLocation loc = null;
            if(in.length == 1) loc = new ResourceLocation("minecraft", in[0].trim());
            else if(in.length == 2) loc = new ResourceLocation(in[0].trim(), in[1].trim());
            if(loc != null) {
                Item item = ForgeRegistries.ITEMS.getValue(loc);
                if(item != null) {
                    this.stack = new ItemStack(item, 1, this.strict ? this.metadata : OreDictionary.WILDCARD_VALUE);
                    this.valid = true;
                }
                else Socketed.LOGGER.log(Level.WARN, "Invalid Item entry, " + this.name + ", item does not exist: " + loc);
            }
            else Socketed.LOGGER.log(Level.WARN, "Invalid Item entry, " + this.name + ", item name is not valid");
        }
        this.parsed = true;
    }
}