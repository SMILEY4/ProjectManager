package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes.*;

public class ContentNodeFactory {


	public static AttributeContentNode create(TaskAttribute attribute) {
		switch (attribute.type.get()) {
			case FLAG:
				return new FlagContentNode(attribute);
			case TEXT:
				return new TextContentNode(attribute);
			case NUMBER:
				return new NumberContentNode(attribute);
			case BOOLEAN:
				return new BooleanContentNode(attribute);
			case CHOICE:
				return new ChoiceContentNode(attribute);
			case DATE:
				return new DateContentNode(attribute);
			case DEPENDENCY:
				return new GenericContentNode(attribute);
			default:
				return new UnchangeableContentNode(attribute);
		}

	}




	public static boolean isUnchangable(TaskAttribute attribute) {
		AttributeType type = attribute.type.get();
		return type != AttributeType.BOOLEAN &&
				type != AttributeType.TEXT &&
				type != AttributeType.FLAG &&
				type != AttributeType.NUMBER &&
				type != AttributeType.CHOICE &&
				type != AttributeType.DATE &&
				type != AttributeType.DEPENDENCY;
	}

}
