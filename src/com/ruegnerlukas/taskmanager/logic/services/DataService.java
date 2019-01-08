package com.ruegnerlukas.taskmanager.logic.services;


public class DataService {
	
	public static final ProjectService project = new ProjectService();
	public static final FlagService flags = new FlagService();
	public static final IDService id = new IDService();
	public static final ListService lists = new ListService();
	public static final TaskService tasks = new TaskService();
	public static final CustomAttributeService customAttributes = new CustomAttributeService();

}
