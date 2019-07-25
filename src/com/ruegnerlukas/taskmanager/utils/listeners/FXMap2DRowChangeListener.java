package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMap2DRowChangeListener<R, C, V> extends FXMap2DChangeListener<R, C, V> {


	private final Set<R> rows = new HashSet<>();




	/**
	 * @param map  the {@link ObservableMap2D} this listener will listen to
	 * @param rows the rows this to listen to. Changes to in other rows will be ignored
	 */
	public FXMap2DRowChangeListener(ObservableMap2D<R, C, V> map, R... rows) {
		addTo(map);
		for (R row : rows) {
			addRow(row);
		}
	}




	/**
	 * @param maps the {@link ObservableMap2D}s this listener will listen to
	 * @param row  the row this to listen to. Changes to in other rows will be ignored
	 */
	public FXMap2DRowChangeListener(R row, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addRow(row);
	}




	/**
	 * @param maps the {@link ObservableMap2D}s this listener will listen to
	 * @param rows the rows this to listen to. Changes to in other rows will be ignored
	 */
	public FXMap2DRowChangeListener(ObservableMap2D<R, C, V>[] maps, R[] rows) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		for (R row : rows) {
			addRow(row);
		}
	}




	/**
	 * Adds the given row. Changes to other columns will be ignored.
	 */
	public FXMap2DRowChangeListener<R, C, V> addRow(R row) {
		rows.add(row);
		return this;
	}




	/**
	 * Removes the given row. Future changes to this column will be ignored.
	 */
	public FXMap2DRowChangeListener<R, C, V> removeRow(R row) {
		rows.remove(row);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isMuted() && c.getRow() != null && rows.contains(c.getRow())) {
			onChanged(c);
		}
	}


}
