package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class TasksController implements IViewController {


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






	@Override
	public void create() {
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








