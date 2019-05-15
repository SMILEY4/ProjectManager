package com.ruegnerlukas.taskmanager_old.architecture.eventsystem.events;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;

public class ProjectClosedEvent extends Event {

	private Project project;
	
	
	public ProjectClosedEvent(Project project, Object source) {
		super(source);
		this.project = project;
	}

	
	
	
	public Project getLastProject() {
		return project;
	}
	
	
	
}
