package com.ruegnerlukas.taskmanager.utils.map2d;

public interface ObservableMap2D<R, C, V> extends Map2D<R, C, V> {


	/**
	 * Creates a new empty {@link ObservableMap2D}
	 */
	static <R, C, V> ObservableMap2D<R, C, V> create() {
		return new ObservableMap2DWrapper<>(new HashMap2D<>());
	}


	/**
	 * Creates a new {@link ObservableMap2D} from the given {@link HashMap2D}
	 */
	static <R, C, V> ObservableMap2D<R, C, V> create(HashMap2D<R, C, V> map) {
		return new ObservableMap2DWrapper<>(map);
	}


	/**
	 * Adds the given {@link Map2DChangeListener} to this map.
	 */
	void addListener(Map2DChangeListener<R, C, V> listener);


	/**
	 * Removes the given {@link Map2DChangeListener} from this map.
	 */
	void removeListener(Map2DChangeListener<R, C, V> listener);

}
