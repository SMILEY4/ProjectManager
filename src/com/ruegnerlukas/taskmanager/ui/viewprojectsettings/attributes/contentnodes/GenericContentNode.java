package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.DisplayTypeItem;


/**
 * For {@link TaskAttribute}s that have no special values that can be changed.
 */
public class GenericContentNode extends ChangeableContentNode {


	public GenericContentNode(TaskAttribute attribute) {
		super(attribute);

		// card display type
		DisplayTypeItem itemDisplayType = new DisplayTypeItem(attribute);
		addItem(itemDisplayType);
	}




	@Override
	public double getContentHeight() {
		return 110;
	}


}
