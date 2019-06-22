package com.ruegnerlukas.taskmanager.utils.listeners;

import com.ruegnerlukas.taskmanager.utils.map2d.Map2DChangeListener;
import com.ruegnerlukas.taskmanager.utils.map2d.ObservableMap2D;

import java.util.ArrayList;
import java.util.List;

public abstract class FXMap2DChangeListener<R, C, V> implements CustomListener<ObservableMap2D<R, C, V>> {


	private final Map2DChangeListener<R, C, V> listener = this::onMapChanged;
	private final List<ObservableMap2D<R, C, V>> maps = new ArrayList<>();
	private boolean isSilenced = false;




	public FXMap2DChangeListener(ObservableMap2D<R, C, V>... maps) {
		for (ObservableMap2D<R, C, V> map : maps) {
			addTo(map);
		}
	}




	@Override
	public FXMap2DChangeListener<R, C, V> addTo(ObservableMap2D<R, C, V> map) {
		map.addListener(listener);
		maps.add(map);
		return this;
	}




	@Override
	public FXMap2DChangeListener<R, C, V> removeFrom(ObservableMap2D<R, C, V> map) {
		map.removeListener(listener);
		maps.remove(map);
		return this;
	}




	@Override
	public FXMap2DChangeListener<R, C, V> removeFromAll() {
		for (ObservableMap2D<R, C, V> map : maps) {
			map.removeListener(listener);
		}
		maps.clear();
		return this;
	}




	@Override
	public void setSilenced(boolean silenced) {
		this.isSilenced = silenced;
	}




	@Override
	public boolean isSilenced() {
		return this.isSilenced;
	}




	public Map2DChangeListener<R, C, V> getListener() {
		return this.listener;
	}




	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if(!isSilenced()) {
			onChanged(c);
		}
	}




	public abstract void onChanged(Map2DChangeListener.Change<R, C, V> c);




	public boolean wasValueAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasAdded() && !c.wasRemoved();
	}




	public boolean wasValueRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasAdded() && c.wasRemoved();
	}




	public boolean wasValueChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasAdded() && c.wasRemoved();
	}




	public boolean wasRowAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasRowAdded() && !c.wasRowRemoved();
	}




	public boolean wasRowRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasRowAdded() && c.wasRowRemoved();
	}




	public boolean wasRowChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasRowAdded() && c.wasRowRemoved();
	}




	public boolean wasColumnAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasColumnAdded() && !c.wasColumnRemoved();
	}




	public boolean wasColumnRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasColumnAdded() && c.wasColumnRemoved();
	}




	public boolean wasColumnChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasColumnAdded() && c.wasColumnRemoved();
	}


}
