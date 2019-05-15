package com.ruegnerlukas.taskmanager_old.data.filter;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;

public class FilterCriteria {


	public enum ComparisonOp {
		EQUALITY("equal"),
		INEQUALITY("not equal"),
		LESS_THAN("less than"),
		LESS_THAN_EQUAL("less equal"),
		GREATER_THAN("greater than"),
		GREATER_THAN_EQUAL("greater equal"),
		IN_RANGE("in range"),
		NOT_IN_RANGE("not in range"),
		IN_LIST("in list"),
		NOT_IN_LIST("not in list"),
		CONTAINS("contains"),
		CONTAINS_NOT("contains not"),
		IS_DEPENDENT_ON_FILTER("dependent on"),
		IS_PREREQUISITE_OF_FILTER("prerequisite of"),
		IS_INDEPENDENT("independent");

		public final String display;




		ComparisonOp(String display) {
			this.display = display;
		}

	}






	public TaskAttribute attribute;
	public ComparisonOp comparisonOp;
	public TaskAttributeValue comparisionValue;




	public FilterCriteria(TaskAttribute attribute, ComparisonOp comparisonOp, TaskAttributeValue comparisionValue) {
		this.attribute = attribute;
		this.comparisonOp = comparisonOp;
		this.comparisionValue = comparisionValue;
	}


}
