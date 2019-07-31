package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RawPresetFilter {


	public String presetName;
	public RawCriteria criteria;




	public static RawPresetFilter toRaw(String presetName, FilterCriteria criteria) {
		RawPresetFilter raw = new RawPresetFilter();
		raw.presetName = presetName;
		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			raw.criteria = new RawAnd((AndFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			raw.criteria = new RawOr((OrFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			raw.criteria = new RawTerminal((TerminalFilterCriteria) criteria);
		}
		return raw;
	}




	static class RawAnd implements RawCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.AND;
		public List<RawCriteria> criterias;




		public RawAnd(AndFilterCriteria criteria) {
			criterias = new ArrayList<>();
			for (FilterCriteria c : criteria.subCriteria) {
				if (c.type == FilterCriteria.CriteriaType.AND) {
					this.criterias.add(new RawAnd((AndFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.OR) {
					this.criterias.add(new RawOr((OrFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.TERMINAL) {
					this.criterias.add(new RawTerminal((TerminalFilterCriteria) c));
				}
			}
		}


	}






	static class RawOr implements RawCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.OR;
		public List<RawCriteria> criterias;




		public RawOr(OrFilterCriteria criteria) {
			criterias = new ArrayList<>();
			for (FilterCriteria c : criteria.subCriteria) {
				if (c.type == FilterCriteria.CriteriaType.AND) {
					this.criterias.add(new RawAnd((AndFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.OR) {
					this.criterias.add(new RawOr((OrFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.TERMINAL) {
					this.criterias.add(new RawTerminal((TerminalFilterCriteria) c));
				}
			}
		}


	}






	static class RawTerminal implements RawCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.TERMINAL;
		public int attribute;
		public FilterOperation operation;
		public List<String> values;




		public RawTerminal(TerminalFilterCriteria criteria) {
			this.attribute = criteria.attribute.id;
			this.operation = criteria.operation;
			this.values = new ArrayList<>();
			for (Object value : criteria.values) {

				String strValue = null;

				if (value instanceof Boolean) {
					strValue = "Boolean:" + value;
				}
				if (value instanceof Integer) {
					strValue = "Integer:" + value;
				}
				if (value instanceof Double) {
					strValue = "Double:" + value;
				}
				if (value instanceof String) {
					strValue = "String:" + value;
				}
				if (value instanceof TaskFlag) {
					strValue = "TaskFlag:" + ((TaskFlag) value).name.get();
				}
				if (value instanceof LocalDate) {
					strValue = "LocalDate:" + DateTimeFormatter.ISO_LOCAL_DATE.format(((LocalDate) value));
				}
				if (value instanceof LocalDateTime) {
					strValue = "LocalDateTime:" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(((LocalDateTime) value));
				}
				if (value instanceof Task) {
					Task task = (Task) value;
					for (TaskValue<?> tv : task.values.values()) {
						if (tv.getAttType() == AttributeType.ID) {
							IDValue idValue = (IDValue) tv;
							strValue = "Task:" + idValue.getValue();
						}
					}
				}

				values.add(strValue);
			}

		}

	}






	interface RawCriteria {


	}

}
