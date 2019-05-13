package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;

public class FlagValue extends TaskValue<TaskFlag> {


	public FlagValue(TaskFlag value) {
		super(value, AttributeType.FLAG);
	}




	@Override
	public int compare(TaskValue<?> o) {
		if (getAttType() != o.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), o.getAttType() == null ? -1 : o.getAttType().ordinal());
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

}
