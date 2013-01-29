/*
 * Copyright (c) 2012-2013 Sean Porter <glitchkey@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stacked;

//* IMPORTS: JDK/JRE
	//* NOT NEEDED
//* IMPORTS: BUKKIT
	import org.bukkit.ChatColor;
	import org.bukkit.command.CommandSender;
	import org.bukkit.entity.Player;
	import org.bukkit.plugin.Plugin;
//* IMPORTS: OTHER
	import com.sk89q.worldedit.blocks.ItemType;

public class Message {
	public static boolean info(Plugin p, CommandSender s, String msg) {
		String pre = prefix(p, ChatColor.WHITE);
		return send(s, pre, msg);
	}

	public static String prefix(Plugin p, ChatColor c) {
		if (p == null && c == null)
			return "";
		else if (p == null)
			return c.toString();
		else if (c == null)
			c = ChatColor.WHITE;

		String name = p.getName();
		return String.format("%s[%s]%s ", ChatColor.DARK_AQUA, name, c);
	}

	private static boolean send(CommandSender s, String pre, String msg) {
		if (s == null || msg == null)
			return false;

		if (!(s instanceof Player)) {
			pre = ChatColor.stripColor(pre);
			msg = ChatColor.stripColor(msg);
		}

		String messages[];

		try {
			messages = msg.split("\\[nN]");
		}
		catch (Exception e) {
			return false;
		}

		if (messages == null || messages.length < 1)
			return false;

		s.sendMessage(pre + messages[0]);

		if (messages.length < 2)
			return true;

		for (int i = 1; i < messages.length; i++) {
			s.sendMessage(messages[i]);
		}

		return true;
	}

	public static boolean severe(Plugin p, CommandSender s, String msg) {
		String pre = prefix(p, ChatColor.RED);
		return send(s, pre, msg);
	}

	public static boolean warning(Plugin p, CommandSender s, String msg) {
		String pre = prefix(p, ChatColor.YELLOW);
		return send(s, pre, msg);
	}
}
