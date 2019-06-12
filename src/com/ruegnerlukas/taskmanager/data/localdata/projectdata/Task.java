package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.*;


public class Task {


	private Map<TaskAttribute, List<EventHandler<ActionEvent>>> listeners = new HashMap<>();

	public ObservableMap<TaskAttribute, TaskValue<?>> values = FXCollections.observableHashMap();




	public Task() {
		values.addListener((MapChangeListener<TaskAttribute, TaskValue<?>>) c -> {
			List<EventHandler<ActionEvent>> list = listeners.get(c.getKey());
			if (list != null) {
				for (EventHandler<ActionEvent> handler : list) {
					handler.handle(new ActionEvent());
				}
			}
		});
	}




	public TaskValue<?> getValue(TaskAttribute attribute) {
		return values.get(attribute);
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
