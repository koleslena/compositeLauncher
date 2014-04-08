package ru.koleslena.eclipse.compositelauncher.ui.control;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.debug.core.ILaunchConfiguration;

import ru.koleslena.eclipse.compositelauncher.core.model.CompositeConfiguration;

/**
 * @author koleslena
 *
 */
public class CompositeModel {

	private TreeSet<CompositeConfiguration> set;
	
	public CompositeModel(Comparator<CompositeConfiguration> comparator) {
		set = new TreeSet<CompositeConfiguration>(comparator);
	}
	
	public void clear() {
		set.clear();
	}
	
	public void setSet(Set<String> set, Map<String, ILaunchConfiguration> map) {
		for(String name: set) {
			CompositeConfiguration c = new CompositeConfiguration(map.get(name));
			this.addElement(c);
		}
	}
	
	public int getSize() {
		return set.size();
	}
	
	public boolean isEmpty() {
		return set.isEmpty();
	}
	
	public Object[] getArray() {
		return this.set.toArray();
	}
	
	public Set<String> getStringSet() {
		Set<String> ret = new LinkedHashSet<String>();
		for(CompositeConfiguration c: set) {
			ret.add(c.getName());
		}
		return ret;
	}
	
	public void addElement(CompositeConfiguration arg0) {
		arg0.setLevel(set.size());
		set.add(arg0);
	}
	
	public void removeElement(ILaunchConfiguration conf) {
		int ret = set.size();
		for(CompositeConfiguration c: set) {
			if(c.getConfiguration().getName().equals(conf.getName())) {
				ret = c.getLevel();
				break;
			}
		}
		for(CompositeConfiguration el: set) {
			if(el.getLevel() > ret)
				el.setLevel(el.getLevel() - 1);
		}
	}
	
	public void moveUp(CompositeConfiguration el) {
		int level = el.getLevel(); 
		if(level > 1) {
			
			int ret = el.getLevel() - 1;
			for(CompositeConfiguration c: set) {
				if(c.getLevel() == ret)
					c.setLevel(level);
			}
			
			el.setLevel(level - 1);
		}
	}
	
	public void moveDown(CompositeConfiguration el) {
		int level = el.getLevel(); 
		if(level < set.size()) {
			int ret = el.getLevel() + 1;
			for(CompositeConfiguration c: set) {
				if(c.getLevel() == ret)
					c.setLevel(level);
			}
			el.setLevel(level + 1);
		}
	}
	
	public int getNextlevel() {
		return set.size();
	}
	
}
