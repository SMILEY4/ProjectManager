package com.ruegnerlukas.taskmanager.ui.infobar;

import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.EventCause;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagAddedRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedColorEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagChangedNameRejection;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.FlagRemovedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ProjectRenamedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListChangedColorEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListDeletedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListsChangedOrderEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class InfobarHandler {

	public void create(AnchorPane pane, Label label) {
		
		final String colorSecondary = "#6f7f8c";
		final String colorInfo = "#5c8f94";
		final String colorDanger = "#cc330d";
		final String colorSuccess = "#3e4d59";
		final String colorWarning = "#6e9fa5";
		final String colorLight = "#eceeec";
		final String colorDark = "#1e2b37";
		
		
		// FLAGS
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagAddedEvent event = (FlagAddedEvent)e;
				label.setText("[Info] Added new flag: " + event.getAddedFlag().name + ", " + event.getAddedFlag().color.toString().toLowerCase());
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, FlagAddedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagAddedRejection event = (FlagAddedRejection)e;
				if(event.getCause() == EventCause.NAME_EXISTS) {
					label.setText("[Error] Could no add flag, because name already exists: " + event.getRejectedFlagName());
					pane.setStyle("-fx-background-color: " + colorWarning + ";");
				}
			}
		}, FlagAddedRejection.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedColorEvent event = (FlagChangedColorEvent)e;
				label.setText("[Info] Changed color of flag: " + event.getFlag().name + " to " + event.getNewColor().toString().toLowerCase());
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, FlagChangedColorEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedNameEvent event = (FlagChangedNameEvent)e;
				label.setText("[Info] Changed name of flag from " + event.getOldName() + " to " + event.getNewName());
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, FlagChangedNameEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagChangedNameRejection event = (FlagChangedNameRejection)e;
				if(event.getCause() == EventCause.NAME_EXISTS) {
					label.setText("[Error] Could not change name, name alreadx exists " + event.getNewName());
					pane.setStyle("-fx-background-color: " + colorWarning + ";");
				}
			}
		}, FlagChangedNameRejection.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				FlagRemovedEvent event = (FlagRemovedEvent)e;
				label.setText("[Info] Removed flag: " + event.getRemovedFlag().name);
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, FlagRemovedEvent.class);
		
		
		// PROJECT
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectClosedEvent event = (ProjectClosedEvent)e;
				label.setText("[Info] Project was closed: " + event.getLastProject().name);
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ProjectClosedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectCreatedEvent event = (ProjectCreatedEvent)e;
				label.setText("[Info] Project was created: " + event.getNewProject().name);
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ProjectCreatedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ProjectRenamedEvent event = (ProjectRenamedEvent)e;
				label.setText("[Info] Project was renamed from " + event.getOldName() + " to " + event.getNewName());
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ProjectRenamedEvent.class);
		
		
		// TASK LISTS
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListChangedNameEvent event = (ListChangedNameEvent)e;
				label.setText("[Info] TaskList was renamed from " + event.getOldName() + " to " + event.getNewName());
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ListChangedNameEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListCreatedEvent event = (ListCreatedEvent)e;
				label.setText("[Info] A new TaskList was created: " + event.getCreatedList().title);
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ListCreatedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListsChangedOrderEvent event = (ListsChangedOrderEvent)e;
				label.setText("[Info] The order of the TaskLists has been changed");
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ListsChangedOrderEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListDeletedEvent event = (ListDeletedEvent)e;
				label.setText("[Info] A TaskList has been deleted: " + event.getDeletedList().title);
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ListDeletedEvent.class);
	
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListChangedColorEvent event = (ListChangedColorEvent)e;
				label.setText("[Info] The color of the list  " + event.getList().title + " has been changed");
				pane.setStyle("-fx-background-color: " + colorInfo + ";");
			}
		}, ListChangedColorEvent.class);
		
	}
	
	
}






