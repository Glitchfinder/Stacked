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
	import java.io.File;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.logging.Logger;
	import java.util.Map;
	import java.util.Set;
//* IMPORTS: BUKKIT
	import org.bukkit.configuration.ConfigurationSection;
	import org.bukkit.configuration.file.YamlConfiguration;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Configuration extends YamlConfiguration {
	public	boolean	automatic, damaged, illegitimate, potions;
	public	boolean	dataPermissions, itemPermissions;
	public	boolean	sizeDataPermissions, sizeItemPermissions;
	public	boolean	sizePermissions;
	public	int	maxSize;

	private	File		config;
	public	List<Integer>	maxSizes = new ArrayList<Integer>();
	private	Logger		log;

	public Configuration(File config, Stacked plugin) {
		this.config	= config;
		this.log	= plugin.getLogger();

		automatic	= true;
		damaged		= true;
		illegitimate	= true;
		potions		= true;
		maxSize		= 64;

		maxSizes.add(64);
		load();
	}

	public void load() {
		try {
			super.load(config);
		}
		catch (Exception e) {
			String msg = "Unable to load configuration, using ";
			msg += "defaults instead.";
			log.warning(msg);
		}

		if (!isConfigurationSection("Stacking"))
			return;

		ConfigurationSection s = getConfigurationSection("Stacking");

		automatic	= s.getBoolean("Automatic",		true);
		damaged		= s.getBoolean("Damaged",		true);
		illegitimate	= s.getBoolean("Illegitimate",		true);
		potions		= s.getBoolean("Potions",		true);
		dataPermissions	= s.getBoolean("Data_Permissions",	false);
		itemPermissions	= s.getBoolean("Item_Permissions",	false);

		if (!isConfigurationSection("Sizes"))
			return;

		s = getConfigurationSection("Sizes");
		maxSize = s.getInt("Maximum_Size", 64);

		if (!s.isConfigurationSection("Permissions"))
			return;

		s = s.getConfigurationSection("Permissions");
		sizePermissions		= s.getBoolean("Enabled",	false);
		sizeDataPermissions	= s.getBoolean("Data_Based",	false);
		sizeItemPermissions	= s.getBoolean("Item_Based",	false);

		if (!s.isList("Maximum_Sizes"))
			return;

		List<Integer> temp = s.getIntegerList("Maximum_Sizes");

		if (temp != null)
			maxSizes = temp;

		if (maxSizes.isEmpty())
			maxSizes.add(64);
	}
}
