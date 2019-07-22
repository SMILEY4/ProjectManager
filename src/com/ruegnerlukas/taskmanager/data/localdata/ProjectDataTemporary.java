package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskGroup;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class ProjectDataTemporary {


	public final CustomProperty<Boolean> lastGroupsValid;
	public final ObservableList<TaskGroup> lastTaskGroups;
	public final List<EventHandler<ActionEvent>> listenersTaskGroupsChanged;




	ProjectDataTemporary(Project project) {
		lastGroupsValid = new CustomProperty<>(false);
		lastTaskGroups = FXCollections.observableArrayList();
		listenersTaskGroupsChanged = new ArrayList<>();
	}




	public void dispose() {
	}

}
