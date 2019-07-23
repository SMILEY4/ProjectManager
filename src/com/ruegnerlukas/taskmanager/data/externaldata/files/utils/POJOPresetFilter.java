package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

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

public class POJOPresetFilter {


	public String presetName;
	public POJOCriteria criteria;




	public POJOPresetFilter(String presetName, FilterCriteria criteria) {
		this.presetName = presetName;
		if (criteria.type == FilterCriteria.CriteriaType.AND) {
			this.criteria = new POJOAnd((AndFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.OR) {
			this.criteria = new POJOOr((OrFilterCriteria) criteria);
		}
		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
			this.criteria = new POJOTerminal((TerminalFilterCriteria) criteria);
		}
	}




	class POJOAnd implements POJOCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.AND;
		public List<POJOCriteria> criterias;




		public POJOAnd(AndFilterCriteria criteria) {
			criterias = new ArrayList<>();
			for (FilterCriteria c : criteria.subCriteria) {
				if (c.type == FilterCriteria.CriteriaType.AND) {
					this.criterias.add(new POJOAnd((AndFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.OR) {
					this.criterias.add(new POJOOr((OrFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.TERMINAL) {
					this.criterias.add(new POJOTerminal((TerminalFilterCriteria) c));
				}
			}
		}


	}






	class POJOOr implements POJOCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.OR;
		public List<POJOCriteria> criterias;




		public POJOOr(OrFilterCriteria criteria) {
			criterias = new ArrayList<>();
			for (FilterCriteria c : criteria.subCriteria) {
				if (c.type == FilterCriteria.CriteriaType.AND) {
					this.criterias.add(new POJOAnd((AndFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.OR) {
					this.criterias.add(new POJOOr((OrFilterCriteria) c));
				}
				if (c.type == FilterCriteria.CriteriaType.TERMINAL) {
					this.criterias.add(new POJOTerminal((TerminalFilterCriteria) c));
				}
			}
		}


	}






	class POJOTerminal implements POJOCriteria {


		public FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.TERMINAL;
		public String attribute;
		public FilterOperation operation;
		public List<String> values;




		public POJOTerminal(TerminalFilterCriteria criteria) {
			this.attribute = criteria.attribute.name.get();
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






	interface POJOCriteria {


	}

}
