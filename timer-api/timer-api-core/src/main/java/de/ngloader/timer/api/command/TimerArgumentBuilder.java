package de.ngloader.timer.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

public class TimerArgumentBuilder {

	public static LiteralArgumentBuilder<TimerCommandInfo> literal(String literal) {
		return LiteralArgumentBuilder.literal(literal);
	}

	public static <T> RequiredArgumentBuilder<TimerCommandInfo, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	public static BoolArgumentType bool() {
		return BoolArgumentType.bool();
	}

	public static boolean getBool(CommandContext<?> context, String name) {
		return BoolArgumentType.getBool(context, name);
	}

	public static DoubleArgumentType doubleArg() {
		return DoubleArgumentType.doubleArg();
	}

	public static DoubleArgumentType doubleArg(double min) {
		return DoubleArgumentType.doubleArg(min);
	}

	public static DoubleArgumentType doubleArg(double min, double max) {
		return DoubleArgumentType.doubleArg(min, max);
	}

	public static double getDouble(CommandContext<?> context, String name) {
		return DoubleArgumentType.getDouble(context, name);
	}

	public static FloatArgumentType floatArg() {
		return FloatArgumentType.floatArg();
	}

	public static FloatArgumentType floatArg(float min) {
		return FloatArgumentType.floatArg(min);
	}

	public static FloatArgumentType floatArg(float min, float max) {
		return FloatArgumentType.floatArg(min, max);
	}

	public static float getFloat(CommandContext<?> context, String name) {
		return FloatArgumentType.getFloat(context, name);
	}

	public static IntegerArgumentType integer() {
		return IntegerArgumentType.integer();
	}

	public static IntegerArgumentType integer(int min) {
		return IntegerArgumentType.integer(min);
	}

	public static IntegerArgumentType integer(int min, int max) {
		return IntegerArgumentType.integer(min, max);
	}

	public static int getInteger(CommandContext<?> context, String name) {
		return IntegerArgumentType.getInteger(context, name);
	}

	public static LongArgumentType longArg() {
		return LongArgumentType.longArg();
	}

	public static LongArgumentType longArg(long min) {
		return LongArgumentType.longArg(min);
	}

	public static LongArgumentType longArg(long min, long max) {
		return LongArgumentType.longArg(min, max);
	}

	public static long getLong(CommandContext<?> context, String name) {
		return LongArgumentType.getLong(context, name);
	}

	public static StringArgumentType string() {
		return StringArgumentType.string();
	}

	public static StringArgumentType greedyString() {
		return StringArgumentType.greedyString();
	}

	public static StringArgumentType word() {
		return StringArgumentType.word();
	}

	public static String getString(CommandContext<?> context, String name) {
		return StringArgumentType.getString(context, name);
	}
}