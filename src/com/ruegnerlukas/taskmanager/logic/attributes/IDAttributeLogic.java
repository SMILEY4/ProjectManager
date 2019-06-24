package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class IDAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<Integer> COMPARATOR_ASC = Integer::compareTo;
	private final Comparator<Integer> COMPARATOR_DESC = (x, y) -> x.compareTo(y) * -1;




	protected IDAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.NOT_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.GREATER_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_THAN, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.LESS_EQUALS, new Class<?>[]{Integer.class});
		mapData.put(FilterOperation.IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
		mapData.put(FilterOperation.NOT_IN_RANGE, new Class<?>[]{Integer.class, Integer.class});
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




	public void initAttribute(TaskAttribute attribute) {
		attribute.values.clear();
	}




	public boolean matchesFilter(Task task, TerminalFilterCriteria criteria) {

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute.get());
		List<Object> filterValues = criteria.values;

		switch (criteria.operation.get()) {

			case EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) == 0;
					}
				} else {
					return false;
				}
			}

			case NOT_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) != 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) > 0;
					}
				} else {
					return false;
				}
			}

			case GREATER_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) >= 0;
					}
				} else {
					return false;
				}
			}

			case LESS_THAN: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) < 0;
					}
				} else {
					return false;
				}
			}

			case LESS_EQUALS: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						return ((IDValue) valueTask).getValue().compareTo((Integer) filterValues.get(0)) <= 0;
					}
				} else {
					return false;
				}
			}

			case IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Integer && filterValues.get(1) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						int min = (Integer) filterValues.get(0);
						int max = (Integer) filterValues.get(1);
						int id = ((IDValue) valueTask).getValue();
						return min <= id && max >= id;
					}
				} else {
					return false;
				}
			}

			case NOT_IN_RANGE: {
				if (filterValues.size() == 2 && filterValues.get(0) instanceof Integer && filterValues.get(1) instanceof Integer) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						int min = (Integer) filterValues.get(0);
						int max = (Integer) filterValues.get(1);
						int id = ((IDValue) valueTask).getValue();
						return !(min <= id && max >= id);
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
		return value.getAttType() == AttributeType.ID;
	}




	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return null;
	}


}
