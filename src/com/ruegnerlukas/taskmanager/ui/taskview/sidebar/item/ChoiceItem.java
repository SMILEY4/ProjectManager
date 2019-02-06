package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ChoiceItem extends SidebarItem {


	protected ChoiceItem(TaskAttribute attribute) {
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

		// right - choicebox
		VBox boxRight = new VBox();
		boxRight.setMinSize(0, 32);
		boxRight.setPrefSize(10000, 32);
		boxRight.setMaxSize(10000, 32);
		this.getChildren().add(boxRight);

		ChoiceBox<String> choiceBox = new ChoiceBox<>();
		ChoiceAttributeData data = (ChoiceAttributeData)attribute.data;
		choiceBox.getItems().addAll(data.values);
		choiceBox.setMinSize(0, 32);
		choiceBox.setPrefSize(200, 32);
		choiceBox.setMaxSize(200, 32);
		boxRight.getChildren().add(choiceBox);

	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
