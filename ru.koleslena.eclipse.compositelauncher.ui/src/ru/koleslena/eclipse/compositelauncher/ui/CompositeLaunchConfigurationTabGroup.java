package ru.koleslena.eclipse.compositelauncher.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * @author koleslena
 *
 */
public class CompositeLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	public CompositeLaunchConfigurationTabGroup() {
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[]{new CompositeLaunchConfigurationTab(dialog)});
	}

}
