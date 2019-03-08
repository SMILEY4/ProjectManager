package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.ChoiceValueItem;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.FlagListValueItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FlagDataNode extends DataNode {


	private FlagListValueItem itemFlags;
	private ChoiceValueItem itemDefault;




	public FlagDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems(attribute);
		createListener(attribute);
	}




	private void createItems(TaskAttribute attribute) {
		FlagAttributeData data = (FlagAttributeData) attribute.data;

		itemFlags = new FlagListValueItem("Flags:", data.flags, data.defaultFlag) {
			@Override
			public void onChanged() {
				TaskFlag[] flags =  itemFlags.getValue();
				TaskFlag flagDefault = null;
				for(TaskFlag flag : flags) {
					if(flag.name.equals(itemDefault.getValue())) {
						flagDefault = flag;
						break;
					}
				}
				itemDefault.setChoices(getAsNames(flags));
				if(flagDefault == null) {
					itemDefault.setValue(flags[0].name);
				} else {
					itemDefault.setValue(flagDefault.name);
				}
				itemFlags.unlockAll();
				itemFlags.lockFlag(flagDefault);
				onChange();
			}
		};

		itemDefault = new ChoiceValueItem("Default Flag:", data.defaultFlag.name, getAsNames(data.flags)) {
			@Override
			public void onChanged() {
				itemFlags.unlockAll();
				for(TaskFlag flag : itemFlags.getValue()) {
					if(flag.name.equals(itemDefault.getValue())) {
						itemFlags.lockFlag(flag);
					}
				}
				onChange();
			}
		};

		addVaueItem(itemFlags);
		addVaueItem(itemDefault);
	}




	private String[] getAsNames(TaskFlag[] flags) {
		String[] names = new String[flags.length];
		for (int i = 0; i < flags.length; i++) {
			names[i] = flags[i].name;
		}
		return names;
	}




	private void createListener(TaskAttribute attribute) {
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute && event.getAttribute().data.getType() == TaskAttributeType.BOOLEAN) {
				FlagAttributeData data = (FlagAttributeData) attribute.data;
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.FLAG_ATT_FLAGS)) {
					itemFlags.setValue(data.flags);
					itemFlags.setChanged(false);
					itemDefault.setChoices(getAsNames(data.flags));
					itemDefault.setValue(data.defaultFlag.name);
					itemDefault.setChanged(false);
				}
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.DEFAULT_VALUE)) {
					itemDefault.setValue(data.defaultFlag.name);
					itemDefault.setChanged(false);
					itemFlags.unlockAll();
					itemFlags.lockFlag(data.defaultFlag);
				}
				onChange();
			}
		}, AttributeUpdatedEvent.class);
	}




	private void onChange() {
		FlagAttributeData data = (FlagAttributeData) getAttribute().data;
		int nDiffs = 0;

		if (!Arrays.equals(data.flags, itemFlags.getValue())) {
			nDiffs++;
		}
		if (!data.defaultFlag.name.equals(itemDefault.getValue())) {
			nDiffs++;
		}
		FlagDataNode.this.getAttributeNode().setChanged(nDiffs != 0);
	}




	@Override
	public void writeChanges() {
		Map<TaskAttributeData.Var, TaskAttributeValue> valuesMap = new HashMap<>();
		TaskFlag flagDefault = null;
		for(TaskFlag flag : itemFlags.getValue()) {
			if(flag.name.equals(itemDefault.getValue())) {
				flagDefault = flag;
				break;
			}
		}
		valuesMap.put(TaskAttributeData.Var.FLAG_ATT_FLAGS, new FlagArrayValue(itemFlags.getValue()));
		valuesMap.put(TaskAttributeData.Var.DEFAULT_VALUE, new FlagValue(flagDefault));

		if (warningOnSave(valuesMap)) {
			Logic.attribute.updateTaskAttribute(getAttribute().name, valuesMap);
			itemDefault.setChanged(false);
			itemFlags.setChanged(false);
			onChange();
		}

	}




	@Override
	public void discardChanges() {
		FlagAttributeData data = (FlagAttributeData) getAttribute().data;
		itemFlags.setValue(data.flags);
		itemFlags.unlockAll();
		itemFlags.lockFlag(data.defaultFlag);
		itemFlags.setChanged(false);
		itemDefault.setChoices(getAsNames(data.flags));
		itemDefault.setValue(data.defaultFlag.name);
		itemDefault.setChanged(false);
		onChange();
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}


}
