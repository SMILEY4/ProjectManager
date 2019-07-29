package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.*;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;

import java.lang.reflect.Type;

public class JsonUtils {


	private static Project lastProject;
	private static Gson gson;




	public static Gson getGson(Project project) {
		if (gson == null || (project != lastProject && project != null)) {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(AttributeValue.class, new AttributeValueDeserializer(project));
			builder.registerTypeAdapter(TaskValue.class, new TaskValueDeserializer(project));
			builder.registerTypeAdapter(TaskValue.class, new TaskValueSerializer(project));
			builder.registerTypeAdapter(TaskFlag.class, new TaskFlagSerializer(project));
			builder.registerTypeAdapter(TaskFlag.class, new TaskFlagDeserializer(project));
			builder.setPrettyPrinting();
			Converters.registerAll(builder);
			gson = builder.create();
			lastProject = project;
		}
		return gson;
	}


//	static class DependencyValueSerializer implements JsonSerializer<DependencyValue> {
//
//
//		private Project project;
//
//
//
//
//		public DependencyValueSerializer(Project project) {
//			this.project = project;
//		}
//
//
//
//
//		@Override
//		public JsonElement serialize(DependencyValue data, Type typeOfSrc, JsonSerializationContext context) {
//			Task[] tasks = data.getValue();
//			int[] ids = new int[tasks.length];
//			for (int i = 0; i < tasks.length; i++) {
//				ids[i] = TaskLogic.getTaskID(tasks[i]);
//			}
//			return getGson(project).toJsonTree(ids);
//		}
//
//	}


//
//
//	static class DependencyValueDeserializer implements JsonDeserializer<DependencyValue> {
//
//
//		private Project project;
//
//
//
//
//		public DependencyValueDeserializer(Project project) {
//			this.project = project;
//		}
//
//
//
//
//		@Override
//		public DependencyValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//			Gson gson = getGson(project);
//			int[] ids = gson.fromJson(json, int[].class);
//			Task[] tasks = new Task[ids.length];
//			for (int i = 0; i < ids.length; i++) {
//				tasks[i] = TaskLogic.findTaskByID(project, ids[i]);
//			}
//			return new DependencyValue(tasks);
//		}
//
//	}






	static class TaskFlagSerializer implements JsonSerializer<TaskFlag> {


		private Project project;




		public TaskFlagSerializer(Project project) {
			this.project = project;
		}




		@Override
		public JsonElement serialize(TaskFlag flag, Type typeOfSrc, JsonSerializationContext context) {
			POJOFlag data = new POJOFlag(flag);
			return getGson(project).toJsonTree(data);
		}

	}






	static class TaskFlagDeserializer implements JsonDeserializer<TaskFlag> {


		private Project project;




		public TaskFlagDeserializer(Project project) {
			this.project = project;
		}




		@Override
		public TaskFlag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			Gson gson = getGson(project);
			POJOFlag data = gson.fromJson(json, POJOFlag.class);
			return data.toFlag();
		}

	}






	static class AttributeValueDeserializer implements JsonDeserializer<AttributeValue<?>> {


		private Project project;




		public AttributeValueDeserializer(Project project) {
			this.project = project;
		}




		@Override
		public AttributeValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();

			if (!(jsonObject.has("type") && jsonObject.has("value"))) {
				Logger.get().warn("Could not deserialize AttributeValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeValueType type = AttributeValueType.valueOf(strType);

			Gson gson = getGson(project);

			switch (type) {
				case USE_DEFAULT: {
					return gson.fromJson(jsonObject, UseDefaultValue.class);
				}
				case DEFAULT_VALUE: {
					return gson.fromJson(jsonObject, DefaultValue.class);
				}
				case CARD_DISPLAY_TYPE: {
					return gson.fromJson(jsonObject, CardDisplayTypeValue.class);
				}
				case NUMBER_DEC_PLACES: {
					return gson.fromJson(jsonObject, NumberDecPlacesValue.class);
				}
				case NUMBER_MIN: {
					return gson.fromJson(jsonObject, NumberMinValue.class);
				}
				case NUMBER_MAX: {
					return gson.fromJson(jsonObject, NumberMaxValue.class);
				}
				case CHOICE_VALUES: {
					return gson.fromJson(jsonObject, ChoiceListValue.class);
				}
				case FLAG_LIST: {
					return gson.fromJson(jsonObject, FlagListValue.class);
				}
				case TEXT_MULTILINE: {
					return gson.fromJson(jsonObject, TextMultilineValue.class);
				}
				default: {
					Logger.get().warn("Could not deserialize AttributeValue: Unknown type: " + type);
					return null;
				}
			}

		}

	}






	static class TaskValueSerializer implements JsonSerializer<TaskValue<?>> {


		private Project project;




		public TaskValueSerializer(Project project) {
			this.project = project;
		}




		@Override
		public JsonElement serialize(TaskValue<?> value, Type typeOfSrc, JsonSerializationContext context) {

			Gson gson = getGson(project);

			switch (value.getAttType()) {
				case ID: {
					return gson.toJsonTree(value, IDValue.class);
				}
				case DESCRIPTION: {
					return gson.toJsonTree(value, DescriptionValue.class);
				}
				case CREATED: {
					return gson.toJsonTree(value, CreatedValue.class);
				}
				case LAST_UPDATED: {
					return gson.toJsonTree(value, LastUpdatedValue.class);
				}
				case FLAG: {
					return gson.toJsonTree(value, FlagValue.class);
				}
				case TEXT: {
					return gson.toJsonTree(value, TextValue.class);
				}
				case NUMBER: {
					return gson.toJsonTree(value, NumberValue.class);
				}
				case BOOLEAN: {
					return gson.toJsonTree(value, BoolValue.class);
				}
				case CHOICE: {
					return gson.toJsonTree(value, ChoiceValue.class);
				}
				case DATE: {
					return gson.toJsonTree(value, DateValue.class);
				}
				case DEPENDENCY: {
					return gson.toJsonTree(new POJODependencyValue((DependencyValue) value), POJODependencyValue.class);
				}
				default: {
					Logger.get().warn("Could not serialize TaskValue: Unknown type: " + value.getAttType());
					return null;
				}
			}

		}

	}






	static class TaskValueDeserializer implements JsonDeserializer<TaskValue<?>> {


		private Project project;




		public TaskValueDeserializer(Project project) {
			this.project = project;
		}




		@Override
		public TaskValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();
			if (!jsonObject.has("type")) {
				Logger.get().warn("Could not deserialize TaskValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeType type = AttributeType.valueOf(strType);

			Gson gson = getGson(project);

			switch (type) {
				case ID: {
					return gson.fromJson(jsonObject, IDValue.class);
				}
				case DESCRIPTION: {
					return gson.fromJson(jsonObject, DescriptionValue.class);
				}
				case CREATED: {
					return gson.fromJson(jsonObject, CreatedValue.class);
				}
				case LAST_UPDATED: {
					return gson.fromJson(jsonObject, LastUpdatedValue.class);
				}
				case FLAG: {
					return gson.fromJson(jsonObject, FlagValue.class);
				}
				case TEXT: {
					return gson.fromJson(jsonObject, TextValue.class);
				}
				case NUMBER: {
					return gson.fromJson(jsonObject, NumberValue.class);
				}
				case BOOLEAN: {
					return gson.fromJson(jsonObject, BoolValue.class);
				}
				case CHOICE: {
					return gson.fromJson(jsonObject, ChoiceValue.class);
				}
				case DATE: {
					return gson.fromJson(jsonObject, DateValue.class);
				}
				case DEPENDENCY: {
					return gson.fromJson(jsonObject, POJODependencyValue.class).toValue(project);
				}
				default: {
					Logger.get().warn("Could not deserialize TaskValue: Unknown type: " + type);
					return null;
				}
			}
		}

	}

}
