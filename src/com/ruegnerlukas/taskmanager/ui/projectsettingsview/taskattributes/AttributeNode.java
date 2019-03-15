package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRenamedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeTypeChangedEvent;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes.*;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class AttributeNode extends AnchorPane {


	private final TaskAttribute attribute;

	private static final double HEADER_HEIGHT = 35;
	private AnchorPane headerPane;
	private HBox headerBox;

	private Button btnRemove;
	private ComboBox<TaskAttributeType> choiceType;
	private EditableLabel labelName;
	private Button btnExpand;

	private AnchorPane contentPane;
	private DataNode dataNode;

	private boolean hasSaveButtons = true;
	private HBox boxButtons;
	private Button btnDiscard;
	private Button btnSave;

	private boolean isExpanded = false;




	public AttributeNode(TaskAttribute attribute) {
		this.attribute = attribute;
		this.hasSaveButtons = !attribute.data.getType().fixed() || attribute.data.getType() == TaskAttributeType.FLAG;
		createFrame();
		createListeners();
		insertTaskAttribute(attribute);
	}




	private void createFrame() {

		/*

		THIS: AnchorPane
		  - HEADER_PANE: AnchorPane
		  .   - HEADER_BOX: HBox
		  .   .   - BTN_REMOVE: Button
		  .   .   - CHOICE_TYPE: ChoiceBox
		  .   .   - ATTRIB_NAME: EditableLabel
		  .   .   - BTN_EXPAND: Button
		  - CONTENT_PANE: AnchorPane
		  .   .   - dataNode: DATA_NODE
		  .   .   - boxButtons: HBOX [optional]
		  .   .   .   - btnDiscard: BUTTON
		  .   .   .   - btnSave: BUTTON
		 */


		// this / root
		this.setMinSize(100, 34);
		this.setPrefSize(10000, 34);
		this.setMaxSize(10000, 34);
		this.setId("attribute");


		// header pane
		headerPane = new AnchorPane();
		headerPane.setId("pane_header");
		headerPane.setMinSize(100, HEADER_HEIGHT);
		headerPane.setPrefSize(10000, HEADER_HEIGHT);
		headerPane.setMaxSize(10000, HEADER_HEIGHT);

		this.getChildren().add(headerPane);
		AnchorPane.setTopAnchor(headerPane, 0.0);
		AnchorPane.setLeftAnchor(headerPane, 0.0);
		AnchorPane.setRightAnchor(headerPane, 0.0);


		// box header
		headerBox = new HBox();
		headerBox.setMinSize(100, HEADER_HEIGHT);
		headerBox.setPrefSize(10000, HEADER_HEIGHT);
		headerBox.setMaxSize(10000, HEADER_HEIGHT);
		headerBox.setSpacing(5);

		headerPane.getChildren().add(headerBox);
		AnchorPane.setTopAnchor(headerBox, 0.0);
		AnchorPane.setLeftAnchor(headerBox, 0.0);
		AnchorPane.setRightAnchor(headerBox, 0.0);


		// remove-button
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		headerBox.getChildren().add(btnRemove);
		btnRemove.setOnAction(event -> {
			onRemoveAttribute();
		});


		// choice attribute-type
		choiceType = new ComboBox<>();
		choiceType.setButtonCell(ComboboxUtils.createListCellAttributeType());
		choiceType.setCellFactory(param -> ComboboxUtils.createListCellAttributeType());
		if (attribute.data.getType().fixed()) {
			choiceType.getItems().add(attribute.data.getType());
		} else {
			for (TaskAttributeType type : TaskAttributeType.values()) {
				if (!type.fixed()) {
					choiceType.getItems().add(type);
				}
			}
		}
		choiceType.getSelectionModel().select(this.attribute.data.getType());
		choiceType.setMinSize(150, 32);
		choiceType.setPrefSize(150, 32);
		choiceType.setMaxSize(150, 32);
		headerBox.getChildren().add(choiceType);
		choiceType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				onTypeSelected(newValue);
			}
		});


		// attribute name
		labelName = new EditableLabel(attribute.name);
		labelName.setMinSize(32, 32);
		labelName.setPrefSize(10000, 32);
		labelName.setMaxSize(10000, 32);
		headerBox.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> {
			onRename(newValue);
		});


		// expand-button
		btnExpand = new Button();
		btnExpand.setMinSize(32, 32);
		btnExpand.setPrefSize(32, 32);
		btnExpand.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f, "black");
		headerBox.getChildren().add(btnExpand);
		btnExpand.setOnAction(event -> {
			if (isExpanded) {
				isExpanded = false;
				onCollapse();
			} else {
				isExpanded = true;
				onExpand();
			}
		});


		// content pane
		contentPane = new AnchorPane();
		contentPane.setPadding(new Insets(0, 10, 0, 10));
		contentPane.setId("pane_content");
		contentPane.setVisible(isExpanded);
		contentPane.setMinSize(0, 0);
		contentPane.setPrefSize(10000, 10000);
		contentPane.setMaxSize(10000, 10000);
		AnchorUtils.setAnchors(contentPane, 34, 0, 0, 0);
		this.getChildren().add(contentPane);


		// buttons discard, save
		if (hasSaveButtons) {

			boxButtons = new HBox();
			boxButtons.setAlignment(Pos.CENTER_RIGHT);
			boxButtons.setSpacing(5);
			boxButtons.setMinSize(0, 32);
			boxButtons.setPrefSize(10000, 32);
			boxButtons.setMaxSize(10000, 32);
			AnchorPane.setLeftAnchor(boxButtons, 0.0);
			AnchorPane.setRightAnchor(boxButtons, 0.0);
			AnchorPane.setBottomAnchor(boxButtons, 5.0);
			contentPane.getChildren().add(boxButtons);

			btnDiscard = new Button("Discard");
			btnDiscard.setPrefSize(100, 32);
			boxButtons.getChildren().add(btnDiscard);
			btnDiscard.setOnAction(event -> {
				dataNode.discardChanges();
			});

			btnSave = new Button("Save");
			btnSave.setPrefSize(100, 32);
			boxButtons.getChildren().add(btnSave);
			btnSave.setOnAction(event -> {
				dataNode.writeChanges();
			});

			boxButtons.setDisable(true);
		}
	}




	private void onRemoveAttribute() {
		SyncRequest<List<Task>> request = new SyncRequest<>();
		Logic.tasks.getTaskWithValue(attribute, request);
		Response<List<Task>> response = request.getResponse();
		List<Task> effectedTasks = response.getValue();

		if (effectedTasks.isEmpty()) {
			Logic.attribute.deleteAttribute(attribute.name);

		} else {
			ButtonType alert = Alerts.confirmation(
					"Deleting \"" + attribute.name + "\" affects " + effectedTasks.size() + " tasks.",
					"Delete \"" + attribute.name + "\" ?",
					ButtonType.YES, ButtonType.CANCEL);

			if (alert == ButtonType.YES) {
				Logic.attribute.deleteAttribute(attribute.name);
			}
		}
	}




	private void onTypeSelected(TaskAttributeType type) {

		SyncRequest<List<Task>> request = new SyncRequest<>();
		Logic.tasks.getTaskWithValue(attribute, request);
		Response<List<Task>> response = request.getResponse();
		List<Task> effectedTasks = response.getValue();

		if (effectedTasks.isEmpty()) {
			Logic.attribute.setAttributeType(attribute.name, type);

		} else {
			ButtonType alert = Alerts.confirmation(
					"Changing \"" + attribute.name + "\" affects " + effectedTasks.size() + " tasks.",
					"Change \"" + attribute.name + "\" to " + type.display + "?",
					ButtonType.YES, ButtonType.CANCEL);

			if (alert == ButtonType.YES) {
				Logic.attribute.setAttributeType(attribute.name, type);
			}
		}
	}




	private void onRename(String newName) {
		labelName.setText(attribute.name);
		Logic.attribute.renameAttribute(attribute.name, newName);
	}




	private void onExpand() {
		contentPane.setVisible(true);
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_UP, 0.75f, "black");
		if (dataNode != null) {
			fitToContentSize();
		}
	}




	private void onCollapse() {
		contentPane.setVisible(false);
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f, "black");
		if (dataNode != null) {
			this.setMinSize(100, HEADER_HEIGHT);
			this.setPrefSize(10000, HEADER_HEIGHT);
			this.setMaxSize(10000, HEADER_HEIGHT);
		}
	}




	public void setChanged(boolean changed) {
		if (changed) {
			boxButtons.setDisable(false);
		} else {
			boxButtons.setDisable(true);
		}
		fitToContentSize();
	}




	private void createListeners() {

		// listen for changed type
		EventManager.registerListener(this, e -> {
			AttributeTypeChangedEvent event = (AttributeTypeChangedEvent) e;
			if (event.getAttribute() == attribute) {
				choiceType.getSelectionModel().select(event.getAttribute().data.getType());
				insertTaskAttribute(attribute);
				if (isExpanded) {
					onExpand();
				}
			}
		}, AttributeTypeChangedEvent.class);

		// listen for changed name
		EventManager.registerListener(this, e -> {
			AttributeRenamedEvent event = (AttributeRenamedEvent) e;
			if (event.getAttribute() == attribute) {
				labelName.setText(event.getAttribute().name);
			}
		}, AttributeRenamedEvent.class);

	}




	private void insertTaskAttribute(TaskAttribute attribute) {

		TaskAttributeType type = attribute.data.getType();

		btnRemove.setDisable(type.fixed());
		labelName.setDisable(type.fixed());
		choiceType.setDisable(type.fixed());

		if (dataNode != null) {
			dataNode.dispose();
		}

		switch (type) {
			case BOOLEAN: {
				dataNode = new BoolDataNode(this, attribute);
				break;
			}
			case CHOICE: {
				dataNode = new ChoiceDataNode(this, attribute);
				break;
			}
			case FLAG: {
				dataNode = new FlagDataNode(this, attribute);
				break;
			}
			case NUMBER: {
				dataNode = new NumberDataNode(this, attribute);
				break;
			}
			case TEXT: {
				dataNode = new TextDataNode(this, attribute);
				break;
			}
			default: {
				dataNode = new EmptyDataNode(this, attribute);
				break;
			}
		}

		this.hasSaveButtons = !attribute.data.getType().fixed() || attribute.data.getType() == TaskAttributeType.FLAG;
		if(dataNode instanceof EmptyDataNode) {
			hasSaveButtons = false;
		}

		AnchorUtils.setAnchors(dataNode, 0, 0, (hasSaveButtons ? 42 : 0), 0);
		contentPane.getChildren().clear();
		contentPane.getChildren().add(dataNode);
		if(hasSaveButtons) {
			contentPane.getChildren().add(boxButtons);
		}
		fitToContentSize();

	}




	public void fitToContentSize() {
		final double nodeHeight = dataNode.getNodeHeight();
		if (isExpanded) {
			this.setMinHeight(HEADER_HEIGHT + nodeHeight + (hasSaveButtons ? 42 : 0));
			this.setPrefHeight(HEADER_HEIGHT + nodeHeight + (hasSaveButtons ? 42 : 0));
			this.setMaxHeight(HEADER_HEIGHT + nodeHeight + (hasSaveButtons ? 42 : 0));
			contentPane.setMinHeight(nodeHeight + (hasSaveButtons ? 42+10 : 0));
			contentPane.setPrefHeight(nodeHeight + (hasSaveButtons ? 42+10 : 0));
			contentPane.setMaxHeight(nodeHeight + (hasSaveButtons ? 42+10 : 0));
		} else {
			this.setMinHeight(HEADER_HEIGHT);
			this.setPrefHeight(HEADER_HEIGHT);
			this.setMaxHeight(HEADER_HEIGHT);
			contentPane.setMinHeight(0);
			contentPane.setPrefHeight(0);
			contentPane.setMaxHeight(0);
		}
	}




	public void setLocked(boolean locked) {
		if (!attribute.data.getType().fixed()) {
			btnRemove.setDisable(locked);
			choiceType.setDisable(locked);
			labelName.setDisable(locked);
		}
		contentPane.setDisable(locked);
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public void dispose() {
		dataNode.dispose();
		EventManager.deregisterListeners(this);
	}

}
