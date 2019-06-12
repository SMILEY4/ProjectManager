package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.CommandHandler;
import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandAttributeSetValue {


	public static Command create() {

		String[] strType = new String[AttributeValueType.values().length];
		for (int i = 0; i < AttributeValueType.values().length; i++) {
			strType[i] = AttributeValueType.values()[i].toString().toLowerCase();
		}

		return new CommandBuilder()
				.text("attribute")
				.text("set")
				.text("value")
				.variable("attribute", String.class)
				.alternative("name", strType)
				.variable("value", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.optionalAlternative("force", "-f", "--force")
				.optionalAlternative("print-info", "-i", "--show-info")
				.setDescription("Sets the value with the given name of the given attribute to the new value. Add -l or --use-logic to use the rename-function of the Logic-Classes.")
				.setExecutor(CommandAttributeSetValue::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		final String attribName = result.getValue("attribute");
		final String varName = result.getValue("name");
		final String strValue = result.getValue("value");
		final boolean force = result.hasValue("force");
		final boolean useLogic = result.hasValue("use-logic");
		final boolean printInfo = result.hasValue("print-info");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could set attribute value: No project opened.");
		} else {

			TaskAttribute attribute = AttributeLogic.findAttribute(project, attribName);

			if (attribute == null) {
				ConsoleWindowHandler.print(Color.RED, "Could set attribute value: No attribute with name \"" + attribName + "\" found.");
			} else {

				if (attribute.type.get().fixed && !force) {
					ConsoleWindowHandler.print(Color.RED, "Could set attribute value: Attribute \"" + attribute.name.get() + "\" is fixed attribute.");

				} else {

					AttributeValue<?> attribValue = null;

					try {

						switch (varName.toUpperCase()) {
							case "USE_DEFAULT": {
								attribValue = new UseDefaultValue(Boolean.parseBoolean(strValue));
								break;
							}
							case "DEFAULT_VALUE": {

								switch (attribute.type.get()) {
									case ID: {
										attribValue = new DefaultValue(new IDValue(Integer.parseInt(strValue)));
										break;
									}
									case DESCRIPTION: {
										attribValue = new DefaultValue(new DescriptionValue(strValue));
										break;
									}
									case CREATED: {
										attribValue = new DefaultValue(new CreatedValue(LocalDateTime.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
										break;
									}
									case LAST_UPDATED: {
										attribValue = new DefaultValue(new LastUpdatedValue(LocalDateTime.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
										break;
									}
									case FLAG: {
										boolean success = false;
										for(TaskFlag flag : AttributeLogic.FLAG_LOGIC.getFlagList(attribute)) {
											if(flag.name.get().equalsIgnoreCase(strValue)) {
												attribValue = new DefaultValue(new FlagValue(flag));
												success = true;
												break;
											}
										}
										if(!success) {
											throw new Exception();
										}
										break;
									}
									case TEXT: {
										attribValue = new DefaultValue(new TextValue(strValue));
										break;
									}
									case NUMBER: {
										attribValue = new DefaultValue(new NumberValue(Double.parseDouble(strValue)));
										break;
									}
									case BOOLEAN: {
										attribValue = new DefaultValue(new BoolValue(Boolean.parseBoolean(strValue)));
										break;
									}
									case CHOICE: {
										attribValue = new DefaultValue(new ChoiceValue(strValue));
										break;
									}
									case DATE: {
										attribValue = new DefaultValue(new DateValue(LocalDate.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
										break;
									}
									case DEPENDENCY: {
										ConsoleWindowHandler.print(Color.RED, "Could set attribute value: Dependency-type is not yet supported.");
										throw new Exception();
									}
								}
								break;
							}
							case "CARD_DISPLAY_TYPE": {
								attribValue = new CardDisplayTypeValue(Boolean.parseBoolean(strValue));
								break;
							}
							case "NUMBER_DEC_PLACES": {
								attribValue = new NumberDecPlacesValue(Integer.parseInt(strValue));
								break;
							}
							case "NUMBER_MIN": {
								attribValue = new NumberMinValue(Double.parseDouble(strValue));
								break;
							}
							case "NUMBER_MAX": {
								attribValue = new NumberMaxValue(Double.parseDouble(strValue));
								break;
							}
							case "CHOICE_VALUES": {
								attribValue = new ChoiceListValue(strValue.split(","));
								break;
							}
							case "FLAG_LIST": {
								String[] strFlags = strValue.split(",");
								TaskFlag[] flags = new TaskFlag[strFlags.length];
								for(int i=0; i<strFlags.length; i++) {
									String[] strFlag = strFlags[i].split(":");
									flags[i] = new TaskFlag(strFlag[0], TaskFlag.FlagColor.valueOf(strFlag[1].toUpperCase()));
								}
								attribValue = new FlagListValue(flags);
								break;
							}
							case "TEXT_MULTILINE": {
								attribValue = new TextMultilineValue(Boolean.parseBoolean(strValue));
								break;
							}
						}

					} catch (Exception e) {
						ConsoleWindowHandler.print(Color.RED, "Could set attribute value: \"" + strValue + "\" is invalid value.");
						return;
					}

					if (attribValue == null) {
						ConsoleWindowHandler.print(Color.RED, "Could set attribute value: Value \"" + varName + "\" not found.");
					} else {
						if (useLogic) {
							AttributeLogic.setAttributeValue(project, attribute, attribValue, true);
						} else {
							attribute.values.put(attribValue.getType(), attribValue);
						}
					}

					if(printInfo) {
						CommandHandler.onCommand("attribute info \"" + attribute.name.get() + "\"");
					}

				}
			}

		}
	}


}
