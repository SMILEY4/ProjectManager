package com.ruegnerlukas.taskmanager.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProjectSettings {

	public final SimpleStringProperty name = new SimpleStringProperty();
	public final SimpleBooleanProperty attributesLocked = new SimpleBooleanProperty(false);


}
