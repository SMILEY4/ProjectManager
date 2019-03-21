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
import com.ruegnerlukas.taskmanager.logic.attributes.filter.*;
import com.ruegnerlukas.taskmanager.logic.attributes.updaters.*;
import com.ruegnerlukas.taskmanager.logic.attributes.validation.*;

import java.util.*;

public class AttributeLogic {


	//======================//
	//       INTERNAL       //
	//======================//


	public static final Map<TaskAttributeType, AttributeFilter> FILTER_MAP;
	public static final Map<TaskAttributeType, AttributeUpdater> UPDATER_MAP;
	public static final Map<TaskAttributeType, AttributeValidator> VALIDATOR_MAP;




	static {
		Map<TaskAttributeType, AttributeFilter> filterMap = new HashMap<>();
		filterMap.put(TaskAttributeType.BOOLEAN, new BoolAttributeFilter());
		filterMap.put(TaskAttributeType.CHOICE, new ChoiceAttributeFilter());
		filterMap.put(TaskAttributeType.DESCRIPTION, new DescriptionAttributeFilter());
		filterMap.put(TaskAttributeType.FLAG, new FlagAttributeFilter());
		filterMap.put(TaskAttributeType.ID, new IDAttributeFilter());
		filterMap.put(TaskAttributeType.NUMBER, new NumberAttributeFilter());
		filterMap.put(TaskAttributeType.TEXT, new TextAttributeFilter());
		filterMap.put(TaskAttributeType.DEPENDENCY, new DependencyAttributeFilter());
		FILTER_MAP = Collections.unmodifiableMap(filterMap);

		Map<TaskAttributeType, AttributeUpdater> updaterMap = new HashMap<>();
		updaterMap.put(TaskAttributeType.BOOLEAN, new BoolAttributeUpdater());
		updaterMap.put(TaskAttributeType.CHOICE, new ChoiceAttributeUpdater());
		updaterMap.put(TaskAttributeType.DESCRIPTION, new DescriptionAttributeUpdater());
		updaterMap.put(TaskAttributeType.FLAG, new FlagAttributeUpdater());
		updaterMap.put(TaskAttributeType.ID, new IDAttributeUpdater());
		updaterMap.put(TaskAttributeType.NUMBER, new NumberAttributeUpdater());
		updaterMap.put(TaskAttributeType.TEXT, new TextAttributeUpdater());
		updaterMap.put(TaskAttributeType.DEPENDENCY, new DependencyAttributeUpdater());
		UPDATER_MAP = Collections.unmodifiableMap(updaterMap);

		Map<TaskAttributeType, AttributeValidator> validatorMap = new HashMap<>();
		validatorMap.put(TaskAttributeType.BOOLEAN, new BoolAttributeValidation());
		validatorMap.put(TaskAttributeType.CHOICE, new ChoiceAttributeValidation());
		validatorMap.put(TaskAttributeType.DESCRIPTION, new DescriptionAttributeValidation());
		validatorMap.put(TaskAttributeType.FLAG, new FlagAttributeValidation());
		validatorMap.put(TaskAttributeType.ID, new IDAttributeValidation());
		validatorMap.put(TaskAttributeType.NUMBER, new NumberAttributeValidation());
		validatorMap.put(TaskAttributeType.TEXT, new BoolAttributeValidation());
		validatorMap.put(TaskAttributeType.DEPENDENCY, new DependencyAttributeValidation());
		VALIDATOR_MAP = Collections.unmodifiableMap(validatorMap);

	}




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




	protected AttributeValidator getAttributeValidator(TaskAttributeType type) {
		return VALIDATOR_MAP.get(type);
	}


	protected AttributeUpdater getAttributeUpdater(TaskAttributeType type) {
		return UPDATER_MAP.get(type);
	}


	//======================//
	//        GETTER        //
	//======================//




	/**
	 * Request a list of all attributes
	 */
	public Response<List<TaskAttribute>> getAttributes() {
		Project project = Logic.project.getProject();
		if (project != null) {
			return new Response<List<TaskAttribute>>().complete(new ArrayList<>(project.attributes));
		} else {
			return new Response<List<TaskAttribute>>().complete(new ArrayList<>(), Response.State.FAIL);
		}
	}




	/**
	 * request the attribute with the given name
	 */
	public Response<TaskAttribute> getAttribute(String name) {
		TaskAttribute attribute = findAttribute(name);
		return new Response<TaskAttribute>()
				.complete(attribute, attribute == null ? Response.State.FAIL : Response.State.SUCCESS);
	}




	/**
	 * request all attributes of the given type
	 */
	public Response<List<TaskAttribute>> getAttributes(TaskAttributeType type, Request<List<TaskAttribute>> request) {
		List<TaskAttribute> attributes = findAttributes(type);
		return new Response<List<TaskAttribute>>()
				.complete(attributes, (attributes == null || attributes.isEmpty()) ? Response.State.FAIL : Response.State.SUCCESS);
	}




	/**
	 * request the first attribute of the given type
	 */
	public Response<TaskAttribute> getAttribute(TaskAttributeType type) {
		List<TaskAttribute> attributes = findAttributes(type);
		if (attributes != null && !attributes.isEmpty()) {
			return new Response<TaskAttribute>().complete(attributes.get(0));
		} else {
			return new Response<TaskAttribute>().complete(null, Response.State.FAIL);
		}
	}




	/**
	 * Checks whether the attributes of the current project are locked
	 */
	public Response<Boolean> getAttributeLock() {
		Project project = Logic.project.getProject();
		if (project != null) {
			return new Response<Boolean>().complete(project.attributesLocked);
		} else {
			return new Response<Boolean>().complete(null, Response.State.FAIL);
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

			// createItem attribute
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
	 * NOT_EXISTS = given name does not exist) <p>
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

			} else if (attribute.data.getType().type == TaskAttributeType.Type.FIXED) {
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
	 * NOT_UNIQUE = given new name already exists, NOT_EXISTS = attribute with given name does not exist) <p>
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

			} else if (attributeOld.data.getType().type == TaskAttributeType.Type.FIXED) {
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
	 * NOT_EXISTS = attribute with given name does not exist, UNKNOWN = error when creating new attributeData with given type) <p>
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

			} else if (attribute.data.getType().type == TaskAttributeType.Type.FIXED || type.type == TaskAttributeType.Type.FIXED) {
				EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.NOT_ALLOWED, this));


			} else {
				TaskAttributeType prevType = attribute.data.getType();
				boolean valid = attribute.createNewData(type);
				if (valid) {
					EventManager.fireEvent(new AttributeTypeChangedEvent(attribute, prevType, this));
				} else {
					EventManager.fireEvent(new AttributeTypeChangedRejection(attribute, type, EventCause.UNKNOWN, this));
				}
			}
		}

	}




	public void updateTaskAttribute(String name, TaskAttributeData.Var var, TaskAttributeValue value) {
		HashMap<TaskAttributeData.Var, TaskAttributeValue> map = new HashMap<>();
		updateTaskAttribute(name, map);
	}




	public void updateTaskAttribute(String name, Map<TaskAttributeData.Var, TaskAttributeValue> values) {

		Project project = Logic.project.getProject();
		if (project != null) {

			// find attribute
			TaskAttribute attribute = findAttribute(name);

			// try to update variable
			if (attribute == null) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, values, EventCause.NOT_EXISTS, this));

			} else if (project.attributesLocked) {
				EventManager.fireEvent(new AttributeUpdatedRejection(attribute, values, EventCause.NOT_ALLOWED, this));

			} else {

				AttributeUpdater updater = UPDATER_MAP.get(attribute.data.getType());
				Map<TaskAttributeData.Var, TaskAttributeValue> changedVars = new HashMap<>();

				for (Map.Entry<TaskAttributeData.Var, TaskAttributeValue> entry : values.entrySet()) {

					boolean valid = updater.update(attribute.data, entry.getKey(), entry.getValue(), changedVars);

					if (!valid) {
						HashMap<TaskAttributeData.Var, TaskAttributeValue> invalidPair = new HashMap<>();
						invalidPair.put(entry.getKey(), entry.getValue());
						EventManager.fireEvent(new AttributeUpdatedRejection(attribute, invalidPair, EventCause.INVALID, this));
					}

				}

				EventManager.fireEvent(new AttributeUpdatedEvent(attribute, changedVars, this));

			}

		}

	}


}
