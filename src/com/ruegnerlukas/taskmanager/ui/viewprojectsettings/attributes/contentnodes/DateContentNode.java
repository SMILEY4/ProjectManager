package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DefaultDateItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DisplayTypeItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.UseDefaultItem;

public class DateContentNode extends ChangeableContentNode {


	public DateContentNode(TaskAttribute attribute) {
		super(attribute);

		// use default
		UseDefaultItem itemUseDefault = new UseDefaultItem(attribute);
		addItem(itemUseDefault);

		// default value
		DefaultDateItem itemDefault = new DefaultDateItem(attribute);
		addItem(itemDefault);

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
		return 171;
	}


}
