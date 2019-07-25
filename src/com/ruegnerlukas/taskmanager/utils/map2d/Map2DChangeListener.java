package com.ruegnerlukas.taskmanager.utils.map2d;

public interface Map2DChangeListener<R, C, V> {


	abstract class Change<R, C, V> {


		private final ObservableMap2D<R, C, V> map;




		public Change(ObservableMap2D<R, C, V> map) {
			this.map = map;
		}




		public ObservableMap2D<R, C, V> getMap() {
			return map;
		}




		public abstract boolean wasRowAdded();

		public abstract boolean wasRowRemoved();

		public abstract boolean wasColumnAdded();

		public abstract boolean wasColumnRemoved();

		public abstract boolean wasAdded();

		public abstract boolean wasRemoved();

		public abstract R getRow();

		public abstract C getColumn();

		public abstract V getValueRemoved();

		public abstract V getValueAdded();

	}


	void onChanged(Map2DChangeListener.Change<R, C, V> change);


}

