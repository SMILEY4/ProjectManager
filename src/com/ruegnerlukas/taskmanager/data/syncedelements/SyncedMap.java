package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.sun.javafx.collections.ObservableMapWrapper;

import java.util.HashMap;

public class SyncedMap<K, V> extends ObservableMapWrapper<K, V> implements SyncedElement {


	public final String identifier;
	public final boolean useKeysAsIdentifiers;




	public SyncedMap(String identifier, boolean useKeysAsIdentifiers) {
		super(new HashMap<>());
		this.identifier = identifier;
		this.useKeysAsIdentifiers = useKeysAsIdentifiers;
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			for (Entry<K, V> entry : this.entrySet()) {

				// select by key
				if (useKeysAsIdentifiers && entry.getValue() instanceof SyncedElement) {
					SyncedElement managedElement = (SyncedElement) entry.getValue();
					if (entry.getKey().toString().equalsIgnoreCase(nextChange.getIdentifier())) {
						managedElement.applyChange(nextChange);
						break;
					}

					// select by value-identifier
				} else {
					if (entry.getValue() instanceof SyncedElement) {
						SyncedElement managedElement = (SyncedElement) entry.getValue();
						if (managedElement.getIdentifier().equalsIgnoreCase(nextChange.getIdentifier())) {
							managedElement.applyChange(nextChange);
							break;
						}
					}
				}
			}
		}
		if (change instanceof MapChange) {
			MapChange mapChange = (MapChange) change;
			if (mapChange.wasAdded) {
				this.put((K) mapChange.key, (V) mapChange.value);
			} else {
				this.remove((K) mapChange.key);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	@Override
	public void dispose() {
		DataHandler.deregisterSyncedElement(this);
	}

}
