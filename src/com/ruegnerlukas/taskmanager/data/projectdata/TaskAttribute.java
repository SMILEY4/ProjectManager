package com.ruegnerlukas.taskmanager.data.projectdata;

import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttribute {


	public static final String ATTRIB_TASK_VALUE_TYPE = "attrib_task_value_type";
	public static final String ATTRIB_USE_DEFAULT = "attrib_use_default";
	public static final String ATTRIB_DEFAULT_VALUE = "attrib_default_value";
	public static final String ATTRIB_CARD_DISPLAY_TYPE = "attrib_card_display_type";






	public enum CardDisplayType {
		NONE("Don't Show"),
		ICON("Icon"),
		KV_PAIR("Key-Value-Pair"),
		BOTH("Both");

		public final String text;




		CardDisplayType(String text) {
			this.text = text;
		}


	}






	public final SimpleStringProperty name = new SimpleStringProperty();
	public final CustomProperty<AttributeType> type = new CustomProperty<>();
	public final ObservableMap<String, Object> values = FXCollections.observableHashMap();




	public TaskAttribute(String name, AttributeType type) {
		this.name.set(name);
		this.type.set(type);
	}




	public <T> T getValue(String key) {
		return (T) values.get(key);
	}

}
