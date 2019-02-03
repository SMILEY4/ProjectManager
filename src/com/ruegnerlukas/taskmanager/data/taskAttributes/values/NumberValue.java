package com.ruegnerlukas.taskmanager.data.taskAttributes.values;

public class NumberValue implements TaskAttributeValue {


	private double value;
	private boolean isInt;




	public NumberValue(int value) {
		this.value = value;
		this.isInt = true;
	}




	public NumberValue(double value) {
		this.value = value;
		this.isInt = false;
	}




	public boolean isInt() {
		return this.isInt;
	}




	public int getInt() {
		return (int) value;
	}




	public double getDouble() {
		return value;
	}




	@Override
	public Object getValue() {
		if (isInt) {
			return (int) value;
		} else {
			return value;
		}
	}




	@Override
	public String toString() {
		return "" + (isInt ? (int) value : value);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NumberValue that = (NumberValue) o;

		if (Double.compare(that.value, value) != 0) return false;
		return isInt == that.isInt;
	}




	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = (int) (temp ^ (temp >>> 32));
		result = 31 * result + (isInt ? 1 : 0);
		return result;
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		if (o instanceof NumberValue) {
			final NumberValue other = (NumberValue) o;
			if ((this.isInt ? this.getInt() : this.getDouble()) < (other.isInt ? other.getInt() : other.getDouble())) {
				return -1;
			}
			if ((this.isInt ? this.getInt() : this.getDouble()) > (other.isInt ? other.getInt() : other.getDouble())) {
				return -1;
			}
			return 0;
		} else {
			return -2;
		}
	}

}
