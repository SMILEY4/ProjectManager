package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;

import java.util.ArrayList;
import java.util.List;

public class RawPresetSort {


	public String presetName;
	public List<RawSortElement> elements = new ArrayList<>();




	public static RawPresetSort toRaw(String name, SortData data) {
		RawPresetSort raw = new RawPresetSort();
		raw.presetName = name;
		for (SortElement e : data.sortElements) {
			raw.elements.add(RawSortElement.toRaw(e));
		}
		return raw;
	}




	static class RawSortElement {


		public int attribute;
		public SortElement.SortDir dir;




		public static RawSortElement toRaw(SortElement element) {
			RawSortElement raw = new RawSortElement();
			raw.dir = element.dir.get();
			raw.attribute = element.attribute.get().id;
			return raw;
		}


	}

}
