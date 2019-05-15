package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes.DataNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.BoolValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.IntegerValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.MLTextValueItem;

import java.util.HashMap;
import java.util.Map;

public class TextDataNode extends DataNode {


	private IntegerValueItem itemCharLimit;
	private BoolValueItem itemMultiline;
	private IntegerValueItem itemNumLines;
	private BoolValueItem itemUseDefault;
	private MLTextValueItem itemDefault;




	public TextDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems(attribute);
		createListener(attribute);
	}




	private void createItems(TaskAttribute attribute) {
		TextAttributeData data = (TextAttributeData) attribute.data;

		itemCharLimit = new IntegerValueItem("Character Limit:", data.charLimit, 0, Integer.MAX_VALUE, 1) {
			@Override
			public void onChanged() {
				if (itemDefault.getValue().length() > itemCharLimit.getValue()) {
					itemDefault.setValue(itemDefault.getValue().substring(0, itemCharLimit.getValue()));
				}
				onChange();
			}
		};

		itemMultiline = new BoolValueItem("Multiline:", data.multiline) {
			@Override
			public void onChanged() {
				itemNumLines.setDisable(!itemMultiline.getValue());
				if (itemMultiline.getValue()) {
					itemDefault.setNumLines(itemNumLines.getValue());
				} else {
					itemDefault.setNumLines(1);
				}
				onChange();
			}
		};

		itemNumLines = new IntegerValueItem("Expected Number of Lines:", data.nLinesExpected, 1, Integer.MAX_VALUE, 1) {
			@Override
			public void onChanged() {
				itemDefault.setNumLines(itemNumLines.getValue());
				onChange();
			}
		};
		itemNumLines.setDisable(!itemMultiline.getValue());

		itemUseDefault = new BoolValueItem("Use Default:", data.useDefault) {
			@Override
			public void onChanged() {
				itemDefault.setDisable(!itemUseDefault.getValue());
				onChange();
			}
		};

		itemDefault = new MLTextValueItem("Default Value:", data.defaultValue, "", data.nLinesExpected) {
			@Override
			public void onChanged() {
				if (itemDefault.getValue().length() > itemCharLimit.getValue()) {
					itemDefault.setValue(itemDefault.getValue().substring(0, itemCharLimit.getValue()));
				}
				onChange();
			}
		};
		itemDefault.setNumLines(itemMultiline.getValue() ? data.nLinesExpected : 1);
		itemDefault.setDisable(!itemUseDefault.getValue());

		addVaueItem(itemCharLimit);
		addVaueItem(itemMultiline);
		addVaueItem(itemNumLines);
		addVaueItem(itemUseDefault);
		addVaueItem(itemDefault);
	}




	private void createListener(TaskAttribute attribute) {
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute && event.getAttribute().data.getType() == TaskAttributeType.BOOLEAN) {
				TextAttributeData data = (TextAttributeData) attribute.data;
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.TEXT_CHAR_LIMIT)) {
					itemCharLimit.setValue(data.charLimit);
					itemCharLimit.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.TEXT_MULTILINE)) {
					itemMultiline.setValue(data.multiline);
					itemMultiline.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.TEXT_N_LINES_EXP)) {
					itemNumLines.setValue(data.nLinesExpected);
					itemNumLines.setChanged(false);
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




	private void onChange() {
		TextAttributeData data = (TextAttributeData) getAttribute().data;
		int nDiffs = 0;
		if (data.charLimit != itemCharLimit.getValue()) {
			nDiffs++;
		}
		if (data.multiline != itemMultiline.getValue()) {
			nDiffs++;
		}
		if (data.nLinesExpected != itemNumLines.getValue()) {
			nDiffs++;
		}
		if (data.useDefault != itemUseDefault.getValue()) {
			nDiffs++;
		}
		if (!data.defaultValue.equals(itemDefault.getValue())) {
			nDiffs++;
		}
		TextDataNode.this.getAttributeNode().setChanged(nDiffs != 0);
	}




	@Override
	public void writeChanges() {
		Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap = new HashMap<>();
		valuesMap.put(TaskAttributeData.Var.TEXT_CHAR_LIMIT, new NumberValue(itemCharLimit.getValue()));
		valuesMap.put(TaskAttributeData.Var.TEXT_MULTILINE, new BoolValue(itemMultiline.getValue()));
		valuesMap.put(TaskAttributeData.Var.TEXT_N_LINES_EXP, new NumberValue(itemNumLines.getValue()));
		valuesMap.put(TaskAttributeData.Var.USE_DEFAULT, new BoolValue(itemUseDefault.getValue()));
		valuesMap.put(TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(itemDefault.getValue()));

		if (warningOnSave(valuesMap)) {
			Logic.attribute.updateTaskAttribute(getAttribute().name, valuesMap);
			itemCharLimit.setChanged(false);
			itemMultiline.setChanged(false);
			itemNumLines.setChanged(false);
			itemUseDefault.setChanged(false);
			itemDefault.setChanged(false);
			onChange();
		}

	}




	@Override
	public void discardChanges() {
		TextAttributeData data = (TextAttributeData) getAttribute().data;
		itemCharLimit.setValue(data.charLimit);
		itemCharLimit.setChanged(false);
		itemMultiline.setValue(data.multiline);
		itemMultiline.setChanged(false);
		itemNumLines.setValue(data.nLinesExpected);
		itemNumLines.setChanged(false);
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
