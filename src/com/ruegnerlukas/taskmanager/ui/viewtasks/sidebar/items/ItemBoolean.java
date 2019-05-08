package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.NumberAttributeLogic;
import javafx.scene.control.CheckBox;


public class ItemBoolean extends SimpleSidebarItem {


	private CheckBox checkBox;




	public ItemBoolean(TaskAttribute attribute, Task task) {
		super(attribute, task);
	}




	@Override
	protected void setupControls() {
		checkBox = new CheckBox();
		checkBox.setText("");

		this.setValueNode(checkBox);
		this.setShowButton(!NumberAttributeLogic.getUseDefault(getAttribute()));
	}




	@Override
	protected void setupInitialValue() {

		final Object objValue = TaskLogic.getValue(getTask(), getAttribute());
		if (objValue != null && !(objValue instanceof NoValue)) {
			checkBox.setSelected((Boolean) objValue);
			this.setEmpty(false);
		} else {
			setEmpty(true);
		}

	}




	@Override
	protected void setupLogic() {
		checkBox.setOnAction(event -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), checkBox.isSelected());
		});
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
			final boolean value = false;
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), value);
			checkBox.setSelected(value);
		}
	}

}
