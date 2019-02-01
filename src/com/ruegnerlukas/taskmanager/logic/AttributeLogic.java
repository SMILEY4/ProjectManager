package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.List;

public class AttributeLogic {


	/**
	 * Locks all TaskAttributes <p>
	 * Events: <p>
	 * - AttributeLockEvent: when the lock was changed
	 *
	 * @return true, if completed successful
	 */
	public boolean setAttributeLock(boolean locked) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			final boolean lockPrev = project.attributesLocked;
			if (lockPrev != locked) {
				project.attributesLocked = locked;
				EventManager.fireEvent(new AttributeLockEvent(lockPrev, locked, this));
			}
			return true;
		} else {
			return false;
		}
	}




	/**
	 * creates a new TaskAttribute with the given name and type <p>
	 * Events: <p>
	 * - AttributeCreatedRejection: when the attribute could not be created (NOT_ALLOWED = values are locked, NAME_EXISTS = given name is not unique) <p>
	 * - AttributeCreatedEvent: when the attribute was created
	 *
	 * @return true, if completed successful
	 */
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




	/**
	 * Deletes the attribute with the given name<p>
	 * Events: <p>
	 * - AttributeRemovedRejection: when the attribute could not be deleted (NOT_ALLOWED = values are locked / attribute is fixed, NOT_EXISTS = given name does not exist) <p>
	 * - AttributeRemovedEvent: when the attribute was deleted
	 *
	 * @return true, if completed successful
	 */
	public boolean deleteAttribute(String name) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = findAttribute(name);

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_ALLOWED, this));
				return false;
			}
			if (attribute == null) {
				EventManager.fireEvent(new AttributeRemovedRejection(null, EventCause.NOT_EXISTS, this));
				return false;
			}
			if (attribute.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRemovedRejection(null, EventCause.NOT_ALLOWED, this));
				return false;
			}

			project.attributes.remove(attribute);
			EventManager.fireEvent(new AttributeRemovedEvent(attribute, this));
			return true;

		} else {
			return false;
		}
	}




	/**
	 * renames the attribute with the given name to the new name <p>
	 * Events: <p>
	 * - AttributeRenamedRejection: when the attribute could not be renamed (NOT_ALLOWED = values are locked / attribute is fixed, NAME_EXISTS = given new name already exists, NOT_EXISTS = attribute with given name does not exist) <p>
	 * - AttributeRenamedEvent: when the attribute was renamed
	 *
	 * @return true, if completed successful
	 */
	public boolean renameAttribute(String oldName, String newName) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attributeOld = findAttribute(oldName);
			TaskAttribute attributeNew = findAttribute(newName);

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;
			}
			if (attributeNew != null) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NAME_EXISTS, this));
				return false;
			}
			if (attributeOld == null) {
				EventManager.fireEvent(new AttributeRenamedRejection(null, newName, EventCause.NOT_EXISTS, this));
				return false;
			}
			if (attributeOld.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));
				return false;
			}

			attributeOld.name = newName;
			EventManager.fireEvent(new AttributeRenamedEvent(attributeOld, oldName, this));
			return true;

		} else {
			return false;
		}
	}




	/**
	 * changes the attribute-type of the given attribute to the new type <p>
	 * Events <p>
	 * - AttributeTypeChangedRejection: when the type could not be changed (NOT_ALLOWED = values are locked / attribute is fixed / new attribute is fixed, NOT_EXISTS = attribute with given name does not exist) <p>
	 * - AttributeTypeChangedEvent: when the type was changed <p>
	 *
	 * @return true, if completed successful
	 */
	public boolean changeAttributeType(String name, TaskAttributeType newType) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = findAttribute(name);

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, newType, EventCause.NOT_ALLOWED, this));
				return false;
			}
			if (attribute == null) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(null, newType, EventCause.NOT_EXISTS, this));
				return false;
			}
			if (attribute.data.getType().fixed || newType.fixed) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, newType, EventCause.NOT_ALLOWED, this));
				return false;
			}

			TaskAttributeType oldType = attribute.data.getType();
			attribute.createRequirement(newType);
			EventManager.fireEvent(new AttributeTypeChangedEvent(attribute, oldType, this));
			return true;

		} else {
			return false;
		}
	}




	/**
	 * Updates a variable of a given task with a new value <p>
	 * Events <p>
	 * - AttributeUpdatedRejection: when the type could not be changed (NOT_ALLOWED = values are locked / attribute is fixed, NOT_EXISTS = attribute with given name does not exist, INVALID: new value is invalid) <p>
	 * - AttributeUpdatedEvent: when the value was changed <p>
	 *
	 * @return true, if completed successful
	 */
	public boolean updateTaskAttribute(String attributeName, TaskAttributeData.Var var, TaskAttributeValue newValue) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			TaskAttribute attribute = findAttribute(attributeName);

			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, newValue, EventCause.NOT_ALLOWED, this));
				return false;
			}
			if (attribute == null) {
				EventManager.fireEvent(new AttributeUpdatedRejection(null, var, newValue, EventCause.NOT_EXISTS, this));
				return false;
			}

			TaskAttributeData.Var[] changedVars = attribute.data.update(var, newValue);
			if (changedVars != null) {
				EventManager.fireEvent(new AttributeUpdatedEvent(attribute, changedVars, newValue, this));
				return true;
			} else {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, newValue, EventCause.INVALID, this));
				return false;
			}

		} else {
			return false;
		}
	}




	/**
	 * @return all values of the given type in a list
	 */
	public List<TaskAttribute> getAttributes(TaskAttributeType type) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();

			List<TaskAttribute> attributes = new ArrayList<>();
			for (TaskAttribute attribute : project.attributes) {
				if (attribute.data.getType() == type) {
					attributes.add(attribute);
				}
			}

			return attributes;

		} else {
			return new ArrayList<>();
		}
	}




	/**
	 * @return the attribute with the given name or null
	 */
	public TaskAttribute findAttribute(String name) {
		if (Logic.project.isProjectOpen()) {
			Project project = Logic.project.getProject();
			for (TaskAttribute attribute : project.attributes) {
				if (attribute.name.equals(name)) {
					return attribute;
				}
			}
			return null;
		} else {
			return null;
		}
	}


}
