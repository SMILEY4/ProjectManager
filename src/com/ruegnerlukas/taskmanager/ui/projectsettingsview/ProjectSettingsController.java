package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedColorEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagRemovedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ProjectSettingsController implements IViewController {

	@FXML private AnchorPane rootProjectSettingsView;
	
	@FXML private AnchorPane paneHeader;
	@FXML private VBox boxFlags;
	
	
	
	@Override
	public void create() {
		
		// Project name
		EditableLabel labelName = new EditableLabel(DataService.project.getProject().name);
		labelName.getLabel().setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		labelName.getTextField().setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);
		
		labelName.addListener(new ChangeListener<String>(){
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				DataService.project.renameProject(newValue);
			}
		});
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectRenamedEvent event = (ProjectRenamedEvent)e;
				labelName.setText(event.getNewName());
			}
		}, ProjectRenamedEvent.class);
		
		
		// flags
		Button btnAddFlag = new Button("Add New Flag");
		btnAddFlag.setMinSize(150, 32);
		btnAddFlag.setMaxSize(150, 32);
		btnAddFlag.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.flags.addNewFlag("New Flag " + (DataService.flags.getFlags().size()+1), FlagColor.GRAY);
			}
		});
		boxFlags.getChildren().add(btnAddFlag);
		
		addFlagItem(boxFlags, DataService.flags.getDefaultFlag());
		for(TaskFlag flag : DataService.flags.getFlags()) {
			addFlagItem(boxFlags, flag);
		}


		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagAddedEvent event = (FlagAddedEvent)e;
				addFlagItem(boxFlags, event.getAddedFlag());
			}
		}, FlagAddedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagAddedRejection event = (FlagAddedRejection)e;
				Alerts.error("Adding flag was rejected. name="+event.getRejectedFlagName());
			}
		}, FlagAddedRejection.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagRemovedEvent event = (FlagRemovedEvent)e;
				removeFlagItem(boxFlags, event.getRemovedFlag().name);
			}
		}, FlagRemovedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedNameEvent event = (FlagChangedNameEvent)e;
				FlagItem item = findFlagItem(boxFlags, event.getOldName());
				item.update();
			}
		}, FlagChangedNameEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedNameRejection event = (FlagChangedNameRejection)e;
				Alerts.error("New name rejected. name=" + event.getNewName());
			}
		}, FlagChangedNameRejection.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedColorEvent event = (FlagChangedColorEvent)e;
				FlagItem item = findFlagItem(boxFlags, event.getFlag().name);
				item.update();
			}
		}, FlagChangedColorEvent.class);
		
	}

	
	
	
	private void addFlagItem(VBox container, TaskFlag flag) {
		container.getChildren().add(container.getChildren().size()-1, new FlagItem(flag));
	}
	
	
	
	
	private void removeFlagItem(VBox container, String name) {
		container.getChildren().remove(findFlagItem(container, name));
	}
	
	
	
	
	private FlagItem findFlagItem(VBox container, String name) {
		for(Node child : container.getChildren()) {
			if(child instanceof FlagItem) {
				FlagItem item = (FlagItem)child;
				if(item.flag.name.equalsIgnoreCase(name)) {
					return item;
				}
			}
		}
		return null;
	}
	
	
}






class FlagItem extends HBox {
	
	public TaskFlag flag;
	private Pane pane;
	private EditableLabel label;

	
	
	
	public FlagItem(TaskFlag flag) {
		this.flag = flag;
		
		this.setSpacing(5);
		this.setPrefSize(-1, -1);
		this.setAlignment(Pos.CENTER_LEFT);
	
		// remove flag button
		Button btnRemoveFlag = new Button();
		if(flag.isDefaultFlag) {
			btnRemoveFlag.setDisable(true);
			btnRemoveFlag.setVisible(false);
		}
		btnRemoveFlag.setMinSize(32, 32);
		btnRemoveFlag.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemoveFlag, SVGIcons.getCross(), 40, 40, "black");
		btnRemoveFlag.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.flags.removeFlag(FlagItem.this.flag, false);
			}			
		});
		this.getChildren().add(btnRemoveFlag);
		
		// flag color
		pane = new Pane();
		this.getChildren().add(pane);
		pane.setMinSize(32, 32);
		pane.setMaxSize(32, 32);
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				
				// select-color menu
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount() == 2) {
						ContextMenu menu = new ContextMenu();
						for(int i=0; i<FlagColor.values().length; i++) {
							final FlagColor flagColor = FlagColor.values()[i];
							Color color = flagColor.color;
							
							Pane colorPane = new Pane();
							colorPane.setMinSize(60, 30);
							colorPane.setPrefSize(60, 30);
							colorPane.setMaxSize(60, 30);
							colorPane.setStyle("-fx-background-radius: 5; -fx-background-color: rgba(" + (int)(255*color.getRed()) +","+ (int)(255*color.getGreen()) +","+ (int)(255*color.getBlue()) + ",255);");
							
							CustomMenuItem item = new CustomMenuItem();
							item.setContent(colorPane);
							item.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									DataService.flags.recolorFlag(FlagItem.this.flag, flagColor);
								}
							});
							menu.getItems().add(item);
						}
						menu.show(pane, Side.RIGHT, 0, 0);
					}	
				}
				
			}
		});
		
		// flag name
		label = new EditableLabel();
		if(flag.isDefaultFlag) {
			label.setEditable(false);
		}
		this.getChildren().add(label);
		label.setMinWidth(300);
		label.setMaxWidth(300);
		label.addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				label.setText(FlagItem.this.flag.name);
				DataService.flags.renameFlag(FlagItem.this.flag, newValue);
			}
		});
		
		update();
	}
	
	
	
	
	public void update() {
		Color color = flag.color.color;
		String hexColor = String.format( "#%02X%02X%02X", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255) );
		pane.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 5;");
		this.label.setText(flag.name);
	}
	
	
}
