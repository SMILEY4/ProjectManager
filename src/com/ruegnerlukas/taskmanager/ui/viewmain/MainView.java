package com.ruegnerlukas.taskmanager.ui.viewmain;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewmain.notifications.NotificationArea;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.ProjectSettingsView;
import com.ruegnerlukas.taskmanager.ui.viewtasks.TaskView;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MenuFunction;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;


public class MainView extends AnchorPane {


	@FXML private MenuBar menuBar;
	@FXML private TabPane tabPane;

	@FXML private AnchorPane paneInfobar;
	@FXML private Label labelInfobar;
	@FXML private Button btnExpandNotifications;

	@FXML private AnchorPane paneNotifications;
	@FXML private ScrollPane scrollNotifications;
	@FXML private VBox boxNotifications;


	private NotificationArea notificationArea;

	private MenuFunction functionCloseProject;

	private MainViewModule moduleProjectSettings;
	private MainViewModule moduleTabs;




	public MainView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_MAIN, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading MainView-FXML", e);
		}

		create();
	}




	private void create() {

		notificationArea = new NotificationArea(labelInfobar, btnExpandNotifications, paneNotifications, scrollNotifications, boxNotifications);

		// Create new local Project
		MenuFunction functionNewProject = new MenuFunction("File", "New Local Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					ProjectLogic.closeCurrentProject();
				}
				DirectoryChooser dirChooser = new DirectoryChooser();
				dirChooser.setTitle("Choose Project Directory");
				File selectedDir = dirChooser.showDialog(TaskManager.getPrimaryStage());
				if (selectedDir != null && selectedDir.exists()) {
					ProjectLogic.setCurrentProject(ProjectLogic.createNewLocalProject(selectedDir, selectedDir.getName()));
				}
			}
		}.addToMenuBar(menuBar);


		// open local project
		MenuFunction functionOpenProjectLocal = new MenuFunction("File", "Open Local Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					ProjectLogic.closeCurrentProject();
				}
				DirectoryChooser dirChooser = new DirectoryChooser();
				dirChooser.setTitle("Choose Project Directory");
				File selectedDir = dirChooser.showDialog(TaskManager.getPrimaryStage());
				if (selectedDir != null && selectedDir.exists()) {
					ProjectLogic.setCurrentProject(ProjectLogic.loadLocalProject(selectedDir));
				}
			}
		}.addToMenuBar(menuBar);


		// close project
		functionCloseProject = new MenuFunction("File", "Close Project") {
			@Override
			public void onAction() {
				if (Data.projectProperty.get() != null) {
					ProjectLogic.closeCurrentProject();
				}
			}
		}.addToMenuBar(menuBar);
		functionCloseProject.setDisable(true);


		// separator
		menuBar.getMenus().get(0).getItems().add(new SeparatorMenuItem());


		// openNew console
		MenuFunction functionOpenConsole = new MenuFunction("File", "Open console") {
			@Override
			public void onAction() {
				ConsoleWindowHandler.openNew();
			}
		}.addToMenuBar(menuBar);


		// Used for various debug purposes
		MenuFunction functionDebug = new MenuFunction("Dev", "Debug") {
			@Override
			public void onAction() {
				Logger.get().info("Info " + System.currentTimeMillis());
				Logger.get().warn("Info " + System.currentTimeMillis());
				Logger.get().error("Info " + System.currentTimeMillis());
				Object o = null;
				String s = o.toString();
//				try {
//					throw new IllegalArgumentException("test exception");
//				} catch (Exception e) {
//					e.printStackTrace();
//					Logger.get().error("Exception Description", e);
//				}
			}
		}.addToMenuBar(menuBar);


		// listen for tab selection
		new FXChangeListener<Tab>(tabPane.getSelectionModel().selectedItemProperty()) {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				onTabSelected(oldValue, newValue);
			}
		};


		// listen project property
		new FXChangeListener<Project>(Data.projectProperty) {
			@Override
			public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
				if (newValue == null) {
					functionCloseProject.setDisable(true);
					closeTabs();
				} else {
					functionCloseProject.setDisable(false);
					openTabs();
				}
			}
		};
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

		// project data
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
