package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.collections.MapChangeListener;
import javafx.scene.control.ComboBox;

public class DefaultFlagItem extends SimpleContentNodeItem {


	private ComboBox<TaskFlag> choice;
	private FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>> listener;




	public DefaultFlagItem(TaskAttribute attribute) {
		super(attribute, "Default Flag");

		// create control
		choice = new ComboBox<>();
		choice.setButtonCell(ComboboxUtils.createListCellFlag());
		choice.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choice.getItems().addAll(getMasterFlags());
		choice.getSelectionModel().select(getMasterDefaultFlag());
		choice.setMinSize(60, 32);
		choice.setPrefSize(150, 32);
		choice.setMaxSize(150, 32);
		setNode(choice);

		// set initial value
		reset();

		// add logic
		choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkChanged());
		listener = new FXMapEntryChangeListener<AttributeValueType, AttributeValue<?>>(attribute.values, AttributeValueType.DEFAULT_VALUE) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends AttributeValueType, ? extends AttributeValue<?>> c) {
				checkChanged();
			}
		};

	}




	/**
	 * Adds the given {@link TaskFlag}s to the list of flags and the {@link ComboBox} of default flags.
	 * Removes the previous flags.
	 */
	public void setFlagList(TaskFlag... flags) {

		TaskFlag flagSelected = getValue();

		choice.getItems().setAll(flags);

		TaskFlag flagSame = null;
		TaskFlag flagSimilar = null;
		for (TaskFlag flag : flags) {
			if (flag.compare(flagSelected)) {
				flagSame = flag;
			}
			if (flag.name.get().equals(flagSelected.name.get())) {
				flagSimilar = flag;
			}
		}

		if (flagSame == null) {
			if (flagSimilar != null) {
				choice.getSelectionModel().select(flagSimilar);
			} else {
				choice.getSelectionModel().selectFirst();
			}

		} else {
			choice.getSelectionModel().select(flagSame);
		}


	}




	/**
	 * @return the unchanged list of flags of the {@link TaskAttribute}
	 */
	private TaskFlag[] getMasterFlags() {
		return AttributeLogic.FLAG_LOGIC.getFlagList(attribute);
	}




	/**
	 * @return the unchanged default flag of the {@link TaskAttribute}
	 */
	private TaskFlag getMasterDefaultFlag() {
		return AttributeLogic.FLAG_LOGIC.getDefaultValue(attribute).getValue();
	}




	/**
	 * Checks whether this value was changed / is different from the value of the {@link TaskAttribute} and sets the changed-property.
	 */
	public void checkChanged() {
		changedProperty.set(!getMasterDefaultFlag().compare(choice.getValue()));
	}




	@Override
	public void reset() {
		choice.getItems().setAll(getMasterFlags());
		choice.getSelectionModel().select(getMasterDefaultFlag());
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
	public TaskFlag getValue() {
		return choice.getValue();
	}




	@Override
	public DefaultValue getAttributeValue() {
		return new DefaultValue(new FlagValue(getValue()));
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
	}

}
