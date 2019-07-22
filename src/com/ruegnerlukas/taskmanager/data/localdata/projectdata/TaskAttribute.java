package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedNode;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import com.ruegnerlukas.taskmanager.utils.listeners.CustomListener;

public class TaskAttribute implements SyncedElement {


	private final SyncedNode node;

	public final SyncedProperty<String> name;
	public final SyncedProperty<AttributeType> type;
	public final SyncedMap<AttributeValueType, AttributeValue<?>> values;




	public TaskAttribute(String name, AttributeType type, Project project, DataHandler handler) {
		this(name, type, project.data.attributes.getNode(), handler);
	}




	private TaskAttribute(String name, AttributeType type, SyncedNode parent, DataHandler handler) {
		this.node = new SyncedNode(name, parent, handler);
		this.node.setManagedElement(this);

		this.name = new SyncedProperty<>(Identifiers.ATTRIBUTE_NAME, node, handler);
		this.name.set(name);

		this.type = new SyncedProperty<>(Identifiers.ATTRIBUTE_TYPE, node, handler);
		this.type.set(type);

		this.values = new SyncedMap<>(Identifiers.ATTRIBUTE_VALUES, node, handler);
	}




	public AttributeValue<?> getValue(AttributeValueType key) {
		return values.get(key);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			if (nextChange.getIdentifier().equalsIgnoreCase(name.getNode().identifier)) {
				name.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(type.getNode().identifier)) {
				type.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(values.getNode().identifier)) {
				values.applyChange(nextChange);
			}
		}
	}




	@Override
	public void dispose() {
		name.dispose();
		type.dispose();
		values.dispose();
		node.dispose();
	}




	@Override
	public SyncedNode getNode() {
		return node;
	}




	@Override
	public CustomListener<?> getListener() {
		return null;
	}


}
