package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.CardDisplayTypeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.CheckBox;

import java.util.Collections;
import java.util.List;

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




	/**
	 * @return the unchanged value of the {@link TaskAttribute}
	 */
	private boolean getMasterValue() {
		return AttributeLogic.getShowOnTaskCard(attribute);
	}




	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
	public void checkChanged() {
		changedProperty.set(getMasterValue() != checkBox.isSelected());
	}




	@Override
	public List<SetAttributeValueEffect> getSetAttributeValueEffects() {
		return Collections.emptyList();
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




	/**
	 * @return the current value of this item.
	 */
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
