package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

public class ProjectCreatedEvent extends Event {

	private Project project;
	
	
	public ProjectCreatedEvent(Project project, Object source) {
		super(source);
		this.project = project;
	}

	
	
	
	public Project getNewProject() {
		return project;
	}
	
	
	
}
