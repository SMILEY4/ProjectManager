package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Data;
import com.ruegnerlukas.taskmanager.logic.data.Project;

import java.io.File;

public class ProjectLogic {


	public boolean isProjectOpen() {
		return getProject() != null;
	}




	public boolean createProject() {
		setProject(new Project("New Project"));
		EventManager.fireEvent(new ProjectCreatedEvent(getProject(), this));
		return true;
	}




	public boolean loadProject(File rootFile) {
		// TODO
		return false;
	}




	public boolean saveProject() {
		// TODO
		return false;
	}




	public boolean closeProject() {
		Project project = getProject();
		setProject(null);
		EventManager.fireEvent(new ProjectClosedEvent(project, this));
		return true;
	}




	public Project getProject() {
		return Data.get().project;
	}




	public void setProject(Project project) {
		Data.get().project = project;
	}




	public boolean renameProject(String name) {
		if (getProject() == null) {
			return false;
		} else {
			String newName = name.trim();
			String oldName = getProject().name;
			if (newName.isEmpty()) {
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, oldName, this));
			} else {
				getProject().name = newName;
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, newName, this));
			}
			return true;
		}
	}


}
