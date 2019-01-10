package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeLockEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic_v2.LogicService;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs.TaskAttributeNode;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class ProjectSettingsController implements IViewController {

	@FXML private AnchorPane rootProjectSettingsView;
	
	@FXML private AnchorPane paneHeader;

	@FXML private Button btnLockAttributes;
	private boolean attributesLocked = false;
	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;




	@Override
	public void create() {
		
		// Project name label
		EditableLabel labelName = new EditableLabel(LogicService.get().getProject().name);
		labelName.getLabel().setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		labelName.getTextField().setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);

		// rename project
		labelName.addListener(new ChangeListener<String>(){
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				LogicService.get().renameProject(newValue);
			}
		});

		// listen for project renamed
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectRenamedEvent event = (ProjectRenamedEvent)e;
				labelName.setText(event.getNewName());
			}
		}, ProjectRenamedEvent.class);

		// lock task attributes
		attributesLocked = LogicService.get().getProject().attributesLocked;
		if(attributesLocked) {
			ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.SVG_LOCK_LOCKED, 40, 40, "black");
		} else {
			ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.SVG_LOCK_UNLOCKED, 40, 40, "black");
		}
		btnLockAttributes.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				LogicService.get().setAttributeLock(!attributesLocked);
			}
		});
		disableAttributes(attributesLocked);

		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				AttributeLockEvent event = (AttributeLockEvent)e;
				attributesLocked = event.getLockNow();
				if(attributesLocked) {
					ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.SVG_LOCK_LOCKED, 40, 40, "black");
				} else {
					ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.SVG_LOCK_UNLOCKED, 40, 40, "black");
				}
				disableAttributes(attributesLocked);
			}
		}, AttributeLockEvent.class);


		
		// task attributes
		boxTaskAttribs.getChildren().clear();
		boxTaskAttribs.setSpacing(3);
		VBoxDragAndDrop.enableDragAndDrop(boxTaskAttribs);

		btnAddAttribute.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				LogicService.get().createAttribute(
						"Attribute " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()),
						TaskAttributeType.TEXT);
			}
		});

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

		// add attributes on startup
		for(TaskAttribute attribute : LogicService.get().getProject().attributes) {
			TaskAttributeNode attrNode = new TaskAttributeNode(attribute);
			boxTaskAttribs.getChildren().add(attrNode);
		}


	}



	private void disableAttributes(boolean locked) {
		btnAddAttribute.setDisable(locked);
		for(Node node : boxTaskAttribs.getChildren()) {
			if(node instanceof TaskAttributeNode) {
				((TaskAttributeNode)node).setLocked(locked);
			}
		}
	}

	
}

