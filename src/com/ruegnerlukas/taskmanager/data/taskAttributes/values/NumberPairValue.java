package com.ruegnerlukas.taskmanager.data.taskAttributes.values;

public class NumberPairValue implements TaskAttributeValue {


	private double v0;
	private double v1;
	private boolean isInt;




	public NumberPairValue(int v0, int v1) {
		this.v0 = v0;
		this.v1 = v1;
		this.isInt = true;
	}




	public NumberPairValue(double v0, double v1) {
		this.v0 = v0;
		this.v1 = v1;
		this.isInt = false;
	}




	public int getInt0() {
		return (int) v0;
	}




	public int getInt1() {
		return (int) v1;
	}




	public double getDouble0() {
		return v0;
	}




	public double getDouble1() {
		return v1;
	}




	public NumberValue getNumberValue0() {
		return new NumberValue(isInt ? getInt0() : getDouble0());
	}




	public NumberValue getNumberValue1() {
		return new NumberValue(isInt ? getInt1() : getDouble1());
	}




	@Override
	public Object getValue() {
		if (isInt) {
			return new int[]{(int) v0, (int) v1};
		} else {
			return new double[]{v0, v1};
		}
	}




	@Override
	public String toString() {
		return "(" + v0 + "," + v1 + ")";
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NumberPairValue that = (NumberPairValue) o;

		if (Double.compare(that.v0, v0) != 0) return false;
		if (Double.compare(that.v1, v1) != 0) return false;
		return isInt == that.isInt;
	}




	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(v0);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(v1);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (isInt ? 1 : 0);
		return result;
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		if (o instanceof NumberPairValue) {
			NumberValue tValue0 = this.getNumberValue0();
			NumberValue tValue1 = this.getNumberValue1();
			NumberValue oValue0 = ((NumberPairValue) o).getNumberValue0();
			NumberValue oValue1 = ((NumberPairValue) o).getNumberValue1();
			if (tValue0.compareTo(oValue0) == -1) {
				return -1;
			}
			if (tValue0.compareTo(oValue0) == +1) {
				return +1;
			}
			if (tValue1.compareTo(oValue1) == -1) {
				return -1;
			}
			if (tValue1.compareTo(oValue1) == +1) {
				return +1;
			}
			return 0;
		} else {
			return -2;
		}
	}




	public boolean inRange(NumberValue other) {
		NumberValue min = this.getNumberValue0();
		NumberValue max = this.getNumberValue1();
		return (min.compareTo(other) != +1 && other.compareTo(max) != +1);
	}


}
