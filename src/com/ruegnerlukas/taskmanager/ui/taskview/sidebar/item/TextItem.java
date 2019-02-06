package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TextAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class TextItem extends SidebarItem {


	protected TextItem(Task task, TaskAttribute attribute) {
		super(task, attribute);

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

		// right - textarea
		VBox boxRight = new VBox();
		boxRight.setMinSize(0, 32);
		boxRight.setPrefSize(10000, -1);
		boxRight.setMaxSize(10000, -1);
		this.getChildren().add(boxRight);

		TextAttributeData data = (TextAttributeData)attribute.data;
		double fieldHeight = Math.max(33, 21.3*(data.multiline ? data.nLinesExpected : 1) + 9);
		TextArea textArea = new TextArea();
		textArea.setMinSize(0, 33);
		textArea.setPrefSize(10000, fieldHeight);
		textArea.setMaxSize(10000, fieldHeight);
		boxRight.getChildren().add(textArea);

		this.setPrefSize(10000, -1);
		this.setMaxSize(10000, 10000);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if(response.getValue() instanceof NoValue) {
					textArea.setText("");
				} else {
					textArea.setText( ((TextValue)response.getValue()).getText() );
				}
			}
		});

		textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue) {
				Logic.tasks.setAttributeValue(task, attribute, new TextValue(textArea.getText()));
			}

		});

	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
