package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.*;

public class NumberContentNode extends ChangeableContentNode {


	public NumberContentNode(TaskAttribute attribute) {
		super(attribute);

		// dec places
		NumberDecPlacesItem itemDecPlaces = new NumberDecPlacesItem(attribute);
		addItem(itemDecPlaces);

		// min value
		NumberLimitItem itemMin = new NumberLimitItem(attribute, false);
		addItem(itemMin);

		// max value
		NumberLimitItem itemMax = new NumberLimitItem(attribute, true);
		addItem(itemMax);

		// use default
		UseDefaultItem itemUseDefault = new UseDefaultItem(attribute);
		addItem(itemUseDefault);

		// default value
		DefaultNumberItem itemDefault = new DefaultNumberItem(attribute);
		addItem(itemDefault);

		// handle changed dec-places
		itemDecPlaces.handlerModified = e -> {
			itemMin.setValues(itemDecPlaces.getValue(), -999999, itemMax.getValue());
			itemMax.setValues(itemDecPlaces.getValue(), itemMin.getValue(), 999999);
			itemDefault.setValues(itemDecPlaces.getValue(), itemMin.getValue(), itemMax.getValue());
		};

		// handle changed min
		itemMin.handlerModified = e -> {
			itemMax.setValues(itemDecPlaces.getValue(), itemMin.getValue(), 999999);
			itemDefault.setValues(itemDecPlaces.getValue(), itemMin.getValue(), itemMax.getValue());
		};

		// handle changed max
		itemMax.handlerModified = e -> {
			itemMin.setValues(itemDecPlaces.getValue(), -999999, itemMax.getValue());
			itemDefault.setValues(itemDecPlaces.getValue(), itemMin.getValue(), itemMax.getValue());
		};

		// disable default value if not used
		itemUseDefault.changedProperty.addListener(((observable, oldValue, newValue) -> {
			itemDefault.setDisable(!itemUseDefault.getValue());
		}));
		itemDefault.setDisable(!itemUseDefault.getValue());

		// card display type
		DisplayTypeItem itemDisplayType = new DisplayTypeItem(attribute);
		addItem(itemDisplayType);
	}




	@Override
	public double getContentHeight() {
		return 282;
	}


}
