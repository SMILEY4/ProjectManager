package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.AndFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;

public class RawPresetFilter {


	public String presetName;
	public RawFilterCriteria criteria;




	public static RawPresetFilter toRaw(String presetName, FilterCriteria criteria) {
		RawPresetFilter raw = new RawPresetFilter();
		raw.presetName = presetName;
		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			raw.criteria = RawFilterAnd.toRaw((AndFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			raw.criteria = RawFilterOr.toRaw((OrFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			raw.criteria = RawFilterTerminal.toRaw((TerminalFilterCriteria) criteria);
		}
		return raw;
	}




	public static FilterCriteria fromRaw(RawPresetFilter rawPreset, Project project) {
		if (rawPreset.criteria == null) {
			return null;
		} else {
			return RawFilterCriteria.fromRaw(rawPreset.criteria, project);
		}
	}


}
