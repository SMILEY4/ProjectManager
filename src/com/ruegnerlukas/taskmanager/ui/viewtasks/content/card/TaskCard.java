package com.ruegnerlukas.taskmanager.ui.viewtasks.content.card;

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
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.TaskList;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
	private FXListChangeListener<TaskAttribute> listenerAttributes;
	private EventHandler<AttributeValueChangeEvent> listenerAttribValue;




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
			parent.getTaskContent().selectTask(this.task);
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


		buildAttributes();

		listenerAttributes = new FXListChangeListener<TaskAttribute>(Data.projectProperty.get().data.attributes) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskAttribute> c) {
				buildAttributes();
			}
		};

		listenerAttribValue = e -> {
			if (e.getKey().equals(TaskAttribute.ATTRIB_CARD_DISPLAY_TYPE)) {
				buildAttributes();
			}
		};
		AttributeLogic.addOnAttributeValueChanged(listenerAttribValue);

	}




	private void buildAttributes() {

		// reset / clean up
		for (Node node : boxAttribs.getChildren()) {
			if (node instanceof CardItem) {
				((CardItem) node).dispose();
			}
		}
		boxAttribs.getChildren().clear();

		// rebuild
		for (TaskAttribute attribute : Data.projectProperty.get().data.attributes) {
			if (!attribute.type.get().fixed) {
				if (AttributeLogic.getShowOnTaskCard(attribute)) {
					addAttributeAsKVPair(attribute);
				}
			}
		}
	}




	private void addAttributeAsKVPair(TaskAttribute attribute) {
		CardItem item = new CardItem(this, attribute);
		boxAttribs.getChildren().add(item);
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
		listenerAttributes.removeFromAll();
		AttributeLogic.removeOnAttributeValueChanged(listenerAttribValue);
		for (Node node : boxAttribs.getChildren()) {
			if (node instanceof CardItem) {
				((CardItem) node).dispose();
			}
		}
	}


}
