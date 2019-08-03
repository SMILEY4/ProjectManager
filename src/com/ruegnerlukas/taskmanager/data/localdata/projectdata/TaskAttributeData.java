package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class TaskAttributeData implements ITaskAttribute {


	public final int id;

	private CustomProperty<String> name = new CustomProperty<>();
	private CustomProperty<AttributeType> type = new CustomProperty<>();
	private ObservableMap<AttributeValueType, AttributeValue<?>> values;




	public TaskAttributeData(int id, AttributeType type) {
		this.id = id;
		this.type.set(type);
		values = FXCollections.observableHashMap();
	}




	@Override
	public int getID() {
		return 0;
	}




	@Override
	public CustomProperty<String> getName() {
		return this.name;
	}




	@Override
	public CustomProperty<AttributeType> getType() {
		return this.type;
	}




	@Override
	public ObservableMap<AttributeValueType, AttributeValue<?>> getValues() {
		return this.values;
	}




	@Override
	public AttributeValue<?> getValue(AttributeValueType type) {
		return values.get(type);
	}

}
