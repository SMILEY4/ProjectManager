package com.ruegnerlukas.taskmanager.data;


public class Data {

	private static final Data data = new Data();
	
	public static Data get() {
		return data;
	}
	
	
	
	public Project project = null;

	
}
