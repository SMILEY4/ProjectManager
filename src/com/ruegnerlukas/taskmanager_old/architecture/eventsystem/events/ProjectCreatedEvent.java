package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

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
