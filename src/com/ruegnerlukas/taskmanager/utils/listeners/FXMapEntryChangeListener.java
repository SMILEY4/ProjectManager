package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMapEntryChangeListener<K, V> extends FXMapChangeListener<K, V> {


	private final Set<K> keys = new HashSet<>();




	/**
	 * @param map  the {@link ObservableMap} this listener will listen to
	 * @param keys the keys to listen to. Changes with other keys will be ignored
	 */
	public FXMapEntryChangeListener(ObservableMap<K, V> map, K... keys) {
		addTo(map);
		for (K key : keys) {
			addKey(key);
		}
	}




	/**
	 * @param key  the key to listen to. Changes with other keys will be ignored
	 * @param maps the {@link ObservableMap}s this listener will listen to
	 */
	public FXMapEntryChangeListener(K key, ObservableMap<K, V>... maps) {
		for (ObservableMap<K, V> map : maps) {
			addTo(map);
		}
		addKey(key);
	}




	/**
	 * @param maps the {@link ObservableMap}s this listener will listen to
	 * @param keys the keys to listen to. Changes with other keys will be ignored
	 */
	public FXMapEntryChangeListener(ObservableMap<K, V>[] maps, K[] keys) {
		for (ObservableMap<K, V> map : maps) {
			addTo(map);
		}
		for (K key : keys) {
			addKey(key);
		}
	}




	/**
	 * Adds a new key. Changes with other keys will be ignored
	 */
	public FXMapEntryChangeListener<K, V> addKey(K key) {
		keys.add(key);
		return this;
	}




	/**
	 * Removes a key. Future changes to this key will be ignored.
	 */
	public FXMapEntryChangeListener<K, V> removeKey(K key) {
		keys.remove(key);
		return this;
	}




	@Override
	protected void onMapChanged(MapChangeListener.Change<? extends K, ? extends V> c) {
		if (!isMuted() && keys.contains(c.getKey())) {
			onChanged(c);
		}
	}


}
