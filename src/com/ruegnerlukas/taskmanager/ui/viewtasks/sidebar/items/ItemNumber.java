package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.NumberAttributeLogic;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.scene.control.Spinner;


public class ItemNumber extends SimpleSidebarItem {


	private Spinner<Double> spinner;




	public ItemNumber(TaskAttribute attribute, Task task) {
		super(attribute, task);

		spinner = new Spinner<>();
		spinner.setEditable(true);
		SpinnerUtils.initSpinner(
				spinner,
				MathUtils.setDecPlaces(0, NumberAttributeLogic.getDecPlaces(attribute)),
				NumberAttributeLogic.getMinValue(attribute).doubleValue(),
				NumberAttributeLogic.getMaxValue(attribute).doubleValue(),
				1.0 / Math.pow(10, NumberAttributeLogic.getDecPlaces(attribute)),
				NumberAttributeLogic.getDecPlaces(attribute),
				true, null);

		spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), task, attribute, newValue);
		}));

		this.setValueNode(spinner);
		this.setShowButton(!NumberAttributeLogic.getUseDefault(attribute));

		final Object objValue = TaskLogic.getValue(task, attribute);
		if (objValue != null && !(objValue instanceof NoValue)) {
			spinner.getValueFactory().setValue((Double) objValue);
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
			final double value = Math.max(NumberAttributeLogic.getMinValue(getAttribute()).doubleValue(), 0.0);
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), value);
			spinner.getValueFactory().setValue(MathUtils.setDecPlaces(value, NumberAttributeLogic.getDecPlaces(getAttribute())));
		}
	}

}
