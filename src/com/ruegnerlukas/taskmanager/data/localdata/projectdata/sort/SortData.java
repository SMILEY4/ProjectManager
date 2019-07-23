package com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortData {


	public final List<SortElement> sortElements;




	public SortData(SortElement... sortElements) {
		this(Arrays.asList(sortElements));
	}




	public SortData(List<SortElement> sortElements) {
		this.sortElements = Collections.unmodifiableList(new ArrayList<>(sortElements));
	}


}
