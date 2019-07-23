package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;

import java.util.ArrayList;
import java.util.List;

public class POJOPresetSort {


	public String presetName;
	public List<POJOSortElement> elements;




	public POJOPresetSort(String name, SortData data) {
		this.presetName = name;
		this.elements = new ArrayList<>();
		for (SortElement e : data.sortElements) {
			this.elements.add(new POJOSortElement(e));
		}
	}




	class POJOSortElement {


		public String attribte;
		public SortElement.SortDir dir;




		public POJOSortElement(SortElement element) {
			this.dir = element.dir.get();
			this.attribte = element.attribute.get().name.get();
		}

	}

}
