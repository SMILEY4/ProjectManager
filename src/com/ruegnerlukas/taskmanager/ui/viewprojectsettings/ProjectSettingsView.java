package com.ruegnerlukas.taskmanager.ui.viewprojectsettings;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.AttributeType;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeNode;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectSettingsView extends AnchorPane {


	public static final String TITLE = "Project Settings";

	@FXML private AnchorPane rootProjectSettingsView;

	private EditableLabel labelName;

	@FXML private AnchorPane paneHeader;
	@FXML private VBox boxSettings;

	@FXML private VBox boxSettingsAttribs;
	@FXML private Button btnLockAttributes;
	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;

	private ObservableList<AttributeNode> attributeNodeList = FXCollections.observableArrayList();




	public ProjectSettingsView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_PROJECTSETTINGS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading ProjectSettings-FXML: " + e);
		}
		create();
	}




	private void create() {

		// Project name
		labelName = new EditableLabel("");
		AnchorUtils.setAnchors(labelName, 0, 0, 0, 0);
		paneHeader.getChildren().add(labelName);
		labelName.setText(Data.projectProperty.get().settings.name.getValue());
		labelName.addListener((observable, oldValue, newValue) -> {
			onRenameProject(newValue);
		});
		Data.projectProperty.get().settings.name.addListener((observable, oldValue, newValue) -> {
			labelName.setText(newValue);
		});

		// lock attributes
		if (Data.projectProperty.get().settings.attributesLocked.get()) {
			ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1, "black");
		} else {
			ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1, "black");
		}
		Data.projectProperty.get().settings.attributesLocked.addListener(((observable, oldValue, newValue) -> {
			ButtonUtils.makeIconButton(btnLockAttributes, newValue ? SVGIcons.LOCK_CLOSED : SVGIcons.LOCK_OPEN, 0.7, "black");
			boxTaskAttribs.setDisable(newValue);
			btnAddAttribute.setDisable(newValue);
		}));
		btnLockAttributes.setOnAction(event -> {
			ProjectLogic.lockSwitchTaskAttributes(Data.projectProperty.get());
		});


		// add attributes
		for (TaskAttribute attribute : Data.projectProperty.get().data.attributes) {
			AttributeNode node = new AttributeNode(attribute);
			attributeNodeList.add(node);
			boxTaskAttribs.getChildren().add(node);
		}

		// attributes added/removed to/from project
		Data.projectProperty.get().data.attributes.addListener((ListChangeListener<TaskAttribute>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					for (TaskAttribute attribute : c.getAddedSubList()) {
						AttributeNode node = new AttributeNode(attribute);
						attributeNodeList.add(node);
						boxTaskAttribs.getChildren().add(node);
					}
				}
				if (c.wasRemoved()) {
					List<AttributeNode> removed = new ArrayList<>();
					for (TaskAttribute attribute : c.getRemoved()) {
						for (AttributeNode node : attributeNodeList) {
							if (node.getAttribute() == attribute) {
								removed.add(node);
							}
						}
					}
					attributeNodeList.removeAll(removed);
					boxTaskAttribs.getChildren().removeAll(removed);
				}
			}
		});


		// button add attribute
		btnAddAttribute.setOnAction(event -> {
			onAddAttribute();
		});

	}




	private void onRenameProject(String name) {
		ProjectLogic.renameProject(Data.projectProperty.get(), name);
	}




	private void onAddAttribute() {
		ProjectLogic.addAttributeToProject(
				Data.projectProperty.get(),
				AttributeLogic.createTaskAttribute(AttributeType.getFreeTypes()[0])
		);
	}


}
