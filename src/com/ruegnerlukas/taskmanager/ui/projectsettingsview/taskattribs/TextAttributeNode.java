package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;

public class TextAttributeNode extends AttributeDataNode {


	@FXML private Spinner<Integer> charLimit;
	@FXML private CheckBox multiline;
	@FXML private Spinner<Integer> nLines;
	@FXML private CheckBox useDefault;
	@FXML private TextArea defaultValue;

	private double nodeHeight;




	public TextAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "taskattribute_text.fxml", true);
	}




	@Override
	protected void onCreate() {

		nodeHeight = this.getPrefHeight();

		// get data
		TextAttributeData attributeData = (TextAttributeData) getAttribute().data;


		// character limit
		SpinnerUtils.initSpinner(charLimit, attributeData.charLimit, 1, Integer.MAX_VALUE, 1, 0, (observable, oldValue, newValue) -> {
			setChanged();
		});


		// multiline
		multiline.setSelected(attributeData.multiline);
		multiline.setOnAction(event -> {
			if (multiline.isSelected()) {
				setMultilineFieldHeight();
			} else {
				defaultValue.setMinHeight(33);
				defaultValue.setPrefHeight(33);
				nodeHeight = 187;
			}
			nLines.setDisable(!multiline.isSelected());
			getParentAttributeNode().refresh();
			setChanged();
		});


		// expected number of lines
		nLines.setDisable(!attributeData.multiline);
		SpinnerUtils.initSpinner(nLines, attributeData.nLinesExpected, 1, Integer.MAX_VALUE, 1, 0, (observable, oldValue, newValue) -> {
			setMultilineFieldHeight();
			getParentAttributeNode().refresh();
			setChanged();
		});


		// use default
		useDefault.setSelected(attributeData.useDefault);
		useDefault.setOnAction(event -> {
			defaultValue.setDisable(!useDefault.isSelected());
			setChanged();
		});


		// default value
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());
		defaultValue.textProperty().addListener((observable, oldValue, newValue) -> {
			if (defaultValue.getText().length() > attributeData.charLimit) {
				defaultValue.setText(defaultValue.getText().substring(0, attributeData.charLimit));
			}
		});
		defaultValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
			setChanged();
		});
		defaultValue.textProperty().addListener((observable, oldValue, newValue) -> { // make textarea behave like textfield if multiline disabled
			final char newLine = (char) 10;
			if (!multiline.isSelected() && newValue.contains("" + newLine)) {
				defaultValue.setText(newValue.replaceAll("" + newLine, ""));
				Platform.runLater(() -> defaultValue.positionCaret(0));
				setChanged();
			}
		});

	}




	private void setMultilineFieldHeight() {
		double fieldHeight = Math.max(33, 21.3 * nLines.getValue() + 9); // plz dont change font size :(
		nodeHeight = 187 + fieldHeight;
		defaultValue.setMinHeight(fieldHeight);
		defaultValue.setPrefHeight(fieldHeight);
		nodeHeight = 187 - 33 + fieldHeight;
	}




	@Override
	protected void onChange() {
		TextAttributeData attributeData = (TextAttributeData) getAttribute().data;
		SpinnerUtils.initSpinner(charLimit, attributeData.charLimit, 1, Integer.MAX_VALUE, 1, 0, null);
		multiline.setSelected(attributeData.multiline);
		nLines.setDisable(!attributeData.multiline);
		SpinnerUtils.initSpinner(nLines, attributeData.nLinesExpected, 1, Integer.MAX_VALUE, 1, 0, null);
		useDefault.setSelected(attributeData.useDefault);
		defaultValue.setText(attributeData.defaultValue);
		defaultValue.setDisable(!useDefault.isSelected());
	}




	@Override
	protected void onSave() {
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.TEXT_CHAR_LIMIT, new NumberValue(charLimit.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.TEXT_MULTILINE, new BoolValue(multiline.isSelected()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.TEXT_N_LINES_EXP, new NumberValue(nLines.getValue()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.USE_DEFAULT, new BoolValue(useDefault.isSelected()));
		Logic.attribute.updateTaskAttribute(getAttribute().name, TaskAttributeData.Var.DEFAULT_VALUE, new TextValue(defaultValue.getText()));
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
		return this.nodeHeight + 42; // 42 = height for save,discard-buttons
	}




	@Override
	public boolean getUseDefault() {
		return useDefault.isSelected();
	}


}
