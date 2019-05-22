package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DateValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;


public class ItemDate extends SimpleSidebarItem {


	private DatePicker picker;




	public ItemDate(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void setupControls() {
		picker = new DatePicker();
		picker.setValue(LocalDate.now());

		this.setValueNode(picker);
		this.setText(getAttribute().name.getName() + ":");
		this.setShowButton(true);
	}




	@Override
	protected void setupInitialValue() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			picker.setValue( ((DateValue)objValue).getValue());
		}
		final TaskValue<?> objValueRAW = TaskLogic.getTaskValue(getTask(), getAttribute());
		if (objValueRAW == null || objValueRAW.getAttType() == null) {
			setEmpty(true);
		} else {
			setEmpty(false);
		}
	}




	@Override
	protected void setupLogic() {
		picker.setOnAction(event -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new DateValue(picker.getValue()));
		});
	}




	@Override
	public void dispose() {
		super.dispose();
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		if (empty) {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
		} else {
			final LocalDate value = LocalDate.now();
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new DateValue(value));
			picker.setValue(value);
		}
		this.setEmpty(empty);
	}


}
