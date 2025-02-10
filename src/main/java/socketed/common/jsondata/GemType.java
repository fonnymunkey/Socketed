package socketed.common.jsondata;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.filter.GenericFilter;
import socketed.common.util.SocketedUtil;

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
    
    @SerializedName("Effects")
    private List<GenericGemEffect> effects;
    
    @SerializedName("Filters")
    private List<GenericFilter> filters;

    public GemType(String displayName, Integer tier, TextFormatting color, List<GenericGemEffect> effects, List<GenericFilter> filters) {
        this.displayName = displayName;
        this.tier = tier;
        this.color = color;
        this.effects = effects;
        this.filters = filters;
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
    public List<GenericFilter> getFilters() {
        return this.filters;
    }
    
    //Instantiation shouldn't affect validity
    public boolean hasEffectsForStack(ItemStack stack) {
        if(stack.isEmpty()) return false;
        for(GenericGemEffect effect : this.effects) {
            if(SocketedUtil.isStackValidForSlot(stack, effect.getSlotType())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean matches(ItemStack input) {
        if(input == null || input.isEmpty()) return false;
        for(GenericFilter filter : this.getFilters()) {
            if(filter.matches(input)) return true;
        }
        return false;
    }
    
    public boolean validate() {
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, name null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", display name null or empty");
        else if(this.tier == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid tier");
        else if(this.color == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid color");
        else if(this.effects == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid effect list");
        else if(this.filters == null) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", invalid filter list");
        else {
            List<GenericGemEffect> validEffects = new ArrayList<>();
            for(GenericGemEffect effect : this.effects) {
                if(effect.validate()) validEffects.add(effect);
            }
            int totalEffectsSize = this.effects.size();
            this.effects = validEffects;
            int validEffectsSize = this.effects.size();
            
            List<GenericFilter> validFilters = new ArrayList<>();
            for(GenericFilter filter : this.filters) {
                if(filter.validate()) validFilters.add(filter);
            }
            int totalFiltersSize = this.filters.size();
            this.filters = validFilters;
            int validFiltersSize = this.filters.size();
            
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
        for(GemType gemType : JsonConfig.getSortedGemDataList()) {
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