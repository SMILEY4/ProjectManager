package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public abstract class FXListChangeListener<E> {


	private final ListChangeListener<E> listener = this::onChanged;
	private final List<ObservableList<? extends E>> lists = new ArrayList<>();




	public FXListChangeListener(ObservableList<E>... lists) {
		for (ObservableList<E> list : lists) {
			addTo(list);
		}
	}




	public void addTo(ObservableList<? extends E> list) {
		list.addListener(listener);
		lists.add(list);
	}




	public void removeFrom(ObservableList<? extends E> list) {
		list.removeListener(listener);
		lists.remove(list);
	}




	public void removeFromAll() {
		for (ObservableList<? extends E> list : lists) {
			list.removeListener(listener);
		}
		lists.clear();
	}




	public abstract void onChanged(ListChangeListener.Change<? extends E> c);




	private ListChangeListener.Change<? extends E> lastProcessedChange = null;
	private final List<E> listAdded = new ArrayList<>();
	private final List<E> listRemoved = new ArrayList<>();




	private void processChange(ListChangeListener.Change<? extends E> c) {

		listAdded.clear();
		listRemoved.clear();

		while (c.next()) {
			if (c.wasAdded()) {
				listAdded.addAll(c.getAddedSubList());
			}
			if (c.wasRemoved()) {
				listRemoved.addAll(c.getRemoved());
			}
		}
	}




	public List<E> getAllAdded(ListChangeListener.Change<? extends E> c) {
		if (lastProcessedChange != c) {
			processChange(c);
		}
		return listAdded;
	}




	public List<E> getAllRemoved(ListChangeListener.Change<? extends E> c) {
		if (lastProcessedChange != c) {
			processChange(c);
		}
		return listRemoved;
	}

}
