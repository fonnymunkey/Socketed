package socketed.api.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import socketed.Socketed;

public abstract class ComparingCondition extends EntityPropertyCondition {
	@SerializedName("Comparison Type")
	protected final ConditionComparisonType comparisonType;

	protected ComparingCondition(ConditionComparisonType comparisonType, boolean checkForPlayer){
		super(checkForPlayer);
		this.comparisonType = comparisonType;
	}

	//TODO: default: LESS?
	@Override
	public boolean validate() {
		if(super.validate()) {
			if (this.comparisonType == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Condition, must define comparison type");
			else return true;
		}
		return false;
	}

	public enum ConditionComparisonType {
		LESS {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2) {
				return value1.compareTo(value2) < 0;
			}
		},
		GREATER {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2) {
				return value1.compareTo(value2) > 0;
			}
		},
		EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2) {
				return value1.compareTo(value2) == 0;
			}
		},
		LESS_EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2) {
				return value1.compareTo(value2) <= 0;
			}
		},
		GREATER_EQUAL {
			@Override
			public <T extends Comparable<T>> boolean test(T value1, T value2) {
				return value1.compareTo(value2) >= 0;
			}
		};

		public abstract <T extends Comparable<T>> boolean test(T value1, T value2);
	}
}