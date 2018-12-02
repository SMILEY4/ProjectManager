package com.ruegnerlukas.taskmanager.ui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.ui.infobar.InfobarHandler;
import com.ruegnerlukas.taskmanager.utils.LoremIpsum;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainController implements IViewController {
	
	@FXML private MenuBar menuBar;
	@FXML private TabPane tabPane;

	@FXML private AnchorPane paneInfobar;
	@FXML private Label labelInfobar;
	
	private MenuFunction functionNewProject;
	private MenuFunction functionOpenProject;
	private MenuFunction functionOpenRemoteProject;
	private List<MenuFunction> functionsOpenRecentProject = new ArrayList<MenuFunction>();
	private MenuFunction functionSaveProject;
	private MenuFunction functionCloseProject;
	private MenuFunction functionExit;

	private MenuFunction functionSettings;
	private MenuFunction functionKeyBindings;
	private MenuFunction functionAbout;

	
	
	
	@Override
	public void create() {
		
		InfobarHandler infobarHandler = new InfobarHandler();
		infobarHandler.create(paneInfobar, labelInfobar);
		
		setupListeners();
		setupMenuFunctions();
	}
	
	
	
	
	private void setupListeners() {
	
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event event) {
				functionCloseProject.setDisable(true);
				functionSaveProject.setDisable(true);
				closeAllTabs();
			}
		}, ProjectClosedEvent.class);
		
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event event) {
				functionCloseProject.setDisable(false);
				functionSaveProject.setDisable(false);
				openAllTabs();
			}
		}, ProjectCreatedEvent.class);
	
	}
	
	
	
	
	private void setupMenuFunctions() {
		
		// FILE
		functionNewProject = new MenuFunction("File", "New Project") {
			@Override public void onAction() {

				// save/close last project
				if(DataService.project.getProject() != null) {
					ButtonType alertSaveResult = Alerts.confirmation("Save current Project before closing?", "Current project: " + DataService.project.getProject().name);
					if(alertSaveResult == ButtonType.YES) {
						DataService.project.saveProject();
						DataService.project.closeProject();
					}
					if(alertSaveResult == ButtonType.NO) {
						DataService.project.closeProject();
					}
					if(alertSaveResult == ButtonType.CANCEL) {
						return;
					}
				}
				
				// create new
				DataService.project.createProject();
				
			}
		}.addToMenuBar(menuBar);
		
		
		functionOpenProject = new MenuFunction("File", "Open Project") { @Override public void onAction() {
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select project root-file.");
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File file = fileChooser.showOpenDialog(ViewManager.getPrimaryStage());
			
			if(file == null) {
				return;
				
			} else {
				
				if(DataService.project.getProject() != null) {
					ButtonType alertSaveResult = Alerts.confirmation("Save Project current Project before closing?", "Current project: " + DataService.project.getProject().name);
					if(alertSaveResult == ButtonType.YES) {
						DataService.project.saveProject();
						DataService.project.closeProject();
					}
					if(alertSaveResult == ButtonType.NO) {
						DataService.project.closeProject();
					}
					if(alertSaveResult == ButtonType.CANCEL) {
						return;
					}
				}

				DataService.project.loadProject(file);
				
			}
			
		}}.addToMenuBar(menuBar);
		
		
		functionOpenRemoteProject = new MenuFunction("File", "Open Remote Project") { @Override public void onAction() {
			// TODO
			System.out.println("TODO: open remove project (->git?)");
		}}.addToMenuBar(menuBar);
		
		for(int i=0; i<5; i++) {
			final String filepath = LoremIpsum.get(2, 8, true);
			MenuFunction fctOpenRecentFile = new MenuFunction("File", "Open Recent", filepath) { @Override public void onAction() {


				File file = new File(filepath);
				if(!file.exists()) {
					return;
					
				} else {

					if(DataService.project.getProject() != null) {
						ButtonType alertSaveResult = Alerts.confirmation("Save current Project before closing?", "Current project: " + DataService.project.getProject().name);
						if(alertSaveResult == ButtonType.YES) {
							DataService.project.saveProject();
							DataService.project.closeProject();
						}
						if(alertSaveResult == ButtonType.NO) {
							DataService.project.closeProject();
						}
						if(alertSaveResult == ButtonType.CANCEL) {
							return;
						}
					}
					
					DataService.project.loadProject(file);
					
				}
				
			}};
			fctOpenRecentFile.addToMenuBar(menuBar);
			functionsOpenRecentProject.add(fctOpenRecentFile);
		}
		
		
		functionSaveProject = new MenuFunction("File", "Save") { @Override public void onAction() {
			if(DataService.project.getProject() != null) {
				DataService.project.saveProject();
				Alerts.info("Project has been saved.", DataService.project.getProject().name);
			}
		}}.addToMenuBar(menuBar);
		functionSaveProject.setDisable(true);
		
		functionCloseProject = new MenuFunction("File", "Close Project") { @Override public void onAction() {
			
			if(DataService.project.getProject() != null) {
			
				ButtonType alertSaveResult = Alerts.confirmation("Save Project current Project before closing?", "Current project: " + DataService.project.getProject().name);
				if(alertSaveResult == ButtonType.YES) {
					DataService.project.saveProject();
				}
				if(alertSaveResult == ButtonType.NO) {
				}
				if(alertSaveResult == ButtonType.CANCEL) {
					return;
				}
				
				DataService.project.closeProject();

			}
		}}.addToMenuBar(menuBar);
		functionCloseProject.setDisable(true);
		
		functionExit = new MenuFunction("File", "Exit") { @Override public void onAction() {
			
			if(DataService.project.getProject() != null) {
				ButtonType alertSaveResult = Alerts.confirmation("Save current Project before closing?", "Current project: " + DataService.project.getProject().name);
				if(alertSaveResult == ButtonType.YES) {
					DataService.project.saveProject();
				}
				if(alertSaveResult == ButtonType.NO) {
				}
				if(alertSaveResult == ButtonType.CANCEL) {
					return;
				}
				DataService.project.closeProject();
			}
			// TODO exit application
			System.out.println("TODO: Exit application");
		}}.addToMenuBar(menuBar);
		
		
		
		
		// PREFERENCES
		functionSettings = new MenuFunction("Preferences", "Settings") { @Override public void onAction() {
			// TODO
			System.out.println("TODO: Open Settings");
		}}.addToMenuBar(menuBar);
		
		
		functionKeyBindings = new MenuFunction("Preferences", "Key Bindings") { @Override public void onAction() {
			// TODO
			System.out.println("TODO: Open KeyBindings");
		}}.addToMenuBar(menuBar);
		
		
		
		
		// HELP
		functionAbout = new MenuFunction("Help", "About") { @Override public void onAction() {
			// TODO
			System.out.println("TODO: Open About");	
		}}.addToMenuBar(menuBar);

	}
	



	private void openAllTabs() {
		openTap_projectSettings();
		openTap_dashboard();
		openTap_tasks();
		openTap_documentation();
		openTap_team();
	}
	
	
	
	
	public void closeAllTabs() {
		closeTap_projectSettings();
		closeTap_dashboard();
		closeTap_tasks();
		closeTap_documentation();
		closeTap_team();
	}
	
	
	
	
	private void openTap_projectSettings() {
		Tab tab = new Tab("Project Settings");
		AnchorPane paneTasks = new AnchorPane();
		ViewManager.getLoader("view_projectsettings").load(null);
		Parent rootTasks = ViewManager.getRoot("view_projectsettings");
		AnchorPane.setTopAnchor(rootTasks, 0.0);
		AnchorPane.setBottomAnchor(rootTasks, 0.0);
		AnchorPane.setLeftAnchor(rootTasks, 0.0);
		AnchorPane.setRightAnchor(rootTasks, 0.0);
		paneTasks.getChildren().add(rootTasks);
		tab.setContent(paneTasks);
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




	private void openTap_dashboard() {
		Tab tab = new Tab("Dashboard");
		AnchorPane paneTasks = new AnchorPane();
		tab.setContent(paneTasks);
		tabPane.getTabs().add(tab);
	}




	private void closeTap_dashboard() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equalsIgnoreCase("Dashboard")) {
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}




	private void openTap_tasks() {
		Tab tab = new Tab("Tasks");
		AnchorPane paneTasks = new AnchorPane();
		ViewManager.getLoader("view_tasks").load(null);
		Parent rootTasks = ViewManager.getRoot("view_tasks");
		AnchorPane.setTopAnchor(rootTasks, 0.0);
		AnchorPane.setBottomAnchor(rootTasks, 0.0);
		AnchorPane.setLeftAnchor(rootTasks, 0.0);
		AnchorPane.setRightAnchor(rootTasks, 0.0);
		paneTasks.getChildren().add(rootTasks);
		tab.setContent(paneTasks);
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




	private void openTap_documentation() {
		Tab tab = new Tab("Documentation");
		tab.setId("tabDocumentation");
		AnchorPane paneTasks = new AnchorPane();
		tab.setContent(paneTasks);
		tabPane.getTabs().add(tab);
	}




	private void closeTap_documentation() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equalsIgnoreCase("Documentation")) {
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}




	private void openTap_team() {
		Tab tab = new Tab("Team");
		AnchorPane paneTasks = new AnchorPane();
		tab.setContent(paneTasks);
		tabPane.getTabs().add(tab);
	}




	private void closeTap_team() {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equalsIgnoreCase("Team")) {
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}
	
}
