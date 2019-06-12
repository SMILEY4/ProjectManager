package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NumberValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.scene.control.Spinner;


public class ItemNumber extends SimpleSidebarItem {


	private Spinner<Double> spinner;




	public ItemNumber(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		spinner = new Spinner<>();
		spinner.setEditable(true);
		spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NumberValue(newValue));
		}));
		this.setValueNode(spinner);
		this.setShowButton(true);
	}




	@Override
	protected void refresh() {

		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());

		double spinnerValue = (objValue != null && objValue.getAttType() != null) ? ((NumberValue) objValue).getValue() : 0;
		SpinnerUtils.initSpinner(
				spinner,
				MathUtils.setDecPlaces(spinnerValue, AttributeLogic.NUMBER_LOGIC.getDecPlaces(getAttribute())),
				AttributeLogic.NUMBER_LOGIC.getMinValue(getAttribute()).doubleValue(),
				AttributeLogic.NUMBER_LOGIC.getMaxValue(getAttribute()).doubleValue(),
				1.0 / Math.pow(10, AttributeLogic.NUMBER_LOGIC.getDecPlaces(getAttribute())),
				AttributeLogic.NUMBER_LOGIC.getDecPlaces(getAttribute()),
				true, null);

		if (objValue != null && objValue.getAttType() != null) {
			spinner.getValueFactory().setValue(((NumberValue) objValue).getValue());
		}
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
			final double value = Math.max(AttributeLogic.NUMBER_LOGIC.getMinValue(getAttribute()).doubleValue(), 0.0);
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NumberValue(value));
			spinner.getValueFactory().setValue(MathUtils.setDecPlaces(value, AttributeLogic.NUMBER_LOGIC.getDecPlaces(getAttribute())));
		}
		this.setEmpty(empty);
	}

}
