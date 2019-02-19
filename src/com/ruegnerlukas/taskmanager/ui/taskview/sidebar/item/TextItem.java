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
import javafx.scene.Parent;
import javafx.scene.control.TextArea;

public class TextItem extends SidebarItem {


	private double fieldHeight;
	private TextArea textArea;




	protected TextItem(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected boolean isComplexItem() {
		return false;
	}




	@Override
	protected double getFieldHeight() {
		return fieldHeight;
	}




	@Override
	protected Parent createValueField(Task task, TaskAttribute attribute) {

		TextAttributeData data = (TextAttributeData) attribute.data;
		this.fieldHeight = Math.max(33, 21.3 * (data.multiline ? data.nLinesExpected : 1) + 9);
		textArea = new TextArea();
		textArea.setMinSize(0, 33);
		textArea.setPrefSize(10000, fieldHeight);
		textArea.setMaxSize(10000, fieldHeight);

		this.setPrefSize(10000, -1);
		this.setMaxSize(10000, 10000);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if (response.getValue() instanceof NoValue) {
					textArea.setText("");
				} else {
					textArea.setText(((TextValue) response.getValue()).getText());
				}
			}
		});

		textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				Logic.tasks.setAttributeValue(task, attribute, new TextValue(textArea.getText()));
			}

		});

		return textArea;
	}




	@Override
	protected TaskAttributeValue getValue() {
		return new TextValue(textArea.getText());
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
