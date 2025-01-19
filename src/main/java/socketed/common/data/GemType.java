package socketed.common.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.CustomConfig;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.data.entry.filter.FilterEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GemType {

    @SerializedName("Effect Group Name")
    private final String name;
    @SerializedName("Display Name")
    private final String displayName;
    @SerializedName("Text Color")
    private final TextFormatting color;
    @SerializedName("Effect Entries")
    private final List<GenericGemEffect> effects;
    @SerializedName("Filter Entries")
    private final List<FilterEntry> filterEntries;

    private transient boolean parsed;
    private transient boolean valid;

    public GemType(String name, String display, TextFormatting clr, List<GenericGemEffect> effects, List<FilterEntry> filters) {
        this.name = name;
        this.displayName = display;
        this.color = clr;
        this.effects = effects;
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

    public List<GenericGemEffect> getEffects() {
        if(this.effects == null || !this.isValid()) return Collections.emptyList();
        return this.effects;
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
            int validEffects = 0;
            int totalEffects = 0;
            List<GenericGemEffect> effects = this.effects;
            if(effects != null) {
                for(GenericGemEffect effect : effects) {
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
                            ", Valid Effects: " + validEffects + "/" + totalEffects +
                            ", Valid Entries: " + validEntries + "/" + totalEntries);
            if(validEffects <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", no valid effects");
            if(validEntries <= 0) Socketed.LOGGER.log(Level.WARN, "Invalid Effect Group, " + this.name + ", no valid entries");
            this.valid = validEntries > 0 && validEffects > 0;
        }
        this.parsed = true;
    }

    public static GemType getGemTypeFromItemStack(@Nullable ItemStack itemStack){
        for (GemType gemType : CustomConfig.getGemData().values())
            if (gemType.matches(itemStack))
                return gemType;
        return null;
    }

    public static GemType getGemTypeFromName(@Nullable String gemTypeName){
        if(gemTypeName==null)
            return null;
        else
            return CustomConfig.getGemData().get(gemTypeName);
    }
}