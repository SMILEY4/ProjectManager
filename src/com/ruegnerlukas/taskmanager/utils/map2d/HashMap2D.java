package com.ruegnerlukas.taskmanager.utils.map2d;

import java.util.*;

public class HashMap2D<R, C, V> implements Map2D<R, C, V> {


	private Map<R, Entry2D<C, V>> table = new HashMap<>();
	private Set<C> columns = new HashSet<>();




	@Override
	public boolean containsRow(R row) {
		return table.containsKey(row);
	}




	@Override
	public boolean containsColumn(C column) {
		for (Entry2D<C, V> entry : table.values()) {
			if (entry.containsKey(column)) {
				return true;
			}
		}
		return false;
	}




	@Override
	public boolean put(R row) {
		return table.put(row, new Entry2D<>()) != null;
	}




	@Override
	public boolean putIfAbsent(R row) {
		return table.putIfAbsent(row, new Entry2D<>()) == null;
	}




	@Override
	public V put(R row, C column, V value) {
		V ret;
		Entry2D<C, V> entry = getEntry(row);
		if (entry == null) {
			entry = new Entry2D<>();
			table.put(row, entry);
		}
		ret = entry.put(column, value);
		columns.add(column);
		return ret;
	}




	@Override
	public V get(R row, C column) {
		Entry2D<C, V> entry = getEntry(row);
		if (entry == null) {
			return null;
		}
		return entry.get(column);
	}




	@Override
	public V getOrDefault(R row, C column, V defaultValue) {
		V value = get(row, column);
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}




	private Entry2D<C, V> getEntry(R row) {
		return table.get(row);
	}




	@Override
	public void clear() {
		table.clear();
		columns.clear();
	}




	@Override
	public <T> T map(R row, Map2DEntryMapper<R, C, V, T> mapper) {
		Entry2D<C, V> entry = getEntry(row);
		if (entry != null) {
			Map<C, V> values = new HashMap<>(entry);
			return mapper.map(row, values);
		} else {
			return mapper.mapNull();
		}
	}




	@Override
	public V remove(R row, C column) {
		Entry2D<C, V> entry = getEntry(row);
		if (entry != null) {
			return entry.remove(column);
		} else {
			return null;
		}
	}




	@Override
	public boolean removeRow(R row) {
		return table.remove(row) != null;
	}




	@Override
	public boolean removeColumn(C column) {
		boolean removed = false;
		for (Entry2D<C, V> entry : table.values()) {
			if (entry.remove(column) != null) {
				removed = true;
			}
		}
		return removed;
	}




	@Override
	public Set<R> getRowKeys() {
		return Collections.unmodifiableSet(table.keySet());
	}




	@Override
	public Set<C> getColumnKeys() {
		return Collections.unmodifiableSet(columns);
	}




	@Override
	public boolean isEmpty() {
		return table.isEmpty();
	}




	@Override
	public boolean isEmpty(R row) {
		Entry2D<C, V> entry = getEntry(row);
		if (entry != null) {
			return entry.isEmpty();
		} else {
			return true;
		}
	}


}






class Entry2D<C, V> extends HashMap<C, V> {


}