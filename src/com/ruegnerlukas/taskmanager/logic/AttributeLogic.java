package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeLogic {


	//======================//
	//       INTERNAL       //
	//======================//




	protected TaskAttribute findAttribute(String name) {
		return findAttribute(name, false);
	}




	protected TaskAttribute findAttribute(String name, boolean ignoreCase) {
		Project project = Logic.project.getProject();
		if (project != null) {
			for (int i = 0; i < project.attributes.size(); i++) {
				TaskAttribute attribute = project.attributes.get(i);
				if (ignoreCase) {
					if (attribute.name.equalsIgnoreCase(name)) {
						return attribute;
					}
				} else {
					if (attribute.name.equals(name)) {
						return attribute;
					}
				}
			}
			return null;
		} else {
			return null;
		}
	}




	protected TaskAttribute findAttribute(TaskAttributeType type) {
		Project project = Logic.project.getProject();
		if (project != null) {
			for (int i = 0; i < project.attributes.size(); i++) {
				TaskAttribute attribute = project.attributes.get(i);
				if (attribute.data.getType() == type) {
					return attribute;
				}
			}
			return null;
		} else {
			return null;
		}
	}




	protected List<TaskAttribute> findAttributes(TaskAttributeType type) {
		Project project = Logic.project.getProject();
		if (project != null) {
			List<TaskAttribute> attributes = new ArrayList<>();
			for (int i = 0; i < project.attributes.size(); i++) {
				TaskAttribute attribute = project.attributes.get(i);
				if (attribute.data.getType() == type) {
					attributes.add(attribute);
				}
			}
			return attributes;
		} else {
			return null;
		}
	}


	//======================//
	//        GETTER        //
	//======================//




	/**
	 * request the attribute with the given name
	 */
	public void getAttribute(String name, Request request) {
		TaskAttribute attribute = findAttribute(name);
		if (attribute != null) {
			request.onResponse(new Response<>(Response.State.SUCCESS, attribute));
		} else {
			request.onResponse(new Response<TaskAttribute>(Response.State.FAIL, "Attribute '" + name + "' not found.", null));
		}
	}




	/**
	 * request all attributes of the given type
	 */
	public void getAttributes(TaskAttributeType type, Request request) {
		List<TaskAttribute> attributes = findAttributes(type);
		if (attributes != null && !attributes.isEmpty()) {
			request.onResponse(new Response<>(Response.State.SUCCESS, attributes));
		} else {
			request.onResponse(new Response<List<TaskAttribute>>(Response.State.FAIL, "No attributes with type '" + type + "' found.", null));
		}
	}


	//======================//
	//        SETTER        //
	//======================//




	/**
	 * Locks all TaskAttributes and prevents any changes<p>
	 * Events: <p>
	 * - AttributeLockEvent: when the lock was changed
	 */
	public void setAttributeLock(boolean lockAttributes) {
		Project project = Logic.project.getProject();
		if (project != null) {
			final boolean lockPrev = project.attributesLocked;
			if (lockPrev != lockAttributes) {
				project.attributesLocked = lockAttributes;
				EventManager.fireEvent(new AttributeLockEvent(lockPrev, lockAttributes, this));
			}
		}
	}




	/**
	 * Creates a new TaskAttribute with the given name and type <p>
	 * Events: <p>
	 * - AttributeCreatedRejection: when the attribute could not be created (NOT_ALLOWED = values are locked, NOT_UNIQUE = given name is not unique) <p>
	 * - AttributeCreatedEvent: when the attribute was created
	 */
	public void createAttribute(String name, TaskAttributeType type) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// create attribute
			TaskAttribute attribute = new TaskAttribute(name, type);

			// try to add attribute to project
			if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeCreatedRejection(attribute, EventCause.NOT_ALLOWED, this));

			} else if (findAttribute(name) != null) {
				EventManager.fireEvent(new AttributeCreatedRejection(attribute, EventCause.NOT_UNIQUE, this));

			} else {
				project.attributes.add(attribute);
				EventManager.fireEvent(new AttributeCreatedEvent(attribute, this));
			}

		}

	}




	/**
	 * Deletes the attribute with the given name<p>
	 * Events: <p>
	 * - AttributeRemovedRejection: when the attribute could not be deleted (NOT_ALLOWED = values are locked / attribute is fixed,
	 * 		NOT_EXISTS = given name does not exist) <p>
	 * - AttributeRemovedEvent: when the attribute was deleted
	 */
	public void deleteAttribute(String name) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// find attribute
			TaskAttribute attribute = findAttribute(name);

			// try to delete attribute
			if (attribute == null) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_EXISTS, this));

			} else if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_ALLOWED, this));

			} else if (attribute.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRemovedRejection(attribute, EventCause.NOT_ALLOWED, this));

			} else {
				project.attributes.remove(attribute);
				EventManager.fireEvent(new AttributeRemovedEvent(attribute, this));
			}

		}

	}




	/**
	 * renames the attribute with the given name to the new name <p>
	 * Events: <p>
	 * - AttributeRenamedRejection: when the attribute could not be renamed (NOT_ALLOWED = values are locked / attribute is fixed,
	 * 		NOT_UNIQUE = given new name already exists, NOT_EXISTS = attribute with given name does not exist) <p>
	 * - AttributeRenamedEvent: when the attribute was renamed
	 */
	public void renameAttribute(String oldName, String newName) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// find attributes
			TaskAttribute attributeOld = findAttribute(oldName);
			TaskAttribute attributeNew = findAttribute(newName);

			// try to rename attribute
			if (attributeOld == null) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_EXISTS, this));

			} else if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));

			} else if (attributeOld.data.getType().fixed) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_ALLOWED, this));

			} else if (attributeNew != null) {
				EventManager.fireEvent(new AttributeRenamedRejection(attributeOld, newName, EventCause.NOT_UNIQUE, this));

			} else {
				attributeOld.name = newName;
				EventManager.fireEvent(new AttributeRenamedEvent(attributeOld, newName, this));
			}

		}

	}




	/**
	 * changes the attribute-type of the given attribute to the new type <p>
	 * Events <p>
	 * - AttributeTypeChangedRejection: when the type could not be changed (NOT_ALLOWED = values are locked / attribute is fixed / new attribute is fixed,
	 * 		NOT_EXISTS = attribute with given name does not exist, UNKNOWN = error when creating new attributeData with given type) <p>
	 * - AttributeTypeChangedEvent: when the type was changed <p>
	 */
	public void setAttributeType(String name, TaskAttributeType type) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// find attributes
			TaskAttribute attribute = findAttribute(name);

			// try to set type of attribute
			if (attribute == null) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.NOT_EXISTS, this));

			} else if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.NOT_ALLOWED, this));

			} else if (attribute.data.getType().fixed || type.fixed) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.NOT_ALLOWED, this));

			} else {
				TaskAttributeType prevType = attribute.data.getType();
				boolean valid = attribute.createNewData(type);
				if(valid) {
					EventManager.fireEvent(new AttributeTypeChangedEvent(attribute, prevType, this));
				} else {
					EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.UNKNOWN, this));
				}
			}
		}

	}




	/**
	 * Updates a variable of a given task with a new value <p>
	 * Events <p>
	 * - AttributeUpdatedRejection: when the type could not be changed (NOT_ALLOWED = values are locked / attribute is fixed,
	 * 		NOT_EXISTS = attribute with given name does not exist, INVALID: new value / variable is invalid) <p>
	 * - AttributeUpdatedEvent: when the value was changed <p>
	 */
	public void updateTaskAttribute(String name, TaskAttributeData.Var var, TaskAttributeValue value) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// find attribute
			TaskAttribute attribute = findAttribute(name);

			// try to update variable
			if (attribute == null) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, value, EventCause.NOT_EXISTS, this));

			} else if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, value, EventCause.NOT_ALLOWED, this));

			} else if (attribute.data.getType().fixed) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, value, EventCause.NOT_ALLOWED, this));

			} else {


				Map<TaskAttributeData.Var, TaskAttributeValue> changedVars = attribute.data.update(var, value);
				if (changedVars == null || changedVars.isEmpty()) {
					EventManager.fireEvent(new AttributeUpdatedRejection(attribute, var, value, EventCause.INVALID, this));
				} else {
					EventManager.fireEvent(new AttributeUpdatedEvent(attribute, changedVars, value, this));
				}

			}

		}

	}


}
