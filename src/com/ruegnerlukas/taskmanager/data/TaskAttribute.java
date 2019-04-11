package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.utils.observables.AttributeTypeProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttribute {


	public final SimpleStringProperty name = new SimpleStringProperty();
	public final AttributeTypeProperty type = new AttributeTypeProperty();
	public final ObservableMap<String, Object> values = FXCollections.observableHashMap();




	public TaskAttribute(String name, AttributeType type) {
		this.name.set(name);
		this.type.set(type);
	}




	public <T> T getValue(String key, Class<? extends T> type) {
		return (T) values.get(key);
	}

}
