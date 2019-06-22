package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ListChangeListener;

import java.util.ArrayList;

public class SyncedList<E> extends ObservableListWrapper<E> implements SyncedElement {


	private final SyncedNode node;

	private final FXListChangeListener<E> listener;




	public SyncedList(String identifier, SyncedNode parent) {
		super(new ArrayList<>());
		this.node = new SyncedNode(identifier, parent);
		this.node.setManagedElement(this);
		this.listener = new FXListChangeListener<E>(this) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends E> c) {
				for (E removed : this.getAllRemoved(c)) {
					if (removed instanceof SyncedElement) {
						SyncedElement removedElement = (SyncedElement) removed;
						ListChange change = new ListChange(node.identifier, false, removedElement.getNode().identifier, null);
						node.onManagedElementChanged(change);
					}
				}
				for (E added : this.getAllAdded(c)) {
					if (added instanceof SyncedElement) {
						SyncedElement addedElement = (SyncedElement) added;
						ListChange change = new ListChange(node.identifier, true, null, addedElement);
						node.onManagedElementChanged(change);
					}
				}
			}
		};
	}




	@SuppressWarnings ("Duplicates")
	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			for (E e : this) {
				if (e instanceof SyncedElement) {
					SyncedElement managedElement = (SyncedElement) e;
					if (managedElement.getNode().identifier.equalsIgnoreCase(nextChange.getIdentifier())) {
						managedElement.applyChange(nextChange);
						break;
					}
				}
			}
		}

		if (change instanceof ListChange) {
			this.listener.setSilenced(true);
			ListChange listChange = (ListChange) change;
			if (listChange.wasAdded) {
				this.add((E) listChange.objAdded);
			} else {
				for (E e : this) {
					if (e instanceof SyncedElement) {
						SyncedElement managedElement = (SyncedElement) e;
						if (managedElement.getNode().identifier.equalsIgnoreCase(listChange.removedIdentifier)) {
							this.remove(e);
							break;
						}
					}
				}
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
	public FXListChangeListener<E> getListener() {
		return this.listener;
	}

}
