package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMap2DColumnChangeListener<R, C, V> extends FXMap2DChangeListener<R, C, V> {


	private final Set<C> columns = new HashSet<>();




	public FXMap2DColumnChangeListener(ObservableMap2D<R, C, V> map, C... columns) {
		addTo(map);
		for (C column : columns) {
			addColumn(column);
		}
	}




	public FXMap2DColumnChangeListener(C column, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addColumn(column);
	}




	public FXMap2DColumnChangeListener(ObservableMap2D<R, C, V>[] maps, C[] columns) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		for (C column : columns) {
			addColumn(column);
		}
	}




	public FXMap2DColumnChangeListener<R, C, V> addColumn(C column) {
		columns.add(column);
		return this;
	}




	public FXMap2DColumnChangeListener<R, C, V> removeColumn(C column) {
		columns.remove(column);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isSilenced() && c.getColumn() != null && columns.contains(c.getColumn())) {
			onChanged(c);
		}
	}


}
