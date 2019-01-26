package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.data.Data;
import com.ruegnerlukas.taskmanager.logic.data.Project;

import java.io.File;

public class ProjectLogic {


	/**
	 * @return true, if a project is currently open
	 * */
	public boolean isProjectOpen() {
		return getProject() != null;
	}




	/**
	 * creates a new project <p>
	 * Events: <p>
	 * - ProjectCreatedEvent: when the project was created
	 * @return true, if completed successful
	 * */
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




	/**
	 * closes the current project
	 * Events: <p>
	 * - ProjectClosedEvent: when a project was closed
	 * @return true, if completed successful
	 * */
	public boolean closeProject() {
		Project project = getProject();
		if(project != null) {
			setProject(null);
			EventManager.fireEvent(new ProjectClosedEvent(project, this));
			return true;
		} else {
			return false;
		}
	}




	/**
	 * @return the current project
	 * */
	public Project getProject() {
		return Data.get().project;
	}




	/**
	 * sets the current project to the given project
	 * */
	private void setProject(Project project) {
		Data.get().project = project;
	}




	/**
	 * renames the current project, if the new name is empty, it will rename it but using the current name as the new name
	 * Events: <p>
	 * - ProjectRenamedEvent: when the project was renamed
	 * @return true, if completed successful
	 * */
	public boolean renameProject(String name) {
		if (getProject() == null) {
			return false;
		} else {
			String newName = name.trim();
			String oldName = getProject().name;
			if (!newName.isEmpty()) {
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, oldName, this));
			} else {
				getProject().name = newName;
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, newName, this));
			}
			return true;
		}
	}


}
