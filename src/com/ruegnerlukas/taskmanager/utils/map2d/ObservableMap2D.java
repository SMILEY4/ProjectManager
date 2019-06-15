package com.ruegnerlukas.taskmanager.utils.map2d;

public interface ObservableMap2D<R, C, V> extends Map2D<R, C, V> {


	static <R, C, V> ObservableMap2D<R, C, V> create() {
		return new ObservableMap2DWrapper<>(new HashMap2D<>());
	}

	static <R, C, V> ObservableMap2D<R, C, V> create(HashMap2D<R, C, V> map) {
		return new ObservableMap2DWrapper<>(map);
	}

	void addListener(Map2DChangeListener<R, C, V> listener);

	void removeListener(Map2DChangeListener<R, C, V> listener);

}
