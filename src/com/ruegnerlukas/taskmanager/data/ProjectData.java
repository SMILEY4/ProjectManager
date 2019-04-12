package com.ruegnerlukas.taskmanager.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectData {


	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();
	public final ObservableList<Task> tasks = FXCollections.observableArrayList();

}
