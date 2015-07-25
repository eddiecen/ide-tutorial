package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

// NOTE: page 105
//The TimeZoneTableView and the TimeZoneTreeView can propagate their selection to
//the Properties view. Responding to selection changes gives a unified feel despite the fact that
//the views are independent entities.
//They can be further linked so that when a TimeZone is selected in either of these views, it
//automatically reveals in the other. To do this, a selection listener will need to be registered,
//and if the selected object is a type of TimeZone, display it in the view (with the reveal()
//and setSelection() methods).

// NOTE: page 107
//Selection events occur a lot in the Eclipse workspace, so it is important that the selection
//listeners be performant. By filtering events fired from the same part, or filtering
//uninteresting types, the event delivery will be more efficient. In this case, the selection is
//checked to ensure that the selection contains (at least) one element, which is a TimeZone,
//before performing any UI updates.
//
//The selection of the viewer can be synchronized with the setSelection() call; this
//saves having to instantiate a new selection object and set the data appropriately. However,
//setting the selection alone is not enough; the reveal() method needs to be called to
//ensure that it is appropriately highlighted. If multiple objects are selected, this will only
//reveal the first element.
//The reveal() method is only available to StructuredViewers, so the selection stamps
//the selection as it is, and explicitly sets the IStructuredSelection for those that are
//StructuredViewers.
//Finally, the listeners are registered when the view is created, and removed when
//the view is disposed. To do this, get hold of the ISelectionService via the part
//and then invoke the addSelectionListener() method to add it, and invoke the
//removeSelectionListener() method to remove it.
//
//	E4: If using E4, instead of using the ISelectionService, the
//	ESelectionService is used. This provides a similar, but not identical,
//	interface in that the WorkbenchPart is no longer present. Typically, the
//	ESelectionService is injected into the view and the listener is wired
//	in the @PostConstruct and removed in the @PreDestroy call.
public class TimeZoneSelectionListener implements ISelectionListener {
	private Viewer viewer;
	private IWorkbenchPart part;

	public TimeZoneSelectionListener(Viewer v, IWorkbenchPart p) {
		this.viewer = v;
		this.part = p;
	}

	public void selectionChanged(IWorkbenchPart p, ISelection sel) {
		// Ignore the event if it was fired by the same part
		// Get the selected object from the event and compare it with the current one
		// If different, and the selected object is a TimeZone, update the viewer
		if (p != this.part) {
			Object selected = ((IStructuredSelection) sel).getFirstElement();
			Object current = ((IStructuredSelection) viewer.getSelection())
					.getFirstElement();
			if (selected != current && selected instanceof TimeZone) {
				viewer.setSelection(sel);
				if (viewer instanceof StructuredViewer) {
					((StructuredViewer) viewer).reveal(selected);
				}
			}
		}
	}
}
