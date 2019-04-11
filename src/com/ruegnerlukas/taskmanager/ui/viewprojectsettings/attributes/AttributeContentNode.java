package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskAttributeLogic;
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
				TaskAttributeLogic.setTaskAttributeValue(attribute, key, value);
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
			if(valueAttribute instanceof Object[]) {
				Object[] arrayAttribute = (Object[])valueAttribute;
				Object[] arrayMap = (Object[])valueMap;
				if(arrayAttribute.length != arrayMap.length) {
					return false;
				}
				for(int i=0; i<arrayAttribute.length; i++) {
					if(!arrayAttribute[i].equals(arrayMap[i])) {
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
