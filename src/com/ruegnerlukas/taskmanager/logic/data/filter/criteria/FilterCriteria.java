package com.ruegnerlukas.taskmanager.logic.data.filter.criteria;

import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.TaskAttributeValue;

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
		CONTAINS_NOT("contains not")
		;

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




	public static ComparisonOp[] getPossibleComparisionOps(TaskAttribute attribute) {
		if(attribute == null) {
			return new ComparisonOp[]{
				ComparisonOp.EQUALITY
			};
		}

		if(attribute.data.getType() == TaskAttributeType.BOOLEAN) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY
			};
		}
		if(attribute.data.getType() == TaskAttributeType.CHOICE) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.IN_LIST,
					ComparisonOp.NOT_IN_LIST,
					ComparisonOp.CONTAINS,
					ComparisonOp.CONTAINS_NOT
			};
		}
		if(attribute.data.getType() == TaskAttributeType.DESCRIPTION) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.CONTAINS,
					ComparisonOp.CONTAINS_NOT
			};
		}
		if(attribute.data.getType() == TaskAttributeType.FLAG) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.IN_LIST,
					ComparisonOp.NOT_IN_LIST,
					ComparisonOp.CONTAINS,
					ComparisonOp.CONTAINS_NOT
			};
		}
		if(attribute.data.getType() == TaskAttributeType.ID) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.IN_LIST,
					ComparisonOp.NOT_IN_LIST,
					ComparisonOp.GREATER_THAN,
					ComparisonOp.GREATER_THAN_EQUAL,
					ComparisonOp.LESS_THAN,
					ComparisonOp.LESS_THAN_EQUAL,
					ComparisonOp.IN_RANGE,
					ComparisonOp.NOT_IN_RANGE
			};
		}
		if(attribute.data.getType() == TaskAttributeType.NUMBER) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.IN_LIST,
					ComparisonOp.NOT_IN_LIST,
					ComparisonOp.GREATER_THAN,
					ComparisonOp.GREATER_THAN_EQUAL,
					ComparisonOp.LESS_THAN,
					ComparisonOp.LESS_THAN_EQUAL,
					ComparisonOp.IN_RANGE,
					ComparisonOp.NOT_IN_RANGE
			};
		}
		if(attribute.data.getType() == TaskAttributeType.TEXT) {
			return new ComparisonOp[] {
					ComparisonOp.EQUALITY,
					ComparisonOp.INEQUALITY,
					ComparisonOp.CONTAINS,
					ComparisonOp.CONTAINS_NOT
			};
		}

		return new ComparisonOp[]{
				ComparisonOp.EQUALITY
		};
	}


}
