package com.ruegnerlukas.taskmanager.utils.map2d;

import java.util.Map;

public interface Map2DEntryMapper<R, C, V, T> {


	T map(R row, Map<C, V> values);

	default T mapNull() {
		return null;
	}


}
