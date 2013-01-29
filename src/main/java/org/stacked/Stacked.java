/*
 * Copyright (c) 2012 Sean Porter <glitchkey@gmail.com>
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
	import org.bukkit.command.Command;
	import org.bukkit.command.CommandExecutor;
	import org.bukkit.command.CommandSender;
	import org.bukkit.entity.Player;
	import org.bukkit.plugin.java.JavaPlugin;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Stacked extends JavaPlugin implements CommandExecutor {

	public void onDisable() {
		getLogger().info("Successfully disabled.");
	}

	public void onEnable() {
		getCommand("stack").setExecutor(this);
		getLogger().info("Successfully enabled.");
	}

	@Override
	public boolean onCommand(
		CommandSender sender,
		Command command,
		String label,
		String[] args) {
		if(sender == null || command == null)
			return false;
		else if(label == null || args == null)
			return false;

		if(!(sender instanceof Player)) {
			String msg = "You have to be a player to do that!";
			Message.severe(this, sender, msg);
			return true;
		}

		Player player = (Player) sender;
		StackInventory stacker = new StackInventory(this, player, true);
		return stacker.stack();
	}
}
