package socketed.common.jsondata;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.filter.FilterEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GemType {

    //Serialized via file name
    protected transient String name;
    @SerializedName("Gem Tier")
    private final Integer tier;
    @SerializedName("Display Name")
    private final String displayName;
    @SerializedName("Text Color")
    private final TextFormatting color;
    @SerializedName("Effect Entries")
    private final List<GenericGemEffect> effects;
    @SerializedName("Filter Entries")
    private final List<FilterEntry> filterEntries;

    protected transient boolean parsed;
    protected transient boolean valid;

    public GemType(String display, TextFormatting clr, List<GenericGemEffect> effects, List<FilterEntry> filters, Integer tier) {
        this.displayName = display;
        this.color = clr;
        this.effects = effects;
        this.filterEntries = filters;
        this.tier = tier;
    }

    public void setName(String name){
        if(this.name == null)
            this.name = name;
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

    protected void validate() {
        this.valid = false;
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type name, null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", null or empty display name");
        else if(this.color == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid color");
        else if(this.tier == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid tier");
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

            Socketed.LOGGER.info("Effect Group Validating, Name: " + this.name +
                            ", Display Name: " + this.displayName +
                            ", Color: " + this.color.name() +
                            ", Valid Effects: " + validEffects + "/" + totalEffects +
                            ", Valid Entries: " + validEntries + "/" + totalEntries +
                            ", Tier: " + this.tier);
            if(validEffects <= 0) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", no valid effects");
            if(validEntries <= 0) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", no valid entries");
            this.valid = validEntries > 0 && validEffects > 0;
        }
        this.parsed = true;
    }

    public static GemType getGemTypeFromItemStack(@Nullable ItemStack itemStack){
        for (GemType gemType : JsonConfig.getGemData().values())
            if (gemType.matches(itemStack))
                return gemType;
        return null;
    }

    public static GemType getGemTypeFromName(@Nullable String gemTypeName){
        if(gemTypeName==null)
            return null;
        else
            return JsonConfig.getGemData().get(gemTypeName);
    }

    public int getTier() {
        return this.tier;
    }
}