package de.ngloader.timer.api.timer;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;

public interface TimerType <T extends TimerType<?>> extends Runnable {

	public static <T extends TimerType<?>> T newInstance(Timer timer, Class<T> clazz) throws Exception {
		if (clazz.isInterface()) {
			throw new InvalidClassException("Unable to create a interface class");
		}

		try {
			return clazz.getConstructor(Timer.class).newInstance(timer);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw e;
		}
	}

	public void enable();
	public void disable();

	public Timer getTimer();
}