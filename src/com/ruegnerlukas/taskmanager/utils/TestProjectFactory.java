package com.ruegnerlukas.taskmanager.utils;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestProjectFactory {


	public static Project build(String name, AttributeType[] attTypes, String[] attNames, int nTasks, boolean randomValues, long timeBetweenTasks) {

		Project project = ProjectLogic.createNewProject(name);

		// values
		for (int i = 0; i < attTypes.length; i++) {
			TaskAttribute attribute = AttributeLogic.createTaskAttribute(attTypes[i], attNames[i]);
			ProjectLogic.addAttributeToProject(project, attribute);

			if(attTypes[i] == AttributeType.CHOICE) {
				Set<String> choices = new HashSet<>(Arrays.asList(LoremIpsum.get(3, 7, true).split(" ")));
				choices.add("SomeValue");
				String[] array = new String[choices.size()];
				int j=0;
				for(String choice : choices) {
					array[j++] = choice;
				}
				AttributeLogic.CHOICE_LOGIC.setValueList(attribute, array);
			}

		}

		// tasks
		for (int i = 0; i < nTasks; i++) {
			Task task = TaskLogic.createTask(project);
			ProjectLogic.addTaskToProject(project, task);

			for (TaskAttribute attribute : project.data.attributes) {
				if(!attribute.type.get().fixed) {
					TaskValue<?> randomValue = createRandomValue(attribute);
					if(randomValue != null) {
						TaskLogic.setValue(project, task, attribute, randomValue);
					}
				}
			}

			try {
				Thread.sleep(timeBetweenTasks);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		return project;
	}



	public static TaskValue<?> createRandomValue(TaskAttribute attribute) {
		return createRandomValue(attribute, System.nanoTime());
	}


	public static TaskValue<?> createRandomValue(TaskAttribute attribute, long seed) {

		final double PERC_NULL = 0.2;
		final Random random = new Random();

		if(random.nextDouble() < PERC_NULL) {
			return null;

		} else {

			switch (attribute.type.get()) {
				case TEXT: {
					TextValue value = new TextValue(LoremIpsum.get(random.nextInt(20)+5, true));
					return AttributeLogic.TEXT_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				case NUMBER: {
					NumberValue value = new NumberValue(random.nextFloat()*random.nextInt(100) - 50);
					return AttributeLogic.NUMBER_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				case BOOLEAN: {
					BoolValue value = new BoolValue(random.nextBoolean());
					return AttributeLogic.BOOLEAN_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				case CHOICE: {
					String[] choices = AttributeLogic.CHOICE_LOGIC.getValueList(attribute);
					ChoiceValue value = new ChoiceValue(choices[random.nextInt(choices.length)]);
					return AttributeLogic.CHOICE_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				case DATE: {
					DateValue value = new DateValue(LocalDate.now().plusDays(random.nextInt(10)));
					return AttributeLogic.DATE_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				default: {
					return new NoValue();
				}
			}


		}

	}


}
