package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

public class NoValue extends TaskValue {


	public NoValue() {
		super(null, null);
	}




	@Override
	public int compare(TaskValue other) {
		if (getAttType() != other.getAttType()) {
			return 1;
		} else {
			return 0;
		}
	}




	@Override
	public String toString() {
		return "TaskValue.NoValue@" + Integer.toHexString(this.hashCode());
	}


}
