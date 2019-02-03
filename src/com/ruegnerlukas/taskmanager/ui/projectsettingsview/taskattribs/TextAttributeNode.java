package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TextAttributeNode extends AnchorPane implements AttributeRequirementNode {


	private TaskAttribute attribute;

	@FXML private Spinner<Integer> charLimit;
	@FXML private CheckBox multiline;
	@FXML private CheckBox useDefault;
	@FXML private TextField defaultValue;




	public TextAttributeNode(TaskAttribute attribute) {
		try {
			this.attribute = attribute;
			loadFromFXML();
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void loadFromFXML() throws IOException {

		// create root
		AnchorPane root = (AnchorPane) FXMLUtils.loadFXML(getClass().getResource("taskattribute_text.fxml"), this);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight());
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight());


		// get data
		TextAttributeData attributeData = (TextAttributeData) attribute.data;


		// character limit
		SpinnerUtils.initSpinner(charLimit, attributeData.charLimit, 1, Integer.MAX_VALUE, 1, 0, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.TEXT_CHAR_LIMIT, new NumberValue(charLimit.getValue()));
		});


		// multiline
		multiline.setSelected(attributeData.multiline);
		multiline.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.TEXT_MULTILINE, new BoolValue(multiline.isSelected()));
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		});


		// default value
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.textProperty().addListener((observable, oldValue, newValue) -> {
			if (defaultValue.getText().length() > attributeData.charLimit) {
				defaultValue.setText(defaultValue.getText().substring(0, attributeData.charLimit));
			}
		});
		defaultValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getText()));
		});
		defaultValue.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getText()));
		});


		// listen for changes
		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			if (event.getAttribute() == attribute) {
				updateData();
			}
		}, AttributeUpdatedEvent.class);


		// listen for rejections
		EventManager.registerListener(this, e -> {
			AttributeUpdatedRejection event = (AttributeUpdatedRejection) e;
			if (event.getAttribute() == attribute) {
				updateData();
			}
		}, AttributeUpdatedRejection.class);

	}




	@Override
	public void close() {
		EventManager.deregisterListeners(this);
	}




	private void updateData() {
		TextAttributeData attributeData = (TextAttributeData) attribute.data;
		SpinnerUtils.initSpinner(charLimit, attributeData.charLimit, 1, Integer.MAX_VALUE, 1, 0, null);
		multiline.setSelected(attributeData.multiline);
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


}
