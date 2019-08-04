package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
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




	public SyncedList(String identifier, SyncedNode parent, DataHandler handler) {
		super(new ArrayList<>());
		this.node = new SyncedNode(identifier, parent, handler);
		this.node.setManagedElement(this);
		this.listener = new FXListChangeListener<E>(this) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends E> c) {
				for (E removed : this.getAllRemoved(c)) {
					ListChange change = new ListChange(node.identifier, false, removed,
							(removed instanceof SyncedElement ? ((SyncedElement) removed).getNode().identifier : null));
					node.onManagedElementChanged(change);
				}
				for (E added : this.getAllAdded(c)) {
					ListChange change = new ListChange(node.identifier, true, added);
					node.onManagedElementChanged(change);
				}
			}
		};
	}




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
			this.listener.setMuted(true);
			ListChange listChange = (ListChange) change;
			if (listChange.wasAdded()) {
				this.add((E) listChange.getAdded());
			}
			if (listChange.wasRemoved()) {
				if (listChange.removeByIdentifier()) {
					for (E e : this) {
						if (e instanceof SyncedElement) {
							SyncedElement managedElement = (SyncedElement) e;
							if (managedElement.getNode().identifier.equalsIgnoreCase(listChange.getRemovedIdentifier())) {
								this.remove(e);
								break;
							}
						}
					}
				} else {
					this.remove(listChange.getRemoved());
				}
			}

			this.listener.setMuted(false);
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
