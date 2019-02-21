package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;

public class DescriptionAttributeNode extends AttributeDataNode {


	public DescriptionAttributeNode(TaskAttribute attribute, TaskAttributeNode parent) {
		super(attribute, parent, "fxmlfiles/taskattribute_static.fxml", false);
	}




	@Override
	protected void onCreate() {
	}




	@Override
	protected void onChange() {
	}




	@Override
	protected void onSave() {
	}




	@Override
	protected void onDiscard() {
	}




	@Override
	protected void onClose() {
	}




	@Override
	public void close() {
	}




	@Override
	public double getNodeHeight() {
		return this.getPrefHeight();
	}


	@Override
	public boolean getUseDefault() {
		return true;
	}

}
