package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, List<FilterCriteria>> savedFilters = new HashMap<>();

	// 3. group by
	public AttributeGroupData attribGroupData = new AttributeGroupData();
	public Map<String, AttributeGroupData> savedGroupOrders = new HashMap<>();

	// 4. sort
	public List<SortElement> sortElements = new ArrayList<>();
	public Map<String, List<SortElement>> savedSortElements = new HashMap<>();

	// presets
	public Map<String, Preset> presets = new HashMap<>();




	public Project(String name) {
		this.name = name;
		attributes.add(new TaskAttribute(IDAttributeData.NAME, TaskAttributeType.ID));
		attributes.add(new TaskAttribute(FlagAttributeData.NAME, TaskAttributeType.FLAG));
		attributes.add(new TaskAttribute(DescriptionAttributeData.NAME, TaskAttributeType.DESCRIPTION));
	}


}
