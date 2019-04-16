package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
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




	protected void saveValues(Map<String, Object> values) {
		for (String key : attribute.values.keySet()) {
			Object value = values.get(key);
			if (value != null) {
				AttributeLogic.setTaskAttributeValue(attribute, key, value);
			}
		}
	}




	protected void discardValues(Map<String, Object> map) {
		for (String key : attribute.values.keySet()) {
			Object value = attribute.getValue(key, Object.class);
			map.put(key, value);
		}
	}




	protected boolean compareValues(Map<String, Object> values) {
		for (String key : attribute.values.keySet()) {
			Object valueAttribute = attribute.getValue(key, Object.class);
			Object valueMap = values.get(key);
			if (valueAttribute instanceof Object[]) {
				Object[] arrayAttribute = (Object[]) valueAttribute;
				Object[] arrayMap = (Object[]) valueMap;
				if (arrayAttribute.length != arrayMap.length) {
					return false;
				}
				for (int i = 0; i < arrayAttribute.length; i++) {
					if (!arrayAttribute[i].equals(arrayMap[i])) {
						return false;
					}
				}

			} else {
				if (valueAttribute == null || !valueAttribute.equals(valueMap)) {
					return false;
				}
			}
		}
		return true;
	}


}
