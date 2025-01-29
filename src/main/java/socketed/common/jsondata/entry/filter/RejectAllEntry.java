package socketed.common.jsondata.entry.filter;

import net.minecraft.item.ItemStack;

public class RejectAllEntry extends FilterEntry {

    public static final String TYPE_NAME = "Reject All";

    public RejectAllEntry() {
        this.type = TYPE_NAME;
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