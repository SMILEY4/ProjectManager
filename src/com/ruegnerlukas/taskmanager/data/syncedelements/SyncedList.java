package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.sun.javafx.collections.ObservableListWrapper;

import java.util.ArrayList;

public class SyncedList<E> extends ObservableListWrapper<E> implements SyncedElement {


	public final String identifier;




	public SyncedList(String identifier) {
		super(new ArrayList<>());
		this.identifier = identifier;
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			for (E e : this) {
				if (e instanceof SyncedElement) {
					SyncedElement managedElement = (SyncedElement) e;
					if (managedElement.getIdentifier().equalsIgnoreCase(nextChange.getIdentifier())) {
						managedElement.applyChange(nextChange);
						break;
					}
				}
			}
		}
		if (change instanceof ListChange) {
			ListChange listChange = (ListChange) change;

			if (listChange.wasAdded) {
				this.add((E) listChange.objAdded);
			} else {
				for (E e : this) {
					if (e instanceof SyncedElement) {
						SyncedElement managedElement = (SyncedElement) e;
						if (managedElement.getIdentifier().equalsIgnoreCase(listChange.removedIdentifier)) {
							this.remove(e);
							break;
						}
					}
				}
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
