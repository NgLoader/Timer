package de.ngloader.timer.core.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import de.ngloader.timer.api.config.Exclude;

public class ExcludeStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes field) {
		return field.getAnnotation(Exclude.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}
}