package com.ruegnerlukas.taskmanager.file;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.file.serialization.AttributeValueDeserializer;
import com.ruegnerlukas.taskmanager.file.serialization.TaskValueDeserializer;
import com.ruegnerlukas.taskmanager.utils.TestProjectFactory;

import java.io.File;

public class FileHandler {


	public static void main(String[] args) {
		Project project = TestProjectFactory.build(
				"Test Project",
				new AttributeType[]{AttributeType.CHOICE, AttributeType.NUMBER, AttributeType.DATE},
				new String[]{"Att Choice", "Att Number", "Att Date"},
				6,
				true,
				20);

		File file = new File("C:\\Users\\LukasRuegner\\Desktop\\projectTest.json");

		FileHandler.writeProject(project, file);
		System.out.println("project written to " + file.getAbsolutePath());

		System.out.println(file);

		Project fileProject = FileHandler.readerProject(file);
		System.out.println("project read from " + file.getAbsolutePath());

		System.out.println(fileProject);

	}




	private static ProjectFileReader reader = new ProjectFileReaderImpl();
	private static ProjectFileWriter writer = new ProjectFileWriterImpl();




	public static Project readerProject(File file) {
		return reader.readProject(file);
	}




	public static void writeProject(Project project, File file) {
		writer.writeToFile(project, file);
	}




	public static Gson buildGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AttributeValue.class, new AttributeValueDeserializer());
		builder.registerTypeAdapter(TaskValue.class, new TaskValueDeserializer());
		builder.setPrettyPrinting();
		Converters.registerAll(builder);
		return builder.create();
	}

}
