package com.ruegnerlukas.taskmanager.ui.viewtasks.content;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import com.ruegnerlukas.taskmanager.logic.events.TaskValueChangeEvent;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class TaskCard extends AnchorPane {


	private Task task;
	protected TaskList parent;

	@FXML private Pane paneFlag;
	@FXML private Label labelID;
	@FXML private HBox boxIcons;
	@FXML private Label labelDesc;
	@FXML private VBox boxAttribs;

	private EventHandler<ActionEvent> handlerChangedFlag;
	private EventHandler<ActionEvent> handlerChangedDescription;

	private EventHandler<AttributeValueChangeEvent> handlerChangedAttribValue;
	private EventHandler<TaskValueChangeEvent> handlerChangedTaskValue;
	private ChangeListener<String> listenerAttributeName;
	private FXListChangeListener<TaskAttribute> listenerAttributes;




	public TaskCard(Task task, TaskList list) {
		this.task = task;
		this.parent = list;
		try {
			AnchorPane root = (AnchorPane) UIDataHandler.loadFXML(UIModule.ELEMENT_TASKCARD, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskCard-FXML: " + e);
		}
		create();
	}




	private void create() {
		this.setPrefSize(320, 200);

		this.setOnMouseClicked(event -> {
			parent.parent.selectTask(this.task);
		});

		handlerChangedFlag = e -> onFlagChanged();
		handlerChangedDescription = e -> onDescriptionChanged();
		task.addOnChange(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG), handlerChangedFlag);
		task.addOnChange(AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION), handlerChangedDescription);

		final TaskValue<?> valueID = TaskLogic.getValueOrDefault(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID));
		final TaskValue<?> valueFlag = TaskLogic.getValueOrDefault(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG));
		final TaskValue<?> valueDescr = TaskLogic.getValueOrDefault(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION));

		labelID.setText("T-" + valueID.getValue());
		labelDesc.setText((String) valueDescr.getValue());
		paneFlag.setStyle("-fx-background-color: " + ((TaskFlag) valueFlag.getValue()).color.get().asHex());


		handlerChangedAttribValue = e -> buildAttributes();
		AttributeLogic.addOnAttributeValueChanged(handlerChangedAttribValue);

		handlerChangedTaskValue = e -> buildAttributes();
		TaskLogic.addOnTaskValueChanged(handlerChangedTaskValue);

		listenerAttributeName = (observable, oldValue, newValue) -> buildAttributes();

		listenerAttributes = new FXListChangeListener<TaskAttribute>(Data.projectProperty.get().data.attributes) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskAttribute> c) {
				buildAttributes();
			}
		};

		buildAttributes();
	}




	private void buildAttributes() {

		boxAttribs.getChildren().clear();
		boxIcons.getChildren().clear();

		for (TaskAttribute attribute : Data.projectProperty.get().data.attributes) {
			attribute.name.removeListener(listenerAttributeName);
		}

		for (TaskAttribute attribute : Data.projectProperty.get().data.attributes) {
			if (!attribute.type.get().fixed) {
				if (TaskAttribute.CardDisplayType.ICON == AttributeLogic.getCardDisplayType(attribute)) {
					addAttributeAsIcon(attribute);
				}
				if (TaskAttribute.CardDisplayType.KV_PAIR == AttributeLogic.getCardDisplayType(attribute)) {
					addAttributeAsKVPair(attribute);
				}
				if (TaskAttribute.CardDisplayType.BOTH == AttributeLogic.getCardDisplayType(attribute)) {
					addAttributeAsIcon(attribute);
					addAttributeAsKVPair(attribute);
				}
			}
		}
	}




	private void addAttributeAsIcon(TaskAttribute attribute) {

		Label label = new Label();
		label.setMinSize(20, 20);
		label.setPrefSize(20, 20);
		label.setMaxSize(20, 20);
		label.setAlignment(Pos.CENTER);
		label.setStyle("-fx-background-color: #575757; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 12;");

		final TaskValue<?> taskValue = TaskLogic.getValueOrDefault(getTask(), attribute);
		if (taskValue.getAttType() == null) {
			label.setText("");
		} else {
			switch (taskValue.getAttType()) {
				case TEXT: {
					TextValue value = (TextValue) taskValue;
					label.setText("?");
					break;
				}
				case NUMBER: {
					NumberValue value = (NumberValue) taskValue;
					label.setText(value.getValue().toString());
					break;
				}
				case DATE: {
					DateValue value = (DateValue) taskValue;
					label.setText("?");
					break;
				}
				case CHOICE: {
					ChoiceValue value = (ChoiceValue) taskValue;
					label.setText(value.getValue());
					break;
				}
				case BOOLEAN: {
					BoolValue value = (BoolValue) taskValue;
					label.setText(value.getValue() ? "T" : "F");
					break;
				}
				default: {
					label.setText("-");
				}
			}
		}

		if (taskValue.getAttType() != null) {
			boxIcons.getChildren().add(label);
		}

	}




	private void addAttributeAsKVPair(TaskAttribute attribute) {

		attribute.name.addListener(listenerAttributeName);

		HBox box = new HBox();
		box.setMinSize(0, 22);
		box.setPrefSize(100000, 22);
		box.setMaxSize(100000, 22);
		box.setSpacing(15);

		Label labelKey = new Label(attribute.name.get() + ":");
		labelKey.setMinSize(0, 22);
		labelKey.setPrefSize(10000, 22);
		labelKey.setMaxSize(10000, 22);
		labelKey.setAlignment(Pos.CENTER_RIGHT);
		box.getChildren().add(labelKey);

		Label labelValue = new Label();
		labelValue.setMinSize(0, 22);
		labelValue.setPrefSize(10000, 22);
		labelValue.setMaxSize(10000, 22);
		labelValue.setAlignment(Pos.CENTER_LEFT);
		box.getChildren().add(labelValue);

		final TaskValue<?> value = TaskLogic.getValueOrDefault(getTask(), attribute);
		if (value.getAttType() == null) {
			labelValue.setText("");
		} else {
			labelValue.setText(value.getValue().toString());
		}

		boxAttribs.getChildren().add(box);
	}




	public void select() {
		this.borderProperty().set(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
	}




	public void deselect() {
		this.borderProperty().set(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
	}




	private void onFlagChanged() {
		final TaskFlag newFlag = (TaskFlag) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.FLAG)).getValue();
		paneFlag.setStyle("-fx-background-color: " + newFlag.color.get().asHex());
	}




	private void onDescriptionChanged() {
		final String newDescr = (String) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.DESCRIPTION)).getValue();
		labelDesc.setText(newDescr);
	}




	public Task getTask() {
		return this.task;
	}




	public void dispose() {
		task.removeHandler(handlerChangedFlag);
		task.removeHandler(handlerChangedDescription);
		AttributeLogic.removeOnAttributeValueChanged(handlerChangedAttribValue);
		TaskLogic.removeOnTaskValueChanged(handlerChangedTaskValue);
		for (TaskAttribute attribute : Data.projectProperty.get().data.attributes) {
			attribute.name.removeListener(listenerAttributeName);
		}
		listenerAttributes.removeFromAll();
	}


}
