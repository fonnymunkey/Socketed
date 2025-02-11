package socketed.common.socket.gem;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.filter.GenericFilter;
import socketed.common.util.SocketedUtil;

import javax.annotation.Nonnull;
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
    private Integer tier;
    
    @SerializedName("Text Color")
    private TextFormatting color;
    
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
    
    /**
     * DisplayName: Required
     * GemTier: Optional, default 0
     * TextColor: Optional, default gray
     * Effects: Required
     * Filters: Required
     */
    public boolean validate() {
        if(this.tier == null) this.tier = 0;
        if(this.color == null) this.color = TextFormatting.GRAY;
        
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, name null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", display name null or empty");
        else if(this.tier < 0) Socketed.LOGGER.warn("Invalid Gem Type, " + this.name + ", tier must not be less than 0");
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
                //Warn but don't invalidate
                if(validEffectsSize != totalEffectsSize) Socketed.LOGGER.warn("Possible invalid Gem Type, " + this.name + ", not all effects valid");
                if(validFiltersSize != totalFiltersSize) Socketed.LOGGER.warn("Possible invalid Gem Type, " + this.name + ", not all filters valid");
                return true;
            }
        }
        return false;
    }
}