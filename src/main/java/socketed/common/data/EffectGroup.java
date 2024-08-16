package socketed.common.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.CustomConfig;
import socketed.common.data.entry.effect.EffectEntry;
import socketed.common.data.entry.filter.FilterEntry;

import java.util.Collections;
import java.util.List;

public class EffectGroup {

    @SerializedName("Effect Group Name")
    private final String name;
    @SerializedName("Display Name")
    private final String displayName;
    @SerializedName("Text Color")
    private final TextFormatting color;
    @SerializedName("Recipient Group Names")
    private final List<String> recipientEntries;
    @SerializedName("Effect Entries")
    private final List<EffectEntry> effectEntries;
    @SerializedName("Filter Entries")
    private final List<FilterEntry> filterEntries;

    private transient boolean parsed;
    private transient boolean valid;

    public EffectGroup(String name, String display, TextFormatting clr, List<String> groups, List<EffectEntry> effects, List<FilterEntry> filters) {
        this.name = name;
        this.displayName = display;
        this.color = clr;
        this.recipientEntries = groups;
        this.effectEntries = effects;
        this.filterEntries = filters;
    }

    public String getName() {
        if(!this.isValid()) return "INVALID";
        return this.name;
    }

    public String getDisplayName() {
        if(!this.isValid()) return "INVALID";
        return this.displayName;
    }

    public TextFormatting getColor() {
        if(!this.isValid()) return TextFormatting.RESET;
        return this.color;
    }

    public List<String> getRecipientEntries() {
        if(this.recipientEntries == null || !this.isValid()) return Collections.emptyList();
        return this.recipientEntries;
    }

    public List<EffectEntry> getEffectEntries() {
        if(this.effectEntries == null || !this.isValid()) return Collections.emptyList();
        return this.effectEntries;
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
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group name, null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", null or empty display name");
        else if(this.color == null) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", invalid color");
        else {
            int validRecipients = 0;
            int totalRecipients = 0;
            List<String> recipients = this.recipientEntries;
            if(recipients != null) {
                for(String recipient : recipients) {
                    totalRecipients++;
                    if(CustomConfig.getRecipientData().containsKey(recipient)) validRecipients++;
                }
            }

            int validEffects = 0;
            int totalEffects = 0;
            List<EffectEntry> effects = this.effectEntries;
            if(effects != null) {
                for(EffectEntry effect : effects) {
                    totalEffects++;
                    if(effect.isValid()) validEffects++;
                }
            }

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
                    "Effect Group Validating, Name: " + this.name +
                            ", Display Name: " + this.displayName +
                            ", Color: " + this.color.name() +
                            ", Valid Recipients: " + validRecipients + "/" + totalRecipients +
                            ", Valid Effects: " + validEffects + "/" + totalEffects +
                            ", Valid Entries: " + validEntries + "/" + totalEntries);
            if(validRecipients <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", no valid recipients");
            if(validEffects <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", no valid effects");
            if(validEntries <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", no valid entries");
            this.valid = validEntries > 0 && validEffects > 0 && validRecipients > 0;
        }
        this.parsed = true;
    }
}