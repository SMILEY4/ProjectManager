package com.ruegnerlukas.taskmanager.data.projectdata.filter;

public class FilterCriteria {


	public enum CriteriaType {
		TERMINAL,
		AND,
		OR
	}






	public final CriteriaType type;




	public FilterCriteria(CriteriaType type) {
		this.type = type;
	}


}
