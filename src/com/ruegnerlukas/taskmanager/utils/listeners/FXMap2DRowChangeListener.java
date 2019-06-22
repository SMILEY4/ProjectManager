package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMap2DRowChangeListener<R, C, V> extends FXMap2DChangeListener<R, C, V> {


	private final Set<R> rows = new HashSet<>();




	public FXMap2DRowChangeListener(ObservableMap2D<R, C, V> map, R... rows) {
		addTo(map);
		for (R row : rows) {
			addRow(row);
		}
	}




	public FXMap2DRowChangeListener(R row, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addRow(row);
	}




	public FXMap2DRowChangeListener(ObservableMap2D<R, C, V>[] maps, R[] rows) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		for (R row : rows) {
			addRow(row);
		}
	}




	public FXMap2DRowChangeListener<R, C, V> addRow(R row) {
		rows.add(row);
		return this;
	}




	public FXMap2DRowChangeListener<R, C, V> removeRow(R row) {
		rows.remove(row);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isSilenced() && c.getRow() != null && rows.contains(c.getRow())) {
			onChanged(c);
		}
	}


}
