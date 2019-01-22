package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.utils.FXEvents;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Random;

public class FlagAttributeNode extends AnchorPane implements AttributeRequirementNode {


	public TaskAttribute attribute;

	@FXML private VBox boxFlags;
	@FXML private ComboBox<String> defaultFlag;

	private Button btnAddFlag;




	public FlagAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {
		final String PATH = "taskattribute_flag.fxml";

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
		loader.setController(this);
		AnchorPane root = (AnchorPane) loader.load();
		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		root.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.R) {
					root.getStylesheets().clear();
					root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
					root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
				}
			}
		});

		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);

		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());

		FlagAttributeData attributeData = (FlagAttributeData) attribute.data;


		// flags
		btnAddFlag = new Button("Add Flag");
		btnAddFlag.setMinSize(0, 32);
		btnAddFlag.setPrefSize(100000, 32);
		btnAddFlag.setMaxSize(100000, 32);
		boxFlags.getChildren().add(btnAddFlag);

		btnAddFlag.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TaskFlag flag = new TaskFlag(TaskFlag.FlagColor.GRAY, "Flag " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()), false);
				TaskFlag[] flagArray = new TaskFlag[attributeData.flags.length + 1];
				for (int i = 0; i < flagArray.length - 1; i++) {
					flagArray[i] = attributeData.flags[i];
				}
				flagArray[flagArray.length - 1] = flag;
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.FLAG_ATT_FLAGS, new FlagArrayValue(flagArray));
			}
		});

		// default flag
		for (TaskFlag flag : attributeData.flags) {
			defaultFlag.getItems().add(flag.name);
		}
		defaultFlag.getSelectionModel().select(attributeData.defaultFlag.name);
		defaultFlag.setOnAction(FXEvents.register(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (TaskFlag flag : attributeData.flags) {
					if (flag.name.equals(defaultFlag.getValue())) {
						Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new FlagValue(flag));
					}
				}
			}
		}, defaultFlag));


		// listen for changes
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
				if (event.getAttribute() == attribute) {
					updateData();
				}
			}
		}, AttributeUpdatedEvent.class);


		// listen for rejections
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeUpdatedRejection event = (AttributeUpdatedRejection) e;
				if (event.getAttribute() == attribute) {
					updateData();
				}
			}
		}, AttributeUpdatedRejection.class);


		updateData();
	}




	@Override
	public void close() {
		EventManager.deregisterListeners(this);
	}




	private void updateData() {

		FlagAttributeData attributeData = (FlagAttributeData) attribute.data;

		// default
		FXEvents.mute(defaultFlag);
		defaultFlag.getItems().clear();
		for (TaskFlag flag : attributeData.flags) {
			defaultFlag.getItems().add(flag.name);
		}
		defaultFlag.getSelectionModel().select(attributeData.defaultFlag.name);
		FXEvents.unmute(defaultFlag);

		// flag list
		boxFlags.getChildren().clear();
		for (TaskFlag flag : attributeData.flags) {
			FlagNode flagNode = new FlagNode(this, flag);
			boxFlags.getChildren().add(flagNode);
		}
		boxFlags.getChildren().add(btnAddFlag);

	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
