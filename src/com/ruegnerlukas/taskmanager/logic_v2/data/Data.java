package com.ruegnerlukas.taskmanager.logic_v2.data;


public class Data {

	private static final Data data = new Data();
	
	public static Data get() {
		return data;
	}
	
	
	
	public Project project = null;

	
}
