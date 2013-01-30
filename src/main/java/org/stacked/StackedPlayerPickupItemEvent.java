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
	import org.bukkit.entity.Item;
	import org.bukkit.entity.Player;
	import org.bukkit.event.player.PlayerPickupItemEvent;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class StackedPlayerPickupItemEvent extends PlayerPickupItemEvent {
	public StackedPlayerPickupItemEvent(
		final Player player,
		final Item item,
		final int remaining)
	{
		super(player, item, remaining);
	}
}