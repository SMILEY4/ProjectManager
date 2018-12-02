package com.ruegnerlukas.taskmanager.logic.services;



public class IDService {

	
	public int generateListID() {
		if(DataService.project.getProject() == null) {
			return generateInvalidListID();
		} else {
			return DataService.project.getProject().listIdCounter;
		}
	}
	
	
	public int generateInvalidListID() {
		return -1;
	}
	
	
	
	
	public int generatTaskID() {
		if(DataService.project.getProject() == null) {
			return generateInvalidTaskID();
		} else {
			return DataService.project.getProject().taskIdCounter;
		}
	}
	
	
	public int generateInvalidTaskID() {
		return -1;
	}
	
}
