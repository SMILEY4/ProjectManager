package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.simpleutils.RandomUtils;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.CreatedValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.time.LocalDateTime;
import java.util.*;

public class CreatedAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<LocalDateTime> COMPARATOR_ASC = LocalDateTime::compareTo;
	private final Comparator<LocalDateTime> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected CreatedAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{LocalDateTime.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{LocalDateTime.class, LocalDateTime.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{LocalDateTime.class, LocalDateTime.class});
		FILTER_DATA = Collections.unmodifiableMap(mapData);
	}




	@Override
	public Map<FilterOperation, Class<?>[]> getFilterData() {
		return FILTER_DATA;
	}




	@Override
	public Comparator getComparatorAsc() {
		return COMPARATOR_ASC;
	}




	@Override
	public Comparator getComparatorDesc() {
		return COMPARATOR_DESC;
	}




	public TaskAttribute createAttribute() {
		return createAttribute("CreatedAttribute " + RandomUtils.generateRandomHexString(8));
	}




	public TaskAttribute createAttribute(String name) {
		TaskAttribute attribute = new TaskAttribute(name, AttributeType.CREATED);
		this.initAttribute(attribute);
		return attribute;
	}




	public void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
	}




	public boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((CreatedValue) valueTask).getValue().compareTo((LocalDateTime) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDateTime && filterValues.get(1) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDateTime min = (LocalDateTime) filterValues.get(0);
						LocalDateTime max = (LocalDateTime) filterValues.get(1);
						LocalDateTime time = ((CreatedValue) valueTask).getValue();
						return min.compareTo(time) <= 0 && max.compareTo(time) >= 0;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof LocalDateTime && filterValues.get(1) instanceof LocalDateTime) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						LocalDateTime min = (LocalDateTime) filterValues.get(0);
						LocalDateTime max = (LocalDateTime) filterValues.get(1);
						LocalDateTime time = ((CreatedValue) valueTask).getValue();
						return !(min.compareTo(time) <= 0 && max.compareTo(time) >= 0);
					}
				} else {
					return false;
				}
			}

			default: {
				return false;
			}
		}

	}




	public boolean isValidTaskValue(TaskAttribute attribute, TaskValue<?> value) {
		return value.getAttType() == AttributeType.CREATED;
	}




	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}


}
