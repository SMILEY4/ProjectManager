package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.utils.AttributeValueChangeEvent;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
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




	/**
	 * Creates a new valid {@link AttributeType} with the given {@link AttributeType} for the given {@link Project}. The new {@link AttributeType} will not be added to the given {@link Project}.
	 *
	 * @return the created {@link AttributeType}
	 */
	public static TaskAttribute createTaskAttribute(AttributeType type, Project project) {
		return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()), project);
	}




	/**
	 * Creates a new valid {@link AttributeType}  with the given name and {@link AttributeType} for the given {@link Project}. The new {@link AttributeType} will not be added to the given {@link Project}.
	 *
	 * @return the created {@link AttributeType}
	 */
	public static TaskAttribute createTaskAttribute(AttributeType type, String name, Project project) {
		final int id = project.settings.attIDCounter.get();
		project.settings.attIDCounter.set(id + 1);
		TaskAttribute attribute = new TaskAttribute(id, type, project);
		initTaskAttribute(attribute);
		attribute.name.set(name);
		return attribute;
	}




	/**
	 * Initializes the given {@link TaskAttributeData}. This will completely reset the attribute to its default state.
	 */
	public static void initTaskAttribute(TaskAttributeData attribute) {
		LOGIC_MODULES.get(attribute.getType().get()).initAttribute(attribute);
	}




	/**
	 * Changes the name of the given {@link TaskAttribute} to the given name.
	 *
	 * @return the new name of the given {@link TaskAttribute}
	 */
	public static String renameTaskAttribute(TaskAttributeData attribute, String newName) {
		attribute.getName().set(newName);
		return attribute.getName().get();
	}




	/**
	 * Changes the type of the given {@link TaskAttribute} to the given {@link AttributeType}. <br>
	 * This will completely reset the given attribute and remove the attribute from all {@link Task}s in the given {@link Project}
	 */
	public static void setTaskAttributeType(Project project, TaskAttribute attribute, AttributeType newType) {
		LOGIC_MODULES.get(newType).initAttribute(attribute);
		attribute.type.set(newType);

		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			TaskLogic.setValue(project, task, attribute, null);
		}
	}




	/**
	 * Set the {@link AttributeValue} of the given {@link TaskAttribute} to the given new value. <br>
	 * This will fire an {@link AttributeValueChangeEvent}. It will also check all {@link Task}s in the given {@link Project}
	 * and set their {@link TaskValue}s to valid values or to {@link NoValue} (depending on "preferNoValueTask" and the behaviour of the type of attribute)
	 *
	 * @return true, if the value was successfully set
	 */
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




	/**
	 * @return a list of all effected tasks and their changed values when setting the new {@link AttributeValue}. This will not actually set the value.
	 */
	public static List<SetAttributeValueEffect> getSetValueEffects(Project project, TaskAttribute attribute, AttributeValue<?> nextAttValue, boolean preferNoValueTask) {
		List<SetAttributeValueEffect> list = new ArrayList<>();

		// copy attribute and set new value
		TaskAttribute attributeCopy = copyAttribute(attribute);
		attributeCopy.values.put(nextAttValue.getType(), nextAttValue);

		AttributeValue<?> prevAttValue = attribute.values.get(nextAttValue.getType());

		// for each task
		List<Task> tasks = project.data.tasks;
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);

			TaskValue<?> prevTaskValue = TaskLogic.getValueOrDefault(task, attribute);


//			if(TaskLogic.getTaskValue(task, attribute) != null && !(TaskLogic.getTaskValue(task, attribute) instanceof NoValue) ) {
//				// case 1: task has own value -> check if value is still valid
//
//				TaskValue<?> prevTaskValue = TaskLogic.getValueOrDefault(task, attribute);
//				TaskValue<?> nextTaskValue = null;
//
//				if (!LOGIC_MODULES.get(attributeCopy.type.get()).isValidTaskValue(attributeCopy, prevTaskValue)) {
//					nextTaskValue = LOGIC_MODULES.get(attributeCopy.type.get()).generateValidTaskValue(prevTaskValue, attributeCopy, preferNoValueTask);
//					if (nextTaskValue == null || nextTaskValue instanceof NoValue) {
//						nextTaskValue = AttributeLogic.getDefaultValue(attributeCopy);
//					}
//
//				}
//
//				list.add(
//						new SetAttributeValueEffect(
//								attributeCopy,
//								prevAttValue,
//								nextAttValue,
//								task,
//								prevTaskValue,
//								nextTaskValue
//						)
//				);
//
//			} else {
//				// case 2: task is using default value (does not have own value) -> check if default changes
//
//				TaskValue<?> prevTaskValue = TaskLogic.getValueOrDefault(task, attribute);
//				TaskValue<?> nextTaskValue = TaskLogic.getValueOrDefault(task, attributeCopy);
//
//				if(prevTaskValue != null && nextTaskValue != null && prevTaskValue.compare(nextTaskValue) != 0) {
//					list.add(
//							new SetAttributeValueEffect(
//									attributeCopy,
//									prevAttValue,
//									nextAttValue,
//									task,
//									prevTaskValue,
//									nextTaskValue
//							)
//					);
//				}
//
//			}


		}

		return list;
	}




	public static TaskAttribute copyAttribute(TaskAttribute attribute) {
		TaskAttribute copy = new TaskAttribute(attribute.id, attribute.type.get(), null);
		copy.values.putAll(attribute.values);
		return copy;
	}




	private static final List<EventHandler<AttributeValueChangeEvent>> valueChangedHandlers = new ArrayList<>();




	/**
	 * Add the given {@link EventHandler} to listen to changes of {@link AttributeValue}s of any {@link TaskAttribute}.
	 */
	public static void addOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.add(handler);
	}




	/**
	 * Remove the given {@link EventHandler}.
	 */
	public static void removeOnAttributeValueChanged(EventHandler<AttributeValueChangeEvent> handler) {
		valueChangedHandlers.remove(handler);
	}




	/**
	 * Sends a {@link AttributeValueChangeEvent} to all listening {@link EventHandler}s
	 */
	private static void onAttributeValueChanged(TaskAttribute attribute, AttributeValueType type, AttributeValue<?> prevValue, AttributeValue<?> newValue) {
		AttributeValueChangeEvent event = new AttributeValueChangeEvent(attribute, type, prevValue, newValue);
		for (EventHandler<AttributeValueChangeEvent> handler : valueChangedHandlers) {
			handler.handle(event);
		}
	}




	/**
	 * Searches the given {@link Project} for a {@link TaskAttribute} with the given id
	 *
	 * @return the found {@link TaskAttribute} or null
	 */
	public static TaskAttribute findAttributeByID(Project project, int id) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.id == id) {
				return attribute;
			}
		}
		return null;
	}




	/**
	 * Searches the given {@link Project} for a {@link TaskAttribute}s with the given name
	 *
	 * @return the FIRST found {@link TaskAttribute} or null
	 */
	public static TaskAttribute findAttributeByName(Project project, String name) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.name.get().equals(name)) {
				return attribute;
			}
		}
		return null;
	}




	/**
	 * Searches the given {@link Project} for any {@link TaskAttribute}s with the given name
	 *
	 * @return a list with all {@link TaskAttribute} with the given name
	 */
	public static List<TaskAttribute> findAttributesByName(Project project, String name) {
		List<TaskAttribute> list = new ArrayList<>();
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.name.get().equals(name)) {
				list.add(attribute);
			}
		}
		return list;
	}




	/**
	 * Searches the given {@link Project} for a {@link TaskAttribute}s with the given {@link AttributeType}
	 *
	 * @return the FIRST found {@link TaskAttribute} or null
	 */
	public static TaskAttribute findAttributeByType(Project project, AttributeType type) {
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				return attribute;
			}
		}
		return null;
	}




	/**
	 * Searches the given {@link Project} for any {@link TaskAttribute}s with the given {@link AttributeType}
	 *
	 * @return a list with all {@link TaskAttribute} with the given {@link AttributeType}
	 */
	public static List<TaskAttribute> findAttributesByType(Project project, AttributeType type) {
		List<TaskAttribute> list = new ArrayList<>();
		for (TaskAttribute attribute : project.data.attributes) {
			if (attribute.type.get() == type) {
				list.add(attribute);
			}
		}
		return list;
	}




	/**
	 * @return true, of the given {@link TaskAttributeData} is using a default value.
	 */
	public static boolean getUsesDefault(TaskAttributeData attribute) {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		if (value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	/**
	 * @return the {@link DefaultValue} of the given {@link TaskAttributeData} or null if the attribute is not using a default value.
	 */
	public static TaskValue<?> getDefaultValue(TaskAttributeData attribute) {
		DefaultValue value = (DefaultValue) attribute.getValue(AttributeValueType.DEFAULT_VALUE);
		if (value == null) {
			return null;
		} else {
			return value.getValue();
		}
	}




	/**
	 * Specify if the given {@link TaskAttributeData} should be shown on the task-cards or not
	 */
	public static void setShowOnTaskCard(TaskAttributeData attribute, boolean showOnCard) {
		CardDisplayTypeValue prev = (CardDisplayTypeValue) attribute.getValues().get(AttributeValueType.CARD_DISPLAY_TYPE);
		CardDisplayTypeValue value = new CardDisplayTypeValue(showOnCard);
		attribute.getValues().put(AttributeValueType.CARD_DISPLAY_TYPE, value);
		if (attribute instanceof TaskAttribute) {
			onAttributeValueChanged((TaskAttribute) attribute, AttributeValueType.CARD_DISPLAY_TYPE, prev, value);
		}
	}




	/**
	 * @return true, if the given {@link TaskAttributeData} shuld be shown on the task-cards.
	 */
	public static boolean getShowOnTaskCard(TaskAttributeData attribute) {
		CardDisplayTypeValue value = (CardDisplayTypeValue) attribute.getValues().get(AttributeValueType.CARD_DISPLAY_TYPE);
		if (value == null) {
			return false;
		} else {
			return value.getValue();
		}
	}




	/**
	 * Checks whether the given {@link TerminalFilterCriteria} is a valid criteria
	 *
	 * @return false, if the operation is invalid / has the wrong amount of values or any invalid datatypes
	 */
	public static boolean isValidFilterOperation(TerminalFilterCriteria criteria) {
		Map<FilterOperation, Class<?>[]> FILTER_DATA = LOGIC_MODULES.get(criteria.attribute.type.get()).getFilterData();

		// is invalid operation
		if (!FILTER_DATA.containsKey(criteria.operation)) {
			return false;
		}

		// has invalid amount of values
		Class<?>[] dataTypes = FILTER_DATA.get(criteria.operation);
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
