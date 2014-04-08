package ru.koleslena.eclipse.compositelauncher.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author koleslena
 *
 */
public class CompositeUtil {

	public static final String COMPOSITE_SET_CONFIGURATION = "COMPOSITE_SET_CONFIGURATION";
	public static final String COMPOSITE_CONFIGURATION_LEVEL = "COMPOSITE_CONFIGURATION_LEVEL";
	
	
	public static Map<String, ILaunchConfiguration> getCofigurations() {
		Map<String, ILaunchConfiguration> ret = new HashMap<String, ILaunchConfiguration>();
		try {		
			ILaunchConfiguration[] confs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
			for(ILaunchConfiguration lc: confs) {
				ret.put(lc.getName(), lc);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
