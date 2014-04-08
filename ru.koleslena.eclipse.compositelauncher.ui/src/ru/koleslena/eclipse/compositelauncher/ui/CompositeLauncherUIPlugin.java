package ru.koleslena.eclipse.compositelauncher.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author koleslena
 *
 */
public class CompositeLauncherUIPlugin extends AbstractUIPlugin {

	private static CompositeLauncherUIPlugin plugin;
	
	public CompositeLauncherUIPlugin() {
		super();
		plugin = this;
	}
	
	public static CompositeLauncherUIPlugin getDefault() {
		return plugin;
	}
}
