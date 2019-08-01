package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;

public class RawFilterCriteria {


	public FilterCriteria.CriteriaType type;




	public static FilterCriteria fromRaw(RawFilterCriteria rawCriteria, Project project) {
		switch (rawCriteria.type) {
			case TERMINAL:
				return RawFilterTerminal.fromRaw((RawFilterTerminal) rawCriteria, project);
			case AND:
				return RawFilterAnd.fromRaw((RawFilterAnd) rawCriteria, project);
			case OR:
				return RawFilterOr.fromRaw((RawFilterOr) rawCriteria, project);
			default:
				return null;
		}
	}

}
