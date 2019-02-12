package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXEvents;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlagAttributeNode extends AttributeDataNode {


	private List<FlagNode> flagNodes;
	private FlagNode flagNodeDefault;

	@FXML private VBox boxFlags;
	@FXML private ComboBox<String> defaultFlag;

	private Button btnAddFlag;




	public FlagAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "taskattribute_flag.fxml", true);
	}




	@Override
	protected void onCreate() {


		flagNodes = new ArrayList<>();

		FlagAttributeData attributeData = (FlagAttributeData) getAttribute().data;

		// taskFlags
		btnAddFlag = new Button("Add Flag");
		btnAddFlag.setMinSize(0, 32);
		btnAddFlag.setPrefSize(100000, 32);
		btnAddFlag.setMaxSize(100000, 32);
		boxFlags.getChildren().add(btnAddFlag);

		btnAddFlag.setOnAction(event -> {
			TaskFlag flag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Flag " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()));
			FlagNode node = new FlagNode(this, flag);
			flagNodes.add(node);
			refreshList();
			setChanged();
		});

		// default flag
		for (TaskFlag flag : attributeData.flags) {
			defaultFlag.getItems().add(flag.name);
		}
		defaultFlag.getSelectionModel().select(attributeData.defaultFlag.name);
		defaultFlag.setOnAction(FXEvents.register(event -> {
			for (FlagNode node : flagNodes) {
				if (node.name.equals(defaultFlag.getValue())) {
					node.setDefault(true);
				} else {
					node.setDefault(false);
				}
			}
			setChanged();
		}, defaultFlag));


		// read values from attribute
		onChange();
	}




	protected void removeFlagNode(FlagNode node) {
		flagNodes.remove(node);
		refreshList();
		setChanged();
	}




	private void refreshList() {

		// default
		FXEvents.mute(defaultFlag);
		defaultFlag.getItems().clear();
		for (FlagNode node : flagNodes) {
			defaultFlag.getItems().add(node.flag.name);
			if (node.isDefaultFlag) {
				defaultFlag.getSelectionModel().select(node.flag.name);
			}
		}
		FXEvents.unmute(defaultFlag);

		// flags
		boxFlags.getChildren().clear();
		boxFlags.getChildren().addAll(flagNodes);
		boxFlags.getChildren().add(btnAddFlag);

	}




	@Override
	protected void onChange() {
		FlagAttributeData attributeData = (FlagAttributeData) getAttribute().data;

		// default
		FXEvents.mute(defaultFlag);
		defaultFlag.getItems().clear();
		for (TaskFlag flag : attributeData.flags) {
			defaultFlag.getItems().add(flag.name);
		}
		defaultFlag.getSelectionModel().select(attributeData.defaultFlag.name);
		FXEvents.unmute(defaultFlag);


		// flag list
		flagNodes.clear();
		for (TaskFlag flag : attributeData.flags) {
			FlagNode flagNode = new FlagNode(this, flag);
			flagNodes.add(flagNode);
		}
		refreshList();
	}




	@Override
	protected void onSave() {

		// flags
		TaskFlag[] flagArray = new TaskFlag[flagNodes.size()];
		for (int i = 0; i < flagArray.length; i++) {
			flagArray[i] = flagNodes.get(i).flag;
			flagArray[i].name = flagNodes.get(i).name;
			flagArray[i].color = flagNodes.get(i).color;
		}
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.FLAG_ATT_FLAGS, new FlagArrayValue(flagArray));


		// default flag
		FlagNode defaultNode = null;
		for (FlagNode node : flagNodes) {
			if (node.isDefaultFlag) {
				defaultNode = node;
				break;
			}
		}
		if (defaultNode != null) {
			Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.DEFAULT_VALUE, new FlagValue(defaultNode.flag));
		}
	}




	@Override
	protected void onDiscard() {
		onChange();
	}




	@Override
	protected void onClose() {
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}




	@Override
	public boolean getUseDefault() {
		return true;
	}


}
