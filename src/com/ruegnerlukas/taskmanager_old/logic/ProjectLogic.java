package com.ruegnerlukas.taskmanager_old.logic;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;

import java.io.File;

public class ProjectLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	protected Project getProject() {
		return Data.get().project;
	}




	protected void setProject(Project project) {
		Data.get().project = project;
	}


	//======================//
	//        GETTER        //
	//======================//




	/**
	 * checks, if a project is currently open
	 */
	public Response<Boolean> getIsProjectOpen() {
		return new Response<Boolean>().complete(getProject() != null);
	}




	/**
	 * request the currently opened project. Returns FAIL, if no project opened.
	 */
	public Response<Project> getCurrentProject() {
		Project project = getProject();
		return new Response<Project>().complete(project, project != null ? Response.State.SUCCESS : Response.State.FAIL);
	}


	//======================//
	//        SETTER        //
	//======================//




	/**
	 * Closes the old project and opens a new project <p>
	 * Events: <p>
	 * - ProjectClosedEvent: if a previous project was closed in the process<p>
	 * - ProjectCreatedEvent: after the project was created
	 */
	public void createProject() {
		createProject("New Project");
	}




	/**
	 * Closes the old project and opens a new project with the given name <p>
	 * Events: <p>
	 * - ProjectClosedEvent: if a previous project was closed in the process<p>
	 * - ProjectCreatedEvent: after the project was created
	 */
	public void createProject(String name) {

		// close prev. project
		Project prevProject = getProject();
		if (prevProject != null) {
			setProject(null);
			EventManager.fireEvent(new ProjectClosedEvent(prevProject, this));
		}

		// createItem/open new project
		Project newProject = new Project(name);
		newProject.attributes.add(new TaskAttribute(IDAttributeData.NAME, TaskAttributeType.ID));
		newProject.attributes.add(new TaskAttribute(FlagAttributeData.NAME, TaskAttributeType.FLAG));
		newProject.attributes.add(new TaskAttribute(DescriptionAttributeData.NAME, TaskAttributeType.DESCRIPTION));
		setProject(newProject);
		EventManager.fireEvent(new ProjectCreatedEvent(newProject, this));
	}




	/**
	 * TODO: load project oes nothing
	 */
	public void loadProject(File rootFile) {
		// TODO load project
	}




	/**
	 * TODO: save project oes nothing
	 */
	public void saveProject() {
		// TODO save project
	}




	/**
	 * closes the current project<p>
	 * Events: <p>
	 * - ProjectClosedEvent: when a project was closed
	 */
	public void closeProject() {
		Project project = getProject();
		if (project != null) {
			setProject(null);
			EventManager.fireEvent(new ProjectClosedEvent(project, this));
		}
	}




	/**
	 * renames the current project to the given name. The new name can not be empty.<p>
	 * Events: <p>
	 * - ProjectRenamedEvent: when the project was renamed
	 */
	public void renameProject(String name) {

		// get project
		Project project = getProject();
		if (project == null) {
			return;
		}

		// get names
		String newName = name.trim();
		String oldName = getProject().name;

		// rename project
		if (!newName.isEmpty()) {
			project.name = newName;
			EventManager.fireEvent(new ProjectRenamedEvent(oldName, newName, this));
		}
	}


}
