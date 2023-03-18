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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class NinedListener implements Listener {
	private final Map<UUID, EditorSession> sessions;

	public NinedListener(Map<UUID, EditorSession> sessions) {
		this.sessions = sessions;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChat(PlayerChatEvent e) {
		Player sender = e.getPlayer();
		EditorSession session = sessions.get(sender.getUniqueId());
		if (session == null) return;

		if (!session.process.isAlive()) {
			sessions.remove(sender.getUniqueId());
			session.stop();
			return;
		}

		e.setCancelled(true);

		try {
			BufferedWriter writer = session.process.outputWriter(StandardCharsets.UTF_8);
			writer.write((e.getMessage() + "\n"));
			writer.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
