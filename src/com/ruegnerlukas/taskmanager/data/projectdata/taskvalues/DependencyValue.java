package com.ruegnerlukas.taskmanager.data.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DependencyValue extends TaskValue<Task[]> {


	public DependencyValue(Task... value) {
		super(value, AttributeType.DEPENDENCY);
	}




	@Override
	public String asDisplayableString() {
		return "nDep="+getValue().length;
	}




	@Override
	public int compare(TaskValue<?> other) {
		if (getAttType() != other.getAttType()) {
			return Integer.compare(this.getAttType().ordinal(), other.getAttType() == null ? -1 : other.getAttType().ordinal());
		} else {
			Set<Task> setT = new HashSet<>(Arrays.asList(this.getValue()));
			Set<Task> setO = new HashSet<>(Arrays.asList(((DependencyValue) other).getValue()));
			if (setT.containsAll(setO) && setO.containsAll(setT)) {
				return 0;
			} else {
				return Integer.compare(setT.size(), setO.size());
			}
		}
	}




	@Override
	public String toString() {
		return "TaskValue.Dependency@" + Integer.toHexString(this.hashCode()) + " = n=" + getValue().length;
	}


}
