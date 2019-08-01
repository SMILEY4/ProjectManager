package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RawFilterTerminal extends RawFilterCriteria {


	public int attribute;
	public FilterOperation operation;
	public List<String> values;




	public RawFilterTerminal() {
		this.type = FilterCriteria.CriteriaType.TERMINAL;
	}




	public static RawFilterTerminal toRaw(TerminalFilterCriteria criteria) {

		RawFilterTerminal raw = new RawFilterTerminal();

		raw.attribute = criteria.attribute.id;
		raw.operation = criteria.operation;
		raw.values = new ArrayList<>();
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
				strValue = "Task:" + TaskLogic.getTaskID(task);
			}

			raw.values.add(strValue);
		}

		return raw;
	}




	public static TerminalFilterCriteria fromRaw(RawFilterTerminal rawCriteria, Project project) {

		TaskAttribute attribute = AttributeLogic.findAttributeByID(project, rawCriteria.attribute);
		FilterOperation operation = rawCriteria.operation;

		Object[] values = new Object[rawCriteria.values.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = convertValue(rawCriteria.values.get(i), project);
		}

		return new TerminalFilterCriteria(attribute, operation, values);
	}




	private static Object convertValue(String strObject, Project project) {

		final String strType = strObject.substring(0, strObject.indexOf(":"));
		final String strValue = strObject.substring(strObject.indexOf(":") + 1);

		switch (strType) {
			case "Boolean": {
				return Boolean.parseBoolean(strValue);
			}
			case "Integer": {
				return Integer.parseInt(strValue);

			}
			case "Double": {
				return Double.parseDouble(strValue);
			}
			case "String": {
				return strValue;
			}
			case "TaskFlag": {
				TaskAttribute attribute = AttributeLogic.findAttributeByType(project, AttributeType.FLAG);
				if (attribute == null) {
					return null;
				}
				for (TaskFlag flag : AttributeLogic.FLAG_LOGIC.getFlagList(attribute)) {
					if (flag.name.get().equals(strValue)) {
						return flag;
					}
				}
				return null;
			}
			case "LocalDate": {
				return LocalDate.parse(strValue, DateTimeFormatter.ISO_LOCAL_DATE);
			}
			case "LocalDateTime": {
				return LocalDateTime.parse(strValue, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
			case "Task": {
				return TaskLogic.findTaskByID(project, Integer.parseInt(strValue));
			}
			default: {
				return null;
			}
		}


	}


}
