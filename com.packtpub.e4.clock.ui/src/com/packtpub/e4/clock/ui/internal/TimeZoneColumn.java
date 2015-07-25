package com.packtpub.e4.clock.ui.internal;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

//The table shows a list of the ZoneInfo objects. That's because there is no
//LabelProvider, so they're just being rendered with their toString()
//representation. Because a table has multiple columns, TableViewers have a
//number of TableViewerColumn instances. Each one represents a column in the
//Table, and each has its own size, title, and label provider. Creating a new column
//often involves setting up standard features (such as the width) as well as hooking in
//the required fields to display.
//To make it easy to re-use, create an abstract subclass of ColumnLabelProvider
//called TimeZoneColumn (in the com.packtpub.e4.clock.ui.internal
//package) with abstract getText() and getTitle() methods, and a concrete
//getWidth() method.
// NOTE: page 102
public abstract class TimeZoneColumn extends ColumnLabelProvider {
	public abstract String getText(Object element);

	public abstract String getTitle();

	public int getWidth() {
		return 250;
	}

	// Add a helper method to the TimeZoneColumn class, which makes it easier to add it to a viewer:
	public TableViewerColumn addColumnTo(TableViewer viewer) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		TableColumn column = tableViewerColumn.getColumn();
		column.setMoveable(true);
		column.setResizable(true);
		column.setText(getTitle());
		column.setWidth(getWidth());
		tableViewerColumn.setLabelProvider(this);
		return tableViewerColumn;
	}
}
