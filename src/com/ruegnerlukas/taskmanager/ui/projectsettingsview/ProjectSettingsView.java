package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeLockEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.LogicService;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs.TaskAttributeNode;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Random;

public class ProjectSettingsView extends AnchorPane {


	@FXML private AnchorPane rootProjectSettingsView;

	private EditableLabel labelName;

	@FXML private AnchorPane paneHeader;

	@FXML private Button btnLockAttributes;
	private boolean attributesLocked = false;

	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;




	public ProjectSettingsView() {
		try {

			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_view_projectsettings.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading ProjectSettingsView-FXML: " + e);
		}

		create();
		setupListeners();
	}




	private void create() {

		// Project name
		labelName = new EditableLabel(LogicService.get().getProject().name);
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);


		// lock task attributes
		attributesLocked = LogicService.get().getProject().attributesLocked;
		if(attributesLocked) {
			ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
		} else {
			ButtonUtils.makeIconButton(btnLockAttributes,SVGIcons.LOCK_OPEN, 1f, "black");
		}
		btnLockAttributes.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				LogicService.get().setAttributeLock(!attributesLocked);
			}
		});
		setAttributeLock(attributesLocked);


		// task attributes
		boxTaskAttribs.getChildren().clear();
		boxTaskAttribs.setSpacing(3);
		VBoxDragAndDrop.enableDragAndDrop(boxTaskAttribs);


		// add attribute
		btnAddAttribute.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				LogicService.get().createAttribute(
						"Attribute " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()),
						TaskAttributeType.TEXT);
			}
		});


		// add initial attributes
		for(TaskAttribute attribute : LogicService.get().getProject().attributes) {
			TaskAttributeNode attrNode = new TaskAttributeNode(attribute);
			boxTaskAttribs.getChildren().add(attrNode);
		}
	}




	private void setupListeners() {

		// listen for project renamed
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectRenamedEvent event = (ProjectRenamedEvent)e;
				labelName.setText(event.getNewName());
			}
		}, ProjectRenamedEvent.class);


		// listen for attribute-lock-event
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				AttributeLockEvent event = (AttributeLockEvent)e;
				attributesLocked = event.getLockNow();
				if(attributesLocked) {
					ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
				} else {
					ButtonUtils.makeIconButton(btnLockAttributes,SVGIcons.LOCK_OPEN, 1f, "black");
				}
				setAttributeLock(attributesLocked);
			}
		}, AttributeLockEvent.class);


		// listen for added attributes
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				AttributeCreatedEvent event = (AttributeCreatedEvent)e;
				TaskAttributeNode attrNode = new TaskAttributeNode(event.getAttribute());
				boxTaskAttribs.getChildren().add(attrNode);
			}
		}, AttributeCreatedEvent.class);


		// listen for removed attributes
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				AttributeRemovedEvent event = (AttributeRemovedEvent)e;

				for(Node node : boxTaskAttribs.getChildren()) {
					TaskAttributeNode attributeNode = (TaskAttributeNode)node;

					if(attributeNode.attribute == event.getAttribute()) {
						boxTaskAttribs.getChildren().remove(node);
						attributeNode.requirementNode.dispose();
						break;
					}
				}

			}
		}, AttributeRemovedEvent.class);
	}




	private void setAttributeLock(boolean locked) {
		btnAddAttribute.setDisable(locked);
		for(Node node : boxTaskAttribs.getChildren()) {
			if(node instanceof TaskAttributeNode) {
				((TaskAttributeNode)node).setLocked(locked);
			}
		}
	}

}
