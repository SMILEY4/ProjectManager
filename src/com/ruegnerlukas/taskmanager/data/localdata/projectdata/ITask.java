package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import javafx.collections.ObservableMap;

import java.util.List;

public interface ITask {


	TaskValue<?> getValue(TaskAttributeData attribute);


	/**
	 * @return the map of the {@link TaskValue}s of this {@link Task}
	 */
	ObservableMap<TaskAttributeData, TaskValue<?>> getValues();

	/**
	 * @return a list of all {@link TaskValue}s of this {@link Task}
	 */
	List<TaskValue<?>> getValueList();


}
