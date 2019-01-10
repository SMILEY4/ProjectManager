package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.logic.LogicService;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.requirements.ChoiceAttributeRequirement;
import com.ruegnerlukas.taskmanager.utils.FXEvents;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChoiceAttributeNode extends AnchorPane implements AttributeRequirementNode{


	private TaskAttribute attribute;

	@FXML private TextField values;
	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;

	private EventListener eventListener;



	public ChoiceAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {
		final String PATH = "taskattribute_choice.fxml";

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
		loader.setController(this);
		AnchorPane root = (AnchorPane) loader.load();
		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		root.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent event) {
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

		ChoiceAttributeRequirement attributeData = (ChoiceAttributeRequirement)attribute.data;

		// values
		values.setText(String.join(",", attributeData.values));
//		values.focusedProperty().addListener(new ChangeListener<Boolean>() {
//			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				ChoiceAttributeRequirement updatedRequirement = (ChoiceAttributeRequirement)attributeData.copy();
//				Set<String> valuesSet = new HashSet<>();
//				for(String value : values.getText().split(",")) {
//					if(!value.trim().isEmpty()) {
//						valuesSet.add(value.trim());
//					}
//				}
//				updatedRequirement.values = valuesSet;
//				if(!valuesSet.contains(defaultValue.getValue())) {
//					updatedRequirement.defaultValue = valuesSet.isEmpty() ? "" : valuesSet.iterator().next();
//				}
//				LogicService.get().updateTaskAttribute(attribute.name, updatedRequirement);
//			}
//		});
		values.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				ChoiceAttributeRequirement updatedRequirement = (ChoiceAttributeRequirement)attributeData.copy();
				Set<String> valuesSet = new HashSet<>();
				for(String value : values.getText().split(",")) {
					valuesSet.add(value.trim());
				}
				updatedRequirement.values = valuesSet;
				if(!valuesSet.contains(defaultValue.getValue())) {
					updatedRequirement.defaultValue = valuesSet.isEmpty() ? "" : valuesSet.iterator().next();
				}
				LogicService.get().updateTaskAttribute(attribute.name, updatedRequirement);
			}
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				ChoiceAttributeRequirement updatedRequirement = (ChoiceAttributeRequirement)attributeData.copy();
				updatedRequirement.useDefault = useDefault.isSelected();
				LogicService.get().updateTaskAttribute(attribute.name, updatedRequirement);
			}
		});


		// default value
		defaultValue.getItems().addAll(attributeData.values);
		defaultValue.getSelectionModel().select(attributeData.defaultValue);
		defaultValue.getSelectionModel().selectedItemProperty().addListener(FXEvents.register(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ChoiceAttributeRequirement updatedRequirement = (ChoiceAttributeRequirement)attributeData.copy();
				updatedRequirement.defaultValue = newValue;
				LogicService.get().updateTaskAttribute(attribute.name, updatedRequirement);
			}
		}, defaultValue.getSelectionModel().selectedItemProperty()));
		defaultValue.setDisable(!useDefault.isSelected());


		// listen for changes
		eventListener = new EventListener() {
			@Override public void onEvent(Event e) {
				if(e instanceof  AttributeUpdatedEvent) {
					AttributeUpdatedEvent event = (AttributeUpdatedEvent)e;
					if(event.getAttribute() == attribute) {
						updateData();
					}
				}
				if(e instanceof  AttributeUpdatedRejection) {
					AttributeUpdatedRejection event = (AttributeUpdatedRejection)e;
					if(event.getAttribute() == attribute) {
						updateData();
					}
				}
			}
		};

		EventManager.registerListener(eventListener, AttributeUpdatedEvent.class);
		EventManager.registerListener(eventListener , AttributeUpdatedRejection.class);
	}




	@Override
	public void dispose() {
		EventManager.deregisterListener(eventListener);
	}



	private void updateData() {
		ChoiceAttributeRequirement attributeData = (ChoiceAttributeRequirement)attribute.data;

		FXEvents.mute(defaultValue.getSelectionModel().selectedItemProperty());

		values.setText(String.join(",", attributeData.values));
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.getItems().clear();
		defaultValue.getItems().addAll(attributeData.values);
		defaultValue.getSelectionModel().select(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());

		FXEvents.unmute(defaultValue.getSelectionModel().selectedItemProperty());

	}



	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
