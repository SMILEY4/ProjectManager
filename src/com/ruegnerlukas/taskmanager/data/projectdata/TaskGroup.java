package com.ruegnerlukas.taskmanager.data.projectdata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskGroup {


	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

}
