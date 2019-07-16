package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TaskGroupData {


	public final String customHeaderString;
	public final List<TaskAttribute> attributes;




	public TaskGroupData(String customHeaderString, TaskAttribute... attributes) {
		this(customHeaderString, Arrays.asList(attributes));
	}




	public TaskGroupData(String customHeaderString, List<TaskAttribute> attributes) {
		this.customHeaderString = customHeaderString;
		this.attributes = Collections.unmodifiableList(new ArrayList<>(attributes));
	}

}
