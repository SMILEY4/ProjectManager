package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.NumberDecPlacesValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;


public class NumberDecPlacesItem extends SimpleContentNodeItem {


	private Spinner<Integer> spinner;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;

	public EventHandler<ActionEvent> handlerModified;




	public NumberDecPlacesItem(TaskAttribute attribute) {
		super(attribute, "Decimal Places");

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
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.NUMBER_DEC_PLACES) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

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
		if(handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
		changedProperty.set(getMasterDecPlaces() != getValue());
	}




	@Override
	public void reset() {
		SpinnerUtils.initSpinner(
				spinner,
				getMasterDecPlaces(),
				0,
				9999,
				1,
				0,
				false, null);
		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public int getValue() {
		return spinner.getValue();
	}




	@Override
	public NumberDecPlacesValue getAttributeValue() {
		return new NumberDecPlacesValue(getValue());
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
