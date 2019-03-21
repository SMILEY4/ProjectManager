package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeLockEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.TabContent;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ProjectSettingsView extends AnchorPane implements TabContent {


	public static final String TITLE = "Project Settings";

	@FXML private AnchorPane rootProjectSettingsView;

	private EditableLabel labelName;

	@FXML private AnchorPane paneHeader;

	@FXML private Button btnLockAttributes;
	private boolean attributesLocked = false;

	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;




	public ProjectSettingsView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_PROJECTSETTINGS, this);
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
		labelName = new EditableLabel("");
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> {
			Logic.project.renameProject(newValue);
		});

		Project project = Logic.project.getCurrentProject().getValue();
		if (project != null) {
			labelName.setText(project.name);
		}


		// lock task values
		Boolean attributesLocked = Logic.attribute.getAttributeLock().getValue();
		if (attributesLocked != null) {
			if (attributesLocked) {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			btnLockAttributes.setOnAction(event -> Logic.attribute.setAttributeLock(!attributesLocked));
			setAttributeLock(attributesLocked);
		}


		// task values
		boxTaskAttribs.getChildren().clear();
		boxTaskAttribs.setSpacing(3);
		VBoxDragAndDrop.enableDragAndDrop(boxTaskAttribs);


		// add attribute
		btnAddAttribute.setOnAction(event -> {
			Logic.attribute.createAttribute(
					"Attribute " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()), TaskAttributeType.TEXT);
		});


		// add initial attributes
		List<TaskAttribute> attributes = Logic.attribute.getAttributes().getValue();
		for (TaskAttribute attribute : attributes) {
			AttributeNode attrNode = new AttributeNode(attribute);
			boxTaskAttribs.getChildren().add(attrNode);
		}

	}




	private void setupListeners() {

		// listen for project renamed
		EventManager.registerListener(this, e -> {
			ProjectRenamedEvent event = (ProjectRenamedEvent) e;
			labelName.setText(event.getNewName());
		}, ProjectRenamedEvent.class);

		// listen for attribute-lock-event
		EventManager.registerListener(this, e -> {
			AttributeLockEvent event = (AttributeLockEvent) e;
			attributesLocked = event.getLockNow();
			if (attributesLocked) {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			setAttributeLock(attributesLocked);
		}, AttributeLockEvent.class);

		// listen for added attributes
		EventManager.registerListener(this, e -> {
			AttributeCreatedEvent event = (AttributeCreatedEvent) e;
			AttributeNode attrNode = new AttributeNode(event.getAttribute());
			boxTaskAttribs.getChildren().add(attrNode);
		}, AttributeCreatedEvent.class);

		// listen for removed attributes
		EventManager.registerListener(this, e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			for (Node node : boxTaskAttribs.getChildren()) {
				AttributeNode attributeNode = (AttributeNode) node;
				if (attributeNode.getAttribute() == event.getAttribute()) {
					boxTaskAttribs.getChildren().remove(node);
					attributeNode.dispose();
					break;
				}
			}
		}, AttributeRemovedEvent.class);
	}




	private void setAttributeLock(boolean locked) {
		btnAddAttribute.setDisable(locked);
		for (Node node : boxTaskAttribs.getChildren()) {
			if (node instanceof AttributeNode) {
				((AttributeNode) node).setLocked(locked);
			}
		}
	}




	@Override
	public void onOpen() {

	}




	@Override
	public void onClose() {
		EventManager.deregisterListeners(this);
	}




	@Override
	public void onShow() {

	}




	@Override
	public void onHide() {

	}

}
