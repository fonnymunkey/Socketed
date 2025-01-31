package socketed.common.jsondata.entry.filter;

import net.minecraft.item.ItemStack;

public class RejectAllEntry extends FilterEntry {

    public static final String TYPE_NAME = "Reject All";

    public RejectAllEntry() {
        super();
    }

    @Override
    public boolean matches(ItemStack input) {
        return false;
    }
    
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public boolean validate() {
        return true;
    }
}