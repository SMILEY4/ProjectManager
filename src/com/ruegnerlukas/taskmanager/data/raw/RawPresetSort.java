package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

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




	public static SortData fromRaw(RawPresetSort rawPreset, Project project) {
		SortElement[] elements = new SortElement[rawPreset.elements.size()];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = RawSortElement.fromRaw(rawPreset.elements.get(i), project);
		}
		return new SortData(elements);
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




		public static SortElement fromRaw(RawSortElement rawElement, Project project) {
			return new SortElement(AttributeLogic.findAttributeByID(project, rawElement.attribute), rawElement.dir);
		}

	}

}
