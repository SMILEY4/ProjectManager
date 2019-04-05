package com.ruegnerlukas.taskmanager_old.ui.taskview.filterPopup.values;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.utils.uielements.choicelistfield.ChoiceListField;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Set;

public abstract class FilterValue {


	private EventHandler<ActionEvent> eventHandler;




	public void setOnAction(EventHandler<ActionEvent> eventHandler) {
		this.eventHandler = eventHandler;
	}




	protected void onAction() {
		if (this.eventHandler != null) {
			eventHandler.handle(new ActionEvent());
		}
	}




	public abstract void update(List<Node> outNodes, TaskAttributeData data, FilterCriteria.ComparisonOp compOp, TaskAttributeValue compValue);

	public abstract TaskAttributeValue getValue();




	protected ChoiceBox<String> buildChoiceBox(String selected, String... values) {
		ChoiceBox<String> choice = new ChoiceBox<>();
		setSize(choice);
		choice.getItems().addAll(values);
		choice.getSelectionModel().select(selected);
		return choice;
	}




	protected ComboBox<String> buildComboxString(String selected, String... values) {
		ComboBox<String> choice = new ComboBox<>();
		setSize(choice);
		choice.getItems().addAll(values);
		choice.getSelectionModel().select(selected);
		return choice;
	}




	protected ComboBox<Boolean> buildComboxBoolean(boolean selected) {
		ComboBox<Boolean> choice = new ComboBox<>();
		choice.setButtonCell(ComboboxUtils.createListCellBoolean());
		choice.setCellFactory(param -> ComboboxUtils.createListCellBoolean());
		setSize(choice);
		choice.getItems().addAll(true, false);
		choice.getSelectionModel().select(selected);
		return choice;
	}




	protected ComboBox<TaskFlag> buildComboxFlag(TaskFlag selected, TaskFlag... values) {
		ComboBox<TaskFlag> choice = new ComboBox<>();
		choice.setButtonCell(ComboboxUtils.createListCellFlag());
		choice.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		setSize(choice);
		choice.getItems().addAll(values);
		choice.getSelectionModel().select(selected);
		return choice;
	}




	protected TextField buildTextField(String prompt, String text) {
		TextField textField = new TextField();
		setSizeThin(textField);
		textField.setPromptText(prompt);
		textField.setText(text);
		return textField;
	}




	protected ChoiceListField buildChoiceList(String prompt, String[] values, Set<String> choices) {
		ChoiceListField choiceField = new ChoiceListField(choices, ",", String.join(",", values));
		setSizeThin(choiceField);
		choiceField.setPromptText(prompt);
		return choiceField;
	}




	protected Spinner<Integer> buildIntSpinner(int min, int max, int step, int value) {
		Spinner<Integer> spinner = new Spinner<>();
		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, null);
		spinner.setEditable(true);
		setSizeThin(spinner);
		return spinner;
	}




	protected Spinner<Double> buildDoubleSpinner(double min, double max, double step, int decPlaces, double value) {
		Spinner<Double> spinner = new Spinner<>();
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, null);
		spinner.setEditable(true);
		setSizeThin(spinner);
		return spinner;
	}




	protected void setSize(Region region) {
		region.setMinSize(10, 32);
		region.setPrefSize(100000, 32);
		region.setMaxSize(100000, 32);
	}




	protected void setSizeThin(Region region) {
		region.setMinSize(10, 28);
		region.setPrefSize(100000, 28);
		region.setMaxSize(100000, 28);
	}

}
