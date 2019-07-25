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




	/**
	 * Creates and adds the controls and logic for the project name.
	 */
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




	/**
	 * called when the name of the {@link Project} changed. Changes the text of the project-name label.
	 */
	private void onProjectNameChanged(String newName) {
		labelName.setText(newName);
	}




	/**
	 * Renames the {@link Project}.
	 */
	private void onRenameProject(String name) {
		ProjectLogic.renameProject(Data.projectProperty.get(), name);
	}


	//======================//
	//   LOCK ATTRIBUTES    //
	//======================//




	/**
	 * Creates and adds the controls and logic for locking all {@link TaskAttribute}.
	 */
	private void createLockAttributes() {

		Project project = Data.projectProperty.get();

		final boolean isLocked = project.settings.attributesLocked.get();
		ButtonUtils.makeIconButton(btnLockAttributes, isLocked ? SVGIcons.LOCK_CLOSED : SVGIcons.LOCK_OPEN, 1);
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




	/**
	 * Called when the lock of the {@link TaskAttribute}s changed. Locks/Unlocks all {@link TaskAttribute}s in the list of attributes.
	 */
	private void onAttributeLockChanged(boolean newLock) {
		ButtonUtils.makeIconButton(btnLockAttributes, newLock ? SVGIcons.LOCK_CLOSED : SVGIcons.LOCK_OPEN, 0.7);
		boxTaskAttribs.setDisable(newLock);
		btnAddAttribute.setDisable(newLock);
	}




	/**
	 * Switches the lock of all {@link TaskAttribute}.
	 */
	private void onLockAttributes() {
		ProjectLogic.lockSwitchTaskAttributes(Data.projectProperty.get());
	}


	//======================//
	//    ATTRIBUTE LIST    //
	//======================//




	/**
	 * Creates and adds the controls and logic for the list of {@link TaskAttribute}s.
	 */
	private void createAttributeList() {

		Project project = Data.projectProperty.get();

		btnAddAttribute.setOnAction(event -> onAddAttribute());

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




	/**
	 * Called when a new {@link TaskAttribute} was added to the {@link Project}.
	 * Adds the given {@link TaskAttribute} to the list of attributes.
	 */
	private void onAttributeAdded(TaskAttribute attribute) {
		AttributeNode node = new AttributeNode(attribute);
		boxTaskAttribs.getChildren().add(node);
	}




	/**
	 * Adds a new {@link TaskAttribute} to the {@link Project}.
	 */
	private void onAddAttribute() {
		Project project = Data.projectProperty.get();
		ProjectLogic.addAttributeToProject(project, AttributeLogic.createTaskAttribute(AttributeType.getFreeTypes()[0], project));
	}




	/**
	 * Called when a {@link TaskAttribute} was removed from the {@link Project}.
	 * Removes the given {@link TaskAttribute} from the list of attributes.
	 */
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




	/**
	 * @return a list of all {@link AttributeNode}s in the list.
	 */
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
