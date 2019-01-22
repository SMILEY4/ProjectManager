package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeRenamedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeTypeChangedEvent;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

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
	public AttributeRequirementNode requirementNode;




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
		btnRemove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Logic.attribute.deleteAttribute(attribute.name);
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
		choiceType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!oldValue.equals(newValue)) {
					Logic.attribute.changeAttributeType(attribute.name, TaskAttributeType.getFromDisplay(newValue));
				}
			}
		});
		boxHeader.getChildren().add(choiceType);

		// listen for changed type
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
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
			}
		}, AttributeTypeChangedEvent.class);


		// attribute name
		labelAttName = new EditableLabel(attribute.name);
		labelAttName.setMinSize(32, 32);
		labelAttName.setPrefSize(10000, 32);
		labelAttName.setMaxSize(10000, 32);
		labelAttName.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelAttName.setText(oldValue);
				Logic.attribute.renameAttribute(oldValue, newValue);
			}
		});
		boxHeader.getChildren().add(labelAttName);

		// listen for changed name
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				AttributeRenamedEvent event = (AttributeRenamedEvent) e;
				if (event.getAttribute() == attribute) {
					labelAttName.setText(event.getAttribute().name);
				}
			}
		}, AttributeRenamedEvent.class);


		// expand-button
		btnExpandCollapse = new Button();
		btnExpandCollapse.setMinSize(32, 32);
		btnExpandCollapse.setPrefSize(32, 32);
		btnExpandCollapse.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnExpandCollapse, SVGIcons.ARROW_DOWN, 0.75f, "black");
		boxHeader.getChildren().add(btnExpandCollapse);

		btnExpandCollapse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
			BoolAttributeNode attributeNode = new BoolAttributeNode(attribute);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.CHOICE) {
			ChoiceAttributeNode attributeNode = new ChoiceAttributeNode(attribute);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.NUMBER) {
			NumberAttributeNode attributeNode = new NumberAttributeNode(attribute);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.TEXT) {
			TextAttributeNode attributeNode = new TextAttributeNode(attribute);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.FLAG) {
			FlagAttributeNode attributeNode = new FlagAttributeNode(attribute);
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.ID) {
			IDAttributeNode attributeNode = new IDAttributeNode();
			node = attributeNode;
			requirementNode = attributeNode;

		} else if (type == TaskAttributeType.DESCRIPTION) {
			DescriptionAttributeNode attributeNode = new DescriptionAttributeNode();
			node = attributeNode;
			requirementNode = attributeNode;
		}

		AnchorUtils.setAnchors(node, 0, 0, 0, 0);

		paneBody.getChildren().clear();
		paneBody.getChildren().add(node);
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
