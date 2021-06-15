package de.ngloader.timer.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import de.ngloader.timer.api.command.TimerCommandInfo;

public class BukkitTimerPlugin extends JavaPlugin implements CommandExecutor {

	private BukkitTimerBridge bridge;
	private BrigadierAdapter brigadier;

	private BukkitTask task;

	@Override
	public void onLoad() {
		this.bridge = new BukkitTimerBridge();
		this.brigadier = new BrigadierAdapter();
	}

	@Override
	public void onEnable() {
		this.task = Bukkit.getScheduler().runTaskTimer(this, this.bridge.getDefaultManager(), 0, 0);

		CommandDispatcher<TimerCommandInfo> dispatcher = this.bridge.getCommandManager().getCommandDispatcher();
		LiteralArgumentBuilder<TimerCommandInfo> argumentBuilder = LiteralArgumentBuilder.literal("timer");

		for (CommandNode<TimerCommandInfo> node : dispatcher.getRoot().getChildren()) {
			argumentBuilder.then((CommandNode<TimerCommandInfo>) node);
		}

		this.brigadier.register(this.getCommand("timer"), argumentBuilder.build());
	}

	@Override
	public void onDisable() {
		if (this.task != null) {
			this.task.cancel();
		}

		if (this.bridge != null) {
			this.bridge.disable();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return this.bridge.getCommandManager().executeCommand(args, sender::hasPermission, sender::sendMessage);
	}
}