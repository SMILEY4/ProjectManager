package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import javafx.event.EventHandler;

import java.util.*;

public class AttributeLogic {


	public static final BooleanAttributeLogic BOOLEAN_LOGIC = new BooleanAttributeLogic();
	public static final ChoiceAttributeLogic CHOICE_LOGIC = new ChoiceAttributeLogic();
	public static final CreatedAttributeLogic CREATED_LOGIC = new CreatedAttributeLogic();
	public static final DateAttributeLogic DATE_LOGIC = new DateAttributeLogic();
	public static final DependencyAttributeLogic DEPENDENCY_LOGIC = new DependencyAttributeLogic();
	public static final DescriptionAttributeLogic DESCRIPTION_LOGIC = new DescriptionAttributeLogic();
	public static final IDAttributeLogic ID_LOGIC = new IDAttributeLogic();
	public static final LastUpdatedAttributeLogic LAST_UPDATED_LOGIC = new LastUpdatedAttributeLogic();
	public static final NumberAttributeLogic NUMBER_LOGIC = new NumberAttributeLogic();
	public static final TaskFlagAttributeLogic FLAG_LOGIC = new TaskFlagAttributeLogic();
	public static final TextAttributeLogic TEXT_LOGIC = new TextAttributeLogic();

	public static final Map<AttributeType, AttributeLogicModule> LOGIC_MODULES;




	static {
		Map<AttributeType, AttributeLogicModule> map = new HashMap<>();
		map.put(AttributeType.BOOLEAN, BOOLEAN_LOGIC);
		map.put(AttributeType.CHOICE, CHOICE_LOGIC);
		map.put(AttributeType.CREATED, CREATED_LOGIC);
		map.put(AttributeType.DATE, DATE_LOGIC);
		map.put(AttributeType.DEPENDENCY, DEPENDENCY_LOGIC);
		map.put(AttributeType.DESCRIPTION, DESCRIPTION_LOGIC);
		map.put(AttributeType.ID, ID_LOGIC);
		map.put(AttributeType.LAST_UPDATED, LAST_UPDATED_LOGIC);
		map.put(AttributeType.NUMBER, NUMBER_LOGIC);
		map.put(AttributeType.FLAG, FLAG_LOGIC);
		map.put(AttributeType.TEXT, TEXT_LOGIC);
		LOGIC_MODULES = Collections.unmodifiableMap(map);
	}




	public static TaskAttribute createTaskAttribute(AttributeType type) {
		return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()));
	}




	public static TaskAttribute createTaskAttribute(AttributeType type, String name) {
		return LOGIC_MODULES.get(type).createAttribute(name);
	}




	public static void initTaskAttribute(TaskAttribute attribute) {
		LOGIC_MODULES.get(attribute.type.get()).initAttribute(attribute);
	}




	public static String renameTaskAttribute(Project project, TaskAttribute attribute, String newName) {
		for (TaskAttribute attrib : project.data.attributes) {
			if (attrib == attribute) {
				continue;
			}
			if (attrib.name.get().equalsIgnoreCase(newName)) {
				return attribute.name.get();
			}
		}
		attribute.name.set(newName);
		return attribute.name.get();
	}




	public static void setTaskAttributeType(Project project, TaskAttribute attribute, AttributeType newType) {
		LOGIC_MODULES.get(newType).initAttribute(attribute);
		attribute.type.set(newType);

		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskLogic.setValue(project, task, attribute, null);
		}
	}




	public static boolean setAttributeValue(Project project, TaskAttribute attribute, AttributeValue<?> value, boolean preferNoValueTask) {

		// validate value
		// TODO ?

		// set value
		AttributeValue<?> prevValue = attribute.values.get(value.getType());
		attribute.values.put(value.getType(), value);
		onAttributeValueChanged(attribute, value.getType(), prevValue, value);

		// check tasks for changes
		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskValue<?> taskValue = TaskLogic.getTaskValue(task, attribute);
			if (!LOGIC_MODULES.get(attribute.type.get()).isValidTaskValue(attribute, taskValue)) {
				TaskValue<?> validValue = LOGIC_MODULES.get(attribute.type.get()).generateValidTaskValue(taskValue, attribute, preferNoValueTask);
				TaskLogic.setValue(project, task, attribute, validValue);
			}
		}

		return true;
	}




	private static final List<EventHandler<AttributeValueChangeEvent>> valueChangedHandlers = new ArrayList<>();




	public static void addOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.add(handler);
	}




	public static void removeOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.remove(handler);
	}




	private static void onAttributeValueChanged(TaskAttribute attribute, AttributeValueType type, AttributeValue<?> prevValue, AttributeValue<?> newValue) {
		AttributeValueChangeEvent event = new AttributeValueChangeEvent(attribute, type, prevValue, newValue);
		for (EventHandler<AttributeValueChangeEvent> handler : valueChangedHandlers) {
			handler.handle(event);
		}
	}




	public static TaskAttribute findAttribute(Project project, String name) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.name.get().equals(name)) {
				return attribute;
			}
		}
		return null;
	}




	public static TaskAttribute findAttribute(Project project, AttributeType type) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				return attribute;
			}
		}
		return null;
	}




	public static List<TaskAttribute> findAttributes(Project project, AttributeType type) {
		List<TaskAttribute> list = new ArrayList<>();
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				list.add(attribute);
			}
		}
		return list;
	}




	public static boolean getUsesDefault(TaskAttribute attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	public static TaskValue<?> getDefaultValue(TaskAttribute attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return value.getValue();
		}
	}




	public static void setShowOnTaskCard(TaskAttribute attribute, boolean showOnCard) {
		CardDisplayTypeValue prev = (CardDisplayTypeValue) attribute.values.get(AttributeValueType.CARD_DISPLAY_TYPE);
		CardDisplayTypeValue value = new CardDisplayTypeValue(showOnCard);
		attribute.values.put(AttributeValueType.CARD_DISPLAY_TYPE, value);
		onAttributeValueChanged(attribute, AttributeValueType.CARD_DISPLAY_TYPE, prev, value);
	}




	public static boolean getShowOnTaskCard(TaskAttribute attribute) {
		CardDisplayTypeValue value = (CardDisplayTypeValue) attribute.values.get(AttributeValueType.CARD_DISPLAY_TYPE);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	public static boolean isValidFilterOperation(Task task, TerminalFilterCriteria criteria) {
		Map<FilterOperation, Class<?>[]> FILTER_DATA = LOGIC_MODULES.get(criteria.attribute.get().type.get()).getFilterData();

		// is invalid operation
		if (!FILTER_DATA.containsKey(criteria.operation.get())) {
			return false;
		}

		// has invalid amount of values
		Class<?>[] dataTypes = FILTER_DATA.get(criteria.operation.get());
		if (dataTypes.length != criteria.values.size()) {
			return false;
		}

		// in invalid datatype
		for (int i = 0; i < dataTypes.length; i++) {
			Class<?> expected = dataTypes[i];
			Class<?> actual = criteria.values.get(i).getClass();
			if (actual != expected) {
				return false;
			}
		}

		return true;
	}

}
