package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.CardDisplayTypeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.CheckBox;

public class DisplayTypeItem extends SimpleContentNodeItem {


	private CheckBox checkBox;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public DisplayTypeItem(TaskAttribute attribute) {
		super(attribute, "Show on Cards");

		// create control
		checkBox = new CheckBox("");
		setNode(checkBox);

		// set initial value
		reset();

		// add logic
		checkBox.setOnAction(e -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.CARD_DISPLAY_TYPE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	private boolean getMasterValue() {
		return AttributeLogic.getShowOnTaskCard(attribute);
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
			AttributeLogic.setShowOnTaskCard(attribute, getValue());
			changedProperty.set(false);
		}
	}




	public boolean getValue() {
		return checkBox.isSelected();
	}




	@Override
	public CardDisplayTypeValue getAttributeValue() {
		return new CardDisplayTypeValue(getValue());
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
