package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.format.DateTimeFormatter;

public abstract class TaskValue<T> {


	private final T value;
	private final AttributeType type;




	protected TaskValue(T value, AttributeType type) {
		this.value = value;
		this.type = type;
	}




	public static int compare(TaskValue<?> a, TaskValue<?> b) {
		if (a == b) {
			return 0;
		} else {
			return a == null ? -1 : a.compare(b);
		}
	}




	public AttributeType getAttType() {
		return type;
	}




	public T getValue() {
		return value;
	}




	@Override
	public boolean equals(Object other) {
		if (other instanceof TaskValue) {
			return compare((TaskValue) other) == 0;
		} else {
			return false;
		}
	}




	@Override
	public int hashCode() {
		return getValue() != null ? getValue().hashCode() : (getAttType() != null ? getAttType().hashCode() : 0);
	}




	public abstract String asDisplayableString();


	public abstract int compare(TaskValue<?> other);




	public static String valueToString(TaskValue<?> taskValue) {

		if (taskValue == null || taskValue.getValue() == null) {
			return "empty";
		}

		switch (taskValue.getAttType()) {

			case ID: {
				return "T-" + ((IDValue) taskValue).getValue();
			}
			case DESCRIPTION: {
				return ((DescriptionValue) taskValue).getValue();
			}
			case CREATED: {
				return ((CreatedValue) taskValue).getValue().format(DateTimeFormatter.ofPattern("dd.MM.uu HH:mm"));
			}
			case LAST_UPDATED: {
				return ((LastUpdatedValue) taskValue).getValue().format(DateTimeFormatter.ofPattern("dd.MM.uu HH:mm"));
			}
			case FLAG: {
				return ((FlagValue) taskValue).getValue().name.get();
			}
			case TEXT: {
				return ((TextValue) taskValue).getValue();
			}
			case NUMBER: {
				return ((NumberValue) taskValue).getValue().toString();
			}
			case BOOLEAN: {
				return ((BoolValue) taskValue).getValue() ? "True" : "False";
			}
			case CHOICE: {
				return ((ChoiceValue) taskValue).getValue();
			}
			case DATE: {
				return ((DateValue) taskValue).getValue().format(DateTimeFormatter.ofPattern("dd.MM.uu"));
			}
			case DEPENDENCY: {
				Task[] tasks = ((DependencyValue) taskValue).getValue();
				String[] ids = new String[tasks.length];
				for (int i = 0; i < tasks.length; i++) {
					ids[i] = "T-" + TaskLogic.getTaskID(tasks[i]);
				}
				return String.join(", ", ids);
			}
			default: {
				return "?unknowntype?";
			}
		}

	}


}
