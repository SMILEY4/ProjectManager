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




	/**
	 * @param maps   the {@link ObservableMap2D} this listener will listen to
	 * @param row    the row this to listen to. Changes to in other rows will be ignored.
	 * @param column the column this to listen to. Changes to in other rows will be ignored
	 */
	public FXMap2DEntryChangeListener(R row, C column, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addRow(row);
		addColumn(column);
	}




	/**
	 * @param maps    the {@link ObservableMap2D} this listener will listen to
	 * @param rows    the rows this to listen to. Changes to in other rows will be ignored.
	 * @param columns the columns this to listen to. Changes to in other rows will be ignored
	 */
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




	/**
	 * Adds the given row. Changes to other columns will be ignored.
	 */
	public FXMap2DEntryChangeListener<R, C, V> addRow(R row) {
		rows.add(row);
		return this;
	}




	/**
	 * Removes the given row. Future changes to this column will be ignored.
	 */
	public FXMap2DEntryChangeListener<R, C, V> removeRow(R row) {
		rows.remove(row);
		return this;
	}




	/**
	 * Adds the given column. Changes to other columns will be ignored.
	 */
	public FXMap2DEntryChangeListener<R, C, V> addColumn(C column) {
		columns.add(column);
		return this;
	}




	/**
	 * Removes the given column. Future changes to this column will be ignored.
	 */
	public FXMap2DEntryChangeListener<R, C, V> removeColumns(C column) {
		columns.remove(column);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isMuted() && c.getRow() != null && rows.contains(c.getRow()) && c.getColumn() != null && columns.contains(c.getColumn())) {
			onChanged(c);
		}
	}


}
