package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NumberAttributeNode extends AnchorPane implements AttributeRequirementNode{


	private TaskAttribute attribute;

	@FXML private Spinner<Integer> decPlaces;
	@FXML private Spinner<Double> minValue;
	@FXML private Spinner<Double> maxValue;
	@FXML private CheckBox useDefault;
	@FXML private Spinner<Double> defaultValue;

	private EventListener eventListener;



	public NumberAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {
		final String PATH = "taskattribute_number.fxml";

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

		NumberAttributeData attributeData = (NumberAttributeData)attribute.data;

		// dec places
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, new ChangeListener() {
			@Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_DEC_PLACES, new NumberValue(decPlaces.getValue()));
			}
		});

		// min value
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, new ChangeListener() {
			@Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_MIN, new NumberValue(minValue.getValue()));
			}
		});

		// max value
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, new ChangeListener() {
			@Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.NUMBER_ATT_MAX, new NumberValue(maxValue.getValue()));

			}
		});

		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
			}
		});

		// default value
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, new ChangeListener() {
			@Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new NumberValue(defaultValue.getValue()));
			}
		});


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
		NumberAttributeData attributeData = (NumberAttributeData)attribute.data;
		SpinnerUtils.initSpinner(decPlaces, attributeData.decPlaces, 0, 10, 1, 0, null);
		SpinnerUtils.initSpinner(minValue, attributeData.min, Integer.MIN_VALUE, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, null);
		SpinnerUtils.initSpinner(maxValue, attributeData.max, attributeData.min, Integer.MAX_VALUE, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, null);
		useDefault.setSelected(attributeData.useDefault);
		SpinnerUtils.initSpinner(defaultValue, attributeData.defaultValue, attributeData.min, attributeData.max, Math.pow(10, -attributeData.decPlaces), attributeData.decPlaces, null);
		defaultValue.setDisable(!useDefault.isSelected());
	}



	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
