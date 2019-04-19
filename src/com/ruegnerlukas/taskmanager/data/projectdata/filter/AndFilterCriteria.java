package com.ruegnerlukas.taskmanager.data.projectdata.filter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AndFilterCriteria extends FilterCriteria {


	public final ObservableList<FilterCriteria> subCriteria = FXCollections.observableArrayList();




	public AndFilterCriteria() {
		super(CriteriaType.AND);
	}

}
