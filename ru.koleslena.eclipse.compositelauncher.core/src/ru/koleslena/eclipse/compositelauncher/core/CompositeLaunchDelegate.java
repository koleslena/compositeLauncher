package ru.koleslena.eclipse.compositelauncher.core;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import ru.koleslena.eclipse.compositelauncher.util.CompositeUtil;

/**
 * @author koleslena
 *
 */
public class CompositeLaunchDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		try {
			Set<String> set = configuration.getWorkingCopy().getAttribute(CompositeUtil.COMPOSITE_SET_CONFIGURATION, Collections.emptySet());
			if (set != null && !set.isEmpty()) {
				Map<String, ILaunchConfiguration> map = CompositeUtil.getCofigurations();
				for(String name: set) {
					ILaunchConfiguration conf = map.get(name);
					conf.launch(mode, monitor);
				}
		 	}
 		} catch (CoreException e) {
 			CompositeLauncherPlugin.getDefault().getLog().log(e.getStatus());
 		}
	}
}
