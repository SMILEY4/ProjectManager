package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;

public class FlagValue extends TaskValue<TaskFlag> {


	public FlagValue(TaskFlag value) {
		super(value, AttributeType.FLAG);
	}




	@Override
	public String asDisplayableString() {
		return getValue().name.get();
	}




	@Override
	public int compare(TaskValue<?> o) {
		if (getAttType() != o.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), o.getAttType() == null ? 1 : o.getAttType().ordinal());
		} else {
			FlagValue other = (FlagValue) o;
			int cmpName = this.getValue().name.get().compareTo(other.getValue().name.get());
			if (cmpName == 0) {
				return Integer.compare(this.getValue().color.get().ordinal(), other.getValue().color.get().ordinal());
			} else {
				return cmpName;
			}
		}

	}




	@Override
	public String toString() {
		return "TaskValue.Flag@" + Integer.toHexString(this.hashCode()) + " = " + getValue();
	}


}
