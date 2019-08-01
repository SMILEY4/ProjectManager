package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;

import java.util.ArrayList;
import java.util.List;

public class RawFilterOr extends RawFilterCriteria {


	public List<RawFilterCriteria> criterias;




	public RawFilterOr() {
		this.type = FilterCriteria.CriteriaType.OR;

	}




	public static RawFilterOr toRaw(OrFilterCriteria criteria) {
		RawFilterOr raw = new RawFilterOr();
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




	public static OrFilterCriteria fromRaw(RawFilterOr rawCriteria, Project project) {
		FilterCriteria[] criterias = new FilterCriteria[rawCriteria.criterias.size()];
		for (int i = 0; i < criterias.length; i++) {
			criterias[i] = RawFilterCriteria.fromRaw(rawCriteria.criterias.get(i), project);
		}
		return new OrFilterCriteria(criterias);
	}

}
