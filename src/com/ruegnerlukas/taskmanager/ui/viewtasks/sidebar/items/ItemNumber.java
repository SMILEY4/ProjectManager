package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NumberValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.NumberAttributeLogic;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.scene.control.Spinner;


public class ItemNumber extends SimpleSidebarItem {


	private Spinner<Double> spinner;




	public ItemNumber(TaskAttribute attribute, Task task) {
		super(attribute, task);
	}




	@Override
	protected void setupControls() {
		spinner = new Spinner<>();
		spinner.setEditable(true);
		SpinnerUtils.initSpinner(
				spinner,
				MathUtils.setDecPlaces(0, NumberAttributeLogic.getDecPlaces(getAttribute())),
				NumberAttributeLogic.getMinValue(getAttribute()).doubleValue(),
				NumberAttributeLogic.getMaxValue(getAttribute()).doubleValue(),
				1.0 / Math.pow(10, NumberAttributeLogic.getDecPlaces(getAttribute())),
				NumberAttributeLogic.getDecPlaces(getAttribute()),
				true, null);

		this.setValueNode(spinner);
		this.setShowButton(!NumberAttributeLogic.getUseDefault(getAttribute()));
	}




	@Override
	protected void setupInitialValue() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			spinner.getValueFactory().setValue(((NumberValue) objValue).getValue());
			this.setEmpty(false);
		} else {
			setEmpty(true);
		}
	}




	@Override
	protected void setupLogic() {
		spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NumberValue(newValue));
		}));
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
			final double value = Math.max(NumberAttributeLogic.getMinValue(getAttribute()).doubleValue(), 0.0);
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NumberValue(value));
			spinner.getValueFactory().setValue(MathUtils.setDecPlaces(value, NumberAttributeLogic.getDecPlaces(getAttribute())));
		}
		this.setEmpty(empty);
	}

}
