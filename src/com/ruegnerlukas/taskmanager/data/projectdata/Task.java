package com.ruegnerlukas.taskmanager.data.projectdata;

import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Task {


	private Map<TaskAttribute, List<EventHandler<ActionEvent>>> listeners = new HashMap<>();

	public ObservableMap<TaskAttribute, TaskValue<?>> attributes = FXCollections.observableHashMap();




	public Task() {
		attributes.addListener((MapChangeListener<TaskAttribute, TaskValue<?>>) c -> {


			System.out.println("CHANGE");
			if(c.wasAdded()) {
				System.out.println(" + " + c.getValueAdded().getAttType() + "." + c.getValueAdded().getValue());
			}
			if(c.wasRemoved()) {
				System.out.println(" - " + c.getValueRemoved().getAttType() + "." + c.getValueRemoved().getValue());
			}

			List<EventHandler<ActionEvent>> list = listeners.get(c.getKey());
			if(list != null) {
				for (EventHandler<ActionEvent> handler : list) {
					handler.handle(new ActionEvent());
				}
			}
		});



	}




	public TaskValue<?> getValue(TaskAttribute attribute) {
		return attributes.get(attribute);
	}




	public void addOnChange(TaskAttribute attribute, EventHandler<ActionEvent> handler) {
		List<EventHandler<ActionEvent>> list = listeners.computeIfAbsent(attribute, k -> new ArrayList<>());
		list.add(handler);
	}




	public void removeHandler(EventHandler<ActionEvent> listener) {
		for (TaskAttribute attribute : listeners.keySet()) {
			listeners.get(attribute).remove(listener);
		}
	}

}
