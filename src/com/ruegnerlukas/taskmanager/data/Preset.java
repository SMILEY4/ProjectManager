package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;

import java.util.ArrayList;
import java.util.List;

public class Preset {


	public String name;
	public boolean isIndividual = false;
	public List<FilterCriteria> filterCriteria = new ArrayList<>();
	public AttributeGroupData groupData = new AttributeGroupData();
	public List<SortElement> sortElements = new ArrayList<>();




	public Preset(String name) {
		this(name, false);
	}




	public Preset(String name, boolean isIndividual) {
		this.name = name;
		this.isIndividual = isIndividual;
	}


}
