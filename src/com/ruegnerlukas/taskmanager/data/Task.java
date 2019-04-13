package com.ruegnerlukas.taskmanager.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Task {


	public ObservableMap<TaskAttribute, Object> attributes = FXCollections.observableHashMap();




	public <T> T getValue(TaskAttribute attribute, Class<T> type) {
		return (T) attributes.get(attribute);
	}


}
