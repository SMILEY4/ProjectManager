package com.ruegnerlukas.taskmanager.logic.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;

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
