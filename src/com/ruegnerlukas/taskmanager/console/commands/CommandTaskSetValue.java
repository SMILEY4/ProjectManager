package com.ruegnerlukas.taskmanager.console.commands;

import com.ruegnerlukas.taskmanager.console.CommandHandler;
import com.ruegnerlukas.taskmanager.console.ConsoleWindowHandler;
import com.ruegnerlukas.taskmanager.console.commandbuilder.Command;
import com.ruegnerlukas.taskmanager.console.commandbuilder.CommandBuilder;
import com.ruegnerlukas.taskmanager.console.commandresults.SuccessfulCommandResult;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandTaskSetValue {


	public static Command create() {
		return new CommandBuilder()
				.text("task")
				.text("set")
				.text("value")
				.variable("id", Integer.class)
				.variable("attribute", String.class)
				.variable("value", String.class)
				.optionalAlternative("use-logic", "-l", "--use-logic")
				.optionalAlternative("print-info", "-i", "--show-info")
				.setDescription("Removes the given task from the opened project.")
				.setExecutor(CommandTaskSetValue::onCommand)
				.create();
	}




	private static void onCommand(SuccessfulCommandResult result) {

		int id = result.getValue("id");
		String strAttribute = result.getValue("attribute");
		String strValue = result.getValue("value");
		boolean useLogic = result.hasValue("use-logic");
		final boolean printInfo = result.hasValue("print-info");


		Project project = Data.projectProperty.get();
		if (project == null) {
			ConsoleWindowHandler.print(Color.RED, "Could not set value of task: No project opened.");

		} else {

			Task task = null;

			TaskAttribute attributeID = AttributeLogic.findAttribute(project, AttributeType.ID);
			for (Task t : project.data.tasks) {
				IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(t, attributeID);
				if (valueID.getValue() == id) {
					task = t;
					break;
				}
			}

			if (task == null) {
				ConsoleWindowHandler.print(Color.RED, "Could not set task value: Task T-" + id + " not found.");

			} else {

				TaskAttribute attribute = AttributeLogic.findAttribute(project, strAttribute);
				if (attribute == null) {
					ConsoleWindowHandler.print(Color.RED, "Could not set task value: Attribute \"" + strAttribute + "\" not found.");
					return;
				}

				TaskValue<?> value = null;

				try {
					switch (attribute.type.get()) {
						case ID: {
							value = new IDValue(Integer.parseInt(strValue));
							break;
						}
						case DESCRIPTION: {
							value = new DescriptionValue(strValue);
							break;
						}
						case CREATED: {
							value = new CreatedValue(LocalDateTime.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
							break;
						}
						case LAST_UPDATED: {
							value = new LastUpdatedValue(LocalDateTime.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
							break;
						}
						case FLAG: {
							boolean success = false;
							for(TaskFlag flag : AttributeLogic.FLAG_LOGIC.getFlagList(attribute)) {
								if(flag.name.get().equalsIgnoreCase(strValue)) {
									value = new FlagValue(flag);
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
							value = new TextValue(strValue);
							break;
						}
						case NUMBER: {
							value = new NumberValue(Double.parseDouble(strValue));
							break;
						}
						case BOOLEAN: {
							value = new BoolValue(Boolean.parseBoolean(strValue));
							break;
						}
						case CHOICE: {
							value = new ChoiceValue(strValue);
							break;
						}
						case DATE: {
							value = new DateValue(LocalDate.parse(strValue, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
							break;
						}
						case DEPENDENCY: {
							ConsoleWindowHandler.print(Color.RED, "Could not set task value: Dependency-type is not yet supported.");
							throw new Exception();
						}
					}
				} catch (Exception e) {
					ConsoleWindowHandler.print(Color.RED, "Could not set task value: \"" + strValue + "\" is invalid value.");
					return;
				}


				if(useLogic) {
					TaskLogic.setValue(project, task, attribute, value);
				} else {
					task.values.put(attribute, value);
				}


				if(printInfo) {
					CommandHandler.onCommand("task info " + id);
				}

			}

		}
	}


}
