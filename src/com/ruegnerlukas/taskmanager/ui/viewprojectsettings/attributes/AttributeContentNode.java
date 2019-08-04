package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

/**
 * The content of an {@link AttributeNode}.
 */
public abstract class AttributeContentNode extends AnchorPane {


	private final TaskAttribute attribute;
	protected AttributeNode parent;
	public SimpleBooleanProperty changedProperty = new SimpleBooleanProperty(false);




	public AttributeContentNode(TaskAttribute attribute) {
		this.attribute = attribute;
	}




	/**
	 * Set the {@link AttributeNode} that contains this node. This is required.
	 */
	void setParentNode(AttributeNode parent) {
		this.parent = parent;
	}




	public TaskAttribute getAttribute() {
		return this.attribute;
	}




	/**
	 * @return the required height of this node.
	 */
	public abstract double getContentHeight();


	public abstract void dispose();


}
