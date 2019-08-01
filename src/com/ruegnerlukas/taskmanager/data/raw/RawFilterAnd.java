package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;

import java.util.ArrayList;
import java.util.List;

public class RawFilterAnd extends RawFilterCriteria {


	public List<RawFilterCriteria> criterias;




	public RawFilterAnd() {
		this.type = FilterCriteria.CriteriaType.AND;
	}




	public static RawFilterAnd toRaw(AndFilterCriteria criteria) {
		RawFilterAnd raw = new RawFilterAnd();
		raw.criterias = new ArrayList<>();
		for (FilterCriteria c : criteria.subCriteria) {
			if (c.type == FilterCriteria.CriteriaType.AND) {
				raw.criterias.add(RawFilterAnd.toRaw((AndFilterCriteria) c));
			}
			if (c.type == FilterCriteria.CriteriaType.OR) {
				raw.criterias.add(RawFilterOr.toRaw((OrFilterCriteria) c));
			}
			if (c.type == FilterCriteria.CriteriaType.TERMINAL) {
				raw.criterias.add(RawFilterTerminal.toRaw((TerminalFilterCriteria) c));
			}
		}
		return raw;
	}




	public static AndFilterCriteria fromRaw(RawFilterAnd rawCriteria, Project project) {
		FilterCriteria[] criterias = new FilterCriteria[rawCriteria.criterias.size()];
		for (int i = 0; i < criterias.length; i++) {
			criterias[i] = RawFilterCriteria.fromRaw(rawCriteria.criterias.get(i), project);
		}
		return new AndFilterCriteria(criterias);
	}

}
