package com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskGroupData {


	public final SimpleStringProperty customHeaderString = new SimpleStringProperty();
	public final ObservableList<TaskAttribute> attributes = FXCollections.observableArrayList();

}
