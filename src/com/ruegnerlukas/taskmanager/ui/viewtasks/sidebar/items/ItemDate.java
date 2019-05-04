package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.DateAttributeLogic;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;


public class ItemDate extends SimpleSidebarItem {


	private DatePicker picker;




	public ItemDate(TaskAttribute attribute, Task task) {
		super(attribute, task);

		picker = new DatePicker();
		picker.setValue(LocalDate.now());
		picker.setOnAction(event -> {
			TaskLogic.setValue(Data.projectProperty.get(), task, attribute, picker.getValue());
		});

		this.setValueNode(picker);
		this.setText(attribute.name.getName() + ":");
		this.setShowButton(!DateAttributeLogic.getUseDefault(attribute));

		final Object objValue = TaskLogic.getValue(task, attribute);
		if (objValue != null && !(objValue instanceof NoValue)) {
			picker.setValue((LocalDate) objValue);
			this.setEmpty(false);
		} else {
			setEmpty(true);
		}
	}




	@Override
	public void dispose() {

	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(empty);
		if (empty) {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
		} else {
			final LocalDate value = LocalDate.now();
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), value);
			picker.setValue(value);
		}
	}





}
