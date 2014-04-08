package ru.koleslena.eclipse.compositelauncher.ui.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationTreeContentProvider;
import org.eclipse.swt.widgets.Shell;

/**
 * @author koleslena
 *
 */
public class CompositeLaunchConfigurationTreeContentProvider extends
		LaunchConfigurationTreeContentProvider {

	private String mode;
	public CompositeLaunchConfigurationTreeContentProvider(String mode, Shell shell) {
		super(mode, shell);
		this.mode = mode;
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ILaunchConfigurationType || parentElement instanceof ILaunchConfiguration) {
			return super.getChildren(parentElement);
		} else {
			return getElements(parentElement);
		}
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		List<ILaunchConfigurationType> list = new ArrayList<ILaunchConfigurationType>();
		for(Object ltobj: super.getElements(inputElement)) {
			ILaunchConfigurationType lt = (ILaunchConfigurationType)ltobj;
			if(lt.isPublic() && lt.supportsMode(mode) && hasChildren(lt)
				&& !lt.getIdentifier().equals("ru.koleslena.eclipse.compositelauncher.core.launchConfigurationType")) {
				list.add(lt);
    	  	}
		}
		return list.toArray();
	}

}
