package socketed.common.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.data.entry.ItemEntry;
import socketed.common.data.entry.OreEntry;

import java.util.Collections;
import java.util.List;

public class RecipientGroup {

    @SerializedName("Recipient Group Name")
    private final String name;
    @SerializedName("Max Sockets")
    private final int maxSockets;
    @SerializedName("Item Entries")
    private final List<ItemEntry> itemEntries;
    @SerializedName("Ore Dictionary Entries")
    private final List<OreEntry> oreEntries;

    private transient boolean parsed;
    private transient boolean valid;

    public RecipientGroup(String name, int sockets, List<ItemEntry> items) {
        this(name, sockets, items, Collections.emptyList());
    }

    public RecipientGroup(String name, int sockets, List<ItemEntry> items, List<OreEntry> ores) {
        this.name = name;
        this.maxSockets = sockets;
        this.itemEntries = items;
        this.oreEntries = ores;
    }

    public String getName() {
        if(!this.isValid()) return "INVALID";
        return this.name;
    }

    public int getMaxSockets() {
        if(!this.isValid()) return 0;
        return this.maxSockets;
    }

    public List<ItemEntry> getItemEntries() {
        if(this.itemEntries == null || !this.isValid()) return Collections.emptyList();
        return this.itemEntries;
    }

    public List<OreEntry> getOreEntries() {
        if(this.oreEntries == null || !this.isValid()) return Collections.emptyList();
        return this.oreEntries;
    }

    public boolean matches(ItemStack input) {
        if(!this.isValid()) return false;
        if(input == null || input.isEmpty()) return false;
        for(ItemEntry entry : this.getItemEntries()) {
            if(entry.matches(input)) return true;
        }
        for(OreEntry entry : this.getOreEntries()) {
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
        else if(this.maxSockets <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Recipient Group, " + this.name + ", max sockets must be greater than 0");
        else {
            int validEntries = 0;
            int totalEntries = 0;
            List<ItemEntry> entries1 = this.itemEntries;
            if(entries1 != null) {
                for(ItemEntry entry : entries1) {
                    totalEntries++;
                    if(entry.isValid()) validEntries++;
                }
            }
            List<OreEntry> entries2 = this.oreEntries;
            if(entries2 != null) {
                for(OreEntry entry : entries2) {
                    totalEntries++;
                    if(entry.isValid()) validEntries++;
                }
            }
            Socketed.LOGGER.log(Level.INFO,
                    "Recipient Group Validating, Name: " + this.name +
                            ", Max Sockets: " + this.maxSockets +
                            ", Valid Entries: " + validEntries + "/" + totalEntries);
            if(validEntries <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Recipient Group, " + this.name + ", no valid entries");
            this.valid = validEntries > 0;
        }
        this.parsed = true;
    }
}