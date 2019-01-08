package com.ruegnerlukas.taskmanager.logic.data;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
	
	
	// project
	public String name;

	// flags
	public TaskFlag defaultFlag = new TaskFlag(FlagColor.GRAY, "Default", true);
	public List<TaskFlag> flags = new ArrayList<TaskFlag>();

	// lists
	public int listIdCounter = 0;
	public int taskIdCounter = 0;
	public List<TaskList> lists = new ArrayList<TaskList>();
	public List<Integer> listOrder = new ArrayList<Integer>();
	
	// custom task attributes
	public Map<String,CustomAttribute> customAttributes = new HashMap<String,CustomAttribute>();
	
	
	
	
	public Project(String name) {
		this.name = name;
	}
	
}
