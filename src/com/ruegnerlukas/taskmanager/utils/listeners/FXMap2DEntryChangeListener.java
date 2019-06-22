package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMap2DEntryChangeListener<R, C, V> extends FXMap2DChangeListener<R, C, V> {


	private final Set<R> rows = new HashSet<>();
	private final Set<C> columns = new HashSet<>();




	public FXMap2DEntryChangeListener(ObservableMap2D<R, C, V> map) {
		addTo(map);
	}




	public FXMap2DEntryChangeListener(R row, C column, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addRow(row);
		addColumn(column);
	}




	public FXMap2DEntryChangeListener(ObservableMap2D<R, C, V>[] maps, R[] rows, C[] columns) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		for (R row : rows) {
			addRow(row);
		}
		for (C column : columns) {
			addColumn(column);
		}

	}




	public FXMap2DEntryChangeListener<R, C, V> addRow(R row) {
		rows.add(row);
		return this;
	}




	public FXMap2DEntryChangeListener<R, C, V> removeRow(R row) {
		rows.remove(row);
		return this;
	}




	public FXMap2DEntryChangeListener<R, C, V> addColumn(C column) {
		columns.add(column);
		return this;
	}




	public FXMap2DEntryChangeListener<R, C, V> removeColumns(C column) {
		columns.remove(column);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isSilenced() && c.getRow() != null && rows.contains(c.getRow()) && c.getColumn() != null && columns.contains(c.getColumn())) {
			onChanged(c);
		}
	}


}
