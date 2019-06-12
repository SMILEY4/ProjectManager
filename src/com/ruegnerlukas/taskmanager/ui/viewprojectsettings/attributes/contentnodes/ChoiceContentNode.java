package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.ChoiceListItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DefaultChoiceItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DisplayTypeItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.UseDefaultItem;

public class ChoiceContentNode extends ChangeableContentNode {


	public ChoiceContentNode(TaskAttribute attribute) {
		super(attribute);

		// choice list
		ChoiceListItem itemChoices = new ChoiceListItem(attribute);
		addItem(itemChoices);

		// use default
		UseDefaultItem itemUseDefault = new UseDefaultItem(attribute);
		addItem(itemUseDefault);

		// default value
		DefaultChoiceItem itemDefault = new DefaultChoiceItem(attribute);
		addItem(itemDefault);

		// disable default value if not used
		itemUseDefault.changedProperty.addListener(((observable, oldValue, newValue) -> {
			itemDefault.setDisable(!itemUseDefault.getValue());
			if(itemUseDefault.getValue()) {
				itemChoices.setDefault(itemDefault.getValue());
			} else {
				itemChoices.setDefault(null);
			}
		}));
		itemDefault.setDisable(!itemUseDefault.getValue());

		// update values of default value
		itemChoices.handlerModified = e -> {
			itemDefault.setChoiceList(itemChoices.getValue());
		};

		// update default of choice list
		itemDefault.handlerModified = e -> {
			if(itemUseDefault.getValue()) {
				itemChoices.setDefault(itemDefault.getValue());
			}
		};

		// card display type
		DisplayTypeItem itemDisplayType = new DisplayTypeItem(attribute);
		addItem(itemDisplayType);
	}




	@Override
	public double getContentHeight() {
		return 210;
	}


}
