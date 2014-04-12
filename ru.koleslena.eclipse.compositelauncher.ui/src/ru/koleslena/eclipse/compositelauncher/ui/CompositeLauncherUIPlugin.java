package ru.koleslena.eclipse.compositelauncher.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author koleslena
 *
 */
public class CompositeLauncherUIPlugin extends AbstractUIPlugin {

	public static final String COMPOSITE_LAUNCH_TYPE = "ru.koleslena.eclipse.compositelauncher.core.launchConfigurationType";
	public static final String COMPOSITE_UI = "ru.koleslena.eclipse.compositelauncher.ui";
	
	private static CompositeLauncherUIPlugin plugin;
	
	public CompositeLauncherUIPlugin() {
		super();
		plugin = this;
	}
	
	public static CompositeLauncherUIPlugin getDefault() {
		return plugin;
	}
}
