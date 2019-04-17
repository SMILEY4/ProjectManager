package com.ruegnerlukas.taskmanager_old.data.taskAttributes.values;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TaskArrayValue implements TaskAttributeValue {


	private Task[] value;




	public TaskArrayValue(Task... value) {
		this.value = value;
	}




	public TaskArrayValue(Task[] values0, Task... values1) {
		this.value = new Task[values0.length + values1.length];
		int i = 0;
		for (int j = 0; j < values0.length; j++) {
			this.value[i++] = values0[j];
		}
		for (int j = 0; j < values1.length; j++) {
			this.value[i++] = values1[j];
		}

	}




	public TaskArrayValue(List<Task> value) {
		this.value = new Task[value.size()];
		for (int i = 0; i < value.size(); i++) {
			this.value[i] = value.get(i);
		}
	}




	public TaskArrayValue(Set<Task> value) {
		this.value = new Task[value.size()];
		int i = 0;
		for (Task task : value) {
			this.value[i++] = task;
		}
	}




	public Task[] getTasks() {
		return value;
	}




	@Override
	public Object getValue() {
		return value;
	}




	@Override
	public String toString() {
		return Arrays.toString(value);
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TaskArrayValue other = (TaskArrayValue) o;
		if (value.length == other.getTasks().length) {
			for (int i = 0; i < value.length; i++) {
				if (value[i] != other.getTasks()[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}




	@Override
	public int hashCode() {
		return value.hashCode();
	}




	@Override
	public int compareTo(TaskAttributeValue o) {
		if (o instanceof TaskArrayValue) {
			Task[] other = ((TaskArrayValue) o).getTasks();

			for (int i = 0; i < Math.min(value.length, other.length); i++) {
				int idThis = value[i].getID();
				int idOther = other[i].getID();

				if (idThis != idOther) {
					if (idThis < idOther) {
						return -1;
					} else {
						return +1;
					}
				}
			}

			return MathUtils.clamp(value.length - other.length, -1, +1);

		} else {
			return -2;
		}
	}

}