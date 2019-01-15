package com.ruegnerlukas.taskmanager.ui.main;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.LogicService;
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
				closeTap_projectSettings();
				closeTap_tasks();
			}
		}, ProjectClosedEvent.class);

		// listen for project created
		EventManager.registerListener(new EventListener() {
			@Override
			public void onEvent(Event event) {
				functionCloseProject.setDisable(false);
				functionSaveProject.setDisable(false);
				openTabProjectSettings();
				openTap_tasks();
			}
		}, ProjectCreatedEvent.class);

	}




	private void setupMenuFunctions() {

		// Create new empty Project
		// save/close last project
		MenuFunction functionNewProject = new MenuFunction("File", "New Project") {
			@Override
			public void onAction() {
				// save/close last project
				if (LogicService.get().isProjectOpen()) {
					if (handleOpenProject()) {
						LogicService.get().createProject();
					}
				} else {
					LogicService.get().createProject();
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
					if (LogicService.get().isProjectOpen()) {
						if (handleOpenProject()) {
							LogicService.get().loadProject(file);
						}
					} else {
						LogicService.get().loadProject(file);
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
						if (LogicService.get().isProjectOpen()) {
							if (handleOpenProject()) {
								LogicService.get().loadProject(file);
							}
						} else {
							LogicService.get().loadProject(file);
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
				if (LogicService.get().isProjectOpen()) {
					LogicService.get().saveProject();
					Alerts.info("Project has been saved.", LogicService.get().getProject().name);
				}
			}
		}.addToMenuBar(menuBar);
		functionSaveProject.setDisable(true);


		// close project
		functionCloseProject = new MenuFunction("File", "Close Project") {
			@Override
			public void onAction() {
				if (LogicService.get().isProjectOpen()) {
					handleOpenProject();
				}
			}
		}.addToMenuBar(menuBar);
		functionCloseProject.setDisable(true);


		// exit application
		MenuFunction functionExit = new MenuFunction("File", "Exit") {
			@Override
			public void onAction() {
				if (LogicService.get().isProjectOpen()) {
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
	 * @return true, if the project was closed (with or without saving it); false, if the user cancelled the actions
	 */
	private boolean handleOpenProject() {
		if (!LogicService.get().isProjectOpen()) {
			return false;
		}

		ButtonType alertSaveResult = Alerts.confirmation("Save Project current Project before closing?", "Current project: " + LogicService.get().getProject().name);

		if (alertSaveResult == ButtonType.YES) {
			LogicService.get().saveProject();
			LogicService.get().closeProject();
			return true;
		}

		if (alertSaveResult == ButtonType.NO) {
			LogicService.get().closeProject();
			return true;
		}

		if (alertSaveResult == ButtonType.CANCEL) {
			return false;
		}
		return false;
	}




	private void openTabProjectSettings() {
		Tab tab = new Tab("Project Settings");
		ProjectSettingsView projectSettingsView = new ProjectSettingsView();
		AnchorUtils.setAnchors(projectSettingsView, 0, 0, 0, 0);
		tab.setContent(projectSettingsView);
		tabPane.getTabs().add(tab);
	}




	private void closeTap_projectSettings() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equalsIgnoreCase("Project Settings")) {
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}




	private void openTap_tasks() {
		Tab tab = new Tab("Tasks");
		TaskView taskView = new TaskView();
		AnchorUtils.setAnchors(taskView, 0, 0, 0, 0);
		tab.setContent(taskView);
		tabPane.getTabs().add(tab);
	}




	private void closeTap_tasks() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equalsIgnoreCase("Tasks")) {
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}





}
