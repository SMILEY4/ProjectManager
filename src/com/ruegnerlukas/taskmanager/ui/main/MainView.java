package com.ruegnerlukas.taskmanager.ui.main;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.ProjectSettingsView;
import com.ruegnerlukas.taskmanager.ui.taskview.TaskView;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.LoremIpsum;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class MainView extends AnchorPane {


	@FXML private MenuBar menuBar;
	@FXML private TabPane tabPane;

	@FXML private AnchorPane paneInfobar;
	@FXML private Label labelInfobar;

	private MenuFunction functionSaveProject;
	private MenuFunction functionCloseProject;

	private ProjectSettingsView viewProjectSettings;
	private TaskView viewTasks;



	public MainView() {
		try {

			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_view_main.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading MainView-FXML: " + e);
		}

		setupMenuFunctions();
		setupListeners();
	}




	private void setupListeners() {

		// listen for project closed
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event event) {
				functionCloseProject.setDisable(true);
				functionSaveProject.setDisable(true);
				closeAllTabs();
			}
		}, ProjectClosedEvent.class);

		// listen for project created
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event event) {
				functionCloseProject.setDisable(false);
				functionSaveProject.setDisable(false);
				openTabs();
			}
		}, ProjectCreatedEvent.class);

	}




	private void setupMenuFunctions() {

		// Create new empty Project
		MenuFunction functionNewProject = new MenuFunction("File", "New Project") {
			@Override
			public void onAction() {
				// save/close last project
				if (Logic.project.isProjectOpen()) {
					if (handleOpenProject()) {
						Logic.project.createProject();
					}
				} else {
					Logic.project.createProject();
				}
			}
		}.addToMenuBar(menuBar);


		// Open saved project
		MenuFunction functionOpenProject = new MenuFunction("File", "Open Project") {
			@Override
			public void onAction() {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select project root-file.");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File file = fileChooser.showOpenDialog(ViewManager.getPrimaryStage());
				if (file == null) {
					return;
				} else {
					if (Logic.project.isProjectOpen()) {
						if (handleOpenProject()) {
							Logic.project.loadProject(file);
						}
					} else {
						Logic.project.loadProject(file);
					}
				}
			}
		}.addToMenuBar(menuBar);


		// open a remote project
		MenuFunction functionOpenRemoteProject = new MenuFunction("File", "Open Remote Project") {
			@Override
			public void onAction() {
				// TODO
				System.out.println("TODO: open remove project (->git?)");
			}
		}.addToMenuBar(menuBar);


		// open recent project
		for (int i = 0; i < 5; i++) {
			final String filepath = LoremIpsum.get(2, 8, true);

			MenuFunction fctOpenRecentFile = new MenuFunction("File", "Open Recent", filepath) {
				@Override
				public void onAction() {
					File file = new File(filepath);
					if (!file.exists()) {
						return;
					} else {
						if (Logic.project.isProjectOpen()) {
							if (handleOpenProject()) {
								Logic.project.loadProject(file);
							}
						} else {
							Logic.project.loadProject(file);
						}
					}
				}
			};

			fctOpenRecentFile.addToMenuBar(menuBar);
		}


		// save project
		functionSaveProject = new MenuFunction("File", "Save") {
			@Override
			public void onAction() {
				if (Logic.project.isProjectOpen()) {
					Logic.project.saveProject();
					Alerts.info("Project has been saved.", Logic.project.getProject().name);
				}
			}
		}.addToMenuBar(menuBar);
		functionSaveProject.setDisable(true);


		// close project
		functionCloseProject = new MenuFunction("File", "Close Project") {
			@Override
			public void onAction() {
				if (Logic.project.isProjectOpen()) {
					handleOpenProject();
				}
			}
		}.addToMenuBar(menuBar);
		functionCloseProject.setDisable(true);


		// exit application
		MenuFunction functionExit = new MenuFunction("File", "Exit") {
			@Override
			public void onAction() {
				if (Logic.project.isProjectOpen()) {
					if (handleOpenProject()) {
						// TODO exit application
						System.out.println("TODO: Exit application");
					}
				} else {
					// TODO exit application
					System.out.println("TODO: Exit application");
				}
			}
		}.addToMenuBar(menuBar);


		// PREFERENCES
		MenuFunction functionSettings = new MenuFunction("Preferences", "Settings") {
			@Override
			public void onAction() {
				// TODO
				System.out.println("TODO: Open Settings");
			}
		}.addToMenuBar(menuBar);


		MenuFunction functionKeyBindings = new MenuFunction("Preferences", "Key Bindings") {
			@Override
			public void onAction() {
				// TODO
				System.out.println("TODO: Open KeyBindings");
			}
		}.addToMenuBar(menuBar);


		// HELP
		MenuFunction functionAbout = new MenuFunction("Help", "About") {
			@Override
			public void onAction() {
				// TODO
				System.out.println("TODO: Open About");
			}
		}.addToMenuBar(menuBar);

	}




	/**
	 * shows a dialog and asks user if he wants to save project before closing it.
	 *
	 * @return true, if the project was closed (with or without saving it); false, if the user cancelled the action
	 */
	private boolean handleOpenProject() {
		if (!Logic.project.isProjectOpen()) {
			return false;
		}

		ButtonType alertSaveResult = Alerts.confirmation("Save Project current Project before closing?",
				"Current project: " + Logic.project.getProject().name);

		if (alertSaveResult == ButtonType.YES) {
			Logic.project.saveProject();
			Logic.project.closeProject();
			return true;
		}

		if (alertSaveResult == ButtonType.NO) {
			Logic.project.closeProject();
			return true;
		}

		if (alertSaveResult == ButtonType.CANCEL) {
			return false;
		}
		return false;
	}




	private void openTabProjectSettings() {
		viewProjectSettings = new ProjectSettingsView();
		AnchorUtils.setAnchors(viewProjectSettings, 0, 0, 0, 0);
		Tab tab = new Tab(ProjectSettingsView.TITLE);
		tab.setContent(viewProjectSettings);
		tabPane.getTabs().add(tab);
	}




	private void closeTabProjectSettings() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getContent() == viewProjectSettings) {
				tabPane.getTabs().remove(tab);
				viewProjectSettings.close();
				break;
			}
		}
	}




	private void openTabTasks() {
		viewTasks = new TaskView();
		AnchorUtils.setAnchors(viewTasks, 0, 0, 0, 0);
		Tab tab = new Tab(TaskView.TITLE);
		tab.setContent(viewTasks);
		tabPane.getTabs().add(tab);
	}




	private void closeTabTasks() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getContent() == viewTasks) {
				viewTasks.close();
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}




	private void openTabs() {
		openTabProjectSettings();
		openTabTasks();
	}




	private void closeAllTabs() {
		closeTabTasks();
		closeTabProjectSettings();
	}


}
