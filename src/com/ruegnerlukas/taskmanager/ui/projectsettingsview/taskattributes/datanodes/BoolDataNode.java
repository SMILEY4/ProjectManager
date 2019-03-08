package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.BoolValueItem;

import java.util.HashMap;
import java.util.Map;

public class BoolDataNode extends DataNode {


	private BoolValueItem itemUseDefault;
	private BoolValueItem itemDefault;




	public BoolDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems(attribute);
		createListener(attribute);
	}




	private void createItems(TaskAttribute attribute) {
		BoolAttributeData data = (BoolAttributeData) attribute.data;

		itemUseDefault = new BoolValueItem("Use Default:", data.useDefault) {
			@Override
			public void onChanged() {
				itemDefault.setDisable(!itemUseDefault.getValue());
				onChange();
			}
		};
		itemDefault = new BoolValueItem("Default Value:", data.defaultValue) {
			@Override
			public void onChanged() {
				onChange();
			}
		};

		itemDefault.setDisable(!itemUseDefault.getValue());

		addVaueItem(itemUseDefault);
		addVaueItem(itemDefault);
	}




	private void createListener(TaskAttribute attribute) {
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute && event.getAttribute().data.getType() == TaskAttributeType.BOOLEAN) {
				BoolAttributeData data = (BoolAttributeData) attribute.data;
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




	private void onChange() {
		BoolAttributeData data = (BoolAttributeData) getAttribute().data;
		int nDiffs = 0;
		if (data.useDefault != itemUseDefault.getValue()) {
			nDiffs++;
		}
		if (data.defaultValue != itemDefault.getValue()) {
			nDiffs++;
		}
		BoolDataNode.this.getAttributeNode().setChanged(nDiffs != 0);
	}




	@Override
	public void writeChanges() {
		Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap = new HashMap<>();
		valuesMap.put(TaskAttributeData.Var.USE_DEFAULT, new BoolValue(itemUseDefault.getValue()));
		valuesMap.put(TaskAttributeData.Var.DEFAULT_VALUE, new BoolValue(itemDefault.getValue()));
		if (warningOnSave(valuesMap)) {
			Logic.attribute.updateTaskAttribute(getAttribute().name, valuesMap);
			itemUseDefault.setChanged(false);
			itemDefault.setChanged(false);
			onChange();
		}
	}




	@Override
	public void discardChanges() {
		BoolAttributeData data = (BoolAttributeData) getAttribute().data;
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
