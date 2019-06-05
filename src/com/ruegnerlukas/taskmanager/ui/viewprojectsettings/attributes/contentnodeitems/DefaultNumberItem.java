package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NumberValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Spinner;

public class DefaultNumberItem extends SimpleContentNodeItem {


	private Spinner<Double> spinner;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public DefaultNumberItem(TaskAttribute attribute) {
		super(attribute, "Default Value");

		// create control
		spinner = new Spinner<>();
		spinner.setEditable(true);
		spinner.setMinSize(60, 32);
		spinner.setPrefSize(150, 32);
		spinner.setMaxSize(150, 32);
		setNode(spinner);

		// set initial value
		reset();

		// add logic
		spinner.valueProperty().addListener((observable, oldValue, newValue) -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	private void initSpinner(double value, int decPlaces, double min, double max) {
		SpinnerUtils.initSpinner(
				spinner,
				MathUtils.setDecPlaces(value, decPlaces),
				min,
				max,
				1.0 / Math.pow(10, decPlaces),
				decPlaces,
				true, null);
	}




	public void setValues(int decPlaces, double min, double max) {
		initSpinner(getValue(), decPlaces, min, max);
	}




	private int getMasterDecPlaces() {
		return AttributeLogic.NUMBER_LOGIC.getDecPlaces(attribute);
	}




	private double getMasterMinValue() {
		return AttributeLogic.NUMBER_LOGIC.getMinValue(attribute).doubleValue();
	}




	private double getMasterMaxValue() {
		return AttributeLogic.NUMBER_LOGIC.getMaxValue(attribute).doubleValue();
	}




	private double getMasterValue() {
		return AttributeLogic.NUMBER_LOGIC.getDefaultValue(attribute).getValue();
	}




	public void checkChanged() {
		changedProperty.set(!MathUtils.isNearlyEqual(getMasterValue(), spinner.getValue()));
	}




	@Override
	public void reset() {
		initSpinner(getMasterValue(), getMasterDecPlaces(), getMasterMinValue(), getMasterMaxValue());
		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public double getValue() {
		return spinner.getValue();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new NumberValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
