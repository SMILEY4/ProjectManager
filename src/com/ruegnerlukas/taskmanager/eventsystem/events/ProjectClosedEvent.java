package com.ruegnerlukas.taskmanager.eventsystem.events;

import com.ruegnerlukas.taskmanager.logic_v2.data.Project;
import com.ruegnerlukas.taskmanager.eventsystem.Event;

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
