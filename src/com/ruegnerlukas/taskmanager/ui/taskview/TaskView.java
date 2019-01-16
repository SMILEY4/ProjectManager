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
		create();
	}




	private void create() {

		// badges
		paneHeaderBadges.setMouseTransparent(true);

		badgeFilter = new Label("!");
		badgeFilter.setMinSize(14, 14);
		badgeFilter.setPrefSize(14, 14);
		badgeFilter.setMaxSize(14, 14);
		paneHeaderBadges.getChildren().add(badgeFilter);
		btnFilter.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				double x = newValue.getMinX();
				double y = newValue.getMinY();
				double w = newValue.getWidth();
				double h = newValue.getHeight();
				AnchorPane.setLeftAnchor(badgeFilter, x+w-7-3);
				AnchorPane.setBottomAnchor(badgeFilter, y+h-7-7);

			}
		});

		int nFilter = Logic.project.getProject().filterCriteria.size();
		if(0 < nFilter && nFilter < 10) {
			badgeFilter.setText(""+nFilter);
			badgeFilter.setVisible(true);
		} else if(nFilter >= 10) {
			badgeFilter.setText("!");
			badgeFilter.setVisible(true);
		} else {
			badgeFilter.setText("");
			badgeFilter.setVisible(false);
		}

		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				int n = ((FilterCriteriaChangedEvent)e).getFilterCriteria().size();
				if(0 < n && n < 10) {
					badgeFilter.setText(""+n);
					badgeFilter.setVisible(true);
				} else if(n >= 10) {
					badgeFilter.setText("!");
					badgeFilter.setVisible(true);
				} else {
					badgeFilter.setText("");
					badgeFilter.setVisible(false);
				}
			}
		}, FilterCriteriaChangedEvent.class);


		badgeGroupBy = new Label("!");
		badgeGroupBy.setMinSize(14, 14);
		badgeGroupBy.setPrefSize(14, 14);
		badgeGroupBy.setMaxSize(14, 14);
		paneHeaderBadges.getChildren().add(badgeGroupBy);
		btnGroup.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				double x = newValue.getMinX();
				double y = newValue.getMinY();
				double w = newValue.getWidth();
				double h = newValue.getHeight();
				AnchorPane.setLeftAnchor(badgeGroupBy, x+w-7-3);
				AnchorPane.setBottomAnchor(badgeGroupBy, y+h-7-7);

			}
		});

		int nGroupBy = Logic.project.getProject().groupByOrder.size();
		if(0 < nGroupBy && nGroupBy < 10) {
			badgeGroupBy.setText(""+nGroupBy);
			badgeGroupBy.setVisible(true);
		} else if(nGroupBy >= 10) {
			badgeGroupBy.setText("!");
			badgeGroupBy.setVisible(true);
		} else {
			badgeGroupBy.setText("");
			badgeGroupBy.setVisible(false);
		}

		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				int n = ((GroupByOrderChangedEvent)e).getAttributes().size();
				if(0 < n && n < 10) {
					badgeGroupBy.setText(""+n);
					badgeGroupBy.setVisible(true);
				} else if(n >= 10) {
					badgeGroupBy.setText("!");
					badgeGroupBy.setVisible(true);
				} else {
					badgeGroupBy.setText("");
					badgeGroupBy.setVisible(false);
				}
			}
		}, GroupByOrderChangedEvent.class);


		badgeSort = new Label("!");
		badgeSort.setMinSize(14, 14);
		badgeSort.setPrefSize(14, 14);
		badgeSort.setMaxSize(14, 14);
		paneHeaderBadges.getChildren().add(badgeSort);
		btnSort.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				double x = newValue.getMinX();
				double y = newValue.getMinY();
				double w = newValue.getWidth();
				double h = newValue.getHeight();
				AnchorPane.setLeftAnchor(badgeSort, x+w-7-3);
				AnchorPane.setBottomAnchor(badgeSort, y+h-7-7);

			}
		});

		int nSort = Logic.project.getProject().sortElements.size();
		if(0 < nSort && nSort < 10) {
			badgeSort.setText(""+nSort);
			badgeSort.setVisible(true);
		} else if(nSort >= 10) {
			badgeSort.setText("!");
			badgeSort.setVisible(true);
		} else {
			badgeSort.setText("");
			badgeSort.setVisible(false);
		}

		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				int n = ((SortElementsChangedEvent)e).getSortElements().size();
				if(0 < n && n < 10) {
					badgeSort.setText(""+n);
					badgeSort.setVisible(true);
				} else if(n >= 10) {
					badgeSort.setText("!");
					badgeSort.setVisible(true);
				} else {
					badgeSort.setText("");
					badgeSort.setVisible(false);
				}
			}
		}, SortElementsChangedEvent.class);


		// filter
		btnFilter.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			FilterPopup popup = new FilterPopup(stage);
			Scene scene = new Scene(popup, 1000, 400);
			stage.setScene(scene);
			stage.show();
		});


		// group by
		btnGroup.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			GroupByPopup popup = new GroupByPopup(stage);
			Scene scene = new Scene(popup, 500, 300);
			stage.setScene(scene);
			stage.show();
		});


		// sort
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
//				for(int i=0; i<actionFunctions.size(); i++) {
//					MenuFunction func = actionFunctions.get(i);
//					func.addToContextMenu(popup);
//				}
			popup.show(btnActions, Side.BOTTOM, 0, 0);
		});


		// sidebar show/hide
		if(sidebarHidden) {
			splitContent.setDividerPosition(0, 1);
			labelHideSidebar.setText("<");
		} else {
			splitContent.setDividerPosition(0, 0.75);
			labelHideSidebar.setText(">");
		}

		splitContent.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.doubleValue() > 0.95) {
					splitContent.setDividerPosition(0, 1);
					labelHideSidebar.setText("<");
					sidebarHidden = true;
				} else {
					labelHideSidebar.setText(">");
					sidebarHidden = false;
				}
			}
		});

		LabelUtils.makeAsButton(labelHideSidebar, "-fx-background-color: #c9c9c9;", "-fx-background-color: #bcbcbc;", new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				if(event.getButton() != MouseButton.PRIMARY) {
					return;
				}
				sidebarHidden = !sidebarHidden;
				if(sidebarHidden) {
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



	private void setupListeners() {
	}






}








