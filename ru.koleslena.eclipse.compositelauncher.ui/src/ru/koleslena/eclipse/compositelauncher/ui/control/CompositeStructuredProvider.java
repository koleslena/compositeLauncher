package ru.koleslena.eclipse.compositelauncher.ui.control;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author koleslena
 *
 */
public class CompositeStructuredProvider implements IStructuredContentProvider {
	private CompositeModel model;

	public CompositeStructuredProvider(CompositeModel model) {
		this.model = model;
	}
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getArray();
	}

}
