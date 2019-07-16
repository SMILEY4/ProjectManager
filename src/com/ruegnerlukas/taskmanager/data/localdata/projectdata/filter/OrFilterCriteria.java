package com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrFilterCriteria extends FilterCriteria {


	public final List<FilterCriteria> subCriteria;





	public OrFilterCriteria(FilterCriteria... subCriteria) {
		super(CriteriaType.OR);
		this.subCriteria = Collections.unmodifiableList(Arrays.asList(subCriteria));
	}




	public OrFilterCriteria(List<FilterCriteria> subCriteria) {
		super(CriteriaType.OR);
		this.subCriteria = Collections.unmodifiableList(new ArrayList<>(subCriteria));
	}

}
