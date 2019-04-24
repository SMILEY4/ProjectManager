package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskGroup;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectDataTemporary {

	public final SimpleBooleanProperty lastGroupsValid = new SimpleBooleanProperty(false);
	public final ObservableList<TaskGroup> lastTaskGroups = FXCollections.observableArrayList();

}
