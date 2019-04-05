package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

import java.util.Map;

public abstract class AttributeContentNode extends AnchorPane {


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
				Logic.get().setTaskAttributeValue(attribute, key, value);
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
			if (valueAttribute == null || !valueAttribute.equals(valueMap)) {
				return false;
			}
		}
		return true;
	}


}
