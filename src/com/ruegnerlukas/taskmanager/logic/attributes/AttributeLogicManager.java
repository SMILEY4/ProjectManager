package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AttributeLogicManager {


	public static final Map<AttributeType, Class<?>> LOGIC_CLASSES = new HashMap<>();




	static {
		LOGIC_CLASSES.put(AttributeType.BOOLEAN, BooleanAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.CHOICE, ChoiceAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.CREATED, CreatedAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.DATE, DateAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.DEPENDENCY, DependencyAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.DESCRIPTION, DescriptionAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.ID, IDAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.LAST_UPDATED, LastUpdatedAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.NUMBER, NumberAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.FLAG, TaskFlagAttributeLogic.class);
		LOGIC_CLASSES.put(AttributeType.TEXT, TextAttributeLogic.class);
	}


//
//
//	public static String[] validateLogicClasses() {
//
//		List<String> errors = new ArrayList<>();
//
//
//		// all classes exist and are registered
//		for (AttributeType type : AttributeType.values()) {
//			if (!LOGIC_CLASSES.containsKey(type)) {
//				errors.add("ERR - Logic class for type " + type + " does not exist / is not registered.");
//			}
//		}
//
//
//		// all classes are valid
//		for (AttributeType type : AttributeType.values()) {
//			Class<?> logicClass = LOGIC_CLASSES.get(type);
//			if (logicClass == null) {
//				continue;
//			}
//			List<Field> fields = new ArrayList<>(Arrays.asList(logicClass.getFields()));
//			List<Method> methods = new ArrayList<>(Arrays.asList(logicClass.getMethods()));
//
//
//			// all classes have final field "DATA_TYPES"
//			boolean hasField_dataTypes = false;
//			for (Field field : fields) {
//				if ("DATA_TYPES".equals(field.getName()) && Map.class == field.getType() && Modifier.isFinal(field.getModifiers())) {
//					hasField_dataTypes = true;
//					try {
//						Map<String, Class<?>> map = (Map<String, Class<?>>) field.get(null);
//						if (!map.containsKey(TaskAttribute.ATTRIB_TASK_VALUE_TYPE)) {
//							errors.add("ERROR - Logic class of type " + type + " is missing the \"ATTRIB_TASK_VALUE_TYPE\" entry in \"DATA_TYPES\"");
//						}
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					}
//					break;
//				}
//			}
//			if (!hasField_dataTypes) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the field \"final DATA_TYPES:Map<String,Class<?>>\"");
//			}
//
//
//			// all classes have final field "COMPARATOR_ASC"
//			boolean hasField_comparatorAsc = false;
//			for (Field field : fields) {
//				if ("COMPARATOR_ASC".equals(field.getName()) && Comparator.class == field.getType() && Modifier.isFinal(field.getModifiers())) {
//					hasField_comparatorAsc = true;
//					break;
//				}
//			}
//			if (!hasField_comparatorAsc) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the field \"final COMPARATOR_ASC:Comparator\"");
//			}
//
//
//			// all classes have final field "COMPARATOR_DESC"
//			boolean hasField_comparatorDesc = false;
//			for (Field field : fields) {
//				if ("COMPARATOR_DESC".equals(field.getName()) && Comparator.class == field.getType() && Modifier.isFinal(field.getModifiers())) {
//					hasField_comparatorDesc = true;
//					break;
//				}
//			}
//			if (!hasField_comparatorDesc) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the field \"final COMPARATOR_DESC:Comparator\"");
//			}
//
//
//			// all classes have final field "FILTER_DATA"
//			boolean hasField_filterData = false;
//			for (Field field : fields) {
//				if ("FILTER_DATA".equals(field.getName()) && Map.class == field.getType() && Modifier.isFinal(field.getModifiers())) {
//					hasField_filterData = true;
//					break;
//				}
//			}
//			if (!hasField_filterData) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the field \"final FILTER_DATA:Map<FilterOperation,Class[]>\"");
//			}
//
//
//			// all classes have method "createAttribute():TaskAttribute"
//			boolean hasMethod_createAttribute = false;
//			for (Method method : methods) {
//				if ("createAttribute".equals(method.getName()) && method.getParameterCount() == 0 && method.getReturnType() == TaskAttribute.class) {
//					hasMethod_createAttribute = true;
//					break;
//				}
//			}
//			if (!hasMethod_createAttribute) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"createAttribute():TaskAttribute\"");
//			}
//
//
//			// all classes have method "createAttribute(String):TaskAttribute"
//			boolean hasMethod_createNamedAttribute = false;
//			for (Method method : methods) {
//				if ("createAttribute".equals(method.getName()) && method.getParameterCount() == 1
//						&& method.getParameterTypes()[0] == String.class && method.getReturnType() == TaskAttribute.class) {
//					hasMethod_createNamedAttribute = true;
//					break;
//				}
//			}
//			if (!hasMethod_createNamedAttribute) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"createAttribute(String):TaskAttribute\"");
//			}
//
//
//			// all classes have method "initAttribute(TaskAttribute):void"
//			boolean hasMethod_initAttribute = false;
//			for (Method method : methods) {
//				if ("initAttribute".equals(method.getName()) && method.getParameterCount() == 1
//						&& method.getParameterTypes()[0] == TaskAttribute.class && method.getReturnType() == Void.TYPE) {
//					hasMethod_initAttribute = true;
//					break;
//				}
//			}
//			if (!hasMethod_initAttribute) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"initAttribute(TaskAttribute):void\"");
//			}
//
//
//			// all classes have method "matchesFilter(Task,TerminalFilterCriteria):boolean"
//			boolean hasMethod_matchesFilter = false;
//			for (Method method : methods) {
//				if ("matchesFilter".equals(method.getName()) && method.getParameterCount() == 2
//						&& method.getParameterTypes()[0] == Task.class
//						&& method.getParameterTypes()[1] == TerminalFilterCriteria.class
//						&& method.getReturnType() == boolean.class) {
//					hasMethod_matchesFilter = true;
//					break;
//				}
//			}
//			if (!hasMethod_matchesFilter) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"matchesFilter(Task,TerminalFilterCriteria):boolean\"");
//			}
//
//
//			// all classes have method "isValidTaskValue(TaskAttribute,TaskValue):boolean"
//			boolean hasMethod_isValidTaskValue = false;
//			for (Method method : methods) {
//				if ("isValidTaskValue".equals(method.getName()) && method.getParameterCount() == 2
//						&& method.getParameterTypes()[0] == TaskAttribute.class
//						&& method.getParameterTypes()[1] == TaskValue.class
//						&& method.getReturnType() == boolean.class) {
//					hasMethod_isValidTaskValue = true;
//					break;
//				}
//			}
//			if (!hasMethod_isValidTaskValue) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"isValidTaskValue(TaskAttribute,Object):boolean\"");
//			}
//
//
//			// all classes have method "generateValidTaskValue(TaskValue,TaskAttribute,boolean):Object"
//			boolean hasMethod_generateValidValue = false;
//			for (Method method : methods) {
//				if ("generateValidTaskValue".equals(method.getName()) && method.getParameterCount() == 3
//						&& method.getParameterTypes()[0] == TaskValue.class
//						&& method.getParameterTypes()[1] == TaskAttribute.class
//						&& method.getParameterTypes()[2] == boolean.class
//						&& method.getReturnType() != Void.class) {
//					hasMethod_generateValidValue = true;
//					break;
//				}
//			}
//			if (!hasMethod_generateValidValue) {
//				errors.add("ERROR - Logic class of type " + type + " is missing the method \"generateValidTaskValue(Object,TaskAttribute,boolean):Object\"");
//			}
//
//		}
//
//
//		// print errors / status
//		if (!errors.isEmpty()) {
//			String[] errorArray = new String[errors.size()];
//			for (int i = 0; i < errors.size(); i++) {
//				errorArray[i] = errors.get(i);
//			}
//			return errorArray;
//		} else {
//			return new String[]{"AttributeLogic - OK"};
//		}
//
//
//	}
//
//


	public static Map<String, Class<?>> getDataTypeMap(AttributeType type) {
		Field field = getField(getLogicClass(type), "DATA_TYPES");
		return (Map<String, Class<?>>) getFieldValue(field, null);
	}




	public static Map<FilterOperation, Class<?>[]> getFilterData(AttributeType type) {
		Field field = getField(getLogicClass(type), "FILTER_DATA");
		return (Map<FilterOperation, Class<?>[]>) getFieldValue(field, null);
	}




	public static Comparator getComparatorAsc(AttributeType type) {
		Field field = getField(getLogicClass(type), "COMPARATOR_ASC");
		return (Comparator) getFieldValue(field, null);
	}




	public static Comparator getComparatorDesc(AttributeType type) {
		Field field = getField(getLogicClass(type), "COMPARATOR_DESC");
		return (Comparator) getFieldValue(field, null);
	}




	public static TaskAttribute createTaskAttribute(AttributeType type) {
		Method method = getMethod(getLogicClass(type), "createAttribute", new Class<?>[0]);
		try {
			return (TaskAttribute) method.invoke(null, new Object[0]);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}




	public static TaskAttribute createTaskAttribute(AttributeType type, String name) {
		Method method = getMethod(getLogicClass(type), "createAttribute", new Class<?>[]{String.class});
		try {
			return (TaskAttribute) method.invoke(null, name);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}




	public static void initTaskAttribute(TaskAttribute attribute) {
		initTaskAttribute(attribute, attribute.type.get());
	}




	public static void initTaskAttribute(TaskAttribute attribute, AttributeType type) {
		Method method = getMethod(getLogicClass(type), "initAttribute", TaskAttribute.class);
		try {
			method.invoke(null, attribute);
		} catch (IllegalAccessException | InvocationTargetException e) {
		}
	}




	public static boolean isValidFilterOperation(Task task, TerminalFilterCriteria criteria) {
		Field field = getField(getLogicClass(criteria.attribute.get().type.get()), "FILTER_DATA");
		try {
			Map<FilterOperation, Class<?>[]> FILTER_DATA = (Map<FilterOperation, Class<?>[]>) field.get(null);
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
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}




	public static boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {
		Method method = getMethod(getLogicClass(criteria.attribute.get().type.get()), "matchesFilter", Task.class, TerminalFilterCriteria.class);
		try {
			return (Boolean) method.invoke(null, task, criteria);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return false;
		}
	}




	public static boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value) {
		Method method = getMethod(getLogicClass(attribute.type.get()), "isValidTaskValue", TaskAttribute.class, TaskValue.class);
		try {
			return (Boolean) method.invoke(null, attribute, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return false;
		}
	}




	public static TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		Method method = getMethod(getLogicClass(attribute.type.get()), "generateValidTaskValue", TaskValue.class, TaskAttribute.class, boolean.class);
		try {
			return (TaskValue<?>) method.invoke(null, oldValue, attribute, preferNoValue);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}




	public static Class<?> getLogicClass(AttributeType type) {
		return LOGIC_CLASSES.get(type);
	}




	private static Field getField(Class<?> clazz, String name) {
		try {
			return clazz.getField(name);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}




	private static Object getFieldValue(Field field, Object object) {
		try {
			return field.get(object);
		} catch (IllegalAccessException e) {
			return null;
		}
	}




	private static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}


}
