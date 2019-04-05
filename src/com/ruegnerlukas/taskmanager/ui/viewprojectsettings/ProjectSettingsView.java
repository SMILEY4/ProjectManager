package com.ruegnerlukas.taskmanager.ui.viewprojectsettings;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeNode;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.EditableLabel;
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
			Logger.get().error("Error loading ProjectSettingsView-FXML: " + e);
		}
		create();
	}




	private void create() {

		// Project name
		labelName = new EditableLabel("");
		AnchorUtils.setAnchors(labelName, 0, 0, 0, 0);
		paneHeader.getChildren().add(labelName);
		if (Data.get().getProject() != null) {
			labelName.setText(Data.get().getProject().settings.name.getValue());
		}
		labelName.addListener((observable, oldValue, newValue) -> {
			onRenameProject(newValue);
		});
		Data.get().getProject().settings.name.addListener((observable, oldValue, newValue) -> {
			labelName.setText(newValue);
		});

		// add attributes
		for (TaskAttribute attribute : Data.get().getProject().data.attributes) {
			AttributeNode node = new AttributeNode(attribute);
			attributeNodeList.add(node);
			boxTaskAttribs.getChildren().add(node);
		}

		// attributes added/removed to/from project
		Data.get().getProject().data.attributes.addListener((ListChangeListener<TaskAttribute>) c -> {
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
		Logic.get().renameProject(Data.get().getProject(), name);
	}




	private void onAddAttribute() {
		Logic.get().createTaskAttribute(TaskAttribute.Type.BOOLEAN);
	}


}
