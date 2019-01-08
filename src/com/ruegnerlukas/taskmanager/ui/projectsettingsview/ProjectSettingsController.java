package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.taskmanager.logic.data.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeCreatedRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeDeletedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedColorEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagRemovedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.flag.FlagNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs.CustomAttributeNode;
import com.ruegnerlukas.taskmanager.utils.uielements.alert.Alerts;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ProjectSettingsController implements IViewController {

	@FXML private AnchorPane rootProjectSettingsView;
	
	@FXML private AnchorPane paneHeader;
	@FXML private VBox boxFlags;
	@FXML private Button btnAddAttribute;
	@FXML private VBox boxCustomAttribs;
	
	
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
				FlagNode item = findFlagItem(boxFlags, event.getOldName());
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
				FlagNode item = findFlagItem(boxFlags, event.getFlag().name);
				item.update();
			}
		}, FlagChangedColorEvent.class);
		
		
		
		// custom attribs
		boxCustomAttribs.getChildren().clear();
		VBoxDragAndDrop.enableDragAndDrop(boxCustomAttribs);
		
		btnAddAttribute.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.createCustomAttribute("Attribute " + (DataService.customAttributes.getNumAttributes()+1));
			}
		});
		
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				CustomAttributeCreatedEvent event = (CustomAttributeCreatedEvent)e;
				CustomAttributeNode node = new CustomAttributeNode(event.getCreatedAttribute());
				boxCustomAttribs.getChildren().add(node);
			}
		}, CustomAttributeCreatedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				System.out.println("TODO: CustomAttributeCreatedRejection");
			}
		}, CustomAttributeCreatedRejection.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				CustomAttributeDeletedEvent event = (CustomAttributeDeletedEvent)e;
				CustomAttributeNode node = null;
				for(Node n : boxCustomAttribs.getChildren()) {
					if(((CustomAttributeNode)n).attribute == event.getDeletedAttribute() ) {
						node = (CustomAttributeNode)n;
						break;
					}
				}
				if(node != null) {
					boxCustomAttribs.getChildren().remove(node);
				}
			}
		}, CustomAttributeDeletedEvent.class);
		
	}

	
	
	
	private void addFlagItem(VBox container, TaskFlag flag) {
		container.getChildren().add(container.getChildren().size()-1, new FlagNode(flag));
	}
	
	
	
	
	private void removeFlagItem(VBox container, String name) {
		container.getChildren().remove(findFlagItem(container, name));
	}
	
	
	
	
	private FlagNode findFlagItem(VBox container, String name) {
		for(Node child : container.getChildren()) {
			if(child instanceof FlagNode) {
				FlagNode item = (FlagNode)child;
				if(item.flag.name.equalsIgnoreCase(name)) {
					return item;
				}
			}
		}
		return null;
	}
	
	
}

