package ru.koleslena.eclipse.compositelauncher.core.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author koleslena
 *
 */
public class CompositeConfiguration implements Comparable<CompositeConfiguration> {

	private ILaunchConfiguration configuration;
	private int level;
	
	public CompositeConfiguration() {
	}
	
	public CompositeConfiguration(ILaunchConfiguration launchConfiguration) {
		this.configuration = launchConfiguration;
	}
	
	public CompositeConfiguration(ILaunchConfiguration launchConfiguration, int level) {
		this.configuration = launchConfiguration;
		this.level = level;
	}
	
	public ILaunchConfiguration getConfiguration() {
		return configuration;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getName() {
		return this.configuration.getName();
	}
	
	public int getStartLevel() {
		return this.level;
	}

	@Override
	public int compareTo(CompositeConfiguration o) {
		if(this.getName().equals(o.getName()))
			return this.level - o.getLevel();
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof CompositeConfiguration) {
			return ((CompositeConfiguration)obj).getConfiguration().getName().equals(this.getConfiguration().getName());
		}
		return super.equals(obj);
	}
	
}
