package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.BoolValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.ChoiceValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.TextValueItem;

import java.util.HashMap;
import java.util.Map;

public class ChoiceDataNode extends DataNode {


	private TextValueItem itemValues;
	private BoolValueItem itemUseDefault;
	private ChoiceValueItem itemDefault;




	public ChoiceDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems(attribute);
		createListener(attribute);
	}




	private void createItems(TaskAttribute attribute) {
		ChoiceAttributeData data = (ChoiceAttributeData) attribute.data;

		itemValues = new TextValueItem("Values (CS):", String.join(", ", data.values), "Comma ',' Separated Values") {
			@Override
			public void onChanged() {
				itemDefault.setChoices(getValuesCleaned());
				itemDefault.setChanged(false);
				onChange();
			}
		};
		itemUseDefault = new BoolValueItem("Use Default:", data.useDefault) {
			@Override
			public void onChanged() {
				itemDefault.setDisable(!itemUseDefault.getValue());
				onChange();
			}
		};
		itemDefault = new ChoiceValueItem("Default Value:", data.defaultValue, data.values) {
			@Override
			public void onChanged() {
				onChange();
			}
		};

		itemDefault.setDisable(!itemUseDefault.getValue());

		addVaueItem(itemValues);
		addVaueItem(itemUseDefault);
		addVaueItem(itemDefault);
	}




	private void createListener(TaskAttribute attribute) {
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute && event.getAttribute().data.getType() == TaskAttributeType.CHOICE) {
				ChoiceAttributeData data = (ChoiceAttributeData) attribute.data;
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.CHOICE_ATT_VALUES)) {
					itemValues.setValue(String.join(", ", data.values));
					itemDefault.setChoices(data.values);
					itemDefault.setValue(data.defaultValue);
					itemDefault.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.USE_DEFAULT)) {
					itemUseDefault.setValue(data.useDefault);
					itemUseDefault.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.DEFAULT_VALUE)) {
					itemDefault.setValue(data.defaultValue);
					itemDefault.setChanged(false);
				}
				onChange();
			}
		}, AttributeUpdatedEvent.class);
	}




	private String[] getValuesCleaned() {
		int nEmpty = 0;
		String[] values = itemValues.getValue().split(",");
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
			if (values[i].isEmpty()) {
				nEmpty++;
			}
		}

		String[] valuesCleaned = new String[values.length - nEmpty];
		for (int i = 0, j = 0; i < values.length; i++) {
			if (!values[i].isEmpty()) {
				valuesCleaned[j++] = values[i];
			}
		}

		return valuesCleaned;
	}




	private void onChange() {
		ChoiceAttributeData data = (ChoiceAttributeData) getAttribute().data;
		int nDiffs = 0;

		if (!String.join(", ", data.values).equals(String.join(", ", getValuesCleaned()))) {
			nDiffs++;
		}
		if (data.useDefault != itemUseDefault.getValue()) {
			nDiffs++;
		}
		if (!data.defaultValue.equals(itemDefault.getValue())) {
			nDiffs++;
		}

		ChoiceDataNode.this.getAttributeNode().setChanged(nDiffs != 0);
	}




	@Override
	public void writeChanges() {
		Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap = new HashMap<>();
		valuesMap.put(TaskAttributeData.Var.CHOICE_ATT_VALUES, new TextArrayValue(getValuesCleaned()));
		valuesMap.put(TaskAttributeData.Var.USE_DEFAULT, new BoolValue(itemUseDefault.getValue()));
		valuesMap.put(TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(itemDefault.getValue()));

		if (warningOnSave(valuesMap)) {
			Logic.attribute.updateTaskAttribute(getAttribute().name, valuesMap);
			itemUseDefault.setChanged(false);
			itemDefault.setChanged(false);
			onChange();
		}

	}




	@Override
	public void discardChanges() {
		ChoiceAttributeData data = (ChoiceAttributeData) getAttribute().data;
		itemValues.setValue(String.join(", ", data.values));
		itemValues.setChanged(false);
		itemUseDefault.setValue(data.useDefault);
		itemUseDefault.setChanged(false);
		itemDefault.setValue(data.defaultValue);
		itemDefault.setChanged(false);
		onChange();
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}


}
