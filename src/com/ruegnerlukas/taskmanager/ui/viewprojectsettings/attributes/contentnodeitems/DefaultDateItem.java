package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DateValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class DefaultDateItem extends SimpleContentNodeItem {


	private DatePicker datePicker;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public DefaultDateItem(TaskAttribute attribute) {
		super(attribute, "Default Value");

		// create control
		datePicker = new DatePicker();
		datePicker.setMinSize(60, 32);
		datePicker.setPrefSize(150, 32);
		datePicker.setMaxSize(150, 32);
		setNode(datePicker);

		// set initial value
		reset();

		// add logic
		datePicker.setOnAction(e -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	private LocalDate getMasterValue() {
		return AttributeLogic.DATE_LOGIC.getDefaultValue(attribute).getValue();
	}




	public void checkChanged() {
		changedProperty.set(!getMasterValue().equals(getValue()));
	}




	@Override
	public void reset() {
		datePicker.setValue(getMasterValue());
		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public LocalDate getValue() {
		return datePicker.getValue();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new DateValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
