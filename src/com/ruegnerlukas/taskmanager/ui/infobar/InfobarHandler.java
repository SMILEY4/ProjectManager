package com.ruegnerlukas.taskmanager.ui.infobar;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectClosedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectCreatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.ProjectRenamedEvent;
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

	}
	
	
}






