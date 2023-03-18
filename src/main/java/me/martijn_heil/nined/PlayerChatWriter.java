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

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class PlayerChatWriter extends Writer {
	private final Plugin plugin;
	private final Server server;
	private final Player target;

	public PlayerChatWriter(Plugin plugin, Player target)
	{
		this.plugin = plugin;
		this.server = plugin.getServer();
		this.target = target;
	}

	@Override
	public void write(@NotNull char[] chars, int off, int len) throws IOException {
		char[] buf = new char[len];
		System.arraycopy(chars, off, buf, 0, len);
		String message = String.valueOf(buf);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			target.sendMessage(message);
		}, 0);
	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void close() throws IOException {

	}
}
