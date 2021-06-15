package de.ngloader.timer.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class TimerRequiredArgumentBuilder {

    public static <T> RequiredArgumentBuilder<TimerCommandInfo, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}