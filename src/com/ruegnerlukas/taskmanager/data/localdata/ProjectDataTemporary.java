package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskGroup;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class ProjectDataTemporary {


	public final SimpleBooleanProperty lastGroupsValid = new SimpleBooleanProperty(false);
	public final ObservableList<TaskGroup> lastTaskGroups = FXCollections.observableArrayList();

	public final List<EventHandler<ActionEvent>> listenersTaskGroupsChanged = new ArrayList<>();

}
