package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TextValue;
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




	private void setValueHeight() {
		height = textField.isMultiline() ? MultiTextField.calculateOptimalHeight(5) : 32;
		textField.setMinSize(60, height);
		textField.setPrefSize(10000, height);
		textField.setMaxSize(10000, height);
		this.setMinHeight(height);
	}




	public void setMultiline(boolean multiline) {
		textField.setMultiline(multiline);
		setValueHeight();
	}




	private boolean getMasterMultiline() {
		return AttributeLogic.TEXT_LOGIC.getMultiline(attribute);
	}




	private String getMasterValue() {
		return AttributeLogic.TEXT_LOGIC.getDefaultValue(attribute).getValue();
	}




	public void checkChanged() {
		changedProperty.set(!getMasterValue().equals(textField.getText()));
		setValueHeight();
	}




	public void setText(String text) {
		textField.setText(text);
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
