package com.ruegnerlukas.taskmanager.logic.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.*;

public class DependencyAttributeLogic implements AttributeLogicModule {


	private final Map<FilterOperation, Class<?>[]> FILTER_DATA;

	private final Comparator<Task[]> COMPARATOR_ASC = Comparator.comparingInt(x -> x.length);
	private final Comparator<Task[]> COMPARATOR_DESC = (x, y) -> Integer.compare(x.length, y.length) * -1;




	protected DependencyAttributeLogic() {
		Map<FilterOperation, Class<?>[]> mapData = new HashMap<>();
		mapData.put(FilterOperation.DEPENDENT_ON, new Class<?>[]{Task.class});
		mapData.put(FilterOperation.PREREQUISITE_OF, new Class<?>[]{Task.class});
		mapData.put(FilterOperation.INDEPENDENT, new Class<?>[]{Boolean.class});
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

		TaskValue<?> valueTask = TaskLogic.getValueOrDefault(task, criteria.attribute);
		List<Object> filterValues = criteria.values;

		switch (criteria.operation) {

			case DEPENDENT_ON: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Task) {
					if (valueTask.getAttType() == null) {
						return false;
					} else {
						Task tasksFilter = (Task) filterValues.get(0);
						List<Task> list = Arrays.asList((Task[]) ((DependencyValue) valueTask).getValue());
						return list.contains(tasksFilter);
					}
				} else {
					return false;
				}
			}

			case PREREQUISITE_OF: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Task) {
					Task tasksFilter = (Task) filterValues.get(0);
					TaskValue<?> valueFilter = TaskLogic.getValueOrDefault(tasksFilter, criteria.attribute);
					if (valueFilter.getAttType() != null) {
						List<Task> list = Arrays.asList(((DependencyValue) valueFilter).getValue());
						if (list.contains(task)) {
							return true;
						}
					}
					return false;
				}
			}

			case INDEPENDENT: {
				if (filterValues.size() == 1 && filterValues.get(0) instanceof Boolean) {
					boolean isIndependent = (boolean) filterValues.get(0);
					if (valueTask.getAttType() == null) {
						return isIndependent;
					} else {
						if (isIndependent) {
							return ((DependencyValue) valueTask).getValue().length == 0;
						} else {
							return ((DependencyValue) valueTask).getValue().length != 0;
						}
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
		return value.getAttType() == AttributeType.DEPENDENCY || value.getAttType() == null;
	}




	public TaskValue<?> generateValidTaskValue(TaskValue<?> oldValue, TaskAttribute attribute, boolean preferNoValue) {
		return new NoValue();
	}

}
