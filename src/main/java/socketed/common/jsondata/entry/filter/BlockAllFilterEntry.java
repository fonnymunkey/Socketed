package socketed.common.jsondata.entry.filter;

import net.minecraft.item.ItemStack;

public class BlockAllFilterEntry extends FilterEntry {

    public static final String FILTER_NAME = "Block All";

    public BlockAllFilterEntry() {
        this.type = FILTER_NAME;
    }

    @Override
    public boolean matches(ItemStack input) {
        return false;
    }

    @Override
    protected void validate() {
        this.valid = true;
        this.parsed = true;
    }
}