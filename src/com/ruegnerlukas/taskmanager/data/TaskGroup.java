package com.ruegnerlukas.taskmanager.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskGroup {


	public ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public ObservableList<Task> tasks = FXCollections.observableArrayList();

}
