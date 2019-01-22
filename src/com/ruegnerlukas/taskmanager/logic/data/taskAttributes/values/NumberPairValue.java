package com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values;

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
		return "NumberPairValue:(" + v0 + "," + v1 + ")";
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

}
