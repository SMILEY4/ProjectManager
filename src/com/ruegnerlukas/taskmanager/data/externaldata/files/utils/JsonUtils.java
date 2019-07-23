package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.*;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;

import java.lang.reflect.Type;

public class JsonUtils {


	public static Gson buildGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AttributeValue.class, new AttributeValueDeserializer());
		builder.registerTypeAdapter(TaskValue.class, new TaskValueDeserializer());
		builder.setPrettyPrinting();
		Converters.registerAll(builder);
		return builder.create();
	}




	static class AttributeValueDeserializer implements JsonDeserializer<AttributeValue<?>> {


		@Override
		public AttributeValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();

			if (!(jsonObject.has("type") && jsonObject.has("value"))) {
				Logger.get().warn("Could not deserialize AttributeValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeValueType type = AttributeValueType.valueOf(strType);

			Gson gson = buildGson();

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




	static class TaskValueDeserializer implements JsonDeserializer<TaskValue<?>> {


		@Override
		public TaskValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();
			if(!jsonObject.has("type")) {
				Logger.get().warn("Could not deserialize TaskValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeType type = AttributeType.valueOf(strType);

			Gson gson = buildGson();

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
					return gson.fromJson(jsonObject, DependencyValue.class);
				}
				default: {
					Logger.get().warn("Could not deserialize TaskValue: Unknown type: " + type);
					return null;
				}
			}
		}

	}

}
