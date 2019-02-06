package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class NumberItem extends SidebarItem {


	protected NumberItem(TaskAttribute attribute) {
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
				// do stuff
			}
		});
		spinner.setMinSize(0, 32);
		spinner.setPrefSize(-1, 32);
		spinner.setMaxSize(10000, 32);
		boxRight.getChildren().add(spinner);

	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
