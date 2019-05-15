package com.ruegnerlukas.taskmanager_old.data;


import com.ruegnerlukas.taskmanager.data.Project;

public class Data {

	private static final Data data = new Data();
	
	public static Data get() {
		return data;
	}
	
	
	
	public Project project = null;

	
}
