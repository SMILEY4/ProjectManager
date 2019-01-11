package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TasksController extends AnchorPane {


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




	public TasksController() {
		try {

			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_view_tasks.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskView-FXML: " + e);
		}

		setupListeners();
		createCustomElements();
	}




	private void createCustomElements() {

		ButtonUtils.makeIconButton(btnActions, SVGIcons.getHamburger(), 40, 40, "white");
		btnActions.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				ContextMenu popup = new ContextMenu();
//				for(int i=0; i<actionFunctions.size(); i++) {
//					MenuFunction func = actionFunctions.get(i);
//					func.addToContextMenu(popup);
//				}
				popup.show(btnActions, Side.BOTTOM, 0, 0);
			}
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








