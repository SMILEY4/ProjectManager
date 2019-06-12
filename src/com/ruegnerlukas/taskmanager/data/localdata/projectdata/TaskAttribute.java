package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttribute {


	public final SimpleStringProperty name = new SimpleStringProperty();
	public final CustomProperty<AttributeType> type = new CustomProperty<>();
	public final ObservableMap<AttributeValueType, AttributeValue<?>> values = FXCollections.observableHashMap();




	public TaskAttribute(String name, AttributeType type) {
		this.name.set(name);
		this.type.set(type);


	}




	public AttributeValue<?> getValue(AttributeValueType key) {
		return values.get(key);
	}

}
