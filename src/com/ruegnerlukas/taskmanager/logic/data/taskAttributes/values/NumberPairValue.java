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
			return new int[] {(int)v0, (int)v1};
		} else {
			return new double[] {v0, v1};
		}
	}

}
