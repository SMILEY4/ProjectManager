package com.ruegnerlukas.taskmanager.data.change;

public class Map2DDataChange extends DataChange {


	public final boolean wasValueRemoved;
	public final boolean wasValueAdded;
	public final boolean wasValueChanged;

	public final boolean wasRowRemoved;
	public final boolean wasRowAdded;
	public final boolean wasRowChanged;

	public final boolean wasColumnRemoved;
	public final boolean wasColumnAdded;
	public final boolean wasColumnChanged;

	public final Object row;
	public final Object column;
	public final Object value;




	public Map2DDataChange(String identifier, boolean wasValueRemoved, boolean wasValueAdded, boolean wasValueChanged,
						   boolean wasRowRemoved, boolean wasRowAdded, boolean wasRowChanged,
						   boolean wasColumnRemoved, boolean wasColumnAdded, boolean wasColumnChanged, Object row, Object column, Object value) {
		super(identifier);
		this.wasValueRemoved = wasValueRemoved;
		this.wasValueAdded = wasValueAdded;
		this.wasValueChanged = wasValueChanged;
		this.wasRowRemoved = wasRowRemoved;
		this.wasRowAdded = wasRowAdded;
		this.wasRowChanged = wasRowChanged;
		this.wasColumnRemoved = wasColumnRemoved;
		this.wasColumnAdded = wasColumnAdded;
		this.wasColumnChanged = wasColumnChanged;
		this.row = row;
		this.column = column;
		this.value = value;
	}

}
