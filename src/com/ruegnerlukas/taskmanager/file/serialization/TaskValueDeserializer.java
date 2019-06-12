package com.ruegnerlukas.taskmanager.file.serialization;

import com.google.gson.*;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.file.FileHandler;

import java.lang.reflect.Type;

public class TaskValueDeserializer implements JsonDeserializer<TaskValue<?>> {


	@Override
	public TaskValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();
		if(!jsonObject.has("type")) {
			Logger.get().warn("Could not deserialize TaskValue: Invalid input: " + jsonObject.toString());
			return null;
		}

		String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
		AttributeType type = AttributeType.valueOf(strType);
		
		Gson gson = FileHandler.buildGson();

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
