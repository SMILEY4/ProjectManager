package com.ruegnerlukas.taskmanager.ui.viewprojectsettings;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewmain.MainViewModule;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeNode;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProjectSettingsView extends AnchorPane implements MainViewModule {


	public static final String TITLE = "Project Settings";

	@FXML private AnchorPane rootProjectSettingsView;

	private EditableLabel labelName;

	@FXML private AnchorPane paneHeader;
	@FXML private VBox boxSettings;

	@FXML private VBox boxSettingsAttribs;
	@FXML private Button btnLockAttributes;
	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;

	private FXChangeListener<String> listenerProjectName;
	private FXChangeListener<Boolean> listenerAttributeLock;
	private FXListChangeListener<TaskAttribute> listenerAttributes;




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
		createProjectName();
		createLockAttributes();
		createAttributeList();
	}


	//======================//
	//     PROJECT NAME     //
	//======================//




	private void createProjectName() {

		Project project = Data.projectProperty.get();

		labelName = new EditableLabel(project.settings.name.getValue());
		AnchorUtils.setAnchors(labelName, 0, 0, 0, 0);
		paneHeader.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> onRenameProject(newValue));

		listenerProjectName = new FXChangeListener<String>(project.settings.name) {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				onProjectNameChanged(newValue);
			}
		};
	}




	private void onProjectNameChanged(String newName) {
		labelName.setText(newName);
	}




	private void onRenameProject(String name) {
		ProjectLogic.renameProject(Data.projectProperty.get(), name);
	}


	//======================//
	//   LOCK ATTRIBUTES    //
	//======================//




	private void createLockAttributes() {

		Project project = Data.projectProperty.get();

		final boolean isLocked = project.settings.attributesLocked.get();
		ButtonUtils.makeIconButton(btnLockAttributes, isLocked ? SVGIcons.LOCK_CLOSED : SVGIcons.LOCK_OPEN, 1, "black");
		btnLockAttributes.setOnAction(event -> {
			onLockAttributes();
		});

		listenerAttributeLock = new FXChangeListener<Boolean>(project.settings.attributesLocked) {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				onAttributeLockChanged(newValue);
			}
		};
	}




	private void onLockAttributes() {
		ProjectLogic.lockSwitchTaskAttributes(Data.projectProperty.get());
	}




	private void onAttributeLockChanged(boolean newLock) {
		ButtonUtils.makeIconButton(btnLockAttributes, newLock ? SVGIcons.LOCK_CLOSED : SVGIcons.LOCK_OPEN, 0.7, "black");
		boxTaskAttribs.setDisable(newLock);
		btnAddAttribute.setDisable(newLock);
	}


	//======================//
	//    ATTRIBUTE LIST    //
	//======================//




	private void createAttributeList() {

		Project project = Data.projectProperty.get();

		btnAddAttribute.setOnAction(event -> {
			onAddAttribute();
		});

		for (TaskAttribute attribute : project.data.attributes) {
			AttributeNode node = new AttributeNode(attribute);
			boxTaskAttribs.getChildren().add(node);
		}

		listenerAttributes = new FXListChangeListener<TaskAttribute>(project.data.attributes) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends TaskAttribute> c) {
				for (TaskAttribute attribute : getAllAdded(c)) {
					onAttributeAdded(attribute);
				}
				for (TaskAttribute attribute : getAllRemoved(c)) {
					onAttributeRemoved(attribute);
				}
			}
		};
	}




	private void onAddAttribute() {
		ProjectLogic.addAttributeToProject(
				Data.projectProperty.get(),
				AttributeLogic.createTaskAttribute(AttributeType.getFreeTypes()[0])
		);
	}




	private void onAttributeAdded(TaskAttribute attribute) {
		AttributeNode node = new AttributeNode(attribute);
		boxTaskAttribs.getChildren().add(node);
	}




	private void onAttributeRemoved(TaskAttribute attribute) {
		AttributeNode removed = null;
		for (AttributeNode node : getAttributeNodeList()) {
			if (node.getAttribute() == attribute) {
				removed = node;
				break;
			}
		}
		if (removed != null) {
			boxTaskAttribs.getChildren().remove(removed);
			removed.dispose();
		}

	}


	//======================//
	//         MISC         //
	//======================//




	private List<AttributeNode> getAttributeNodeList() {
		List<AttributeNode> list = new ArrayList<>();
		for (Node node : boxTaskAttribs.getChildren()) {
			if (node instanceof AttributeNode) {
				list.add((AttributeNode) node);
			}
		}
		return list;
	}




	@Override
	public void onModuleClose() {
		for (AttributeNode node : getAttributeNodeList()) {
			node.dispose();
		}
		listenerProjectName.removeFromAll();
		listenerAttributeLock.removeFromAll();
		listenerAttributes.removeFromAll();
	}




	@Override
	public void onModuleOpen() {
	}




	@Override
	public void onModuleSelected() {
	}




	@Override
	public void onModuleDeselected() {
	}

}
