package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.List;

public abstract class FXMapChangeListener<K, V> implements CustomListener<ObservableMap<K, V>> {


	private final MapChangeListener<K, V> listener = this::onMapChanged;
	private final List<ObservableMap<K, V>> maps = new ArrayList<>();
	private boolean isSilenced = false;




	public FXMapChangeListener(ObservableMap<K, V>... maps) {
		for (ObservableMap<K, V> map : maps) {
			addTo(map);
		}
	}




	@Override
	public FXMapChangeListener<K, V> addTo(ObservableMap<K, V> map) {
		map.addListener(listener);
		maps.add(map);
		return this;
	}




	@Override
	public FXMapChangeListener<K, V> removeFrom(ObservableMap<K, V> map) {
		map.removeListener(listener);
		maps.remove(map);
		return this;
	}




	@Override
	public FXMapChangeListener<K, V> removeFromAll() {
		for (ObservableMap<K, V> map : maps) {
			map.removeListener(listener);
		}
		maps.clear();
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
	 * @return the {@link MapChangeListener} used by this listener.
	 */
	public MapChangeListener<K, V> getListener() {
		return this.listener;
	}




	protected void onMapChanged(MapChangeListener.Change<? extends K, ? extends V> c) {
		if (!isMuted()) {
			onChanged(c);
		}
	}




	/**
	 * This method is called if the elements of an registered {@link ObservableMap} change and this listener is not muted.
	 */
	public abstract void onChanged(MapChangeListener.Change<? extends K, ? extends V> c);




	/**
	 * @return true, if the element of the given change was added.
	 */
	public boolean wasAdded(MapChangeListener.Change<? extends K, ? extends V> c) {
		return c.wasAdded() && !c.wasRemoved();
	}




	/**
	 * @return true, if the element of the given change was removed.
	 */
	public boolean wasRemoved(MapChangeListener.Change<? extends K, ? extends V> c) {
		return !c.wasAdded() && c.wasRemoved();
	}




	/**
	 * @return true, if the element of the given change was changed.
	 */
	public boolean wasChanged(MapChangeListener.Change<? extends K, ? extends V> c) {
		return c.wasAdded() && c.wasRemoved();
	}


}
