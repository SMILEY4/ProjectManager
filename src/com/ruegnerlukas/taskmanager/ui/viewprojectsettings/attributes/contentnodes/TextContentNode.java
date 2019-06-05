package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems.*;

public class TextContentNode extends ChangeableContentNode {


	private DefaultTextItem itemDefault;




	public TextContentNode(TaskAttribute attribute) {
		super(attribute);

		// multiline
		TextMultilineItem itemMultiline = new TextMultilineItem(attribute);
		addItem(itemMultiline);

		// use default
		UseDefaultItem itemUseDefault = new UseDefaultItem(attribute);
		addItem(itemUseDefault);

		// default value
		itemDefault = new DefaultTextItem(attribute);
		addItem(itemDefault);

		// set default multiline value
		itemMultiline.changedProperty.addListener(((observable, oldValue, newValue) -> {
			itemDefault.setMultiline(itemMultiline.getValue());
			parent.resize();
		}));

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
		return 181 + 32 + itemDefault.height;
	}


}
