package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeLockEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs.TaskAttributeNode;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
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

public class ProjectSettingsView extends AnchorPane {


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
		labelName = new EditableLabel("");
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> {
			Logic.project.renameProject(newValue);
		});
		Logic.project.getCurrentProject(new Request<Project>(true) {
			@Override
			public void onResponse(Response<Project> response) {
				Project project = response.getValue();
				labelName.setText(project.name);
			}
		});


		// lock task values
		Logic.attribute.getAttributeLock(new Request<Boolean>(true) {
			@Override
			public void onResponse(Response<Boolean> response) {
				attributesLocked = response.getValue();
				if (attributesLocked) {
					ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
				} else {
					ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1f, "black");
				}
				btnLockAttributes.setOnAction(event -> Logic.attribute.setAttributeLock(!attributesLocked));
				setAttributeLock(attributesLocked);
			}
		});


		// task values
		boxTaskAttribs.getChildren().clear();
		boxTaskAttribs.setSpacing(3);
		VBoxDragAndDrop.enableDragAndDrop(boxTaskAttribs);


		// add attribute
		btnAddAttribute.setOnAction(event -> Logic.attribute.createAttribute(
				"Attribute " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()),
				TaskAttributeType.TEXT));


		// add initial values
		Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
			@Override
			public void onResponse(Response<List<TaskAttribute>> response) {
				List<TaskAttribute> attributes = response.getValue();
				for (TaskAttribute attribute : attributes) {
					TaskAttributeNode attrNode = new TaskAttributeNode(attribute);
					boxTaskAttribs.getChildren().add(attrNode);
				}
			}
		});

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

		// listen for added values
		EventManager.registerListener(this, e -> {
			AttributeCreatedEvent event = (AttributeCreatedEvent) e;
			TaskAttributeNode attrNode = new TaskAttributeNode(event.getAttribute());
			boxTaskAttribs.getChildren().add(attrNode);
		}, AttributeCreatedEvent.class);

		// listen for removed values
		EventManager.registerListener(this, e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			for (Node node : boxTaskAttribs.getChildren()) {
				TaskAttributeNode attributeNode = (TaskAttributeNode) node;

				if (attributeNode.attribute == event.getAttribute()) {
					boxTaskAttribs.getChildren().remove(node);
					attributeNode.close();
					break;
				}
			}
		}, AttributeRemovedEvent.class);
	}




	public void close() {
		EventManager.deregisterListeners(this);
	}




	private void setAttributeLock(boolean locked) {
		btnAddAttribute.setDisable(locked);
		for (Node node : boxTaskAttribs.getChildren()) {
			if (node instanceof TaskAttributeNode) {
				((TaskAttributeNode) node).setLocked(locked);
			}
		}
	}


}
