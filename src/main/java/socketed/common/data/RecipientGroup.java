package socketed.common.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.data.entry.filter.FilterEntry;

import java.util.Collections;
import java.util.List;

public class RecipientGroup {

    @SerializedName("Recipient Group Name")
    private final String name;
    @SerializedName("Filter Entries")
    private final List<FilterEntry> filterEntries;

    private transient boolean parsed;
    private transient boolean valid;

    public RecipientGroup(String name, List<FilterEntry> filters) {
        this.name = name;
        this.filterEntries = filters;
    }

    public String getName() {
        if(!this.isValid()) return "INVALID";
        return this.name;
    }

    public List<FilterEntry> getFilterEntries() {
        if(this.filterEntries == null || !this.isValid()) return Collections.emptyList();
        return this.filterEntries;
    }

    public boolean matches(ItemStack input) {
        if(!this.isValid()) return false;
        if(input == null || input.isEmpty()) return false;
        for(FilterEntry entry : this.getFilterEntries()) {
            if(entry.matches(input)) return true;
        }
        return false;
    }

    public boolean isValid() {
        if(!this.parsed) this.validate();
        return this.valid;
    }

    private void validate() {
        this.valid = false;
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Recipient Group name, null or empty");
        //else if(this.maxSockets <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Recipient Group, " + this.name + ", max sockets must be greater than 0");
        else {
            int validEntries = 0;
            int totalEntries = 0;
            List<FilterEntry> entries = this.filterEntries;
            if(entries != null) {
                for(FilterEntry entry : entries) {
                    totalEntries++;
                    if(entry.isValid()) validEntries++;
                }
            }
            Socketed.LOGGER.log(Level.INFO,
                    "Recipient Group Validating, Name: " + this.name +
                            //", Max Sockets: " + this.maxSockets +
                            ", Valid Entries: " + validEntries + "/" + totalEntries);
            if(validEntries <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Recipient Group, " + this.name + ", no valid entries");
            this.valid = validEntries > 0;
        }
        this.parsed = true;
    }
}