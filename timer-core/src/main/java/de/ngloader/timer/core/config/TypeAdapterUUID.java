package de.ngloader.timer.core.config;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import de.ngloader.timer.api.config.TypeAdapter;

public class TypeAdapterUUID implements TypeAdapter<UUID> {

	@Override
	public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return UUID.fromString(json.getAsString());
	}

	@Override
	public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	@Override
	public Class<UUID> getType() {
		return UUID.class;
	}
}