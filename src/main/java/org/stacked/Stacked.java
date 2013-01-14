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
	import java.lang.Integer;
	import java.util.Map;
	import java.util.List;
//* IMPORTS: BUKKIT
	import org.bukkit.ChatColor;
	import org.bukkit.command.Command;
	import org.bukkit.command.CommandExecutor;
	import org.bukkit.command.CommandSender;
	import org.bukkit.enchantments.Enchantment;
	import org.bukkit.entity.Player;
	import org.bukkit.inventory.ItemStack;
	import org.bukkit.inventory.meta.EnchantmentStorageMeta;
	import org.bukkit.Material;
	import org.bukkit.plugin.java.JavaPlugin;
	import org.bukkit.World;
//* IMPORTS: OTHER
	import com.sk89q.worldedit.blocks.ItemType;

public class Stacked extends JavaPlugin implements CommandExecutor
{
	public void onDisable() {}

	public void onEnable() {
		getCommand("stack").setExecutor(this);
	}

	public boolean onCommand(
		CommandSender sender,
		Command command,
		String label,
		String[] args)
	{
		if(sender == null || command == null)
			return false;

		if(label == null || args == null)
			return false;

		if(!(sender instanceof Player)) {
			String msg = "Stacked";
			msg = String.format("%s[%s]", ChatColor.DARK_AQUA, msg);
			msg = String.format("%s%s ", msg, ChatColor.RED);
			msg += "You have to be a player to do that!";
			sender.sendMessage(msg);
			return true;
		}

		if(!sender.hasPermission("stacked.stack")) {
			String msg = "Stacked";
			msg = String.format("%s[%s]", ChatColor.DARK_AQUA, msg);
			msg = String.format("%s%s ", msg, ChatColor.RED);
			msg += "You don't have permission to do that!";
			sender.sendMessage(msg);
			return true;
		}

		String perm = "stacked.stack.illegitimate";
		boolean ignoreMax = sender.hasPermission(perm);
		perm = "stacked.stack.damaged";
		boolean ignoreDamaged = sender.hasPermission(perm);
		perm = "stacked.stack.potions";
		boolean allowPotions = sender.hasPermission(perm);

		Player player = (Player) sender;
		ItemStack[] items = player.getInventory().getContents();
		int len = items.length;

		int affected = 0;

		for (int i = 0; i < len; i++) {
			ItemStack item = items[i];

			// Avoid infinite stacks and stacks with durability
			if (item == null)
				continue;
			else if(item.getAmount() <= 0)
				continue;
			else if(!ignoreMax && item.getMaxStackSize() == 1)
				continue;

			int max = ignoreMax ? 64 : item.getMaxStackSize();

			if (item.getAmount() >= max)
				continue;

			// Avoid stacking potions
			if(item.getType() == Material.POTION && !allowPotions)
				continue;

			if(proc(items, item, i, len, max, ignoreMax, ignoreDamaged))
				affected++;
		}

		if (affected > 0) {
			player.getInventory().setContents(items);
		}

		String message = "" + ChatColor.YELLOW;
		message += "Items compacted into stacks!";
		player.sendMessage(message);

		return true;
	}

	private boolean proc(
		ItemStack[] items,
		ItemStack item,
		int i,
		int len,
		int max,
		boolean ignoreMax,
		boolean ignoreDamaged)
	{
		// Number of needed items until max
		int needed = max - item.getAmount(); 

		boolean affected = false;

		// Find another stack of the same type
		for (int j = i + 1; j < len; j++) {
			ItemStack item2 = items[j];

			// Avoid infinite stacks and stacks with durability
			if (item2 == null)
				continue;
			else if(item2.getAmount() <= 0)
				continue;
			else if(!ignoreMax && item.getMaxStackSize() == 1)
				continue;

			// Avoid stacking different Enchanted Books together
			if(item.getType() == Material.ENCHANTED_BOOK) {
				if(!compareBooks(item, item2))
					continue;
			}

			// Same type?
			// Blocks store their color in the damage value
			if (item2.getTypeId() != item.getTypeId())
				continue;

			if(ItemType.usesDamageValue(item.getTypeId()) || !ignoreDamaged)
				if(item.getDurability() != item2.getDurability())
					continue;

			if (!item.getEnchantments().equals(item2.getEnchantments()))
				continue;

			// This stack won't fit in the parent stack
			if (item2.getAmount() > needed) {
				item.setAmount(max);
				item2.setAmount(item2.getAmount() - needed);
				continue;
			// This stack will
			} else {
				items[j] = null;
				item.setAmount(item.getAmount() + item2.getAmount());
				needed = max - item.getAmount();
			}

			affected = true;
		}
		return affected;
	}

	private boolean compareBooks(ItemStack book1, ItemStack book2) {
		if(book1.getType() != Material.ENCHANTED_BOOK)
			return true;
		if(book2.getType() != Material.ENCHANTED_BOOK)
			return true;

		EnchantmentStorageMeta meta1, meta2;

		meta1 = (EnchantmentStorageMeta) book1.getItemMeta();
		meta2 = (EnchantmentStorageMeta) book2.getItemMeta();

		Map<Enchantment, Integer> map1 = meta1.getStoredEnchants();
		Map<Enchantment, Integer> map2 = meta2.getStoredEnchants();

		if(map1.equals(map2))
			return true;

		return false;
	}
}
