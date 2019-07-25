package com.ruegnerlukas.taskmanager.utils;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestProjectFactory {


	/**
	 * @param name             the name of the project
	 * @param attTypes         an array of {@link AttributeType}s that will get added to the project. This array must be the same size as {@code attNames}.
	 * @param attNames         an array containing the names for the attributes. This array must be the same size as {@code attTypes}.
	 * @param nTasks           specifying how many tasks will get added to the project
	 * @param randomValues
	 * @param timeBetweenTasks how many milliseconds to wait between the creation of the tasks
	 */
	public static Project build(String name, AttributeType[] attTypes, String[] attNames, int nTasks, boolean randomValues, long timeBetweenTasks) {

		Project project = ProjectLogic.createNewLocalProject(name);

		// values
		for (int i = 0; i < attTypes.length; i++) {
			TaskAttribute attribute = AttributeLogic.createTaskAttribute(attTypes[i], attNames[i], project);
			ProjectLogic.addAttributeToProject(project, attribute);

			if (attTypes[i] == AttributeType.CHOICE) {
				Set<String> choices = new HashSet<>(Arrays.asList(LoremIpsum.get(3, 7, true).split(" ")));
				choices.add("SomeValue");
				String[] array = new String[choices.size()];
				int j = 0;
				for (String choice : choices) {
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
				if (!attribute.type.get().fixed) {
					TaskValue<?> randomValue = createRandomValue(attribute);
					if (randomValue != null) {
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




	/**
	 * @return a random but valid {@link TaskValue} for the given {@link TaskAttribute}
	 */
	public static TaskValue<?> createRandomValue(TaskAttribute attribute) {
		return createRandomValue(attribute, System.nanoTime());
	}




	/**
	 * @return a random but valid {@link TaskValue} for the given {@link TaskAttribute} (dependent on the given seed)
	 */
	public static TaskValue<?> createRandomValue(TaskAttribute attribute, long seed) {

		final double PERC_NULL = 0.2;
		final Random random = new Random(seed);

		if (random.nextDouble() < PERC_NULL) {
			return null;

		} else {

			switch (attribute.type.get()) {
				case TEXT: {
					TextValue value = new TextValue(LoremIpsum.get(random.nextInt(20) + 5, true));
					return AttributeLogic.TEXT_LOGIC.generateValidTaskValue(value, attribute, false);
				}
				case NUMBER: {
					NumberValue value = new NumberValue(random.nextFloat() * random.nextInt(100) - 50);
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
