package com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AndFilterCriteria extends FilterCriteria {


	public final List<FilterCriteria> subCriteria;




	public AndFilterCriteria(FilterCriteria... subCriteria) {
		super(CriteriaType.AND);
		this.subCriteria = Collections.unmodifiableList(Arrays.asList(subCriteria));
	}




	public AndFilterCriteria(List<FilterCriteria> subCriteria) {
		super(CriteriaType.AND);
		this.subCriteria = Collections.unmodifiableList(new ArrayList<>(subCriteria));
	}

}
