package com.ruegnerlukas.taskmanager.ui.viewmain;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.ProjectSettingsView;
import com.ruegnerlukas.taskmanager.utils.uielements.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.MenuFunction;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


/**
 * MAIN-VIEW
 * <p>
 * <p>
 * 1. Responsible for:
 * - managing Menu-Functions (file:save,open,close;Preferences;About;...)
 * - Open,Close tabs
 * <p>
 * <p>
 * <p>
 * 2. Details:
 * <p>
 * <p>
 * 2.2. Menu Functions:
 * <p>
 * - File
 * - New Project
 * Creates a new Project.
 * If a project is already open, the user has the choice to save and/or close that project
 * <p>
 * - Open Project
 * Opens a Project that was selected in the FileChooser.
 * If a project is already open, the user has the choice to save and/or close that project
 * <p>
 * - Open Recent
 * Opens a recently used Project.
 * If a project is already open, the user has the choice to save and/or close that project
 * <p>
 * - Save
 * Saves the current Project to its specified Location.
 * If no Location was specified or the file is missing, the user can choose a location
 * <p>
 * - Close
 * Closes the current project. The user can choose to save or cancel before closing.
 * <p>
 * - Exit
 * Exits the application
 * If a project is currently open, the user has the choice to save and/or close that project
 * <p>
 * - Preferences
 * - Settings
 * - Key Bindings
 * <p>
 * - Help
 * - Help
 * - About
 */
public class MainView extends AnchorPane {


	@FXML private MenuBar menuBar;
	@FXML private TabPane tabPane;

	@FXML private AnchorPane paneInfobar;
	@FXML private Label labelInfobar;

	private MenuFunction functionNewProject;
	private MenuFunction functionSaveProject;
	private MenuFunction functionCloseProject;




	public MainView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_MAIN, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading MainView-FXML: " + e);
		}

		Data.projectProperty.addListener((observable, oldValue, newValue) -> {
			System.out.println("Project changed: " + oldValue + "  " + newValue);
		});

		create();
	}




	private void create() {


		// Create new empty Project
		functionNewProject = new MenuFunction("File", "New Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					if (handleOpenProject()) {
						ProjectLogic.setCurrentProject(ProjectLogic.createNewProject());
					}
				} else {
					ProjectLogic.setCurrentProject(ProjectLogic.createNewProject());
				}
			}
		}.addToMenuBar(menuBar);


		// save project
		functionSaveProject = new MenuFunction("File", "Save") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					ProjectLogic.saveProject(Data.projectProperty.get());
				}
			}
		}.addToMenuBar(menuBar);


		// close project
		functionCloseProject = new MenuFunction("File", "Close Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					handleOpenProject();
				}
			}
		}.addToMenuBar(menuBar);


		// Listeners
		functionCloseProject.setDisable(true);
		functionSaveProject.setDisable(true);
		Data.projectProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				functionSaveProject.setDisable(true);
				functionCloseProject.setDisable(true);
				closeTabs();
			} else {
				functionSaveProject.setDisable(false);
				functionCloseProject.setDisable(false);
				openTabs();
			}
		});


	}




	/**
	 * shows a dialog and asks user if he wants to save project before closing it.
	 *
	 * @return true, if the project was closed (with or without saving it); false, if the user cancelled the action
	 */
	private boolean handleOpenProject() {

		// get project
		if (Data.projectProperty.get() == null) {
			return false;
		}
		Project project = Data.projectProperty.get();

		// handle project
		ButtonType alertSaveResult = Alerts.confirmation("Save current Project before closing?",
				"Current project: " + project.settings.name);

		if (alertSaveResult == ButtonType.YES) {
			ProjectLogic.saveProject(project);
			ProjectLogic.closeCurrentProject();
			return true;
		}

		if (alertSaveResult == ButtonType.NO) {
			ProjectLogic.closeCurrentProject();
			return true;
		}

		if (alertSaveResult == ButtonType.CANCEL) {
			return false;
		}

		return false;
	}




	private void openTabs() {
		ProjectSettingsView viewProjectSettings = new ProjectSettingsView();
		AnchorUtils.setAnchors(viewProjectSettings, 0, 0, 0, 0);
		Tab tabProjectSettings = new Tab(ProjectSettingsView.TITLE);
		tabProjectSettings.setContent(viewProjectSettings);
		tabPane.getTabs().add(tabProjectSettings);
	}




	private void closeTabs() {
		tabPane.getTabs().clear();
	}

}
