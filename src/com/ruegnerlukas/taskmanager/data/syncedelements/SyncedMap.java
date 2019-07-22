package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapChangeListener;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.collections.MapChangeListener;

import java.util.HashMap;

public class SyncedMap<K, V> extends ObservableMapWrapper<K, V> implements SyncedElement {


	private final SyncedNode node;

	private final FXMapChangeListener<K, V> listener;




	public SyncedMap(String identifier, SyncedNode parent, DataHandler handler) {
		super(new HashMap<>());

		this.node = new SyncedNode(identifier, parent, handler);
		this.node.setManagedElement(this);

		this.listener = new FXMapChangeListener<K, V>(this) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends K, ? extends V> c) {
				if (this.wasRemoved(c)) {
					SyncedMap.this.onChanged(new MapChange(node.identifier, false, c.getKey(), c.getValueRemoved()));
				}
				if (this.wasAdded(c)) {
					SyncedMap.this.onChanged(new MapChange(node.identifier, true, c.getKey(), c.getValueAdded()));
				}
				if (this.wasChanged(c)) {
					SyncedMap.this.onChanged(new MapChange(node.identifier, true, c.getKey(), c.getValueAdded()));
				}
			}
		};
	}




	private void onChanged(MapChange change) {
		node.onManagedElementChanged(change);
	}




	@Override
	public void applyChange(DataChange change) {

		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();

			for (Entry<K, V> entry : this.entrySet()) {

				if (entry.getValue() instanceof SyncedElement) {
					SyncedElement managedElement = (SyncedElement) entry.getValue();
					if (managedElement.getNode().identifier.equalsIgnoreCase(nextChange.getIdentifier())) {
						managedElement.applyChange(nextChange);
						break;
					}
				}

			}

		}

		if (change instanceof MapChange) {
			MapChange mapChange = (MapChange) change;
			this.listener.setSilenced(true);
			if (mapChange.wasAdded) {
				this.put((K) mapChange.key, (V) mapChange.value);
			} else {
				this.remove((K) mapChange.key);
			}
			this.listener.setSilenced(false);
		}

	}




	@Override
	public void dispose() {
		node.dispose();
		listener.removeFromAll();
	}




	@Override
	public SyncedNode getNode() {
		return node;
	}




	@Override
	public FXMapChangeListener<K, V> getListener() {
		return this.listener;
	}


}
