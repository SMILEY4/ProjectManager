package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.CheckBox;

public class UseDefaultItem extends SimpleContentNodeItem {


	private CheckBox checkBox;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public UseDefaultItem(TaskAttribute attribute) {
		super(attribute, "Use Default");
		this.getStyleClass().add("use_default_item");

		// create control
		checkBox = new CheckBox("");
		setNode(checkBox);

		// set initial value
		reset();

		// add logic
		checkBox.setOnAction(e -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.USE_DEFAULT) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	/**
	 * @return the unchanged value of the {@link TaskAttribute}
	 */
	private boolean getMasterValue() {
		UseDefaultValue value = (UseDefaultValue) attribute.getValue(AttributeValueType.USE_DEFAULT);
		return value.getValue();
	}




	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
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




	/**
	 * @return the current value of this item.
	 */
	public boolean getValue() {
		return checkBox.isSelected();
	}




	@Override
	public UseDefaultValue getAttributeValue() {
		return new UseDefaultValue(getValue());
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
