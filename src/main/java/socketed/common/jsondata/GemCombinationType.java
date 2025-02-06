package socketed.common.jsondata;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemCombinationType {
    
    /**
     * Set prior to validation based on filename
     */
    protected transient String name = null;
    
    @SerializedName("Display Name")
    private final String displayName;
    
    @SerializedName("Text Color")
    private final TextFormatting color;
    
    @SerializedName("Strict Order")
    private final Boolean isStrictOrder;
    
    @SerializedName("Strict Count")
    private final Boolean isStrictSocketCount;
    
    @SerializedName("Allows Wrapping")
    private Boolean allowsWrapping;
    
    @SerializedName("Replaces Original Effects")
    private final Boolean replacesOriginalEffects;
    
    @SerializedName("Gem Types")
    private final List<String> gemTypes;
    
    @SerializedName("Effect Entries")
    private List<GenericGemEffect> effects;

    public GemCombinationType(String displayName, TextFormatting color, Boolean isStrictOrder, Boolean isStrictSocketCount, Boolean allowsWrapping, Boolean replacesOriginalEffects, List<String> gemTypes, List<GenericGemEffect> effects) {
        this.displayName = displayName;
        this.color = color;
        this.isStrictOrder = isStrictOrder;
        this.isStrictSocketCount = isStrictSocketCount;
        this.allowsWrapping = allowsWrapping;
        this.replacesOriginalEffects = replacesOriginalEffects;
        this.gemTypes = gemTypes;
        this.effects = effects;
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
    
    @Nonnull
    public TextFormatting getColor() {
        return this.color;
    }
    
    public boolean getIsStrictOrder() {
        return this.isStrictOrder;
    }

    public boolean getIsStrictSocketCount() {
        return this.isStrictSocketCount;
    }
    
    public boolean getAllowsWrapping() {
        return this.allowsWrapping;
    }

    public boolean getReplacesEffects() {
        return this.replacesOriginalEffects;
    }

    @Nonnull
    public List<String> getGemTypes() {
        return this.gemTypes;
    }
    
    @Nonnull
    public List<GenericGemEffect> getEffects() {
        return this.effects;
    }
    
    /**
     * Checks if the given list of type names matches for this Gem Combination Type
     * @return -1 for no matches, 0 for matches but non-strict order, index of first match for strict order
     */
    public int matches(List<String> input) {
        if(input == null || input.isEmpty()) return -1;
        if(input.size() < this.gemTypes.size()) return -1;
        if(this.isStrictSocketCount && input.size() != this.gemTypes.size()) return -1;
        
        List<String> inputCopy = new ArrayList<>(input);
        if(this.isStrictOrder) {
            if(this.allowsWrapping) {
                //Re-add all but last to allow for checking wrapped ordering
                inputCopy.addAll(input);
                inputCopy.remove(inputCopy.size() - 1);
            }
            return Collections.indexOfSubList(inputCopy, this.gemTypes);
        }
        else {
            for(String gemType : this.gemTypes) {
                if(!inputCopy.contains(gemType)) return -1;
                //Remove found gemTypes to allow for requiring multiple of the same type
                inputCopy.remove(gemType);
            }
            return 0;
        }
    }

    public boolean validate() {
        if(this.name == null || this.name.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Combination Type, name null or empty");
        else if(this.displayName == null || this.displayName.isEmpty()) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", display name null or empty");
        else if(this.color == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid color");
        else if(this.isStrictOrder == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid strict order setting");
        else if(this.isStrictSocketCount == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid strict count setting");
        else if(this.replacesOriginalEffects == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid replaces original effects setting");
        else if(this.gemTypes == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid gem types list");
        else if(this.effects == null) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", invalid effect entry list");
        else {
            List<GenericGemEffect> validEffects = new ArrayList<>();
            for(GenericGemEffect effect : this.effects) {
                if(effect.validate()) validEffects.add(effect);
            }
            int totalEffectsSize = this.effects.size();
            this.effects = validEffects;
            int validEffectsSize = this.effects.size();
            
            int totalGemsSize = 0;
            int validGemsSize = 0;
            for(String gemType : this.gemTypes) {
                totalGemsSize++;
                if(JsonConfig.getGemData().containsKey(gemType)) validGemsSize++;
            }
            
            //Default to true, optional entry
            if(this.allowsWrapping == null) this.allowsWrapping = true;
            
            if(totalGemsSize == 0) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", no Gem Types provided");
            else if(validGemsSize != totalGemsSize) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", not all Gem Types valid");
            else if(validEffectsSize == 0) Socketed.LOGGER.warn("Invalid Gem Combination Type, " + this.name + ", no valid effects");
            else {
                Socketed.LOGGER.info("Gem Combination Type Validating, Name: " + this.name +
                                             ", Display Name: " + this.displayName +
                                             ", Color: " + this.color.name() +
                                             ", Strict Order: " + this.isStrictOrder +
                                             ", Strict Socket Count: " + this.isStrictSocketCount +
                                             ", Allows Wrapping: " + this.allowsWrapping +
                                             ", Replaces Original Effects: " + this.replacesOriginalEffects +
                                             ", Valid Gem Types: " + validGemsSize + "/" + totalGemsSize +
                                             ", Valid Effects: " + validEffectsSize + "/" + totalEffectsSize);
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public static GemCombinationType getGemCombinationTypeFromName(String gemCombinationTypeName) {
        if(gemCombinationTypeName == null || gemCombinationTypeName.isEmpty()) return null;
        return JsonConfig.getGemCombinationData().get(gemCombinationTypeName);
    }
}