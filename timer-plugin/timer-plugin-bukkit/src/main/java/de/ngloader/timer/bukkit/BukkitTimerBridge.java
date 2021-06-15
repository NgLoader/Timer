package de.ngloader.timer.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.ngloader.timer.api.i18n.TimerModule;
import de.ngloader.timer.core.ImplTimerPlugin;

public class BukkitTimerBridge extends ImplTimerPlugin {

	@Override
	public void sendChatMessage(String message, String permission) {
		if (permission == null || permission.isEmpty()) {
			Bukkit.broadcastMessage(message);
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission(permission)) {
					player.sendMessage(message);
				}
			}
		}
	}

	@Override
	public void sendChatCommand(String message, String permission) {
		boolean checkPermission = permission != null && permission.isEmpty();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (checkPermission && player.hasPermission(permission)) {
				player.performCommand(message);
			}
		}
	}

	@Override
	public void sendConsoleCommand(String message) {
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
	}

	@Override
	public void log(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	@Override
	public void logError(TimerModule module, String message, Throwable... throwable) {
		this.log(String.format("§8[§c%s§8] %s", module != null ? module.name() : "§4UNKNOWN", message));
		for (Throwable error : throwable) {
			error.printStackTrace();
		}
	}
}