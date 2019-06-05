package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.ChoiceValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class DefaultChoiceItem extends SimpleContentNodeItem {


	private ComboBox<String> choice;

	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;

	public EventHandler<ActionEvent> handlerModified;




	public DefaultChoiceItem(TaskAttribute attribute) {
		super(attribute, "Default Value");

		// create control
		choice = new ComboBox<>();
		choice.setMinSize(60, 32);
		choice.setPrefSize(150, 32);
		choice.setMaxSize(150, 32);
		setNode(choice);

		// set initial value
		reset();

		// add logic
		choice.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> checkChanged()));
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	public void setChoiceList(String... values) {
		String str = getValue();
		choice.getItems().setAll(values);
		if (choice.getItems().contains(str)) {
			choice.getSelectionModel().select(str);
		} else {
			choice.getSelectionModel().selectFirst();
		}
	}




	private String[] getMasterValueList() {
		return AttributeLogic.CHOICE_LOGIC.getValueList(attribute);
	}




	private String getMasterValue() {
		return AttributeLogic.CHOICE_LOGIC.getDefaultValue(attribute).getValue();
	}




	public void checkChanged() {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
		changedProperty.set(!getMasterValue().equals(getValue()));
	}




	@Override
	public void reset() {
		choice.getItems().setAll(getMasterValueList());
		choice.getSelectionModel().select(getMasterValue());
		changedProperty.set(false);
	}




	@Override
	public void save() {
		if (hasChanged()) {
			AttributeLogic.setAttributeValue(Data.projectProperty.get(), attribute, getAttributeValue(), true);
			changedProperty.set(false);
		}
	}




	public String getValue() {
		return choice.getSelectionModel().getSelectedItem();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new ChoiceValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
