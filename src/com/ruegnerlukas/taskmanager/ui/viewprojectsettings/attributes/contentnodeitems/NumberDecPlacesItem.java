package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.NumberDecPlacesValue;
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



	/**
	 * @return the unchanged number of decimal places of the {@link TaskAttribute}
	 */
	private int getMasterDecPlaces() {
		return AttributeLogic.NUMBER_LOGIC.getDecPlaces(attribute);
	}



	/**
	 * @return the unchanged min value of the {@link TaskAttribute}
	 */
	private double getMasterMinValue() {
		return AttributeLogic.NUMBER_LOGIC.getMinValue(attribute).doubleValue();
	}



	/**
	 * @return the unchanged max value of the {@link TaskAttribute}
	 */
	private double getMasterMaxValue() {
		return AttributeLogic.NUMBER_LOGIC.getMaxValue(attribute).doubleValue();
	}



	/**
	 * @return the unchanged value of the {@link TaskAttribute}
	 */
	private double getMasterValue() {
		return AttributeLogic.NUMBER_LOGIC.getDefaultValue(attribute).getValue();
	}



	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
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



	/**
	 * @return the current value of this item.
	 */
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
