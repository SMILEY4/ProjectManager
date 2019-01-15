package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
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

		// filter
		btnFilter.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			FilterPopup popup = new FilterPopup(stage);
			Scene scene = new Scene(popup, 500, 300);
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
			Scene scene = new Scene(popup, 500, 300);
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








