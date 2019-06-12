package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

public class NoValue extends TaskValue {


	public NoValue() {
		super(null, null);
	}




	@Override
	public int compare(TaskValue other) {
		if (getAttType() != other.getAttType()) {
			return -1;
		} else {
			return 0;
		}
	}




	@Override
	public String toString() {
		return "TaskValue.NoValue@" + Integer.toHexString(this.hashCode());
	}




	@Override
	public String asDisplayableString() {
		return "novalue";
	}

}
