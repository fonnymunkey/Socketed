package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import socketed.Socketed;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;
import java.util.Comparator;

public abstract class ComparingCondition extends GenericCondition {
	
	public static final String TYPE_NAME = "Comparing";

	@SerializedName("Comparison Type")
	protected final ConditionComparisonType comparisonType;

	protected ComparingCondition(ConditionComparisonType comparisonType){
		super();
		this.comparisonType = comparisonType;
	}

	//TODO: default: LESS?
	@Override
	public boolean validate() {
		if(this.comparisonType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define comparison type");
		else return true;

		return false;
	}

	public enum ConditionComparisonType {
		LESS {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				return value1.compareTo(value2) < 0;
			}
		},
		GREATER {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				return value1.compareTo(value2) > 0;
			}
		},
		EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				return value1.compareTo(value2) == 0;
			}
		},
		LESS_EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				return value1.compareTo(value2) <= 0;
			}
		},
		GREATER_EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
				return value1.compareTo(value2) >= 0;
			}
		};

		public abstract <T extends Comparable<T>> boolean test(T value1, T value2, @Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget);
	}
}