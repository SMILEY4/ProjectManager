package com.ruegnerlukas.taskmanager.data.projectdata;

import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttribute {


	public final SimpleStringProperty name = new SimpleStringProperty();
	public final CustomProperty<AttributeType> type = new CustomProperty<>();
	public final ObservableMap<String, Object> values = FXCollections.observableHashMap();




	public TaskAttribute(String name, AttributeType type) {
		this.name.set(name);
		this.type.set(type);
	}




	public <T> T getValue(String key, Class<? extends T> type) {
		return (T) values.get(key);
	}

}
