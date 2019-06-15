package com.ruegnerlukas.taskmanager.utils.map2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObservableMap2DWrapper<R, C, V> implements ObservableMap2D<R, C, V> {


	private final List<Map2DChangeListener<R, C, V>> listeners = new ArrayList<>();

	private final Map2D<R, C, V> backingMap;




	public ObservableMap2DWrapper(Map2D<R, C, V> map) {
		backingMap = map;
	}




	private class SimpleChange extends Map2DChangeListener.Change<R, C, V> {


		private final R row;
		private final C column;
		private final V old;
		private final V added;
		private final boolean wasAdded;
		private final boolean wasRemoved;
		private final boolean wasRowAdded;
		private final boolean wasRowRemoved;
		private final boolean wasColumnAdded;
		private final boolean wasColumnRemoved;




		public SimpleChange(R row, C column, V old, V added, boolean wasAdded, boolean wasRemoved,
							boolean wasRowAdded, boolean wasRowRemoved, boolean wasColumnAdded, boolean wasColumnRemoved) {
			super(ObservableMap2DWrapper.this);
			this.row = row;
			this.column = column;
			this.old = old;
			this.added = added;
			this.wasAdded = wasAdded;
			this.wasRemoved = wasRemoved;
			this.wasRowAdded = wasRowAdded;
			this.wasRowRemoved = wasRowRemoved;
			this.wasColumnAdded = wasColumnAdded;
			this.wasColumnRemoved = wasColumnRemoved;
		}




		@Override
		public boolean wasRowAdded() {
			return wasRowAdded;
		}




		@Override
		public boolean wasRowRemoved() {
			return wasRowRemoved;
		}




		@Override
		public boolean wasColumnAdded() {
			return wasColumnAdded;
		}




		@Override
		public boolean wasColumnRemoved() {
			return wasColumnRemoved;
		}




		@Override
		public boolean wasAdded() {
			return wasAdded;
		}




		@Override
		public boolean wasRemoved() {
			return wasRemoved;
		}




		@Override
		public R getRow() {
			return row;
		}




		@Override
		public C getColumn() {
			return column;
		}




		@Override
		public V getValueRemoved() {
			return old;
		}




		@Override
		public V getValueAdded() {
			return added;
		}

	}




	@Override
	public void addListener(Map2DChangeListener<R, C, V> listener) {
		listeners.add(listener);
	}




	@Override
	public void removeListener(Map2DChangeListener<R, C, V> listener) {
		listeners.remove(listener);
	}




	private void fireChange(Map2DChangeListener.Change<R, C, V> change) {
		for (Map2DChangeListener<R, C, V> listener : listeners) {
			listener.onChanged(change);
		}
	}




	@Override
	public boolean containsRow(R row) {
		return backingMap.containsRow(row);
	}




	@Override
	public boolean containsColumn(C column) {
		return backingMap.containsColumn(column);
	}




	@Override
	public boolean put(R row) {
		boolean changed = backingMap.put(row);
		fireChange(new SimpleChange(row, null, null, null, false, false, true, changed, false, false));
		return changed;
	}




	@Override
	public boolean putIfAbsent(R row) {
		boolean ret = backingMap.putIfAbsent(row);
		if(ret) {
			fireChange(new SimpleChange(row, null, null, null, false, false, true, false, false, false));
		}
		return ret;
	}




	@Override
	public V put(R row, C column, V value) {
		V ret = backingMap.put(row, column, value);
		fireChange(new SimpleChange(row, column, ret, value, true, ret != null, false, false, false, false));
		return ret;
	}




	@Override
	public V get(R row, C column) {
		return backingMap.get(row, column);
	}




	@Override
	public V getOrDefault(R row, C column, V defaultValue) {
		return backingMap.getOrDefault(row, column, defaultValue);
	}




	@Override
	public void clear() {
		List<R> rowKeys = new ArrayList<>(backingMap.getRowKeys());
		while (!rowKeys.isEmpty()) {
			R row = rowKeys.remove(0);
			backingMap.removeRow(row);
			fireChange(new SimpleChange(row, null, null, null, false, false, false, true, false, false));
		}
	}




	@Override
	public <T> T map(R row, Map2DEntryMapper<R, C, V, T> mapper) {
		return backingMap.map(row, mapper);
	}




	@Override
	public V remove(R row, C column) {
		V ret = backingMap.remove(row, column);
		if(ret != null) {
			fireChange(new SimpleChange(row, column, ret, null, false, true, false, false, false, false));
		}
		return ret;
	}




	@Override
	public boolean removeRow(R row) {
		boolean ret = backingMap.removeRow(row);
		if (ret) {
			fireChange(new SimpleChange(row, null, null, null, false, true, false, true, false, false));
		}
		return ret;
	}




	@Override
	public boolean removeColumn(C column) {
		boolean ret = backingMap.removeColumn(column);
		if (ret) {
			fireChange(new SimpleChange(null, column, null, null, false, true, false, false, false, true));
		}
		return ret;
	}




	@Override
	public Set<R> getRowKeys() {
		return backingMap.getRowKeys();
	}




	@Override
	public Set<C> getColumnKeys() {
		return backingMap.getColumnKeys();
	}




	@Override
	public boolean isEmpty() {
		return backingMap.isEmpty();
	}




	@Override
	public boolean isEmpty(R row) {
		return backingMap.isEmpty(row);
	}

}
