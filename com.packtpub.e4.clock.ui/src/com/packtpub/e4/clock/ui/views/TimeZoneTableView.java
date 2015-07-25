package com.packtpub.e4.clock.ui.views;

import java.util.TimeZone;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.internal.TimeZoneDisplayNameColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneIDColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneOffsetColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneSelectionListener;
import com.packtpub.e4.clock.ui.internal.TimeZoneSummerTimeColumn;

// NOTE: page 101
//E4: If creating a part for an E4 application, remember to
//use the @Inject annotation for the constructor and @Focus for the setFocus() method.
public class TimeZoneTableView extends ViewPart {
	private TableViewer tableViewer;
	// NOTE: page 106
	// The selectionListener needs to be added as a field, because it will be
	// necessary
	// to remove the listener when the view is disposed:
	private TimeZoneSelectionListener selectionListener;

	public void dispose() {
		if (selectionListener != null) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectionListener);
			selectionListener = null;
		}
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		// tableViewer.setInput(TimeZone.getAvailableIDs());
		// NOTE: page 101
		String[] ids = TimeZone.getAvailableIDs();
		TimeZone[] timeZones = new TimeZone[ids.length];
		for (int i = 0; i < ids.length; i++) {
			timeZones[i] = TimeZone.getTimeZone(ids[i]);
		}
		tableViewer.setInput(timeZones);
		getSite().setSelectionProvider(tableViewer);

		// NOTE: page 103 binding column data
		// Note that the columns need to be created prior to the
		// setInput() call, otherwise they won't display properly.
		new TimeZoneIDColumn().addColumnTo(tableViewer);
		new TimeZoneOffsetColumn().addColumnTo(tableViewer);
		new TimeZoneDisplayNameColumn().addColumnTo(tableViewer);
		new TimeZoneSummerTimeColumn().addColumnTo(tableViewer);
		tableViewer.setInput(timeZones);
		// A TableViewer was created and multiple ColumnLabelProviders were
		// added to it
		// for displaying individual fields of an object. Subclassing
		// ColumnLabelProvider avoids
		// the need to use anonymous inner classes and it gives a helper
		// function. This can be used
		// to create and wire in the column (with specified title and width),
		// while delegating those
		// properties to the concrete subclasses of TimeZoneIDColumn and so on.
		// This avoids the
		// need for tracking columns by ID.
		// For specific customizations of the columns, the underlying SWT Column
		// is used to set
		// functionality required by the application, including allowing the
		// column to be movable with
		// setMovable(true), and to be resizable with setResizable(true).
		// Similarly, table-wide
		// operations (such as showing the header) are performed by manipulating
		// the underlying SWT
		// Table and invoking setHeaderVisible(true).
		// It's important to note that the columns of the tree viewer are
		// calculated when the
		// setInput() method is called, so columns that are added after this
		// line may not show
		// properly. Generally, the setInput() should be left until the end of
		// the table's construction.
		// All of the other functionality from the other view is portable, for
		// example, by wiring up the
		// selection appropriately, the Properties view can show properties of
		// the selected object.

		// NOTE: page 106
		// The selection listeners need to be registered with the views.
		selectionListener = new TimeZoneSelectionListener(tableViewer, getSite().getPart());
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);

		// NOTE: page 110 adding context menus
//		MenuManager manager = new MenuManager("#PopupMenu");
//		Menu menu = manager.createContextMenu(tableViewer.getControl());
//		tableViewer.getControl().setMenu(menu);
//
//		// NOTE: page 106
//		// If the Menu is empty, the MenuManager won't show any content, so this
//		// currently
//		// has no effect. To demonstrate this, an Action will be added to the
//		// Menu
//		Action deprecated = new Action() {
//			public void run() {
//				MessageDialog.openInformation(null, "Hello", "World");
//			}
//		};
//		deprecated.setText("Hello");
//		manager.add(deprecated);
		// NOTE: page 122 adding context menu (Pop-up) new way
		// Itself won't show the Hello menu, need to update plugin.xml
		hookContextMenu(tableViewer);
	}

	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	// NOTE: page 122 Contributing commands to Pop-up menu
	// It's useful to be able to add contributions to pop-up menus so that they
	// can be used by
	// different places. Fortunately, this can be done fairly easily with the
	// menuContribution
	// element and a combination of enablement tests. This allows the removal of
	// the Action
	// introduced in the first part of this chapter with a more generic command
	// and handler pairing.
	// There is a deprecated extension point—which still works in Eclipse 4.2
	// today—called
	// objectContribution, which is a single specialized hook for contributing a
	// pop-up menu
	// to an object. This has been deprecated for some time, but often older
	// tutorials or examples
	// may refer to it.
	private void hookContextMenu(Viewer viewer) {
		MenuManager manager = new MenuManager("#PopupMenu");
		Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(manager, viewer);
	}
}
