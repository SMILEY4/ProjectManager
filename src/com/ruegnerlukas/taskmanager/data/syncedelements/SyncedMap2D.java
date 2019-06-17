package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListDataChange;
import com.ruegnerlukas.taskmanager.data.change.Map2DDataChange;
import com.ruegnerlukas.taskmanager.utils.map2d.HashMap2D;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2DWrapper;

public class SyncedMap2D<R, C, V> extends ObservableMap2DWrapper<R, C, V> implements SyncedElement {


	public final String identifier;
	public final Class<R> typeRow;
	public final Class<C> typeColumn;
	public final Class<V> typeValue;




	public SyncedMap2D(String identifier, Class<R> typeRow, Class<C> typeColumn, Class<V> typeValue) {
		super(new HashMap2D<>());
		this.identifier = identifier;
		this.typeRow = typeRow;
		this.typeColumn = typeColumn;
		this.typeValue = typeValue;
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof ListDataChange) {
			applyChange((Map2DDataChange) change);
		}
	}




	private void applyChange(Map2DDataChange change) {
		if (change.row.getClass().isAssignableFrom(typeRow) && change.column.getClass().isAssignableFrom(typeColumn) && change.value.getClass().isAssignableFrom(typeValue)) {
			if(change.wasValueAdded || change.wasValueChanged) {
				this.put((R)change.row, (C)change.column, (V)change.value);
			}
			if(change.wasValueRemoved) {
				this.remove((R)change.row, (C)change.column);
			}
			if(change.wasRowAdded || change.wasRowChanged) {
				this.put((R)change.row);
			}
			if(change.wasRowRemoved) {
				this.removeRow((R)change.row);
			}
			if(change.wasColumnAdded || change.wasColumnChanged) {
			}
			if(change.wasColumnRemoved) {
				this.removeColumn((C)change.column);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	public Class<?> getTypeRow() {
		return typeRow;
	}




	public Class<?> getTypeColumn() {
		return typeColumn;
	}




	public Class<?> getTypeValue() {
		return typeValue;
	}




	@Override
	public void dispose() {
		DataHandler.deregisterSyncedElement(this);
	}

}
