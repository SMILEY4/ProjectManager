package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;

public abstract class ContentNodeItem extends HBox {


	public final SimpleBooleanProperty changedProperty = new SimpleBooleanProperty(false);

	protected final TaskAttribute attribute;




	public ContentNodeItem(TaskAttribute attribute) {
		this.attribute = attribute;
	}




	public boolean hasChanged() {
		return changedProperty.get();
	}




	public abstract void reset();


	public abstract void save();


	public abstract AttributeValue<?> getAttributeValue();


	public abstract void dispose();


}
