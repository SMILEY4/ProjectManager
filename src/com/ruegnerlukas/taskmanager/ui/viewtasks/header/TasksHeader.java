package com.ruegnerlukas.taskmanager.ui.viewtasks.header;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter.PopupFilter;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupgroup.PopupGroup;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupmasterpreset.PopupMasterPreset;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupsort.PopupSort;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MenuFunction;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TasksHeader {


	@FXML private AnchorPane rootHeader;

	@FXML private Button btnFilter;
	@FXML private Button btnGroup;
	@FXML private Button btnSort;
	@FXML private Button btnPresets;
	@FXML private Button btnActions;

	@FXML private AnchorPane paneHeaderBadges;
	private Label badgeFilter;
	private Label badgeGroup;
	private Label badgeSort;
	private Label badgePresets;


	@FXML private Label labelNTasks;




	public TasksHeader() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_TASKS_HEADER, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksHeader-FXML: " + e);
		}
		create();
	}




	private void create() {

		// create badges
		paneHeaderBadges.setMouseTransparent(true);
		badgeFilter = createBadge(paneHeaderBadges, btnFilter);
		badgeGroup = createBadge(paneHeaderBadges, btnGroup);
		badgeSort = createBadge(paneHeaderBadges, btnSort);
		badgePresets = createBadge(paneHeaderBadges, btnPresets);

		// badge filter
		showBadge(badgeFilter, false);
		Data.projectProperty.get().data.filterData.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeFilter, newValue != null || Data.projectProperty.get().data.selectedFilterPreset.get() != null);
		}));
		Data.projectProperty.get().data.selectedFilterPreset.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeFilter, newValue != null);
		}));

		// badge group
		showBadge(badgeGroup, false);
		Data.projectProperty.get().data.groupData.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeGroup, newValue != null || Data.projectProperty.get().data.selectedGroupPreset.get() != null);
		}));
		Data.projectProperty.get().data.selectedGroupPreset.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeGroup, newValue != null);
		}));

		// badge sort
		showBadge(badgeSort, false);
		Data.projectProperty.get().data.sortData.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeSort, newValue != null || Data.projectProperty.get().data.selectedSortPreset.get() != null);
		}));
		Data.projectProperty.get().data.selectedSortPreset.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgeSort, newValue != null);
		}));

		// badge presets
		showBadge(badgePresets, false);
		Data.projectProperty.get().data.selectedMasterPreset.addListener(((observable, oldValue, newValue) -> {
			showBadge(badgePresets, newValue != null);
		}));



		// header buttons
		btnFilter.setOnAction(event -> openPopup(new PopupFilter()));
		btnGroup.setOnAction(event -> openPopup(new PopupGroup()));
		btnSort.setOnAction(event -> openPopup(new PopupSort()));
		btnPresets.setOnAction(event -> openPopup(new PopupMasterPreset()));

		// label n tasks
		Data.projectProperty.get().data.tasks.addListener((ListChangeListener<Task>) c -> {
			final int nTotal = Data.projectProperty.get().data.tasks.size();
			final int nDisplayed = TaskDisplayLogic.countDisplayedTasks(Data.projectProperty.get());
			labelNTasks.setText(nDisplayed + "/" + nTotal);
		});
		Data.projectProperty.get().temporaryData.listenersTaskGroupsChanged.add(event -> {
			final int nTotal = Data.projectProperty.get().data.tasks.size();
			final int nDisplayed = TaskDisplayLogic.countDisplayedTasks(Data.projectProperty.get());
			labelNTasks.setText(nDisplayed + "/" + nTotal);
		});

		// TODO actions
		ButtonUtils.makeIconButton(btnActions, SVGIcons.HAMBURGER, 0.7f, "white");
		btnActions.setOnAction(event -> {
			ContextMenu popup = new ContextMenu();
			MenuFunction funcCreateTask = new MenuFunction("Create Task") {
				@Override
				public void onAction() {
					Task task = TaskLogic.createTask(Data.projectProperty.get());
					ProjectLogic.addTaskToProject(Data.projectProperty.get(), task);
				}
			};
			funcCreateTask.addToContextMenu(popup);
			popup.show(btnActions, Side.BOTTOM, 0, 0);
		});

	}




	private void showBadge(Label badge, boolean show) {
		badge.setVisible(show);
	}




	private Label createBadge(AnchorPane pane, Button button) {

		Label badge = new Label("!");
		badge.setMinSize(14, 14);
		badge.setPrefSize(14, 14);
		badge.setMaxSize(14, 14);
		pane.getChildren().add(badge);
		button.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
			double x = newValue.getMinX();
			double y = newValue.getMinY();
			double w = newValue.getWidth();
			double h = newValue.getHeight();
			AnchorPane.setLeftAnchor(badge, x + w - 7 - 3);
			AnchorPane.setBottomAnchor(badge, y + h - 7 - 7);
		});

		return badge;
	}




	private void openPopup(TasksPopup popup) {
		Stage stage = new Stage();
		popup.setStage(stage);
		popup.create();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(TaskManager.getPrimaryStage());
		Scene scene = new Scene(popup, popup.getPopupWidth(), popup.getPopupHeight());
		scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
			if (ke.getCode() == KeyCode.R) {
				UIDataHandler.reloadAll();
				ke.consume();
			}
		});
		stage.setScene(scene);
		stage.show();
	}




	public AnchorPane getAnchorPane() {
		return this.rootHeader;
	}


}