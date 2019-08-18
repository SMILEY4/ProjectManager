package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TextValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MultiTextField;
import javafx.collections.MapChangeListener;

public class DefaultTextItem extends SimpleContentNodeItem {


	private MultiTextField textField;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;

	public double height;




	public DefaultTextItem(TaskAttribute attribute) {
		super(attribute, "Default Value");
		this.getStyleClass().add("default_text_item");

		// create control
		textField = new MultiTextField();
		textField.setMultiline(getMasterMultiline());
		setNode(textField);

		// set initial value
		reset();

		// add logic
		textField.textProperty().addListener((observable, oldValue, newValue) -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

		setValueHeight();
	}




	/**
	 * Sets the height of the input field to the required height.
	 */
	private void setValueHeight() {
		height = textField.isMultiline() ? MultiTextField.calculateOptimalHeight(5) : 32;
		textField.setMinSize(60, height);
		textField.setPrefSize(10000, height);
		textField.setMaxSize(10000, height);
		this.setMinHeight(height);
	}




	/**
	 * Sets whether the input-field should have multiple lines and then adjusts the height.
	 */
	public void setMultiline(boolean multiline) {
		textField.setMultiline(multiline);
		setValueHeight();
	}




	/**
	 * @return the unchanged multiline value of the {@link TaskAttribute}
	 */
	private boolean getMasterMultiline() {
		return AttributeLogic.TEXT_LOGIC.getMultiline(attribute);
	}




	/**
	 * @return the unchanged value of the {@link TaskAttribute}
	 */
	private String getMasterValue() {
		return AttributeLogic.TEXT_LOGIC.getDefaultValue(attribute).getValue();
	}




	/**
	 * Sets the text of the input-field to the given text
	 */
	public void setText(String text) {
		textField.setText(text);
	}




	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
	public void checkChanged() {
		changedProperty.set(!getMasterValue().equals(textField.getText()));
		setValueHeight();
	}




	@Override
	public void reset() {
		textField.setMultiline(getMasterMultiline());
		textField.setText(getMasterValue());
		changedProperty.set(false);
		setValueHeight();
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
	public String getValue() {
		return textField.getText();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new TextValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
