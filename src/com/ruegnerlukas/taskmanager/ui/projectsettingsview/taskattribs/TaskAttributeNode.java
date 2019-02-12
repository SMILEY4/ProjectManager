package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRenamedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeTypeChangedEvent;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.List;

public class TaskAttributeNode extends AnchorPane {


	public TaskAttribute attribute;

	private AnchorPane paneHeader;
	private HBox boxHeader;

	private Button btnRemove;
	private ChoiceBox<String> choiceType;
	private EditableLabel labelAttName;
	private Button btnExpandCollapse;
	private boolean isExpanded = false;

	private AnchorPane paneBody;
	public AttributeDataNode requirementNode;




	public TaskAttributeNode(TaskAttribute attribute) {
		this.attribute = attribute;

		// root
		this.setMinSize(100, 34);
		this.setPrefSize(10000, 34);
		this.setMaxSize(10000, 34);


		// header pane
		paneHeader = new AnchorPane();
		paneHeader.setMinSize(100, 34);
		paneHeader.setPrefSize(10000, 34);
		paneHeader.setMaxSize(10000, 34);

		this.getChildren().add(paneHeader);
		AnchorPane.setTopAnchor(paneHeader, 0.0);
		AnchorPane.setLeftAnchor(paneHeader, 0.0);
		AnchorPane.setRightAnchor(paneHeader, 0.0);


		// box header
		boxHeader = new HBox();
		boxHeader.setMinSize(100, 34);
		boxHeader.setPrefSize(10000, 34);
		boxHeader.setMaxSize(10000, 34);
		boxHeader.setSpacing(5);

		paneHeader.getChildren().add(boxHeader);
		AnchorPane.setTopAnchor(boxHeader, 0.0);
		AnchorPane.setLeftAnchor(boxHeader, 0.0);
		AnchorPane.setRightAnchor(boxHeader, 0.0);


		// remove-button
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setOnAction(event -> {

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
		});

		boxHeader.getChildren().add(btnRemove);


		// attribute type
		choiceType = new ChoiceBox<>();
		if (attribute.data.getType().fixed) {
			choiceType.getItems().add(attribute.data.getType().display);

		} else {
			for (TaskAttributeType type : TaskAttributeType.values()) {
				if (!type.fixed) {
					choiceType.getItems().add(type.display);
				}
			}
		}
		choiceType.getSelectionModel().select(this.attribute.data.getType().display);
		choiceType.setMinSize(150, 32);
		choiceType.setPrefSize(150, 32);
		choiceType.setMaxSize(150, 32);
		choiceType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {

				SyncRequest<List<Task>> request = new SyncRequest<>();
				Logic.tasks.getTaskWithValue(attribute, request);
				Response<List<Task>> response = request.getResponse();
				List<Task> effectedTasks = response.getValue();

				if(effectedTasks.isEmpty()) {
					Logic.attribute.setAttributeType(attribute.name, TaskAttributeType.getFromDisplay(newValue));

				} else {
					ButtonType alert = Alerts.confirmation(
							"Changing \"" + attribute.name + "\" affects " + effectedTasks.size() + " tasks.",
							"Change \"" + attribute.name + "\" to " + newValue + "?",
							ButtonType.YES, ButtonType.CANCEL);

					if (alert == ButtonType.YES) {
						Logic.attribute.setAttributeType(attribute.name, TaskAttributeType.getFromDisplay(newValue));
					}
				}

			}
		});
		boxHeader.getChildren().add(choiceType);

		// listen for changed type
		EventManager.registerListener(this, e -> {
			AttributeTypeChangedEvent event = (AttributeTypeChangedEvent) e;
			if (event.getAttribute() == attribute) {
				choiceType.getSelectionModel().select(event.getAttribute().data.getType().display);
				buildAttributeType(event.getAttribute().data.getType());
				if (isExpanded) {
					TaskAttributeNode.this.setMinSize(100, requirementNode.getNodeHeight() + 35);
					TaskAttributeNode.this.setPrefSize(10000, requirementNode.getNodeHeight() + 35);
					TaskAttributeNode.this.setMaxSize(10000, requirementNode.getNodeHeight() + 35);
				}
			}
		}, AttributeTypeChangedEvent.class);


		// attribute name
		labelAttName = new EditableLabel(attribute.name);
		labelAttName.setMinSize(32, 32);
		labelAttName.setPrefSize(10000, 32);
		labelAttName.setMaxSize(10000, 32);
		labelAttName.addListener((observable, oldValue, newValue) -> {
			labelAttName.setText(oldValue);
			Logic.attribute.renameAttribute(oldValue, newValue);
		});
		boxHeader.getChildren().add(labelAttName);

		// listen for changed name
		EventManager.registerListener(this, e -> {
			AttributeRenamedEvent event = (AttributeRenamedEvent) e;
			if (event.getAttribute() == attribute) {
				labelAttName.setText(event.getAttribute().name);
			}
		}, AttributeRenamedEvent.class);


		// expand-button
		btnExpandCollapse = new Button();
		btnExpandCollapse.setMinSize(32, 32);
		btnExpandCollapse.setPrefSize(32, 32);
		btnExpandCollapse.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnExpandCollapse, SVGIcons.ARROW_DOWN, 0.75f, "black");
		boxHeader.getChildren().add(btnExpandCollapse);

		btnExpandCollapse.setOnAction(event -> {
			if (isExpanded) {
				paneBody.setVisible(false);
				isExpanded = false;
				ButtonUtils.makeIconButton(btnExpandCollapse, SVGIcons.ARROW_DOWN, 0.75f, "black");
				TaskAttributeNode.this.setMinSize(100, 34);
				TaskAttributeNode.this.setPrefSize(10000, 34);
				TaskAttributeNode.this.setMaxSize(10000, 34);
			} else {
				paneBody.setVisible(true);
				isExpanded = true;
				ButtonUtils.makeIconButton(btnExpandCollapse, SVGIcons.ARROW_UP, 0.75f, "black");
				TaskAttributeNode.this.setMinSize(100, requirementNode.getNodeHeight() + 35);
				TaskAttributeNode.this.setPrefSize(10000, requirementNode.getNodeHeight() + 35);
				TaskAttributeNode.this.setMaxSize(10000, requirementNode.getNodeHeight() + 35);
			}
		});

		// body container: keys
		paneBody = new AnchorPane();
		paneBody.setVisible(isExpanded);
		paneBody.setMinSize(0, 0);
		paneBody.setPrefSize(10000, 10000);
		paneBody.setMaxSize(10000, 10000);
		AnchorUtils.setAnchors(paneBody, 34, 0, 0, 0);
		this.getChildren().add(paneBody);

		buildAttributeType(attribute.data.getType());
	}




	private void buildAttributeType(TaskAttributeType type) {

		btnRemove.setDisable(type.fixed);
		labelAttName.setDisable(type.fixed);
		choiceType.setDisable(type.fixed);

		if (requirementNode != null) {
			requirementNode.close();
		}

		Region node = null;

		if (type == TaskAttributeType.BOOLEAN) {
			BoolAttributeNode attributeNode = new BoolAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.CHOICE) {
			ChoiceAttributeNode attributeNode = new ChoiceAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.NUMBER) {
			NumberAttributeNode attributeNode = new NumberAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.TEXT) {
			TextAttributeNode attributeNode = new TextAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.FLAG) {
			FlagAttributeNode attributeNode = new FlagAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.ID) {
			IDAttributeNode attributeNode = new IDAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.DESCRIPTION) {
			DescriptionAttributeNode attributeNode = new DescriptionAttributeNode(attribute, this);
			node = attributeNode;
			requirementNode = attributeNode;
		}

		AnchorUtils.setAnchors(node, 0, 0, 0, 0);

		paneBody.getChildren().clear();
		paneBody.getChildren().add(node);
	}




	protected void refresh() {
		if (isExpanded) {
			TaskAttributeNode.this.setMinSize(100, requirementNode.getNodeHeight() + 35);
			TaskAttributeNode.this.setPrefSize(10000, requirementNode.getNodeHeight() + 35);
			TaskAttributeNode.this.setMaxSize(10000, requirementNode.getNodeHeight() + 35);
		} else {
			TaskAttributeNode.this.setMinSize(100, 34);
			TaskAttributeNode.this.setPrefSize(10000, 34);
			TaskAttributeNode.this.setMaxSize(10000, 34);
		}
	}




	public void setLocked(boolean locked) {
		if (!attribute.data.getType().fixed) {
			btnRemove.setDisable(locked);
			choiceType.setDisable(locked);
			labelAttName.setDisable(locked);
		}
		paneBody.setDisable(locked);
	}




	public void close() {
		this.requirementNode.close();
		EventManager.deregisterListeners(this);
	}


}
