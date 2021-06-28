package de.ngloader.timer.bukkit;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTimerPlugin extends JavaPlugin implements CommandExecutor {

	private BukkitTimerBridge bridge;

	private BukkitTask task;

	@Override
	public void onLoad() {
		this.bridge = new BukkitTimerBridge();
	}

	@Override
	public void onEnable() {
		new Metrics(this, 11707);

		this.task = Bukkit.getScheduler().runTaskTimer(this, this.bridge.getDefaultManager(), 0, 0);
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