package com.ruegnerlukas.taskmanager_old.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.NumberAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.Sidebar;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item.SidebarItem;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;

public class NumberItem extends SidebarItem {


	private Spinner<Double> spinner;




	protected NumberItem(Task task, TaskAttribute attribute, Sidebar sidebar) {
		super(task, attribute, sidebar);
		this.setId("item_number");
	}




	@Override
	protected boolean isComplexItem() {
		return false;
	}




	@Override
	protected double getFieldHeight() {
		return 32;
	}




	@Override
	protected Parent createValueField(Task task, TaskAttribute attribute) {

		NumberAttributeData data = (NumberAttributeData) attribute.data;

		spinner = new Spinner<>();
		spinner.setEditable(true);
		SpinnerUtils.initSpinner(spinner, data.defaultValue, data.min, data.max, Math.pow(10, -data.decPlaces), data.decPlaces, true, false, new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Logic.tasks.setAttributeValue(task, attribute, new NumberValue(data.decPlaces == 0 ? spinner.getValue().intValue() : spinner.getValue()));
			}
		});
		spinner.setMinSize(0, 32);
		spinner.setPrefSize(-1, 32);
		spinner.setMaxSize(10000, 32);

		Response<TaskAttributeValue> response = Logic.tasks.getAttributeValue(task, attribute.name);
		if (response.getValue() instanceof NoValue) {
			spinner.getValueFactory().setValue(data.min);
		} else {
			spinner.getValueFactory().setValue(((NumberValue) response.getValue()).getDouble());
		}

		return spinner;
	}




	@Override
	protected TaskAttributeValue getValue() {
		NumberAttributeData data = (NumberAttributeData) attribute.data;
		return new NumberValue(data.decPlaces == 0 ? spinner.getValue().intValue() : spinner.getValue());
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
