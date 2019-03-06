package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.BoolValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.FloatValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.IntegerValueItem;

import java.util.HashMap;
import java.util.Map;

public class NumberDataNode extends DataNode {


	private IntegerValueItem itemDecPlaces;
	private FloatValueItem itemMin;
	private FloatValueItem itemMax;
	private BoolValueItem itemUseDefault;
	private FloatValueItem itemDefault;




	public NumberDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems(attribute);
		createListener(attribute);
	}




	private void createItems(TaskAttribute attribute) {
		NumberAttributeData data = (NumberAttributeData) attribute.data;

		itemDecPlaces = new IntegerValueItem("Decimal Places:", data.decPlaces, 0, 99, 1) {
			@Override
			public void onChanged() {
				int decPlaces = itemDecPlaces.getValue();

				itemMin.setDecPlaces(decPlaces);
				itemMin.setStep(Math.pow(10, -decPlaces));
				itemMin.setValue(MathUtils.setDecPlaces(itemMin.getValue(), decPlaces));

				itemMax.setDecPlaces(decPlaces);
				itemMax.setStep(Math.pow(10, -decPlaces));
				itemMax.setValue(MathUtils.setDecPlaces(itemMax.getValue(), decPlaces));

				itemDefault.setDecPlaces(decPlaces);
				itemDefault.setStep(Math.pow(10, -decPlaces));
				itemDefault.setValue(MathUtils.setDecPlaces(itemDefault.getValue(), decPlaces));

				onChange();
			}
		};

		itemMin = new FloatValueItem("Min Value:", data.min, Integer.MIN_VALUE, data.max, Math.pow(10, -data.decPlaces), data.decPlaces) {
			@Override
			public void onChanged() {
				itemMax.setRange(itemMin.getValue(), Integer.MAX_VALUE);
				itemDefault.setRange(itemMin.getValue(), itemMax.getValue());
				onChange();
			}
		};

		itemMax = new FloatValueItem("Max Value:", data.max, data.min, Integer.MAX_VALUE, Math.pow(10, -data.decPlaces), data.decPlaces) {
			@Override
			public void onChanged() {
				itemMin.setRange(Integer.MIN_VALUE, itemMax.getValue());
				itemDefault.setRange(itemMin.getValue(), itemMax.getValue());
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

		itemDefault = new FloatValueItem("Default Value:", data.defaultValue, data.min, data.max, Math.pow(10, -data.decPlaces), data.decPlaces) {
			@Override
			public void onChanged() {
				onChange();
			}
		};

		itemDefault.setDisable(!itemUseDefault.getValue());

		addVaueItem(itemDecPlaces);
		addVaueItem(itemMin);
		addVaueItem(itemMax);
		addVaueItem(itemUseDefault);
		addVaueItem(itemDefault);
	}




	private void createListener(TaskAttribute attribute) {
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute && event.getAttribute().data.getType() == TaskAttributeType.NUMBER) {
				NumberAttributeData data = (NumberAttributeData) attribute.data;
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES)) {
					itemDecPlaces.setValue(data.decPlaces);
					itemDecPlaces.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.NUMBER_ATT_MIN)) {
					itemMin.setValue(data.min);
					itemMin.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.NUMBER_ATT_MAX)) {
					itemMax.setValue(data.max);
					itemMax.setChanged(false);
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
		NumberAttributeData data = (NumberAttributeData) getAttribute().data;
		int nDiffs = 0;
		if (data.decPlaces != itemDecPlaces.getValue()) {
			nDiffs++;
		}
		if (data.min != itemMin.getValue()) {
			nDiffs++;
		}
		if (data.max != itemMax.getValue()) {
			nDiffs++;
		}
		if (data.useDefault != itemUseDefault.getValue()) {
			nDiffs++;
		}
		if (data.defaultValue != itemDefault.getValue()) {
			nDiffs++;
		}
		NumberDataNode.this.getAttributeNode().setChanged(nDiffs != 0);
	}




	@Override
	public void writeChanges() {
		Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap = new HashMap<>();
		valuesMap.put(TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES, new NumberValue(itemDecPlaces.getValue()));
		valuesMap.put(TaskAttributeData.Var.NUMBER_ATT_MIN, new NumberValue(itemMin.getValue()));
		valuesMap.put(TaskAttributeData.Var.NUMBER_ATT_MAX, new NumberValue(itemMax.getValue()));
		valuesMap.put(TaskAttributeData.Var.USE_DEFAULT, new BoolValue(itemUseDefault.getValue()));
		valuesMap.put(TaskAttributeData.Var.DEFAULT_VALUE, new NumberValue(itemDefault.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, valuesMap);
		itemDecPlaces.setChanged(false);
		itemMin.setChanged(false);
		itemMax.setChanged(false);
		itemUseDefault.setChanged(false);
		itemDefault.setChanged(false);
		onChange();
	}




	@Override
	public void discardChanges() {
		NumberAttributeData data = (NumberAttributeData) getAttribute().data;

		itemDecPlaces.setValue(data.decPlaces);
		itemDecPlaces.setChanged(false);

		itemMin.setDecPlaces(data.decPlaces);
		itemMin.setRange(Integer.MIN_VALUE, data.max);
		itemMin.setValue(data.min);
		itemMin.setChanged(false);

		itemMax.setDecPlaces(data.decPlaces);
		itemMax.setRange(data.min, Integer.MAX_VALUE);
		itemMax.setValue(data.max);
		itemMax.setChanged(false);

		itemUseDefault.setValue(data.useDefault);
		itemUseDefault.setChanged(false);

		itemDefault.setDecPlaces(data.decPlaces);
		itemDefault.setRange(data.min, data.max);
		itemDefault.setValue(data.defaultValue);
		itemDefault.setChanged(false);
		onChange();
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}


}
