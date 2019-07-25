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
	public void setMuted(boolean silenced) {
		this.isSilenced = silenced;
	}




	@Override
	public boolean isMuted() {
		return this.isSilenced;
	}




	/**
	 * @return the {@link Map2DChangeListener} used by this listener.
	 */
	public Map2DChangeListener<R, C, V> getListener() {
		return this.listener;
	}




	protected void onMapChanged(Map2DChangeListener.Change<R, C, V> c) {
		if (!isMuted()) {
			onChanged(c);
		}
	}




	/**
	 * This method is called if the elements of an registered {@link ObservableMap2D} change and this listener is not muted.
	 */
	public abstract void onChanged(Map2DChangeListener.Change<R, C, V> c);




	/**
	 * @return true, if the value of the given change was added.
	 */
	public boolean wasValueAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasAdded() && !c.wasRemoved();
	}




	/**
	 * @return true, if the value of the given change was removed.
	 */
	public boolean wasValueRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasAdded() && c.wasRemoved();
	}




	/**
	 * @return true, if the value of the given change was changed.
	 */
	public boolean wasValueChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasAdded() && c.wasRemoved();
	}




	/**
	 * @return true, if the row of the given change was added.
	 */
	public boolean wasRowAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasRowAdded() && !c.wasRowRemoved();
	}




	/**
	 * @return true, if the row of the given change was removed.
	 */
	public boolean wasRowRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasRowAdded() && c.wasRowRemoved();
	}




	/**
	 * @return true, if the row of the given change was changed.
	 */
	public boolean wasRowChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasRowAdded() && c.wasRowRemoved();
	}




	/**
	 * @return true, if the column of the given change was added.
	 */
	public boolean wasColumnAdded(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasColumnAdded() && !c.wasColumnRemoved();
	}




	/**
	 * @return true, if the column of the given change was removed.
	 */
	public boolean wasColumnRemoved(Map2DChangeListener.Change<R, C, V> c) {
		return !c.wasColumnAdded() && c.wasColumnRemoved();
	}




	/**
	 * @return true, if the column of the given change was changed.
	 */
	public boolean wasColumnChanged(Map2DChangeListener.Change<R, C, V> c) {
		return c.wasColumnAdded() && c.wasColumnRemoved();
	}


}
