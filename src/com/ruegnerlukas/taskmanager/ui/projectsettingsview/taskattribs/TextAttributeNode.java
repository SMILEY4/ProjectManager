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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TextAttributeNode extends AnchorPane implements AttributeDataNode {


	private TaskAttribute attribute;
	private TaskAttributeNode parent;

	@FXML private Spinner<Integer> charLimit;
	@FXML private CheckBox multiline;
	@FXML private Spinner<Integer> nLines;
	@FXML private CheckBox useDefault;
	@FXML private TextArea defaultValue;

	private double nodeHeight;



	public TextAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		try {
			this.attribute = attribute;
			this.parent = parent;
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
		nodeHeight = this.getPrefHeight();


		// get data
		TextAttributeData attributeData = (TextAttributeData) attribute.data;


		// character limit
		SpinnerUtils.initSpinner(charLimit, attributeData.charLimit, 1, Integer.MAX_VALUE, 1, 0, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.TEXT_CHAR_LIMIT, new NumberValue(charLimit.getValue()));
		});


		// multiline
		multiline.setSelected(attributeData.multiline);
		multiline.setOnAction(event -> {
			if(multiline.isSelected()) {
				double fieldHeight = Math.max(33, 21.3*nLines.getValue() + 9); // plz dont change font size :(
				nodeHeight = 187 + fieldHeight;
				defaultValue.setMinHeight(fieldHeight);
				defaultValue.setPrefHeight(fieldHeight);
				nodeHeight = 187-33 + fieldHeight;
			} else {
				defaultValue.setMinHeight(33);
				defaultValue.setPrefHeight(33);
				nodeHeight = 187;
			}
			parent.refresh();
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.TEXT_MULTILINE, new BoolValue(multiline.isSelected()));

		});


		// expected number of lines
		nLines.setDisable(!attributeData.multiline);
		SpinnerUtils.initSpinner(nLines, attributeData.nLinesExpected, 1, Integer.MAX_VALUE, 1, 0, (observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.TEXT_N_LINES_EXP, new NumberValue(nLines.getValue()));
			double fieldHeight = Math.max(33, 21.3*nLines.getValue() + 9); // plz dont change font size :(
			nodeHeight = 187 + fieldHeight;
			defaultValue.setMinHeight(fieldHeight);
			defaultValue.setPrefHeight(fieldHeight);
			nodeHeight = 187-33 + fieldHeight;
			parent.refresh();
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		});


		// default value
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.setDisable(!attributeData.useDefault);
		defaultValue.textProperty().addListener((observable, oldValue, newValue) -> {
			if (defaultValue.getText().length() > attributeData.charLimit) {
				defaultValue.setText(defaultValue.getText().substring(0, attributeData.charLimit));
			}
		});
		defaultValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
			Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getText()));
		});
		defaultValue.textProperty().addListener((observable, oldValue, newValue) -> { // make textarea behave like textfield if multiline disabled
			final char newLine = (char) 10;
			if (!multiline.isSelected() && newValue.contains("" + newLine)) {
				defaultValue.setText(newValue.replaceAll("" + newLine, ""));
				Logic.attribute.updateTaskAttribute(attribute.name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getText()));
				Platform.runLater(() -> defaultValue.positionCaret(0));
			}
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
		nLines.setDisable(!attributeData.multiline);
		SpinnerUtils.initSpinner(nLines, attributeData.nLinesExpected, 1, Integer.MAX_VALUE, 1, 0, null);
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	public double getNodeHeight() {
		return this.nodeHeight;
	}


}
