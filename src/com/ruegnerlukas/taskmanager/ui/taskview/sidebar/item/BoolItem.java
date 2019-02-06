package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BoolItem extends SidebarItem {


	protected BoolItem(TaskAttribute attribute) {
		super(attribute);

		// left - label
		VBox boxLeft = new VBox();
		boxLeft.setMinSize(0, 32);
		boxLeft.setPrefSize(10000, 32);
		boxLeft.setMaxSize(10000, 32);
		this.getChildren().add(boxLeft);

		Label label = new Label(attribute.name);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinSize(0, 32);
		label.setPrefSize(-1, 32);
		label.setMaxSize(10000, 32);
		boxLeft.getChildren().add(label);

		// right - checkbox
		VBox boxRight = new VBox();
		boxRight.setMinSize(0, 32);
		boxRight.setPrefSize(10000, 32);
		boxRight.setMaxSize(10000, 32);
		this.getChildren().add(boxRight);

		CheckBox checkbox = new CheckBox("");
		checkbox.setAlignment(Pos.CENTER_LEFT);
		checkbox.setMinSize(0, 32);
		checkbox.setPrefSize(-1, 32);
		checkbox.setMaxSize(10000, 32);
		boxRight.getChildren().add(checkbox);
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
