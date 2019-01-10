package com.ruegnerlukas.taskmanager.logic_v1.data;



public class Data {

	private static final Data data = new Data();
	
	public static Data get() {
		return data;
	}
	
	
	
	public Project project = null;

	
}
