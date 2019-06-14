package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapDataChange;
import com.sun.javafx.collections.ObservableMapWrapper;

import java.util.HashMap;

public class SyncedMap<K, V> extends ObservableMapWrapper<K, V> implements SyncedElement {


	public final String identifier;
	public final Class<K> typeKey;
	public final Class<V> typeValue;




	public SyncedMap(String identifier, Class<K> typeKey, Class<V> typeValue) {
		super(new HashMap<>());
		this.identifier = identifier;
		this.typeKey = typeKey;
		this.typeValue = typeValue;
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof MapDataChange) {
			applyChange((MapDataChange) change);
		}
	}




	private void applyChange(MapDataChange change) {
		if (change.key.getClass().isAssignableFrom(typeKey) && change.value.getClass().isAssignableFrom(typeValue)) {
			if (change.wasAdded || change.wasChanged) {
				this.put((K) change.key, (V) change.value);
			}
			if (change.wasRemoved) {
				this.remove((K) change.key);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	public Class<?> getTypeKey() {
		return typeKey;
	}




	public Class<?> getTypeValue() {
		return typeValue;
	}




	@Override
	public void dispose() {

	}

}
