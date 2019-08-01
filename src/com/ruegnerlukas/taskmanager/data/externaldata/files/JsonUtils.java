package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.*;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.raw.*;
import com.ruegnerlukas.taskmanager.data.raw.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.raw.taskvalues.*;

import java.lang.reflect.Type;

public class JsonUtils {


	public static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.registerTypeAdapter(RawAttributeValue.class, new RawAttributeValueDeserializer());
		builder.registerTypeAdapter(RawAttributeValue.class, new RawAttributeValueSerializer());
		builder.registerTypeAdapter(RawTaskValue.class, new RawTaskValueDeserializer());
		builder.registerTypeAdapter(RawTaskValue.class, new RawTaskValueSerializer());
		builder.registerTypeAdapter(RawFilterCriteria.class, new RawFilterCriteriaSerializer());
		builder.registerTypeAdapter(RawFilterCriteria.class, new RawFilterCriteriaDeserializer());
		Converters.registerAll(builder);
		return builder.create();
	}




	static class RawFilterCriteriaSerializer implements JsonSerializer<RawFilterCriteria> {


		@Override
		public JsonElement serialize(RawFilterCriteria rawCriteria, Type typeOfSrc, JsonSerializationContext context) {
			Gson gson = getGson();
			switch (rawCriteria.type) {
				case TERMINAL:
					return gson.toJsonTree(rawCriteria, RawFilterTerminal.class);
				case AND:
					return gson.toJsonTree(rawCriteria, RawFilterAnd.class);
				case OR:
					return gson.toJsonTree(rawCriteria, RawFilterOr.class);
				default:
					return null;
			}
		}

	}






	static class RawFilterCriteriaDeserializer implements JsonDeserializer<RawFilterCriteria> {


		@Override
		public RawFilterCriteria deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();
			if (!jsonObject.has("type")) {
				Logger.get().warn("Could not deserialize RawFilterCriteria: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			FilterCriteria.CriteriaType type = FilterCriteria.CriteriaType.valueOf(strType);

			Gson gson = getGson();

			switch (type) {
				case TERMINAL:
					return gson.fromJson(json, RawFilterTerminal.class);
				case AND:
					return gson.fromJson(json, RawFilterAnd.class);
				case OR:
					return gson.fromJson(json, RawFilterOr.class);
				default:
					return null;
			}

		}

	}






	static class RawTaskValueSerializer implements JsonSerializer<RawTaskValue> {


		@Override
		public JsonElement serialize(RawTaskValue rawValue, Type typeOfSrc, JsonSerializationContext context) {

			Gson gson = getGson();

			switch (rawValue.type) {
				case DESCRIPTION:
					return gson.toJsonTree(rawValue, RawDescriptionValue.class);
				case CREATED:
					return gson.toJsonTree(rawValue, RawCreatedValue.class);
				case LAST_UPDATED:
					return gson.toJsonTree(rawValue, RawLastUpdatedValue.class);
				case FLAG:
					return gson.toJsonTree(rawValue, RawFlagValue.class);
				case TEXT:
					return gson.toJsonTree(rawValue, RawTextValue.class);
				case NUMBER:
					return gson.toJsonTree(rawValue, RawNumberValue.class);
				case BOOLEAN:
					return gson.toJsonTree(rawValue, RawBoolValue.class);
				case CHOICE:
					return gson.toJsonTree(rawValue, RawChoiceValue.class);
				case DATE:
					return gson.toJsonTree(rawValue, RawDateValue.class);
				case DEPENDENCY:
					return gson.toJsonTree(rawValue, RawDependencyValue.class);
				default:
					return null;
			}

		}

	}






	static class RawTaskValueDeserializer implements JsonDeserializer<RawTaskValue> {


		@Override
		public RawTaskValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			JsonObject jsonObject = json.getAsJsonObject();
			if (!jsonObject.has("type")) {
				Logger.get().warn("Could not deserialize RawTaskValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeType type = AttributeType.valueOf(strType);

			Gson gson = getGson();

			switch (type) {
				case ID:
					return null;
				case DESCRIPTION:
					return gson.fromJson(json, RawDescriptionValue.class);
				case CREATED:
					return gson.fromJson(json, RawCreatedValue.class);
				case LAST_UPDATED:
					return gson.fromJson(json, RawLastUpdatedValue.class);
				case FLAG:
					return gson.fromJson(json, RawFlagValue.class);
				case TEXT:
					return gson.fromJson(json, RawTextValue.class);
				case NUMBER:
					return gson.fromJson(json, RawNumberValue.class);
				case BOOLEAN:
					return gson.fromJson(json, RawBoolValue.class);
				case CHOICE:
					return gson.fromJson(json, RawChoiceValue.class);
				case DATE:
					return gson.fromJson(json, RawDateValue.class);
				case DEPENDENCY:
					return gson.fromJson(json, RawDependencyValue.class);
				default:
					return null;
			}

		}

	}






	static class RawAttributeValueSerializer implements JsonSerializer<RawAttributeValue> {


		@Override
		public JsonElement serialize(RawAttributeValue rawValue, Type typeOfSrc, JsonSerializationContext context) {

			Gson gson = getGson();

			switch (rawValue.type) {

				case USE_DEFAULT:
					return gson.toJsonTree(rawValue, RawUseDefault.class);
				case DEFAULT_VALUE:
					return gson.toJsonTree(rawValue, RawDefaultValue.class);
				case CARD_DISPLAY_TYPE:
					return gson.toJsonTree(rawValue, RawCardDisplayType.class);
				case NUMBER_DEC_PLACES:
					return gson.toJsonTree(rawValue, RawNumberDecPlaces.class);
				case NUMBER_MIN:
					return gson.toJsonTree(rawValue, RawNumberMin.class);
				case NUMBER_MAX:
					return gson.toJsonTree(rawValue, RawNumberMax.class);
				case CHOICE_VALUES:
					return gson.toJsonTree(rawValue, RawChoiceValues.class);
				case FLAG_LIST:
					return gson.toJsonTree(rawValue, RawFlagList.class);
				case TEXT_MULTILINE:
					return gson.toJsonTree(rawValue, RawTextMultiline.class);
				default:
					return null;
			}

		}

	}






	static class RawAttributeValueDeserializer implements JsonDeserializer<RawAttributeValue> {


		@Override
		public RawAttributeValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


			JsonObject jsonObject = json.getAsJsonObject();
			if (!jsonObject.has("type")) {
				Logger.get().warn("Could not deserialize RawAttributeValue: Invalid input: " + jsonObject.toString());
				return null;
			}

			String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
			AttributeValueType type = AttributeValueType.valueOf(strType);

			Gson gson = getGson();

			switch (type) {
				case USE_DEFAULT:
					return gson.fromJson(json, RawUseDefault.class);
				case DEFAULT_VALUE:
					return gson.fromJson(json, RawDefaultValue.class);
				case CARD_DISPLAY_TYPE:
					return gson.fromJson(json, RawCardDisplayType.class);
				case NUMBER_DEC_PLACES:
					return gson.fromJson(json, RawNumberDecPlaces.class);
				case NUMBER_MIN:
					return gson.fromJson(json, RawNumberMin.class);
				case NUMBER_MAX:
					return gson.fromJson(json, RawNumberMax.class);
				case CHOICE_VALUES:
					return gson.fromJson(json, RawChoiceValues.class);
				case FLAG_LIST:
					return gson.fromJson(json, RawFlagList.class);
				case TEXT_MULTILINE:
					return gson.fromJson(json, RawTextMultiline.class);
				default:
					return null;
			}

		}

	}


}
