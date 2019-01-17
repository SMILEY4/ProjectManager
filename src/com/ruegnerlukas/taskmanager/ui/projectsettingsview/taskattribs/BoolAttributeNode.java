package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.BoolAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BoolAttributeNode extends AnchorPane implements AttributeRequirementNode{


	private TaskAttribute attribute;

	@FXML private CheckBox useDefault;
	@FXML private ChoiceBox<String> defaultValue;

	private EventListener eventListener;



	public BoolAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {
		final String PATH = "taskattribute_bool.fxml";

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


		BoolAttributeData attributeData = (BoolAttributeData)attribute.data;


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
			}
		});


		// default value
		defaultValue.getItems().addAll("True", "False");
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new BoolValue(newValue.equalsIgnoreCase("True")));
			}
		});
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
		BoolAttributeData attributeData = (BoolAttributeData)attribute.data;
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.getSelectionModel().select(attributeData.defaultValue ? "True" : "False");
		defaultValue.setDisable(!useDefault.isSelected());
	}






	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
