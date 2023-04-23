package de.ngloader.timer.api;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimerLogger {

	private static Logger logger = Logger.getLogger("de.ngloader.timer");

	private static final String LOG_PREFIX = "[Timer] ";
	private static final String LOG_DEBUG_PREFIX = "[Timer/Debug] ";

	private static boolean verbose = false;

	public static void setVerbose(boolean verbose) {
		TimerLogger.verbose = verbose;
	}

	public static void debug(String message) {
		if (TimerLogger.verbose) {
			TimerLogger.logger.log(Level.FINE, LOG_DEBUG_PREFIX + message);
		}
	}

	public static void info(String message) {
		TimerLogger.logger.log(Level.INFO, LOG_PREFIX + message);
	}

	public static void warn(String message) {
		TimerLogger.logger.log(Level.WARNING, LOG_PREFIX + message);
	}

	public static void error(String message, Throwable throwable) {
		TimerLogger.logger.log(Level.SEVERE, LOG_PREFIX + message, throwable);
	}
}