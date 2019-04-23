package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogicManager;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskFlagAttributeLogic;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.DateTimePicker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TerminalNode extends CriteriaNode {


	private ComboBox<TaskAttribute> boxAttribute;
	private ComboBox<FilterOperation> boxOperation;
	private HBox boxValues;




	public TerminalNode(FilterCriteria criteria, EventHandler<ActionEvent> handlerModified) {
		super(criteria, handlerModified);
		this.setId("terminal-criteria");
		this.setPrefSize(100000, 36);
		this.setPadding(new Insets(0, 5, 0, 5));


		TerminalFilterCriteria terminalCriteria = (TerminalFilterCriteria) getCriteria();

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		box.setSpacing(5);
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);


		// box attribute
		boxAttribute = new ComboBox<>();
		boxAttribute.setButtonCell(ComboboxUtils.createListCellAttribute());
		boxAttribute.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		boxAttribute.setMinWidth(200);
		boxAttribute.setPrefWidth(250);
		boxAttribute.setMinHeight(32);
		boxAttribute.setMaxHeight(32);
		boxAttribute.getItems().addAll(Data.projectProperty.get().data.attributes);
		boxAttribute.getSelectionModel().select(terminalCriteria.attribute.get());
		boxAttribute.setOnAction(event -> onSelectAttribute(boxAttribute.getValue()));
		box.getChildren().add(boxAttribute);


		// box comarator
		boxOperation = new ComboBox<>();
		boxOperation.setButtonCell(ComboboxUtils.createListCellFilterOperation());
		boxOperation.setCellFactory(param -> ComboboxUtils.createListCellFilterOperation());
		boxOperation.setMinWidth(100);
		boxOperation.setPrefWidth(150);
		boxOperation.setMinHeight(32);
		boxOperation.setMaxHeight(32);
		boxOperation.getItems().addAll(AttributeLogicManager.getFilterData(terminalCriteria.attribute.get().type.get()).keySet());
		boxOperation.getSelectionModel().select(terminalCriteria.operation.get());
		boxOperation.setOnAction(event -> onSelectFilterOperation(boxOperation.getValue()));
		box.getChildren().add(boxOperation);


		// value
		boxValues = new HBox();
		boxValues.setSpacing(5);
		box.getChildren().add(boxValues);

		Class<?>[] dataTypes = AttributeLogicManager.getFilterData(
				terminalCriteria.attribute.get().type.get()).get(terminalCriteria.operation.get());
		createValueInputFields(dataTypes, null);


		// spacing
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		box.getChildren().add(region);


		// button remove
		Button btnRemove = new Button();
		btnRemove.setMinSize(22, 22);
		btnRemove.setPrefSize(22, 22);
		btnRemove.setMaxSize(22, 22);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7, "black");
		btnRemove.setOnAction(event -> {
			removeThisCriteria();
			onModified();
		});
		box.getChildren().add(btnRemove);
	}




	private void onSelectAttribute(TaskAttribute attribute) {
		boxOperation.getItems().clear();
		boxOperation.getItems().addAll(AttributeLogicManager.getFilterData(attribute.type.get()).keySet());
		boxOperation.getSelectionModel().select(0);
		onModified();
	}




	private void onSelectFilterOperation(FilterOperation operation) {
		Class<?>[] dataTypes = AttributeLogicManager.getFilterData(boxAttribute.getValue().type.get()).get(operation);
		createValueInputFields(dataTypes, null);
		onModified();
	}




	private void createValueInputFields(Class<?>[] dataTypes, Object[] values) {
		if (dataTypes == null) {
			return;
		}

		boxValues.getChildren().clear();

		for (int i = 0; i < dataTypes.length; i++) {
			Class<?> type = dataTypes[i];

			if (Boolean.class == type) {
				ComboBox<Boolean> node = new ComboBox<>();
				node.setButtonCell(ComboboxUtils.createListCellBoolean());
				node.setCellFactory(param -> ComboboxUtils.createListCellBoolean());
				node.getItems().addAll(true, false);
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.getSelectionModel().select((Boolean) values[i]);
				} else {
					node.getSelectionModel().select(true);
				}
				node.setOnAction(event -> onModified());
				boxValues.getChildren().add(node);
			}

			if (Integer.class == type) {
				Spinner<Integer> node = new Spinner<>();
				SpinnerUtils.initSpinner(node, 0, -999999, +999999, 1, 0, false, false, null);
				node.setEditable(true);
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.getValueFactory().setValue((Integer) values[i]);
				} else {
					node.getValueFactory().setValue(0);
				}
				node.valueProperty().addListener(((observable, oldValue, newValue) -> onModified()));
				boxValues.getChildren().add(node);
			}

			if (Double.class == type) {
				Spinner<Double> node = new Spinner<>();
				SpinnerUtils.initSpinner(node, 0, -999999, +999999, 0.1, 1, true, false, null);
				node.setEditable(true);
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.getValueFactory().setValue((Double) values[i]);
				} else {
					node.getValueFactory().setValue(0.0);
				}
				node.valueProperty().addListener(((observable, oldValue, newValue) -> onModified()));
				boxValues.getChildren().add(node);
			}

			if (String.class == type) {
				TextField node = new TextField();
				node.setPrefWidth(400);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.setText((String) values[i]);
				} else {
					node.setText("");
				}
				node.setOnAction(event -> onModified());
				boxValues.getChildren().add(node);
			}

			if (TaskFlag.class == type) {
				ComboBox<TaskFlag> node = new ComboBox<>();
				node.setButtonCell(ComboboxUtils.createListCellFlag());
				node.setCellFactory(param -> ComboboxUtils.createListCellFlag());
				node.getItems().addAll(TaskFlagAttributeLogic.getFlagList(
						AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG)));
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.getSelectionModel().select((TaskFlag) values[i]);
				} else {
					node.getSelectionModel().select(0);
				}
				node.setOnAction(event -> onModified());
				boxValues.getChildren().add(node);
			}

			if (LocalDate.class == type) {
				DatePicker node = new DatePicker();
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.setValue((LocalDate) values[i]);
				} else {
					node.setValue(LocalDate.now());
				}
				node.setOnAction(event -> onModified());
				boxValues.getChildren().add(node);
			}

			if (LocalDateTime.class == type) {
				DateTimePicker node = new DateTimePicker();
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.setDateTimeValue((LocalDateTime) values[i]);
				} else {
					node.setDateTimeValue(LocalDateTime.now());
				}
				node.setOnAction(event -> onModified());
				node.dateTimeValueProperty().addListener((observable, oldValue, newValue) -> onModified());
				boxValues.getChildren().add(node);
			}

			if (Task.class == type) {
				ComboBox<Task> node = new ComboBox<>();
				node.setButtonCell(ComboboxUtils.createListCellTask());
				node.setCellFactory(param -> ComboboxUtils.createListCellTask());
				node.getItems().addAll(Data.projectProperty.get().data.tasks);
				node.setPrefWidth(200);
				node.setMinHeight(32);
				node.setMaxHeight(32);
				if (values != null) {
					node.getSelectionModel().select((Task) values[i]);
				} else {
					node.getSelectionModel().select(0);
				}
				node.setOnAction(event -> onModified());
				boxValues.getChildren().add(node);
			}

		}


	}




	public TaskAttribute getAttribute() {
		return boxAttribute.getValue();
	}




	public FilterOperation getFilterOperation() {
		return boxOperation.getValue();
	}




	@SuppressWarnings ("unchecked")
	public Object[] getValues() {

		Class<?>[] dataTypes = AttributeLogicManager.getFilterData(getAttribute().type.get()).get(getFilterOperation());

		Object[] values = new Object[dataTypes.length];
		for (int i = 0; i < values.length; i++) {
			Class<?> type = dataTypes[i];

			if (Boolean.class == type) {
				ComboBox<Boolean> node = (ComboBox<Boolean>) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}
			if (Integer.class == type) {
				Spinner<Integer> node = (Spinner<Integer>) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}
			if (Double.class == type) {
				Spinner<Double> node = (Spinner<Double>) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}
			if (String.class == type) {
				TextField node = (TextField) boxValues.getChildren().get(i);
				values[i] = node.getText();
			}
			if (TaskFlag.class == type) {
				ComboBox<TaskFlag> node = (ComboBox<TaskFlag>) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}
			if (LocalDate.class == type) {
				DatePicker node = (DatePicker) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}
			if (LocalDateTime.class == type) {
				DateTimePicker node = (DateTimePicker) boxValues.getChildren().get(i);
				values[i] = node.getDateTimeValue();
			}
			if (Task.class == type) {
				ComboBox<Task> node = (ComboBox<Task>) boxValues.getChildren().get(i);
				values[i] = node.getValue();
			}

		}

		return values;
	}




	@Override
	public FilterCriteria buildCriteriaTree() {
		TerminalFilterCriteria criteria = (TerminalFilterCriteria) this.getCriteria();
		criteria.attribute.set(getAttribute());
		criteria.operation.set(getFilterOperation());
		criteria.values.setAll(getValues());
		return criteria;
	}




	@Override
	public void expandTree() {
	}

}