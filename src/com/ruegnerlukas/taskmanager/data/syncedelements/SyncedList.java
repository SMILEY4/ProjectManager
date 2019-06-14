package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListDataChange;
import com.sun.javafx.collections.ObservableListWrapper;

import java.util.ArrayList;

public class SyncedList<E> extends ObservableListWrapper<E> implements SyncedElement {


	public final String identifier;
	public final Class<E> type;




	public SyncedList(String identifier, Class<E> type) {
		super(new ArrayList<>());
		this.identifier = identifier;
		this.type = type;
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof ListDataChange) {
			applyChange((ListDataChange) change);
		}
	}




	private void applyChange(ListDataChange change) {
		if (change.object.getClass().isAssignableFrom(type)) {
			if (change.wasAdded) {
				this.add((E) change.object);
			}
			if (change.wasRemoved) {
				this.remove((E) change.object);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	public Class<?> getType() {
		return type;
	}




	@Override
	public void dispose() {
		DataHandler.deregisterSyncedElement(this);
	}

}
