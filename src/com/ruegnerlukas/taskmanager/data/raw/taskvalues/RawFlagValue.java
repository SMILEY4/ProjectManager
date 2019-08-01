package com.ruegnerlukas.taskmanager.data.raw.taskvalues;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.raw.RawFlag;
import com.ruegnerlukas.taskmanager.data.raw.RawProject;
import com.ruegnerlukas.taskmanager.data.raw.RawTaskAttribute;
import com.ruegnerlukas.taskmanager.data.raw.attributevalues.RawAttributeValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

public class RawFlagValue extends RawTaskValue {


	public RawFlag value;




	public static RawFlagValue toRaw(FlagValue value) {
		RawFlagValue raw = new RawFlagValue();
		raw.type = AttributeType.FLAG;
		raw.value = RawFlag.toRaw(value.getValue());
		return raw;
	}




	public static FlagValue fromRaw(RawFlagValue rawValue, RawProject rawProject, Project project) {
		if (project == null) {
			return null;
		}

		TaskAttribute attribute = AttributeLogic.findAttributeByType(project, AttributeType.FLAG);
		if (attribute == null) {
			// attribute not yet loaded
			RawTaskAttribute rawAttribute = null;
			for (RawTaskAttribute ra : rawProject.attributes) {
				if (ra.type == AttributeType.FLAG) {
					rawAttribute = ra;
					break;
				}
			}
			if (rawAttribute != null) {
				// add attribute
				attribute = RawTaskAttribute.baseFromRaw(rawAttribute, project);
				project.data.attributes.add(attribute);
				RawTaskAttribute.fromRaw(attribute, rawAttribute, rawProject, project);
			}
		}

		if (!attribute.values.containsKey(AttributeValueType.FLAG_LIST)) {
			// flaglist not yet loaded
			RawTaskAttribute rawAttribute = null;
			for (RawTaskAttribute ra : rawProject.attributes) {
				if (ra.type == AttributeType.FLAG) {
					rawAttribute = ra;
					break;
				}
			}
			if (rawAttribute != null) {
				RawAttributeValue rawAttValue = null;
				for (RawAttributeValue rav : rawAttribute.values.values()) {
					if (rav.type == AttributeValueType.FLAG_LIST) {
						rawAttValue = rav;
						break;
					}
				}
				if (rawAttValue != null) {
					// add attvalue to attribute
					AttributeValue<?> attValue = RawAttributeValue.fromRaw(rawAttValue, rawProject, project);
					attribute.values.put(AttributeValueType.FLAG_LIST, attValue);
				}
			}
		}

		TaskFlag[] flags = AttributeLogic.FLAG_LOGIC.getFlagList(attribute);
		for (TaskFlag flag : flags) {
			if (flag.name.get().equals(rawValue.value.name)) {
				return new FlagValue(flag);
			}
		}
		return new FlagValue(RawFlag.fromRaw(rawValue.value));

	}


}
