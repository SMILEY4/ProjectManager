package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedNode;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.CustomListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Task implements SyncedElement {


	private final SyncedNode node;

	public SyncedMap<TaskAttribute, TaskValue<?>> values;

	private Map<TaskAttribute, List<EventHandler<ActionEvent>>> listeners = new HashMap<>();




	public Task(int id, Project project, DataHandler handler) {

		this.node = new SyncedNode("Task-" + id, project.data.tasks.getNode(), handler);
		this.node.setManagedElement(this);

		this.values = new SyncedMap<>(Identifiers.TASK_VALUES, node, handler);
		TaskAttribute idAttribute = AttributeLogic.findAttributeByType(project, AttributeType.ID);
		values.put(idAttribute, new IDValue(id));

		values.addListener((MapChangeListener<TaskAttribute, TaskValue<?>>) c -> {
			List<EventHandler<ActionEvent>> list = listeners.get(c.getKey());
			if (list != null) {
				for (EventHandler<ActionEvent> eventHandler : list) {
					eventHandler.handle(new ActionEvent());
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




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			if (nextChange.getIdentifier().equalsIgnoreCase(values.getNode().identifier)) {
				values.applyChange(nextChange);
			}
		}
	}




	@Override
	public void dispose() {
		values.dispose();
		node.dispose();
	}




	@Override
	public SyncedNode getNode() {
		return node;
	}




	@Override
	public CustomListener<?> getListener() {
		return null;
	}


}
