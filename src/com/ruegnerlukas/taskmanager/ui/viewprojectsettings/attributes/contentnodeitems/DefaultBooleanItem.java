package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.BoolValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.CheckBox;

public class DefaultBooleanItem extends SimpleContentNodeItem {


	private CheckBox checkBox;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public DefaultBooleanItem(TaskAttribute attribute) {
		super(attribute, "Default Value");

		// create control
		checkBox = new CheckBox("");
		setNode(checkBox);

		// set initial value
		reset();

		// add logic
		checkBox.setOnAction(e -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	private boolean getMasterValue() {
		return AttributeLogic.BOOLEAN_LOGIC.getDefaultValue(attribute).getValue();
	}




	public void checkChanged() {
		changedProperty.set(getMasterValue() != checkBox.isSelected());
	}




	@Override
	public void reset() {
		checkBox.setSelected(getMasterValue());
		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public boolean getValue() {
		return checkBox.isSelected();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new BoolValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
