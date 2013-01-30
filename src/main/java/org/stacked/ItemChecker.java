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
	import java.lang.Runnable;
//* IMPORTS: BUKKIT
	import org.bukkit.Bukkit;
	import org.bukkit.entity.Arrow;
	import org.bukkit.entity.Entity;
	import org.bukkit.entity.Item;
	import org.bukkit.entity.Player;
	import org.bukkit.event.player.PlayerPickupItemEvent;
	import org.bukkit.inventory.ItemStack;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.plugin.Plugin;
	import org.bukkit.scheduler.BukkitScheduler;
	import org.bukkit.util.Vector;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class ItemChecker implements Runnable {
	private int taskId = -1;
	private Plugin plugin;

	public ItemChecker(Plugin plugin) {
		if ((plugin == null))
			return;

		this.plugin = plugin;
		start();
	}

	private boolean checkEntity(Entity e) {
		if(e.isDead() || !e.isValid())
			return false;
		else if (e instanceof Item) {
			if (((Item) e).getPickupDelay() <= 0)
				return true;
		}
		else if (e instanceof Arrow) {
			Vector v = e.getVelocity();
			Vector z = v.clone().zero();

			if (v.equals(z))
				return true;
		}

		return false;
	}

	public void start() {
		if (taskId >= 0)
			return;

		if((Stacked.config == null) || !Stacked.config.automatic)
			return;

		BukkitScheduler s = plugin.getServer().getScheduler();
		taskId = s.scheduleSyncRepeatingTask(plugin, this, 5, 5);
	}

	public void stop() {
		if (taskId < 0)
			return;

		plugin.getServer().getScheduler().cancelTask(taskId);
		taskId = -1;
	}

	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(!p.hasPermission("stacked.stack.automatic"))
				continue;

			for (Entity e : p.getNearbyEntities(1.3D, 1.4D, 1.3D)) {
				if (!checkEntity(e))
					continue;

				Item i;
				if (e instanceof Arrow) {
					Material mat = Material.ARROW;
					Location loc = e.getLocation();
					ItemStack is = new ItemStack(mat, 1);
					e.remove();
					i = e.getWorld().dropItem(loc, is);
					i.setPickupDelay(0);
				}
				else
					i = (Item) e;

				PlayerPickupItemEvent ev;
				ev = new PlayerPickupItemEvent(p, i, 0);
				Bukkit.getPluginManager().callEvent(ev);
			}
		}
	}
}