package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.logic.data.Project;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;

public class AttributeLogic {


	public boolean setAttributeLock(boolean locked) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			boolean lockPrev = project.attributesLocked;
			project.attributesLocked = locked;
			EventManager.fireEvent(new AttributeLockEvent(lockPrev, locked, this));
			return true;
		} else {
			return false;
		}
	}




	public boolean createAttribute(String name, TaskAttributeType type) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = new TaskAttribute(name, type);

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeCreatedRejection(attribute, EventCause.NOT_ALLOWED, this));
				return false;
			}

			boolean nameExists = false;
			for (TaskAttribute a : project.attributes) {
				if (a.name.equals(name)) {
					nameExists = true;
					break;
				}
			}

			if (nameExists) {
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
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = null;
			for (TaskAttribute a : project.attributes) {
				if (a.name.equals(name)) {
					attribute = a;
					break;
				}
			}

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if (attribute != null && !attribute.data.getType().fixed) {
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
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attributeOld = null;
			TaskAttribute attributeNew = null;

			for (TaskAttribute a : project.attributes) {
				if (a.name.equals(oldName)) {
					attributeOld = a;
				}
				if (a.name.equals(newName)) {
					attributeNew = a;
				}
			}

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if (attributeNew != null) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NAME_EXISTS, this));
				return false;

			} else if (attributeOld != null && attributeOld.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;

			} else if (attributeOld != null && !attributeOld.data.getType().fixed) {
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
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = null;

			for (TaskAttribute a : project.attributes) {
				if (a.name.equals(name)) {
					attribute = a;
				}
			}

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, newType, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if (attribute != null) {

				if (attribute.data.getType().fixed || newType.fixed) {
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




	public boolean updateTaskAttribute(String attributeName, TaskAttributeData.Var var, Object newValue) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = null;

			for (TaskAttribute a : project.attributes) {
				if (a.name.equals(attributeName)) {
					attribute = a;
				}
			}

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, newValue, EventCause.NOT_ALLOWED, this));
				return false;
			}

			if (attribute != null && attribute.data != null) {
				TaskAttributeData.Var[] changedVars = attribute.data.update(var, newValue);
				if(changedVars != null) {
					EventManager.fireEvent(new AttributeUpdatedEvent(attribute, changedVars, newValue, this));
					return true;
				} else {
					EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, newValue, EventCause.UNKNOWN, this));
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
