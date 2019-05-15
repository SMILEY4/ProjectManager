package com.ruegnerlukas.taskmanager.data.projectdata.filter;

public enum FilterOperation {

	HAS_VALUE, // including default value
	EQUALS,
	EQUALS_IGNORE_CASE,
	NOT_EQUALS,
	GREATER_THAN,
	GREATER_EQUALS,
	LESS_THAN,
	LESS_EQUALS,
	IN_RANGE,
	NOT_IN_RANGE,
	CONTAINS,
	CONTAINS_NOT,
	DEPENDENT_ON,
	PREREQUISITE_OF,
	INDEPENDENT,
	NOT_INDEPENDENT

}
