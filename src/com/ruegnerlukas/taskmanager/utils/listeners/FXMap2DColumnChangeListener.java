package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.HashSet;
import java.util.Set;

public abstract class FXMap2DColumnChangeListener<R, C, V> extends FXMap2DChangeListener<R, C, V> {


	private final Set<C> columns = new HashSet<>();




	/**
	 * @param map     the {@link ObservableMap2D} this listener will listen to
	 * @param columns the columns this to listen to. Changes to in other columns will be ignored
	 */
	public FXMap2DColumnChangeListener(ObservableMap2D<R, C, V> map, C... columns) {
		addTo(map);
		for (C column : columns) {
			addColumn(column);
		}
	}




	/**
	 * @param column the column this to listen to. Changes to in other columns will be ignored
	 * @param maps   the {@link ObservableMap2D}s this listener will listen to
	 */
	public FXMap2DColumnChangeListener(C column, ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		addColumn(column);
	}




	/**
	 * @param maps    the {@link ObservableMap2D}s this listener will listen to
	 * @param columns the columns this to listen to. Changes to in other columns will be ignored
	 */
	public FXMap2DColumnChangeListener(ObservableMap2D<R, C, V>[] maps, C[] columns) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
		for (C column : columns) {
			addColumn(column);
		}
	}




	/**
	 * Adds the given column. Changes to other columns will be ignored.
	 */
	public FXMap2DColumnChangeListener<R, C, V> addColumn(C column) {
		columns.add(column);
		return this;
	}




	/**
	 * Removes the given column. Future changes to this column will be ignored.
	 */
	public FXMap2DColumnChangeListener<R, C, V> removeColumn(C column) {
		columns.remove(column);
		return this;
	}




	@Override
	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isMuted() && c.getColumn() != null && columns.contains(c.getColumn())) {
			onChanged(c);
		}
	}


}
