package socketed.common.jsondata.entry.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import socketed.Socketed;

import javax.annotation.Nonnull;

public class ItemEntry extends FilterEntry {

    public static final String TYPE_NAME = "Item";

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
        super();
        this.name = name;
        this.metadata = meta;
        this.strict = strict;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public boolean matches(ItemStack input) {
        if(input == null || input.isEmpty()) return false;
        return this.stack.getItem().equals(input.getItem()) && (!this.strict || this.stack.getMetadata() == input.getMetadata());
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public boolean validate() {
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter entry, name null or empty");
        else {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.name));
            if(item == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter entry, " + this.name + ", item does not exist");
            else {
                this.stack = new ItemStack(item, 1, this.strict ? this.metadata : OreDictionary.WILDCARD_VALUE);
                return true;
            }
        }
        return false;
    }
}