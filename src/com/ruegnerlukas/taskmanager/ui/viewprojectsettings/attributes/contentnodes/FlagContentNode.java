package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DefaultFlagItem;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.FlagListItem;

public class FlagContentNode extends ChangeableContentNode {


	public FlagContentNode(TaskAttribute attribute) {
		super(attribute);

		// flag list
		FlagListItem itemList = new FlagListItem(attribute);
		addItem(itemList);

		// default value
		DefaultFlagItem itemDefault = new DefaultFlagItem(attribute);
		addItem(itemDefault);

		itemList.handlerChangedChoices = e -> {
			itemDefault.setFlagList(itemList.getTemporaryValue());
		};
	}




	@Override
	public double getContentHeight() {
		return 330;
	}


}
