package ru.koleslena.eclipse.compositelauncher.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.model.WorkbenchViewerComparator;

import ru.koleslena.eclipse.compositelauncher.core.model.CompositeConfiguration;
import ru.koleslena.eclipse.compositelauncher.ui.control.CompositeLaunchConfigurationTreeContentProvider;
import ru.koleslena.eclipse.compositelauncher.ui.control.CompositeModel;
import ru.koleslena.eclipse.compositelauncher.ui.control.CompositeStructuredProvider;
import ru.koleslena.eclipse.compositelauncher.ui.control.UiUtils;
import ru.koleslena.eclipse.compositelauncher.util.CompositeUtil;

/**
 * @author koleslena
 *
 */
public class CompositeLaunchConfigurationTab extends
		AbstractLaunchConfigurationTab {
	
	private static final String TAB_NAME = "Composite";
	
	private static final int MARGIN = 5;
	private static final int NEG_MARGIN = -5;

	public LevelComparator levelComparator = new LevelComparator();
	private CompositeModel model;

	// SWT Widgets
	private Composite wPageComposite;
	private Button moveDownButton;
	private Button moveUpButton;
	
	private TableViewer selectedTableViewer;
	private CheckboxTreeViewer viewer;
	
	public CompositeLaunchConfigurationTab(ILaunchConfigurationDialog dialog) {
		setLaunchConfigurationDialog(dialog);
		model = new CompositeModel(levelComparator);
	}
	
	@Override
	public void createControl(Composite parent) {
			
	 	wPageComposite = new Composite(parent, 0);
	 	setControl(wPageComposite);
	 	FormLayout layout = new FormLayout();
	 	layout.marginHeight = MARGIN;
	 	layout.marginWidth = MARGIN;
	 	wPageComposite.setLayout(layout);
		  
	 	Label wBundleAllLabel = new Label(wPageComposite, SWT.LEFT);
	 	wBundleAllLabel.setText("Available bundles:");
	 	
	 	viewer = new ContainerCheckedTreeViewer(wPageComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	    
	 	final String mode = getLaunchConfigurationDialog().getMode();
	 	viewer.setLabelProvider(new DecoratingLabelProvider(DebugUITools.newDebugModelPresentation(), 
				PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		
	 	viewer.setComparator(new WorkbenchViewerComparator());
		viewer.setContentProvider(new CompositeLaunchConfigurationTreeContentProvider(mode, null));
		//viewer.addFilter(new LaunchGroupFilter(DebugUITools.getLaunchGroup(configuration, mode)));
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		    
		viewer.addCheckStateListener(new ICheckStateListener() {
				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					Object element = event.getElement();
					if (element instanceof ILaunchConfiguration) {
						if(event.getChecked()) {
							CompositeConfiguration conf = new CompositeConfiguration((ILaunchConfiguration) element);
							model.addElement(conf);
						} else
							model.removeElement((ILaunchConfiguration)element);
					} else if (element instanceof ILaunchConfigurationType) {
						Object[] children = ((ITreeContentProvider) viewer.getContentProvider()).getChildren(element);
						for(Object el: children) {
							if(event.getChecked()) {
								CompositeConfiguration conf = new CompositeConfiguration((ILaunchConfiguration) el);
								model.addElement(conf);
							} else
								model.removeElement((ILaunchConfiguration)el);
						}
					}
					selectedTableViewer.refresh();
					UiUtils.packTableColumns(selectedTableViewer.getTable());
					setDirty(true);
		        	updateDialog();
				}
			});

		Label wBundleSelectedLabel = new Label(wPageComposite, SWT.LEFT);
		wBundleSelectedLabel.setText("Selected bundles:");
		Table selectedTable = new Table(wPageComposite, SWT.H_SCROLL | SWT.V_SCROLL  |  SWT.BORDER);
		selectedTableViewer = new TableViewer(selectedTable);
		selectedTableViewer.setContentProvider(new CompositeStructuredProvider(model));
		selectedTableViewer.setInput(model.getArray());
		selectedTableViewer.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
					if(e1 instanceof CompositeConfiguration && e2 instanceof CompositeConfiguration) {
						return levelComparator.compare((CompositeConfiguration)e1, (CompositeConfiguration)e2);
					} else {
						return 0;
					}
				}
			});
		selectedTable.setHeaderVisible(true);
		selectedTable.setLinesVisible(true);
		selectedTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) selectedTableViewer.getSelection();
				if (selection != null && !selection.isEmpty()) {
					if(selection.getFirstElement() instanceof CompositeConfiguration) {
						CompositeConfiguration lc = (CompositeConfiguration) selection.getFirstElement();
						updateUpDownButtons(lc);
			  		}
				}	
			}	
		});	
		// Table	 columns
		final TableViewerColumn viewerColumnName = new TableViewerColumn(selectedTableViewer, SWT.NONE);
		viewerColumnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CompositeConfiguration p = (CompositeConfiguration) element;
				return p.getName();
			}
		});
		
		TableColumn colName = viewerColumnName.getColumn();
		colName.setText("Name");
		
		final TableViewerColumn viewerColumnLocation = new TableViewerColumn(selectedTableViewer, SWT.NONE);
		viewerColumnLocation.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CompositeConfiguration p = (CompositeConfiguration) element;
				return String.valueOf(p.getLevel());
	      	}
		});
			
		TableColumn colLocation = viewerColumnLocation.getColumn();
		colLocation.setText("Start Level");
		
		moveUpButton = new Button(wPageComposite, SWT.CENTER);
		moveUpButton.setText("Up");
		moveUpButton.setEnabled(false);
		moveUpButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) selectedTableViewer.getSelection();
				if (selection != null && !selection.isEmpty()) {
					if(selection.getFirstElement() instanceof CompositeConfiguration) {
						CompositeConfiguration lc = (CompositeConfiguration) selection.getFirstElement();
						int level = lc.getLevel();
						if(level > 1) {
							model.moveUp(lc);
							updateUpDownButtons(lc);
						}
					}	
					selectedTableViewer.refresh();
					UiUtils.packTableColumns(selectedTableViewer.getTable());
					setDirty(true);
		        	updateDialog();
		     	}
  			}	
		});
			
		moveDownButton = new Button(wPageComposite, SWT.CENTER);
		moveDownButton.setText("Down");
		moveDownButton.setEnabled(false);
		moveDownButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) selectedTableViewer.getSelection();
				if (selection != null && !selection.isEmpty()) {
					if(selection.getFirstElement() instanceof CompositeConfiguration) {
						CompositeConfiguration lc = (CompositeConfiguration) selection.getFirstElement();
						int level = lc.getLevel();
						if(level < model.getSize()) {
							model.moveDown(lc);
							updateUpDownButtons(lc);
						}
		        	}
		        	selectedTableViewer.refresh();
		        	UiUtils.packTableColumns(selectedTableViewer.getTable());
		        	setDirty(true);
		        	updateDialog();
	        	}
		  	}
		});
			
		UiUtils.packTableColumns(selectedTableViewer.getTable());
		
		// Layout All Bundles Tree
		FormData data = new FormData();
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		wBundleAllLabel.setLayoutData(data);
			
		data = new FormData();
		data.left = new FormAttachment(viewer.getTree(), MARGIN, SWT.RIGHT);
		data.top = new FormAttachment(0, 0);
		wBundleSelectedLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(wBundleAllLabel, MARGIN, SWT.BOTTOM);
		data.right = new FormAttachment(selectedTable, NEG_MARGIN, SWT.LEFT);
		data.bottom = new FormAttachment(100, 0);
		viewer.getTree().setLayoutData(data);
		
			
		data = new FormData();
		data.left = new FormAttachment(viewer.getTree(), NEG_MARGIN, SWT.RIGHT);
		data.top = new FormAttachment(wBundleSelectedLabel, MARGIN, SWT.BOTTOM);
		data.right = new FormAttachment(moveDownButton, NEG_MARGIN, SWT.LEFT);
		data.bottom = new FormAttachment(100, 0);
		selectedTable.setLayoutData(data);
		
			
		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.left = new FormAttachment(selectedTable, MARGIN, SWT.RIGHT);
		data.top = new FormAttachment(wBundleSelectedLabel, MARGIN, SWT.BOTTOM);
		moveUpButton.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(moveUpButton, MARGIN, SWT.BOTTOM);
		moveDownButton.setLayoutData(data);
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		viewer.getTree().clearAll(true);
		viewer.refresh();
		
		model.clear();
		try {
			Set<String> set = configuration.getAttribute(CompositeUtil.COMPOSITE_SET_CONFIGURATION, Collections.emptySet());
			
			if (set != null) {
				Map<String, ILaunchConfiguration> map = CompositeUtil.getCofigurations();
				for(String name: set) {
					viewer.setChecked(map.get(name), true);
	    	  	}
				model.setSet(set, map);
	      	}
    	} catch (CoreException e) {
    		CompositeLauncherUIPlugin.getDefault().getLog().log(e.getStatus());
    	}
		selectedTableViewer.refresh();
	    
	    UiUtils.packTableColumns(selectedTableViewer.getTable());
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(CompositeUtil.COMPOSITE_SET_CONFIGURATION, model.getStringSet());
	}

	@Override
	public String getName() {
		return TAB_NAME;
	}
	
	private void updateUpDownButtons(CompositeConfiguration lc) {
  		int level = lc.getLevel();
		if(level > 1) {
			moveUpButton.setEnabled(true);
		} else {
			moveUpButton.setEnabled(false);
		}
		if(level < model.getSize()) {
			moveDownButton.setEnabled(true);
		} else {
			moveDownButton.setEnabled(false);
		}
  	}
	
	protected void updateDialog() {
		updateLaunchConfigurationDialog();
	}
  
	class LevelComparator implements Comparator<CompositeConfiguration> {

		@Override
		public int compare(CompositeConfiguration arg0, CompositeConfiguration arg1) {
			return arg0.getLevel() - arg1.getLevel();
		}
	
  	}

}
