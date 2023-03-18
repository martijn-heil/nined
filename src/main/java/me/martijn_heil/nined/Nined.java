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

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Nined extends JavaPlugin {
	public static Nined instance;
	public Map<UUID, EditorSession> sessions = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		String executablePath = this.getDataFolder() + "/bin/ed";
		this.getDataFolder().mkdirs();

		this.getCommand("ed").setExecutor(new NinedCommand(this, executablePath, sessions));
		this.getServer().getPluginManager().registerEvents(new NinedListener(sessions), this);
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Shutting down processes..");
		for (EditorSession session : sessions.values()) {
			session.stop();
		}
		sessions.clear();
	}
}
