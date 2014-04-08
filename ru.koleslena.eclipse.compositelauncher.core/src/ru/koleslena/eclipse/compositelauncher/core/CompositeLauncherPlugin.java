package ru.koleslena.eclipse.compositelauncher.core;

import org.eclipse.core.runtime.Plugin;

/**
 * @author koleslena
 *
 */
public class CompositeLauncherPlugin extends Plugin {

	private static CompositeLauncherPlugin plugin;
	
	public CompositeLauncherPlugin() {
		super();
		plugin = this;
	}
	
	public static CompositeLauncherPlugin getDefault() {
		return plugin;
	}
}
