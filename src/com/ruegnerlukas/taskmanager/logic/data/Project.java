package com.ruegnerlukas.taskmanager.logic.data;

import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.groups.GroupByData;
import com.ruegnerlukas.taskmanager.logic.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.IDAttributeData;

import java.util.ArrayList;
import java.util.List;

public class Project {

	// 0. project data
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
	public List<TaskAttribute> groupByOrder = new ArrayList<>();
	public GroupByData groupByData = new GroupByData();
	public boolean useCustomHeaderString = false;
	public String groupByHeaderString = "";

	// 4. sort
	public List<SortElement> sortElements = new ArrayList<>();





	public Project(String name) {
		this.name = name;
		attributes.add(new TaskAttribute(IDAttributeData.NAME, TaskAttributeType.ID));
		attributes.add(new TaskAttribute(FlagAttributeData.NAME, TaskAttributeType.FLAG));
		attributes.add(new TaskAttribute(DescriptionAttributeData.NAME, TaskAttributeType.DESCRIPTION));
	}

	
}
