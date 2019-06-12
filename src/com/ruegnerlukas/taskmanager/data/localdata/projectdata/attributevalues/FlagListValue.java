package com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FlagListValue extends AttributeValue<TaskFlag[]> {


	public FlagListValue(TaskFlag... flagList) {
		super(flagList, AttributeValueType.FLAG_LIST);
	}




	@Override
	public int compare(AttributeValue<?> other) {
		if (other == null) {
			return 1;
		} else {
			if (other.getType() != this.getType()) {
				return Integer.compare(this.getType().ordinal(), other.getType().ordinal());
			} else {
				Set<TaskFlag> setT = new HashSet<>(Arrays.asList(this.getValue()));
				Set<TaskFlag> setO = new HashSet<>(Arrays.asList(((FlagListValue) other).getValue()));
				if (setT.containsAll(setO) && setO.containsAll(setT)) {
					return 0;
				} else {
					return Integer.compare(setT.size(), setO.size());
				}
			}
		}
	}

}
