package com.ruegnerlukas.taskmanager.utils.map2d;

import java.util.Set;

public interface Map2D<R, C, V> {


	/**
	 * @return true, if this map contains the row with the given key
	 */
	boolean containsRow(R row);

	/**
	 * @return true, if this map contains the column with the given key
	 */
	boolean containsColumn(C column);


	/**
	 * adds an empty row with the given key to this map. If the row already exists, it will be replaced.
	 *
	 * @return true, if a row was replaced
	 */
	boolean put(R row);


	/**
	 * adds an empty row with the given key to this map only if none already exists.
	 *
	 * @return true, if the row with the given key was added
	 */
	boolean putIfAbsent(R row);


	/**
	 * adds the value in the given row and column
	 *
	 * @return the previous value or null
	 */
	V put(R row, C column, V value);


	/**
	 * @return the value in the given row and column or null
	 */
	V get(R row, C column);


	/**
	 * @return the value in the given row and column or the given default value, if it does not exist
	 */
	V getOrDefault(R row, C column, V defaultValue);


	/**
	 * removes all values from this map
	 */
	void clear();


	/**
	 * maps a given row to an object with the given mapper
	 */
	<T> T map(R row, Map2DEntryMapper<R, C, V, T> mapper);


	/**
	 * removes the value in the given row and column
	 *
	 * @return the removed value or null
	 */
	V remove(R row, C column);


	/**
	 * removes the given row
	 *
	 * @return true, if a row was removed
	 */
	boolean removeRow(R row);

	/**
	 * removes the given column
	 *
	 * @return true, if a column was removed
	 */
	boolean removeColumn(C column);


	/**
	 * @return the keys of all rows
	 */
	Set<R> getRowKeys();

	/**
	 * @return the keys of all columns
	 */
	Set<C> getColumnKeys();

	/**
	 * @return true, if this map does not contain any rows
	 */
	boolean isEmpty();

	/**
	 * @return true, if the given row does not contain any values
	 */
	boolean isEmpty(R row);

}
