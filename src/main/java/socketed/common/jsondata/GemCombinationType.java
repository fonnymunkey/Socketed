package socketed.common.jsondata;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.text.TextFormatting;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.filter.BlockAllFilterEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemCombinationType extends GemType {
    @SerializedName("Strict Order")
    private final Boolean isStrictOrder;
    @SerializedName("Strict Count")
    private final Boolean isStrictSocketCount;
    @SerializedName("Replaces Original Effects")
    private final Boolean replacesOriginalEffects;
    @SerializedName("Gem Types")
    private final List<String> gemTypes;

    public GemCombinationType(String name, String displayName, TextFormatting color, List<GenericGemEffect> effects, boolean isStrictOrder, boolean isStrictSocketCount, boolean replacesOriginalEffects, List<String> gemTypes) {
        super(name, displayName, color, effects, Collections.singletonList(new BlockAllFilterEntry()), 0);
        this.isStrictOrder = isStrictOrder;
        this.isStrictSocketCount = isStrictSocketCount;
        this.replacesOriginalEffects = replacesOriginalEffects;
        this.gemTypes = gemTypes;
    }

    public boolean getIsStrictOrder(){
        return isStrictOrder;
    }

    public boolean getIsStrictSocketCount(){
        return isStrictSocketCount;
    }

    public boolean getReplacesEffects(){
        return replacesOriginalEffects;
    }

    public List<String> getGemTypes(){
        return gemTypes;
    }


    public boolean matches(List<String> input) {
        if (!this.isValid()) return false;
        if (input == null || input.isEmpty()) return false;
        if (input.size() < this.gemTypes.size()) return false;

        if (this.isStrictSocketCount && input.size() != this.gemTypes.size()) return false;
        if (this.isStrictOrder) {
            //Compare all ordered sublists of input list with the same size as gemTypes against gemTypes
            int subListCount = input.size() - this.gemTypes.size() + 1;
            for (int i = 0; i < subListCount; i++)
                if (input.subList(i, i + this.gemTypes.size()).equals(this.gemTypes))
                    return true;
            return false;
        } else {
            List<String> inputCopy = new ArrayList<>(input);

            //input needs to contain all entries in gemTypes
            for (String gemType : this.gemTypes)
                if (!inputCopy.contains(gemType))
                    return false;
                else
                    //need to remove found Gems to enable combinations that use the same gem multiple times
                    //important that it removes the first occurrence, as CapabilityHasSockets.refreshCombinations also uses the first occurrence
                    inputCopy.remove(gemType);
            return true;
        }
    }

    protected void validate() {
        super.validate();
        this.parsed = false;
        this.valid = false;
        if (this.isStrictOrder == null) Socketed.LOGGER.warn("Invalid Gem Combination, " + this.name + ", invalid strict order setting");
        else if (this.isStrictSocketCount == null) Socketed.LOGGER.warn("Invalid Gem Combination, " + this.name + ", invalid strict count setting");
        else if (this.replacesOriginalEffects == null) Socketed.LOGGER.warn("Invalid Gem Combination, " + this.name + ", invalid replaces original effects setting");
        else {
            int validGems = 0;
            int totalGems = 0;
            if (this.gemTypes != null) {
                for (String gemType : this.gemTypes) {
                    totalGems++;
                    if (JsonConfig.getGemData().containsKey(gemType)) validGems++;
                }
            }
            if (totalGems <= 0) Socketed.LOGGER.warn("Invalid Gem Combination, " + this.name + ", no Gem Types provided");
            if (validGems != totalGems) Socketed.LOGGER.warn("Invalid Gem Combination, " + this.name + ", not all Gem Types valid");
            this.valid = totalGems > 0 && validGems == totalGems;
        }

        this.parsed = true;
    }
}
