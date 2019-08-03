package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodeitems;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.List;

/**
 * Represents a single changable value of a given {@link TaskAttribute}.
 */
public abstract class ContentNodeItem extends HBox {


	public final SimpleBooleanProperty changedProperty = new SimpleBooleanProperty(false);

	protected final TaskAttribute attribute;




	public ContentNodeItem(TaskAttribute attribute) {
		this.attribute = attribute;
	}




	/**
	 * @return true, when this value has changed, i.e. is different from the value of the {@link TaskAttribute}.
	 */
	public boolean hasChanged() {
		return changedProperty.get();
	}




	/**
	 * @return a list of {@link SetAttributeValueEffect} resulting from saving this item.
	 */
	public List<SetAttributeValueEffect> getSetAttributeValueEffects() {
		if (hasChanged()) {
			return AttributeLogic.getSetValueEffects(Data.projectProperty.get(), attribute, getAttributeValue(), true);
		} else {
			return Collections.emptyList();
		}
	}




	/**
	 * Reset this value to the value of the {@link TaskAttribute}.
	 */
	public abstract void reset();


	/**
	 * Writes this value to the {@link TaskAttribute}.
	 */
	public abstract void save();


	/**
	 * @return this value as a {@link AttributeValue}.
	 */
	public abstract AttributeValue<?> getAttributeValue();


	public abstract void dispose();


}
