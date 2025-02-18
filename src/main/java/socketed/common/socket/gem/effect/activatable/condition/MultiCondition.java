package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;
import java.util.List;

public class MultiCondition extends GenericCondition {
	
	public static final String TYPE_NAME = "Multi";
	
	@SerializedName("Logic Type")
	protected final ConditionLogicType logicType;
	
	@SerializedName("Sub-Conditions")
	protected final List<GenericCondition> conditions;
	
	public MultiCondition(ConditionLogicType logicType, List<GenericCondition> conditions) {
		super();
		this.logicType = logicType;
		this.conditions = conditions;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		return this.logicType.test(this.conditions, callback, playerSource, effectTarget);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	@Override
	public boolean validate() {
		if(this.logicType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define logic type");
		else if(this.conditions == null || this.conditions.isEmpty()) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define sub-conditions");
		else {
			for(GenericCondition condition : this.conditions)
				if(!condition.validate())
					return false;
			return true;
		}
		return false;
	}
	
	public enum ConditionLogicType {
		AND {
			@Override
			public boolean test(List<GenericCondition> conditions, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				for(GenericCondition condition : conditions) {
					if(!condition.testCondition(callback, playerSource, effectTarget)) return false;
				}
				return true;
			}
		},
		OR {
			@Override
			public boolean test(List<GenericCondition> conditions, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				for(GenericCondition condition : conditions) {
					if(condition.testCondition(callback, playerSource, effectTarget)) return true;
				}
				return false;
			}
		};
		
		public abstract boolean test(List<GenericCondition> conditions, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget);
	}
}