package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.FilterCriteriaChangedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByOrderChangedEvent;
import com.ruegnerlukas.taskmanager.eventsystem.events.SortElementsChangedEvent;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.FilterPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.groupPopup.GroupByPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.sortPopup.SortPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.tasklist.TaskList;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskView extends AnchorPane {


	public static final String TITLE = "Tasks";

	@FXML private AnchorPane rootTaskView;

	@FXML private AnchorPane paneHeader;
	@FXML private AnchorPane paneHeaderBadges;
	private Label badgeFilter;
	private Label badgeGroupBy;
	private Label badgeSort;


	@FXML private Button btnFilter;
	@FXML private Button btnGroup;
	@FXML private Button btnSort;
	@FXML private Button btnActions;

	@FXML private SplitPane splitContent;
	@FXML private AnchorPane paneContent;

	@FXML private ScrollPane scollTasks;
	@FXML private AnchorPane paneTasks;
	@FXML private HBox boxTasks;

	@FXML private Label labelHideSidebar;
	@FXML private AnchorPane paneSidebar;
	private boolean sidebarHidden = true;




	public TaskView() {
		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_view_tasks.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskView-FXML: " + e);
		}

		setupListeners();
		createHeader();
		createSidebar();
		createContent();
	}




	private void createHeader() {

		// badges
		paneHeaderBadges.setMouseTransparent(true);
		badgeFilter = createButtonBadge(paneHeaderBadges, btnFilter);
		badgeGroupBy = createButtonBadge(paneHeaderBadges, btnGroup);
		badgeSort = createButtonBadge(paneHeaderBadges, btnSort);


		// button filter
		btnFilter.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			FilterPopup popup = new FilterPopup(stage);
			Scene scene = new Scene(popup, 1000, 400);
			stage.setScene(scene);
			stage.show();
		});


		// button group-by
		btnGroup.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			GroupByPopup popup = new GroupByPopup(stage);
			Scene scene = new Scene(popup, 500, 300);
			stage.setScene(scene);
			stage.show();
		});


		// button sort
		btnSort.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			SortPopup popup = new SortPopup(stage);
			Scene scene = new Scene(popup, 600, 300);
			stage.setScene(scene);
			stage.show();
		});


		// hamburger menu
		ButtonUtils.makeIconButton(btnActions, SVGIcons.HAMBURGER, 0.7f, "white");
		btnActions.setOnAction(event -> {
			ContextMenu popup = new ContextMenu();
			/*
			for(int i=0; i<actionFunctions.size(); i++) {
				MenuFunction func = actionFunctions.get(i);
				func.addToContextMenu(popup);
			}
			*/
			popup.show(btnActions, Side.BOTTOM, 0, 0);
		});

	}




	private void createSidebar() {

		// sidebar show/hide
		if (sidebarHidden) {
			splitContent.setDividerPosition(0, 1);
			labelHideSidebar.setText("<");
		} else {
			splitContent.setDividerPosition(0, 0.75);
			labelHideSidebar.setText(">");
		}

		// listen for dragging
		splitContent.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.doubleValue() > 0.95) {
					splitContent.setDividerPosition(0, 1);
					labelHideSidebar.setText("<");
					sidebarHidden = true;
				} else {
					labelHideSidebar.setText(">");
					sidebarHidden = false;
				}
			}
		});


		// transform label into button
		LabelUtils.makeAsButton(labelHideSidebar, "-fx-background-color: #c9c9c9;", "-fx-background-color: #bcbcbc;", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() != MouseButton.PRIMARY) {
					return;
				}
				sidebarHidden = !sidebarHidden;
				if (sidebarHidden) {
					splitContent.setDividerPosition(0, 1);
					labelHideSidebar.setText("<");
				} else {
					splitContent.setDividerPosition(0, 0.75);
					labelHideSidebar.setText(">");
				}
				event.consume();
			}
		});


	}




	private void createContent() {

		// TMP
		for (int i = 0; i < 10; i++) {
			Logic.tasks.createTask();
		}

		for (int i = 0; i < 3; i++) {
			TaskList list = new TaskList();
			boxTasks.getChildren().add(list);
		}

	}




	private Label createButtonBadge(AnchorPane pane, Button button) {

		// create badge-label
		Label badge = new Label("!");
		badge.setMinSize(14, 14);
		badge.setPrefSize(14, 14);
		badge.setMaxSize(14, 14);
		pane.getChildren().add(badge);
		button.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				double x = newValue.getMinX();
				double y = newValue.getMinY();
				double w = newValue.getWidth();
				double h = newValue.getHeight();
				AnchorPane.setLeftAnchor(badge, x + w - 7 - 3);
				AnchorPane.setBottomAnchor(badge, y + h - 7 - 7);

			}
		});


		// get number to display
		int nSort = 0;
		if (button == btnSort) {
			nSort = Logic.project.getProject().sortElements.size();
		}
		if (button == btnFilter) {
			nSort = Logic.project.getProject().filterCriteria.size();
		}
		if (button == btnGroup) {
			nSort = Logic.project.getProject().groupByOrder.size();
		}


		// set badge-text
		if (0 < nSort && nSort < 10) {
			badge.setText("" + nSort);
			badge.setVisible(true);
		} else if (nSort >= 10) {
			badge.setText("!");
			badge.setVisible(true);
		} else {
			badge.setText("");
			badge.setVisible(false);
		}


		// get event to listen to
		Class eventClass = null;
		if (button == btnSort) {
			eventClass = SortElementsChangedEvent.class;
		}
		if (button == btnFilter) {
			eventClass = FilterCriteriaChangedEvent.class;
		}
		if (button == btnGroup) {
			eventClass = GroupByOrderChangedEvent.class;
		}


		// listen for changes
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {

				// get number to display
				int n = 0;
				if (button == btnSort) {
					n = Logic.project.getProject().sortElements.size();
				}
				if (button == btnFilter) {
					n = Logic.project.getProject().filterCriteria.size();
				}
				if (button == btnGroup) {
					n = Logic.project.getProject().groupByOrder.size();
				}

				// set badge-text
				if (0 < n && n < 10) {
					badge.setText("" + n);
					badge.setVisible(true);
				} else if (n >= 10) {
					badge.setText("!");
					badge.setVisible(true);
				} else {
					badge.setText("");
					badge.setVisible(false);
				}

			}
		}, eventClass);


		return badge;
	}




	private void setupListeners() {
	}




	public void close() {
		EventManager.deregisterListeners(this);
	}


}








