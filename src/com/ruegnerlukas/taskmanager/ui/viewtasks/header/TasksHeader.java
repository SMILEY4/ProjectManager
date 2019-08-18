package com.ruegnerlukas.taskmanager.ui.viewtasks.header;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskDisplayLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter.PopupFilter;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupgroup.PopupGroup;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupmasterpreset.PopupMasterPreset;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupsort.PopupSort;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXEventAdapter;
import com.ruegnerlukas.taskmanager.utils.listeners.FXGenericChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MenuFunction;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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

	private FXGenericChangeListener listenerBadgeFilter;
	private FXGenericChangeListener listenerBadgeGroup;
	private FXGenericChangeListener listenerBadgeSort;
	private FXGenericChangeListener listenerBadgePresets;
	private FXEventAdapter listenerNTasks;




	public TasksHeader() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_TASKS_HEADER, this);
		} catch (IOException e) {
			Logger.get().error("Error loading TasksHeader-FXML.", e);
		}
		create();
	}




	private void create() {

		rootHeader.getStyleClass().add("tasks-header");

		// create badges
		paneHeaderBadges.setMouseTransparent(true);
		badgeFilter = createBadge(paneHeaderBadges, btnFilter);
		badgeGroup = createBadge(paneHeaderBadges, btnGroup);
		badgeSort = createBadge(paneHeaderBadges, btnSort);
		badgePresets = createBadge(paneHeaderBadges, btnPresets);

		badgeFilter.getStyleClass().addAll("label-badge", "badge-filter");
		badgeGroup.getStyleClass().addAll("label-badge", "badge-group");
		badgeSort.getStyleClass().addAll("label-badge", "badge-sort");
		badgePresets.getStyleClass().addAll("label-badge", "badge-presets");


		// badge filter
		showBadge(badgeFilter, false);
		listenerBadgeFilter = new FXGenericChangeListener(Data.projectProperty.get().data.filterData, Data.projectProperty.get().data.presetSelectedFilter) {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				showBadge(badgeFilter, newValue != null || Data.projectProperty.get().data.presetSelectedFilter.get() != null);
			}
		};


		// badge group
		showBadge(badgeGroup, false);
		listenerBadgeGroup = new FXGenericChangeListener(Data.projectProperty.get().data.groupData, Data.projectProperty.get().data.presetSelectedGroup) {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				showBadge(badgeGroup, newValue != null || Data.projectProperty.get().data.presetSelectedGroup.get() != null);
			}
		};


		// badge sort
		showBadge(badgeSort, false);
		listenerBadgeSort = new FXGenericChangeListener(Data.projectProperty.get().data.sortData, Data.projectProperty.get().data.presetSelectedSort) {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				showBadge(badgeSort, newValue != null || Data.projectProperty.get().data.presetSelectedSort.get() != null);
			}
		};


		// badge presets
		showBadge(badgePresets, false);
		listenerBadgePresets = new FXGenericChangeListener(Data.projectProperty.get().data.presetSelectedMaster) {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				showBadge(badgePresets, newValue != null);
			}
		};


		// header buttons
		btnFilter.setOnAction(event -> PopupBase.openPopup(new PopupFilter(), false));
		btnGroup.setOnAction(event -> PopupBase.openPopup(new PopupGroup(), false));
		btnSort.setOnAction(event -> PopupBase.openPopup(new PopupSort(), false));
		btnPresets.setOnAction(event -> PopupBase.openPopup(new PopupMasterPreset(), false));


		// label n tasks
		labelNTasks.getStyleClass().add("label-num-tasks");
		listenerNTasks = new FXEventAdapter() {
			@Override
			public void handle(ActionEvent event) {
				onNumDisplayedTasksChanged();
			}
		}.addTo(Data.projectProperty.get().data.tasks);
		Data.projectProperty.get().temporaryData.listenersTaskGroupsChanged.add(listenerNTasks.getEventHandler());


		// TODO actions hamburger
		btnActions.getStyleClass().add("actions-button");
		ButtonUtils.makeIconButton(btnActions, SVGIcons.HAMBURGER, 0.7f);
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




	/**
	 * shows/hides the given badge.
	 */
	private void showBadge(Label badge, boolean show) {
		badge.setVisible(show);
	}




	/**
	 * Creates a new badge/{@link Label} in the given {@link AnchorPane} for the give {@link Button}.
	 *
	 * @return the creates badge/{@link Label}
	 */
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




	/**
	 * Called when the amount of displayed tasks changes.
	 */
	public void onNumDisplayedTasksChanged() {
		final int nTotal = Data.projectProperty.get().data.tasks.size();
		final int nDisplayed = TaskDisplayLogic.countDisplayedTasks(Data.projectProperty.get());
		labelNTasks.setText(nDisplayed + "/" + nTotal);
	}




	public AnchorPane getAnchorPane() {
		return this.rootHeader;
	}




	public void dispose() {
		listenerBadgeFilter.removeFromAll();
		listenerBadgeGroup.removeFromAll();
		listenerBadgeSort.removeFromAll();
		listenerBadgePresets.removeFromAll();
		listenerNTasks.removeFromAll();
	}

}
