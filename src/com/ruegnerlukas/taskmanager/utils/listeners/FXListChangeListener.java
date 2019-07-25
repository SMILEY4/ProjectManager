package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.simpleutils.arrays.ArrayUtils;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public abstract class FXListChangeListener<E> implements CustomListener<ObservableList<E>> {


	private final ListChangeListener<E> listener = this::onListChanged;
	private final List<ObservableList<? extends E>> lists = new ArrayList<>();
	private boolean isSilenced = false;




	public FXListChangeListener(ObservableList<E>... lists) {
		for (ObservableList<E> list : lists) {
			addTo(list);
		}
	}




	@Override
	public FXListChangeListener<E> addTo(ObservableList<E> list) {
		list.addListener(listener);
		lists.add(list);
		return this;
	}




	@Override
	public FXListChangeListener<E> removeFrom(ObservableList<E> list) {
		list.removeListener(listener);
		lists.remove(list);
		return this;
	}




	@Override
	public FXListChangeListener<E> removeFromAll() {
		for (ObservableList<? extends E> list : lists) {
			list.removeListener(listener);
		}
		lists.clear();
		return this;
	}




	@Override
	public void setMuted(boolean silenced) {
		this.isSilenced = silenced;
	}




	@Override
	public boolean isMuted() {
		return this.isSilenced;
	}




	/**
	 * @return the {@link ListChangeListener} used by this listener.
	 */
	public ListChangeListener<E> getListener() {
		return this.listener;
	}




	private boolean valid = false;
	private final List<E> listAdded = new ArrayList<>();
	private final List<E> listRemoved = new ArrayList<>();
	private final List<ListChangeListener.Change<? extends E>> listPermutations = new ArrayList<>();




	private void onListChanged(ListChangeListener.Change<? extends E> c) {
		if (!isMuted()) {
			valid = false;
			onChanged(c);
		}
	}




	/**
	 * This method is called if the elements of an registered {@link ObservableList} change and this listener is not muted.
	 */
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




	/**
	 * @return all elements added by the given {@link ListChangeListener.Change}.
	 */
	public List<E> getAllAdded(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listAdded;
	}




	/**
	 * @return all elements removed by the given {@link ListChangeListener.Change}.
	 */
	public List<E> getAllRemoved(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listRemoved;
	}




	/**
	 * @return all permutations by the given {@link ListChangeListener.Change}.
	 */
	public List<ListChangeListener.Change<? extends E>> getAllPermutations(ListChangeListener.Change<? extends E> c) {
		if (!valid) {
			processChange(c);
		}
		return listPermutations;
	}




	/**
	 * Apply the given permutations to the given list.
	 */
	public void applyPermutation(List list, ListChangeListener.Change<? extends E> permutation) {
		int[] p = new int[permutation.getTo() - permutation.getFrom()];
		for (int i = 0; i < p.length; i++) {
			p[i] = permutation.getPermutation(i + permutation.getFrom());
		}
		ArrayUtils.applyPermutation(list, p, permutation.getFrom());
	}

}
