package com.ruegnerlukas.taskmanager.ui.viewmain;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.ProjectSettingsView;
import com.ruegnerlukas.taskmanager.ui.viewtasks.TaskView;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MenuFunction;
import javafx.beans.value.ObservableValue;
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
 * - managing Menu-Functions (file:save,openNew,close;Preferences;About;...)
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
 * If a project is already openNew, the user has the choice to save and/or close that project
 * <p>
 * - Open Project
 * Opens a Project that was selected in the FileChooser.
 * If a project is already openNew, the user has the choice to save and/or close that project
 * <p>
 * - Open Recent
 * Opens a recently used Project.
 * If a project is already openNew, the user has the choice to save and/or close that project
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
 * If a project is currently openNew, the user has the choice to save and/or close that project
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

	private MenuFunction functionOpenConsole;

	private MainViewModule moduleProjectSettings;
	private MainViewModule moduleTabs;




	public MainView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_MAIN, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading MainView-FXML: " + e);
		}

		create();
	}




	private void create() {

		// Create new empty Project
		functionNewProject = new MenuFunction("File", "New Local Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					if (handleOpenProject()) {
						ProjectLogic.setCurrentProject(ProjectLogic.createNewLocalProject()); // todo temp
//						ProjectLogic.setCurrentProject(ProjectLogic.loadLocalProject()); // todo temp
					}
				} else {
					ProjectLogic.setCurrentProject(ProjectLogic.createNewLocalProject()); // todo temp
//					ProjectLogic.setCurrentProject(ProjectLogic.loadLocalProject()); // todo temp
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


		functionCloseProject.setDisable(true);
		functionSaveProject.setDisable(true);
		new FXChangeListener<Project>(Data.projectProperty) {
			@Override
			public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
				if (newValue == null) {
					functionSaveProject.setDisable(true);
					functionCloseProject.setDisable(true);
					closeTabs();
				} else {
					functionSaveProject.setDisable(false);
					functionCloseProject.setDisable(false);
					openTabs();
				}
			}
		};


		// seperator
		menuBar.getMenus().get(0).getItems().add(new SeparatorMenuItem());


		// openNew console
		functionOpenConsole = new MenuFunction("File", "Open console") {
			@Override
			public void onAction() {
				ConsoleWindowHandler.openNew();
			}
		}.addToMenuBar(menuBar);


		new FXChangeListener<Tab>(tabPane.getSelectionModel().selectedItemProperty()) {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				onTabSelected(oldValue, newValue);
			}
		};

	}




	/**
	 * shows a dialog and asks user if he wants to save the current project before closing it.
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




	/**
	 * Called when the given {@link Tab} was selected. Deselects the previous {@link Tab}.
	 */
	private void onTabSelected(Tab prevTab, Tab currTab) {
		if (prevTab != null && prevTab.getContent() instanceof MainViewModule) {
			MainViewModule prevModule = (MainViewModule) prevTab.getContent();
			prevModule.onModuleDeselected();
		}
		if (currTab != null && currTab.getContent() instanceof MainViewModule) {
			MainViewModule currModule = (MainViewModule) currTab.getContent();
			currModule.onModuleSelected();
		}
	}




	/**
	 * Opens all {@link Tab} and creates their modules.
	 */
	private void openTabs() {

		// project settings
		ProjectSettingsView viewProjectSettings = new ProjectSettingsView();
		AnchorUtils.setAnchors(viewProjectSettings, 0, 0, 0, 0);
		Tab tabProjectSettings = new Tab(ProjectSettingsView.TITLE);
		tabProjectSettings.setContent(viewProjectSettings);
		tabPane.getTabs().add(tabProjectSettings);

		this.moduleProjectSettings = viewProjectSettings;
		this.moduleProjectSettings.onModuleOpen();

		// projectdata
		TaskView viewTasks = new TaskView();
		AnchorUtils.setAnchors(viewTasks, 0, 0, 0, 0);
		Tab tabTaskView = new Tab(TaskView.TITLE);
		tabTaskView.setContent(viewTasks);
		tabPane.getTabs().add(tabTaskView);

		this.moduleTabs = viewTasks;
		this.moduleTabs.onModuleOpen();

	}




	/**
	 * Closes all tabs.
	 */
	private void closeTabs() {
		if (moduleProjectSettings != null) {
			moduleProjectSettings.onModuleClose();
		}
		if (moduleTabs != null) {
			moduleTabs.onModuleClose();
		}
		tabPane.getTabs().clear();
	}

}
