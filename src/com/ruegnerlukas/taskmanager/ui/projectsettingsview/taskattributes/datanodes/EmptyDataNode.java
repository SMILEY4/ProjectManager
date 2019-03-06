package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.datanodes;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.EmptyLabelItem;

public class EmptyDataNode extends DataNode {


	public EmptyDataNode(AttributeNode parent, TaskAttribute attribute) {
		super(parent, attribute);
		createItems();
	}




	private void createItems() {
		EmptyLabelItem item = new EmptyLabelItem() {
			@Override
			public void onChanged() {
			}
		};
		addVaueItem(item);
	}




	@Override
	public void writeChanges() {
	}




	@Override
	public void discardChanges() {
	}




	@Override
	public void dispose() {
	}


}
