package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AttributeContentNode extends AnchorPane {


	public static final Map<AttributeType, Class<? extends AttributeContentNode>> CONTENT_NODES;




	static {
		Map<AttributeType, Class<? extends AttributeContentNode>> map = new HashMap<>();
		map.put(AttributeType.ID, UnchangeableContentNode.class);
		map.put(AttributeType.DESCRIPTION, UnchangeableContentNode.class);
		map.put(AttributeType.CREATED, UnchangeableContentNode.class);
		map.put(AttributeType.LAST_UPDATED, UnchangeableContentNode.class);
		map.put(AttributeType.FLAG, FlagContentNode.class);
		map.put(AttributeType.BOOLEAN, BooleanContentNode.class);
		map.put(AttributeType.NUMBER, NumberContentNode.class);
		map.put(AttributeType.TEXT, TextContentNode.class);
		map.put(AttributeType.CHOICE, ChoiceContentNode.class);
		map.put(AttributeType.DATE, DateContentNode.class);
		map.put(AttributeType.DEPENDENCY, UnchangeableContentNode.class);
		CONTENT_NODES = Collections.unmodifiableMap(map);
	}




	public final TaskAttribute attribute;
	public final SimpleBooleanProperty changedProperty = new SimpleBooleanProperty(false);




	public AttributeContentNode(TaskAttribute attribute) {
		this.attribute = attribute;
	}




	public abstract double getContentHeight();




	protected void saveValues(Map<AttributeValueType, AttributeValue<?>> values) {
		for (AttributeValueType type : attribute.values.keySet()) {
			AttributeValue value = values.get(type);
			if (value != null) {
				AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, value, true);
			}
		}
	}




	protected void discardValues(Map<AttributeValueType, AttributeValue<?>> map) {
		for (AttributeValueType type : attribute.values.keySet()) {
			map.put(type, attribute.getValue(type));
		}
	}




	/**
	 * @return true, if both values all given values are equal to the values of the attribute
	 * */
	protected boolean compareValues(Map<AttributeValueType, AttributeValue<?>> values) {
		for (AttributeValueType type : attribute.values.keySet()) {
			if(type.equals(AttributeValueType.CARD_DISPLAY_TYPE)) {
				continue;

			}
			AttributeValue<?> valueAttribute = attribute.getValue(type);
			AttributeValue<?> valueMap = values.get(type);
			if(!valueAttribute.equals(valueMap)) {
				return false;
			}
		}
		return true;
	}




	public void dispose() {
		// unused
	}

}
