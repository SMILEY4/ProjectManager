package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class NumberItem extends SidebarItem {


	protected NumberItem(Task task, TaskAttribute attribute) {
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

		// right - spinner
		VBox boxRight = new VBox();
		boxRight.setMinSize(0, 32);
		boxRight.setPrefSize(10000, 32);
		boxRight.setMaxSize(10000, 32);
		this.getChildren().add(boxRight);

		NumberAttributeData data = (NumberAttributeData)attribute.data;
		Spinner<Double> spinner = new Spinner<>();
		spinner.setEditable(true);
		SpinnerUtils.initSpinner(spinner, data.defaultValue, data.min, data.max, Math.pow(10, -data.decPlaces), data.decPlaces, true, false, new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.tasks.setAttributeValue(task, attribute, new NumberValue( data.decPlaces == 0 ? spinner.getValue().intValue() : spinner.getValue() ));
			}
		});
		spinner.setMinSize(0, 32);
		spinner.setPrefSize(-1, 32);
		spinner.setMaxSize(10000, 32);
		boxRight.getChildren().add(spinner);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if(response.getValue() instanceof NoValue) {
					NumberAttributeData data = (NumberAttributeData)attribute.data;
					spinner.getValueFactory().setValue( data.min );
				} else {
					spinner.getValueFactory().setValue( ((NumberValue)response.getValue()).getDouble() );
				}
			}
		});


	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
