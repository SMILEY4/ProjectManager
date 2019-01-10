package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.logic.data.Data;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements.TaskAttributeRequirement;

import java.io.File;

public class LogicService {


	private static final LogicService logicService = new LogicService();

	public static LogicService get() {
		return logicService;
	}



	//===============//
	//    PROJECT    //
	//===============//


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
		if(getProject() == null) {
			return false;
		} else {
			String newName = name.trim();
			String oldName = getProject().name;
			if(newName.isEmpty()) {
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, oldName, this));
			} else {
				getProject().name = newName;
				EventManager.fireEvent(new ProjectRenamedEvent(oldName, newName, this));
			}
			return true;
		}
	}









	//===============//
	//   ATTRIBUTES  //
	//===============//


	public boolean setAttributeLock(boolean locked) {
		if(isProjectOpen()) {
			Project project = getProject();
			boolean lockPrev = project.attributesLocked;
			project.attributesLocked = locked;
			EventManager.fireEvent(new AttributeLockEvent(lockPrev, locked, this));
			return true;
		} else {
			return false;
		}
	}



	public boolean createAttribute(String name, TaskAttributeType type) {
		if(isProjectOpen()) {
			Project project = getProject();

			TaskAttribute attribute = new TaskAttribute(name, type);

			if(project.attributesLocked) {
				EventManager.fireEvent(new AttributeCreatedRejection(attribute, EventCause.NOT_ALLOWED, this));
				return false;
			}

			boolean nameExists = false;
			for(TaskAttribute a : project.attributes) {
				if(a.name.equals(name)) {
					nameExists = true;
					break;
				}
			}

			if(nameExists) {
				EventManager.fireEvent(new AttributeCreatedRejection(attribute, EventCause.NAME_EXISTS, this));
				return false;

			} else {
				project.attributes.add(attribute);
				EventManager.fireEvent(new AttributeCreatedEvent(attribute, this));
				return true;
			}

		} else {
			return false;
		}
	}




	public boolean deleteAttribute(String name) {
		if(isProjectOpen()) {
			Project project = getProject();

			TaskAttribute attribute = null;
			for(TaskAttribute a : project.attributes) {
				if(a.name.equals(name)) {
					attribute = a;
					break;
				}
			}

			if(project.attributesLocked) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if(attribute != null && !attribute.data.getType().fixed) {
				project.attributes.remove(attribute);
				EventManager.fireEvent(new AttributeRemovedEvent(attribute, this));
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}




	public boolean renameAttribute(String oldName, String newName) {
		if(isProjectOpen()) {
			Project project = getProject();

			TaskAttribute attributeOld = null;
			TaskAttribute attributeNew = null;

			for(TaskAttribute a : project.attributes) {
				if(a.name.equals(oldName)) {
					attributeOld = a;
				}
				if(a.name.equals(newName)) {
					attributeNew = a;
				}
			}

			if(project.attributesLocked) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if(attributeNew != null) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NAME_EXISTS, this));
				return false;

			} else if(attributeOld != null && attributeOld.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;

			} else if(attributeOld != null && !attributeOld.data.getType().fixed) {
				attributeOld.name = newName;
				EventManager.fireEvent(new AttributeRenamedEvent(attributeOld, oldName, this));
				return true;

			} else {
				return false;
			}

		} else {
			return false;
		}
	}




	public boolean changeAttributeType(String name, TaskAttributeType newType) {
		if(isProjectOpen()) {
			Project project = getProject();

			TaskAttribute attribute = null;

			for(TaskAttribute a : project.attributes) {
				if(a.name.equals(name)) {
					attribute = a;
				}
			}

			if(project.attributesLocked) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, newType, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if(attribute != null) {

				if(attribute.data.getType().fixed || newType.fixed) {
					EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, newType, EventCause.NOT_ALLOWED, this));
					return false;

				} else {
					TaskAttributeType oldType = attribute.data.getType();
					attribute.createRequirement(newType);
					EventManager.fireEvent(new AttributeTypeChangedEvent(attribute, oldType, this));
					return true;
				}

			} else {
				return false;
			}

		} else {
			return false;
		}
	}




	public boolean updateTaskAttribute(String name, TaskAttributeRequirement updatedData) {
		if(isProjectOpen()) {
			Project project = getProject();

			TaskAttribute attribute = null;

			for(TaskAttribute a : project.attributes) {
				if(a.name.equals(name)) {
					attribute = a;
				}
			}

			if(project.attributesLocked) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, updatedData, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if(attribute != null) {

				TaskAttributeRequirement oldData = attribute.data.copy();

				if(attribute.data.update(updatedData)) {
					EventManager.fireEvent(new AttributeUpdatedEvent(attribute, oldData, this));
					return true;

				} else {
					EventManager.fireEvent(new AttributeUpdatedRejection(attribute, updatedData, EventCause.UNKNOWN, this));
					return false;
				}

			} else {
				return false;
			}

		} else {
			return false;
		}
	}




}
