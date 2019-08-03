package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.ObservableMap;

public interface ITaskAttribute {


	int getID();

	CustomProperty<String> getName();

	CustomProperty<AttributeType> getType();

	ObservableMap<AttributeValueType, AttributeValue<?>> getValues();

	AttributeValue<?> getValue(AttributeValueType type);

}
