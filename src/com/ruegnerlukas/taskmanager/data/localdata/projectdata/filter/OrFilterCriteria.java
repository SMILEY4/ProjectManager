package com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrFilterCriteria extends FilterCriteria {


	public final ObservableList<FilterCriteria> subCriteria = FXCollections.observableArrayList();




	public OrFilterCriteria() {
		super(CriteriaType.OR);
	}

}