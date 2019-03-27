package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {


	// 0. project data / attributes
	public String name;
	public boolean attributesLocked = false;
	public List<TaskAttribute> attributes = new ArrayList<>();
	public boolean appearanceLocked = false;
	public List<TaskAttribute> cardAttributes = new ArrayList<>();
	public Map<TaskAttribute, String> cardAttribDisplayType = new HashMap<>();

	// 1. tasks
	public List<Task> tasks = new ArrayList<>();
	public int idCounter = 1;
	public List<Task> archivedTasks = new ArrayList<>();
	public int archivedTasksLimit = 16;

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
	}


}
