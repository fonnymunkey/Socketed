package socketed.common.socket.gem.filter;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import socketed.Socketed;

public class ItemFilter extends GenericFilter {

    public static final String TYPE_NAME = "Item";

    @SerializedName("Item Name")
    private final String itemName;

    @SerializedName("Item Metadata")
    private Integer metadata;

    @SerializedName("Strict Metadata")
    private Boolean strict;

    private transient Item item;

    public ItemFilter(String name, int meta, boolean strict) {
        super();
        this.itemName = name;
        this.metadata = meta;
        this.strict = strict;
    }

    @Override
    public boolean matches(ItemStack input) {
        if(input == null || input.isEmpty()) return false;
        return this.item == input.getItem() && (!this.strict || this.metadata == input.getMetadata());
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    /**
     * ItemName: Required
     * Metadata: Optional, default 0
     * Strict: Optional, default false
     */
    @Override
    public boolean validate() {
        if(this.metadata == null) this.metadata = 0;
        if(this.strict == null) this.strict = false;
        
        if(this.itemName == null || this.itemName.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter, item name null or empty");
        else if(this.metadata < 0) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter, metadata must not be less than 0");
        else {
            this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.itemName));
            if(this.item == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Filter, " + this.itemName + ", item does not exist");
            else return true;
        }
        return false;
    }
}