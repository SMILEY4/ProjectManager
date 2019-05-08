package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.ChoiceAttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.NumberAttributeLogic;
import javafx.scene.control.ComboBox;


public class ItemChoice extends SimpleSidebarItem {


	private ComboBox<String> choice;




	public ItemChoice(TaskAttribute attribute, Task task) {
		super(attribute, task);
	}




	@Override
	protected void setupControls() {
		choice = new ComboBox<>();
		choice.getItems().addAll(ChoiceAttributeLogic.getValueList(getAttribute()));

		this.setValueNode(choice);
		this.setShowButton(!NumberAttributeLogic.getUseDefault(getAttribute()));
	}




	@Override
	protected void setupInitialValue() {
		final Object objValue = TaskLogic.getValue(getTask(), getAttribute());
		if (objValue != null && !(objValue instanceof NoValue)) {
			choice.getSelectionModel().select((String) objValue);
			this.setEmpty(false);
		} else {
			setEmpty(true);
		}
	}




	@Override
	protected void setupLogic() {
		choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), newValue);
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
			final String[] valueList = ChoiceAttributeLogic.getValueList(getAttribute());
			if (valueList.length == 0) {
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
				choice.getSelectionModel().clearSelection();
			} else {
				final String value = valueList[0];
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), value);
				choice.getSelectionModel().select(value);
			}
		}
	}

}
