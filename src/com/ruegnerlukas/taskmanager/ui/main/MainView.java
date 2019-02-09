package com.ruegnerlukas.taskmanager.ui.main;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.TabContent;
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

		create();
		setupMenuFunctions();
		setupListeners();
	}




	private void create() {

		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null && oldValue.getContent() instanceof TabContent) {
				((TabContent) oldValue.getContent()).onHide();
			}
			if (newValue != null && newValue.getContent() instanceof TabContent) {
				((TabContent) newValue.getContent()).onShow();
			}
		});

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
				Logic.project.getIsProjectOpen(new Request<Boolean>(true) {
					@Override
					public void onResponse(Response<Boolean> response) {
						if (response.getValue()) {
							if (handleOpenProject()) {
								Logic.project.createProject();
							}
						} else {
							Logic.project.createProject();
						}
					}
				});
			}
		}.addToMenuBar(menuBar);


		// Open presets project
		MenuFunction functionOpenProject = new MenuFunction("File", "Open Project") {
			@Override
			public void onAction() {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select project root-file.");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File file = fileChooser.showOpenDialog(ViewManager.getPrimaryStage());

				if (file != null) {

					Logic.project.getIsProjectOpen(new Request<Boolean>(true) {
						@Override
						public void onResponse(Response<Boolean> response) {
							if (response.getValue()) {
								if (handleOpenProject()) {
									Logic.project.loadProject(file);
								}
							} else {
								Logic.project.loadProject(file);
							}
						}
					});

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
					if (file.exists()) {
						Logic.project.getIsProjectOpen(new Request<Boolean>(true) {
							@Override
							public void onResponse(Response<Boolean> response) {
								if (response.getValue()) {
									if (handleOpenProject()) {
										Logic.project.loadProject(file);
									}
								} else {
									Logic.project.loadProject(file);
								}
							}
						});
					}
				}
			};

			fctOpenRecentFile.addToMenuBar(menuBar);
		}


		// save project
		functionSaveProject = new MenuFunction("File", "Save") {
			@Override
			public void onAction() {

				Logic.project.getCurrentProject(new Request<Project>(true) {
					@Override
					public void onResponse(Response<Project> response) {
						Project project = response.getValue();
						Logic.project.saveProject();
						Alerts.info("Project has been presets.", project.name);
					}
				});

			}
		}.addToMenuBar(menuBar);
		functionSaveProject.setDisable(true);


		// close project
		functionCloseProject = new MenuFunction("File", "Close Project") {
			@Override
			public void onAction() {

				Logic.project.getIsProjectOpen(new Request<Boolean>(true) {
					@Override
					public void onResponse(Response<Boolean> response) {
						if (response.getValue()) {
							handleOpenProject();
						}
					}
				});

			}
		}.addToMenuBar(menuBar);
		functionCloseProject.setDisable(true);


		// exit application
		MenuFunction functionExit = new MenuFunction("File", "Exit") {
			@Override
			public void onAction() {

				Logic.project.getIsProjectOpen(new Request<Boolean>(true) {
					@Override
					public void onResponse(Response<Boolean> response) {
						if (response.getValue()) {
							if (handleOpenProject()) {
								// TODO exit application
								System.out.println("TODO: Exit application");
							}
						} else {
							// TODO exit application
							System.out.println("TODO: Exit application");
						}
					}
				});

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

		// get project
		SyncRequest<Project> requestProject = new SyncRequest<Project>();
		Logic.project.getCurrentProject(requestProject);
		if (requestProject.getResponse().getState() == Response.State.FAIL) {
			return false;
		}
		Project project = requestProject.getResponse().getValue();

		// handle project
		ButtonType alertSaveResult = Alerts.confirmation("Save Project current Project before closing?",
				"Current project: " + project.name);

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
		viewProjectSettings.onOpen();
	}




	private void closeTabProjectSettings() {
		Tab tab = findTab(viewProjectSettings);
		viewProjectSettings.onClose();
		tabPane.getTabs().remove(tab);
	}




	private void openTabTasks() {
		viewTasks = new TaskView();
		AnchorUtils.setAnchors(viewTasks, 0, 0, 0, 0);
		Tab tab = new Tab(TaskView.TITLE);
		tab.setContent(viewTasks);
		tabPane.getTabs().add(tab);
		viewTasks.onOpen();
	}




	private void closeTabTasks() {
		Tab tab = findTab(viewTasks);
		viewTasks.onClose();
		tabPane.getTabs().remove(tab);
	}




	private void openTabs() {
		openTabProjectSettings();
		openTabTasks();
	}




	private void closeAllTabs() {
		closeTabTasks();
		closeTabProjectSettings();
	}




	private Tab findTab(TabContent content) {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getContent() == content) {
				return tab;
			}
		}
		return null;
	}


}
