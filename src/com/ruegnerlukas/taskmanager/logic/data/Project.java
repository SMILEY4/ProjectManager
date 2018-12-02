package com.ruegnerlukas.taskmanager.logic.data;

import java.util.ArrayList;
import java.util.List;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;

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
	
	
	
	
	public Project(String name) {
		this.name = name;
	}
	
}
