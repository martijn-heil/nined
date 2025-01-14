/*
 * MIT License
 *
 * Copyright (c) 2023 Martijn Heil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.martijn_heil.nined;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class NinedCommand implements CommandExecutor {
	private final Plugin plugin;
	private final String executablePath;
	private final Map<UUID, EditorSession> sessions;

	public NinedCommand(Plugin plugin, String executablePath, Map<UUID, EditorSession> sessions) {
		this.plugin = plugin;
		this.executablePath = executablePath;
		this.sessions = sessions;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(commandSender instanceof Player sender)) return true;

		EditorSession existingSession = sessions.get(sender.getUniqueId());
		if (existingSession != null) {
			existingSession.stop();
			sender.sendMessage("[nined] " + ChatColor.GRAY + "Destroyed previous session");
		}

		String[] cmd = new String[args.length + 1];
		cmd[0] = executablePath;
		System.arraycopy(args, 0, cmd, 1, args.length);
		EditorSession session = new EditorSession(plugin, sender, cmd);
		session.start();
		sessions.put(sender.getUniqueId(), session);

		sender.sendMessage("[nined] " + ChatColor.GRAY + "Started new session");
		return true;
	}
}
