package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public abstract class FXListChangeListener<E> {


	private final ListChangeListener<E> listener = this::onListChanged;


	private final List<ObservableList<? extends E>> lists = new ArrayList<>();




	public FXListChangeListener(ObservableList<E>... lists) {
		for (ObservableList<E> list : lists) {
			addTo(list);
		}
	}




	public FXListChangeListener<E> addTo(ObservableList<? extends E> list) {
		list.addListener(listener);
		lists.add(list);
		return this;
	}




	public FXListChangeListener<E> removeFrom(ObservableList<? extends E> list) {
		list.removeListener(listener);
		lists.remove(list);
		return this;
	}




	public FXListChangeListener<E> removeFromAll() {
		for (ObservableList<? extends E> list : lists) {
			list.removeListener(listener);
		}
		lists.clear();
		return this;
	}




	public ListChangeListener<E> getListener() {
		return this.listener;
	}




	private boolean valid = false;
	private final List<E> listAdded = new ArrayList<>();
	private final List<E> listRemoved = new ArrayList<>();
	private final List<ListChangeListener.Change<? extends E>> listPermutations = new ArrayList<>();




	private void onListChanged(ListChangeListener.Change<? extends E> c) {
		valid = false;
		onChanged(c);
	}




	public abstract void onChanged(ListChangeListener.Change<? extends E> c);




	private void processChange(ListChangeListener.Change<? extends E> c) {

		listAdded.clear();
		listRemoved.clear();
		listPermutations.clear();

		while (c.next()) {
			if (c.wasPermutated()) {
				listPermutations.add(c);
			}
			if (c.wasAdded()) {
				listAdded.addAll(c.getAddedSubList());
			}
			if (c.wasRemoved()) {
				listRemoved.addAll(c.getRemoved());
			}
		}

		valid = true;
	}




	public List<E> getAllAdded(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listAdded;
	}




	public List<E> getAllRemoved(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listRemoved;
	}




	public List<ListChangeListener.Change<? extends E>> getAllPermutations(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listPermutations;
	}

}
