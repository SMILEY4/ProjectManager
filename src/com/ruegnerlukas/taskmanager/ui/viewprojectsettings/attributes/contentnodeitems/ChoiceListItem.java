package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.ChoiceListValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.TagBar;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChoiceListItem extends SimpleContentNodeItem {


	private TagBar tagbar;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;

	public EventHandler<ActionEvent> handlerModified;




	public ChoiceListItem(TaskAttribute attribute) {
		super(attribute, "Values");
		this.getStyleClass().add("choice_list_item");

		// create control
		tagbar = new TagBar();
		tagbar.allowDuplicates.set(false);
		setNode(tagbar);

		// set initial value
		reset();

		// add logic
		tagbar.setOnTagAdded(((observable, oldValue, newValue) -> checkChanged()));
		tagbar.setOnTagRemoved(((observable, oldValue, newValue) -> checkChanged()));
		tagbar.setOnTagChanged(((observable, oldValue, newValue) -> checkChanged()));

		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.CHOICE_VALUES) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	/**
	 * Removes the "default" style class from all tags and adds it to the current default value / tag.
	 * */
	public void refreshStyleDefault(String value) {
		tagbar.removeCssStyleClass(null, "tag-default");
		if (value != null && tagbar.getTags().contains(value)) {
			tagbar.addCssStyleClass(value, "tag-default");
		}
	}



	/**
	 * @return the unchanged list of values of the {@link TaskAttribute}
	 */
	private String[] getMasterValue() {
		return AttributeLogic.CHOICE_LOGIC.getValueList(attribute);
	}



	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
	public void checkChanged() {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
		Set<String> setMaster = new HashSet<>(Arrays.asList(getMasterValue()));
		Set<String> setLocal = new HashSet<>(Arrays.asList(getValue()));
		if (setMaster.containsAll(setLocal) && setLocal.containsAll(setMaster)) {
			changedProperty.set(false);
		} else {
			changedProperty.set(true);
		}
	}




	@Override
	public void reset() {
		tagbar.removeAll();
		tagbar.addTags(getMasterValue());
		if (AttributeLogic.CHOICE_LOGIC.getUseDefault(attribute) && getMasterValue().length > 0) {
			refreshStyleDefault(AttributeLogic.CHOICE_LOGIC.getDefaultValue(attribute).getValue());
		} else {
			refreshStyleDefault(null);
		}
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
	public String[] getValue() {
		return tagbar.getTagArray();
	}




	@Override
	public ChoiceListValue getAttributeValue() {
		return new ChoiceListValue(getValue());
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
