package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMapEntryChangeListener<K, V> extends FXMapChangeListener<K, V> {


	private final Set<K> keys = new HashSet<>();




	public FXMapEntryChangeListener(ObservableMap<K, V> map, K... keys) {
		addTo(map);
		for (K key : keys) {
			addKey(key);
		}
	}




	public FXMapEntryChangeListener(K key, ObservableMap<K, V>... maps) {
		for (ObservableMap<K, V> map : maps) {
			addTo(map);
		}
		addKey(key);
	}




	public FXMapEntryChangeListener(ObservableMap<K, V>[] maps, K[] keys) {
		for (ObservableMap<K, V> map : maps) {
			addTo(map);
		}
		for (K key : keys) {
			addKey(key);
		}
	}




	public FXMapEntryChangeListener<K, V> addKey(K key) {
		keys.add(key);
		return this;
	}




	public FXMapEntryChangeListener<K, V> removeKey(K key) {
		keys.remove(key);
		return this;
	}




	@Override
	protected void onMapChanged(MapChangeListener.Change<? extends K, ? extends V> c) {
		if (keys.contains(c.getKey())) {
			onChanged(c);
		}
	}


}
