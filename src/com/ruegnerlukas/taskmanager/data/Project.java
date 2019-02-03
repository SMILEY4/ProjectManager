package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;

import java.util.ArrayList;
import java.util.List;

public class Project {


	// 0. project data / attributes
	public String name;
	public boolean attributesLocked = false;
	public List<TaskAttribute> attributes = new ArrayList<>();

	// 1. tasks
	public List<Task> tasks = new ArrayList<>();
	public int idCounter = 1;

	// 2. filter
	public List<FilterCriteria> filterCriteria = new ArrayList<>();
	public List<Task> filteredTasks = new ArrayList<>();

	// 3. group by
	public List<TaskAttribute> taskGroupOrder = new ArrayList<>();
	public TaskGroupData taskGroupData = new TaskGroupData();
	public boolean useCustomHeaderString = false;
	public String taskGroupHeaderString = "";

	// 4. sort
	public List<SortElement> sortElements = new ArrayList<>();




	public Project(String name) {
		this.name = name;
		attributes.add(new TaskAttribute(IDAttributeData.NAME, TaskAttributeType.ID));
		attributes.add(new TaskAttribute(FlagAttributeData.NAME, TaskAttributeType.FLAG));
		attributes.add(new TaskAttribute(DescriptionAttributeData.NAME, TaskAttributeType.DESCRIPTION));
	}


}
