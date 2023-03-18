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
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EditorSession {
	private final Plugin plugin;
	private final Server server;
	private int taskId = 0;
	private final String[] command;
	public Process process;
	public Player editor;

	public EditorSession(Plugin plugin, Player editor, String[] command) {
		this.plugin = plugin;
		this.server = plugin.getServer();
		this.command = command;
		this.editor = editor;
	}

	public void start() {
		try {
			process = new ProcessBuilder(command).start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		process.onExit().thenRun(() -> {
			editor.sendMessage("[nined] " + ChatColor.GRAY + "Process terminated with exit code (" + process.exitValue() + ")");
		});

		taskId = server.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
			PlayerChatWriter writer = new PlayerChatWriter(plugin, editor);
			try {
				BufferedReader reader = process.inputReader(StandardCharsets.UTF_8);
				String line;
				while (true) {
					line = reader.readLine();
					if (line == null) break;
					writer.write(line);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public void stop() {
		server.getScheduler().cancelTask(taskId);
		process.destroy();
	}
}
