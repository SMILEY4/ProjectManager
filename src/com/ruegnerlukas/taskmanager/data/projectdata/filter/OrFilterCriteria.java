package com.ruegnerlukas.taskmanager.data.projectdata.filter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrFilterCriteria extends FilterCriteria {


	public final ObservableList<FilterCriteria> criteria = FXCollections.observableArrayList();




	public OrFilterCriteria() {
		super(CriteriaType.OR);
	}

}
