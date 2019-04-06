package com.ruegnerlukas.taskmanager.data.attributes;

import com.ruegnerlukas.taskmanager.utils.observables.AttributeTypeProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttribute {


	public enum Type {
		ID("ID", true),
		DESCRIPTION("Description", true),
		FLAG("Flag", true),
		TEXT("Text", false),
		NUMBER("Number", false),
		BOOLEAN("Boolean", false);

		public final String display;
		public final boolean fixed;




		Type(String display, boolean fixed) {
			this.display = display;
			this.fixed = fixed;
		}




		public static Type[] getFixedTypes() {
			int n = 0;
			for (Type type : Type.values()) {
				if (type.fixed) n++;
			}
			Type[] types = new Type[n];
			for (int i = 0, j = 0; i < Type.values().length; i++) {
				if (Type.values()[i].fixed) types[j++] = Type.values()[i];
			}
			return types;
		}




		public static Type[] getFreeTypes() {
			int n = 0;
			for (Type type : Type.values()) {
				if (!type.fixed) n++;
			}
			Type[] types = new Type[n];
			for (int i = 0, j = 0; i < Type.values().length; i++) {
				if (!Type.values()[i].fixed) types[j++] = Type.values()[i];
			}
			return types;
		}
	}






	public final SimpleStringProperty name = new SimpleStringProperty();
	public final AttributeTypeProperty type = new AttributeTypeProperty();
	public final ObservableMap<String, Object> values = FXCollections.observableHashMap();




	public TaskAttribute(String name, Type type) {
		this.name.set(name);
		this.type.set(type);
		initValues();
	}




	public void initValues() {
		switch (this.type.get()) {
			case ID: {
				IDAttributeAccess.initAttribute(this);
				break;
			}
			case DESCRIPTION: {
				DescriptionAttributeAccess.initAttribute(this);
				break;
			}
			case BOOLEAN: {
				BooleanAttributeAccess.initAttribute(this);
				break;
			}
			case NUMBER: {
				NumberAttributeAccess.initAttribute(this);
				break;
			}
			case TEXT: {
				TextAttributeAccess.initAttribute(this);
				break;
			}
		}
	}




	public <T> T getValue(String key, Class<? extends T> type) {
		return (T) values.get(key);
	}

}
