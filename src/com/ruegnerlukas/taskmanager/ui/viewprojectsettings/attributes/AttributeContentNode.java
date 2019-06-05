package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

public abstract class AttributeContentNode extends AnchorPane {


	public final TaskAttribute attribute;
	protected AttributeNode parent;

	public SimpleBooleanProperty changedProperty = new SimpleBooleanProperty(false);






	public AttributeContentNode(TaskAttribute attribute) {
		this.attribute = attribute;
	}




	public void setParentNode(AttributeNode parent) {
		this.parent = parent;
	}






	public abstract void dispose();

	public abstract double getContentHeight();


}
