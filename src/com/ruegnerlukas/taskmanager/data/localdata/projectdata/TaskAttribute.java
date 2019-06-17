package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.data.syncedelements.UnmanagedSyncedProperty;

public class TaskAttribute implements SyncedElement {


	public final UnmanagedSyncedProperty<String> name = new UnmanagedSyncedProperty<>("name");
	public final UnmanagedSyncedProperty<AttributeType> type = new UnmanagedSyncedProperty<>("type");
	public final SyncedMap<AttributeValueType, AttributeValue<?>> values = new SyncedMap<>("values", true);




	public TaskAttribute(String name, AttributeType type) {
		this.name.set(name);
		this.type.set(type);


	}




	public AttributeValue<?> getValue(AttributeValueType key) {
		return values.get(key);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			if (nextChange.getIdentifier().equalsIgnoreCase(name.getIdentifier())) {
				name.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(type.getIdentifier())) {
				type.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(values.getIdentifier())) {
				values.applyChange(nextChange);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return name.get();
	}




	@Override
	public void dispose() {

	}

}
