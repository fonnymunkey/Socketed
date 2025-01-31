package socketed.common.jsondata;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.filter.FilterEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GemType {
    
    /**
     * Set prior to validation based on filename
     */
    protected transient String name = null;
    
    @SerializedName("Display Name")
    private final String displayName;
    
    @SerializedName("Gem Tier")
    private final Integer tier;
    
    @SerializedName("Text Color")
    private final TextFormatting color;
    
    @SerializedName("Effect Entries")
    private List<GenericGemEffect> effects;
    
    @SerializedName("Filter Entries")
    private List<FilterEntry> filterEntries;

    public GemType(String displayName, Integer tier, TextFormatting color, List<GenericGemEffect> effects, List<FilterEntry> filterEntries) {
        this.displayName = displayName;
        this.tier = tier;
        this.color = color;
        this.effects = effects;
        this.filterEntries = filterEntries;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Nonnull
    public String getName() {
        return this.name;
    }
    
    @Nonnull
    public String getDisplayName() {
        return this.displayName;
    }
    
    public int getTier() {
        return this.tier;
    }
    
    @Nonnull
    public TextFormatting getColor() {
        return this.color;
    }
    
    @Nonnull
    public List<GenericGemEffect> getEffects() {
        return this.effects;
    }
    
    @Nonnull
    public List<FilterEntry> getFilterEntries() {
        return this.filterEntries;
    }
    
    //Instantiation shouldn't affect validity
    public boolean hasEffectsForStack(ItemStack stack) {
        if(stack.isEmpty()) return false;
        for(GenericGemEffect effect : this.effects) {
            if(effect.getSlotType().isStackValid(stack)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean matches(ItemStack input) {
        if(input == null || input.isEmpty()) return false;
        for(FilterEntry entry : this.getFilterEntries()) {
            if(entry.matches(input)) return true;
        }
        return false;
    }
    
    public boolean validate() {
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, name null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", display name null or empty");
        else if(this.tier == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid tier");
        else if(this.color == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid color");
        else if(this.effects == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid effect entry list");
        else if(this.filterEntries == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid filter entry list");
        else {
            List<GenericGemEffect> validEffects = new ArrayList<>();
            for(GenericGemEffect effect : this.effects) {
                if(effect.validate()) validEffects.add(effect);
            }
            int totalEffectsSize = this.effects.size();
            this.effects = validEffects;
            int validEffectsSize = this.effects.size();
            
            List<FilterEntry> validFilters = new ArrayList<>();
            for(FilterEntry filter : this.filterEntries) {
                if(filter.validate()) validFilters.add(filter);
            }
            int totalFiltersSize = this.filterEntries.size();
            this.filterEntries = validFilters;
            int validFiltersSize = this.filterEntries.size();
            
            if(validEffectsSize == 0) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", no valid effects");
            else if(validFiltersSize == 0) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", no valid filters");
            else {
                Socketed.LOGGER.info("Gem Type Validating, Name: " + this.name +
                                             ", Display Name: " + this.displayName +
                                             ", Tier: " + this.tier +
                                             ", Color: " + this.color.name() +
                                             ", Valid Effects: " + validEffectsSize + "/" + totalEffectsSize +
                                             ", Valid Filters: " + validFiltersSize + "/" + totalFiltersSize);
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static GemType getGemTypeFromItemStack(ItemStack itemStack) {
        if(itemStack.isEmpty()) return null;
        for(GemType gemType : JsonConfig.getGemData().values()) {
            //TODO: Issues with one item being valid for multiple gem types?
            if(gemType.matches(itemStack)) return gemType;
        }
        return null;
    }
    
    @Nullable
    public static GemType getGemTypeFromName(String gemTypeName) {
        if(gemTypeName == null || gemTypeName.isEmpty()) return null;
        return JsonConfig.getGemData().get(gemTypeName);
    }
}