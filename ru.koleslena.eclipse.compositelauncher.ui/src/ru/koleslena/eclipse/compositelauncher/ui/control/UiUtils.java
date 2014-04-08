package ru.koleslena.eclipse.compositelauncher.ui.control;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author koleslena
 *
 */
public class UiUtils {
	
	public static void packTableColumns(Table table) {
		if (table == null)
			return;
	 	TableColumn [] columns = table.getColumns();
	 	if (columns == null)
	 		return;
	 	for(int i=0;i<columns.length;i++) {
	 		columns[i].pack();
	 	}
 	}
}
