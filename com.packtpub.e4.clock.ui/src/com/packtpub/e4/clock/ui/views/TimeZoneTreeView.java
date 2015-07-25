package com.packtpub.e4.clock.ui.views;

import java.net.URL;
import java.util.TimeZone;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySource;

import com.packtpub.e4.clock.ui.internal.TimeZoneComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneDialog;
import com.packtpub.e4.clock.ui.internal.TimeZoneSelectionListener;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerFilter;

public class TimeZoneTreeView extends ViewPart {

	private TreeViewer treeViewer;
	// NOTE: page 106
	private TimeZoneSelectionListener selectionListener;

	public void dispose() {
		if (selectionListener != null) {
			getSite().getWorkbenchWindow().getSelectionService()
					.removeSelectionListener(selectionListener);
			selectionListener = null;
		}
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		// NOTE: page 82 Image resource handling
		// To use a different image, either the global ImageRegistry of
		// JFaceRegistry
		// could be used, or one can be instantiated separately. Using the
		// global one will work,
		// but it means that effectively the Image instance never gets disposed,
		// since the
		// JFaceRegistry will last for the lifetime of your Eclipse instance.
		// Instead, create a LocalResourceManager and ImageRegistry, which are
		// tied
		// to the lifetime of the control. When the parent control is disposed,
		// the images will
		// be disposed automatically. These should be added to the
		// createPartControl()
		// method of TimeZoneTreeView.
		ResourceManager rm = JFaceResources.getResources();
		LocalResourceManager lrm = new LocalResourceManager(rm, parent);
		ImageRegistry ir = new ImageRegistry(lrm);
		URL sample = getClass().getResource("/icons/sample.gif");
		ir.put("sample", ImageDescriptor.createFromURL(sample));

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		// treeViewer.setLabelProvider(new TimeZoneLabelProvider());

		// To render the labels in the tree, a LabelProvider is used. This
		// provides a label (and an
		// optional image) for each element. It is possible to present a
		// different icon for each type of
		// object; this is used by the Package view in the Java perspective to
		// present a class icon for
		// the classes, a package icon for the packages, and so on.
		// The LabelProvider can render the messages in different ways; for
		// example, if could
		// append the timezone offset (or only show the difference between that
		// and GMT).
		// NOTE: page 82
		// use resource registry
		// treeViewer.setLabelProvider(new TimeZoneLabelProvider(ir));
		// NOTE: page 84
		// In order to use the styled label provider, it has to be wrapped
		// within a
		// DelegatingStyledCellLabelProvider.
		// treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new
		// TimeZoneLabelProvider(ir)));
		// NOTE: page 85 pass global font registry from JFaceResources
		FontRegistry fr = JFaceResources.getFontRegistry();
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new TimeZoneLabelProvider(ir, fr)));

		treeViewer.setContentProvider(new TimeZoneContentProvider());
		// NOTE: page 80
		// The data of TreeViewer was provided by the setInput() method, which
		// is almost always
		// an array of objects, even if it contains only a single element.
		treeViewer.setInput(new Object[] { TimeZoneComparator.getTimeZones() });

		// NOTE: page 89 add filter
		// Only Time Zone in the ETC region is listed
		treeViewer.setFilters(new ViewerFilter[] { new TimeZoneViewerFilter(
				"GMT") });

		// NOTE: page 87 sorting
		// treeViewer.setComparator(new TimeZoneViewerComparator());
		// NOTE: page 88 sorting reverse order
		// By adding a ViewerComparator to the Viewer, the data can be sorted in
		// an appropriate
		// manner for the viewer in question. Typically, this will be done in
		// conjunction with selecting
		// an option in the view—for example, an option may be present to
		// reverse the ordering, or to
		// sort by name or offset.
		// When implementing a specific Comparator, check that the method can
		// handle multiple
		// object types (including ones that may not be expected). The data in
		// the viewer may change,
		// or be different at runtime than expected. Use instanceof to check
		// that the items are of
		// the expected type.
		// To store properties that are specific to a viewer, use the setData()
		// and getData() calls
		// on the viewer itself. This allows a generic comparator to be used
		// across views while still
		// respecting per view filtration/sorting operations.
		// The preceding example hardcodes the sort data, which requires an
		// Eclipse relaunch to see
		// the effect. Typically, after modifying properties that may affect the
		// view's sorting or filtering,
		// the viewer has a refresh() invoked to bring the display in line with
		// the new settings.
		treeViewer.setData("REVERSE", Boolean.TRUE);
		treeViewer.setComparator(new TimeZoneViewerComparator());

		// NOTE: page 89 Remove expendable icon from the tree items for those
		// there isn't sub items
		// The TimeZoneViewerFilter class was created as a subclass of
		// ViewerFilter and set
		// on the TreeViewer. When displaying and filtering the data, the filter
		// is called for every
		// element in the tree (including the root node).
		// By default, if the hasChildren() method returns true, the expandable
		// icon is shown.
		// When clicked, it will iterate through the children, applying the
		// filter to them. If all the
		// elements are filtered, the expandable marker will be removed.
		// By calling setExpandPreCheckFilters(true) on the viewer, it will
		// verify that at least
		// one child is left after filtration. This has no negative effect when
		// there aren't any filters set.
		// If there are filters set and there are large data sets, it may take
		// some time to perform the
		// calculation of whether they should be filtered or not.
		// To show all the tree's elements by default, or collapse it down to a
		// single tree, use
		// expandAll() and collapseAll() on the viewer. This is typically bound
		// to a local view
		// command with a [+] and [-] icon (for example, the Synchronize view or
		// the Package Explorer).

		// If the data is a tree structure, which only needs to show some levels
		// by default, there is an
		// expandToLevel() and collapseToLevel(), which take an integer and an
		// object (use
		// the getRoot() of the tree if not specified) and mark everything as
		// expanded or collapsed
		// to that level. The expandAll() method is a short-hand for
		// expandToLevel(getRoot(),
		// ALL_LEVELS).
		// When responding to a selection event, which contains a hidden object,
		// it is conventional
		// to perform a reveal() operation on the object to make it visible in
		// the tree. Note that
		// reveal() only works when the getParent() is correctly implemented,
		// which isn't the
		// case with this example
		treeViewer.setExpandPreCheckFilters(true);

		// NOTE: page 92 add double click listener
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				Viewer viewer = event.getViewer();
				Shell shell = viewer.getControl().getShell();
				// MessageDialog.openInformation(shell, "Double click",
				// "Double click detected");
				// NOTE: page 92 find the selected object on double click
				// NOTE: page 94
				// A double-click listener was added to the viewer by
				// registering it with
				// addDoubleClickListener(). Initially, a standard information
				// dialog was displayed, but
				// then a custom subclass of MessageDialog was used which
				// included a ClockWidget. In
				// order to get the appropriate TimeZone, it was accessed via
				// the currently selected object
				// from the TreeViewer.
				// Selection is managed via an ISelection interface. The
				// viewer's getSelection()
				// method should always return a non-null value, although the
				// value may return true for
				// the isEmpty() call. There are two relevant subinterfaces;
				// IStructuredSelection and
				// ITreeSelection.
				// The ITreeSelection is a subtype of IStructuredSelection, and
				// adds methods specific
				// to trees. This includes the ability to find out what the
				// selected object(s) are and what their
				// parents are in the tree.
				// The IStructuredSelection is the most commanly used interface
				// in dealing with
				// selection types. If the selection is not empty, it is almost
				// always an instance of an
				// IStructuredSelection. As a result, the following snippet of
				// code appears regularly:
				ISelection sel = viewer.getSelection();
				Object selectedValue;
				if (!(sel instanceof IStructuredSelection) || sel.isEmpty()) {
					selectedValue = null;
				} else {
					// This snippet gets the selection from the viewer, and if
					// it's not an IStructuredSelection,
					// or it's empty, it assigns the variable selectedValue to
					// null. If it's non-empty, it casts it to
					// the IStructuredSelection interface and calls
					// getFirstElement() to get the single
					// selected value.
					// The selection may have more than one selected value, in
					// which case the
					// getFirstElement() method only returns the first element
					// selected. The
					// IStructuredSelection class provides an iterator to step
					// through all selected objects.
					selectedValue = ((IStructuredSelection) sel)
							.getFirstElement();
					if (selectedValue instanceof TimeZone) {
						TimeZone timeZone = (TimeZone) selectedValue;
						// MessageDialog.openInformation(shell,
						// timeZone.getID(), timeZone.toString());
						// NOTE: page 93 make it more flexible when displaying
						// dialog
						new TimeZoneDialog(shell, timeZone).open();
					}
				}
			}
		});
		// NOTE: page 97
		System.out.println("Adapter is "
				+ Platform.getAdapterManager().getAdapter(
						TimeZone.getDefault(), IPropertySource.class));
		// NOTE: page 97 The Properties view is not being notified of the change
		// in selection
		// To update the state of the Workbench's selection, the view's
		// selection provider was
		// connected with that of the page (via the getSite() method). When the
		// selection in the
		// viewer changes, it sends a message to registered listeners of the
		// page's selection service so
		// that they can update their views, if necessary.
		//
		// E4: The selection listener needs to be (un)registered manually to
		// provide a hook between the viewer and the selection service. Instead
		// of being ISelectionService, it's ESelectionService. The
		// interface is slightly different, because the ISelectionService is
		// tied
		// to the IWorkbenchPart class, but the ESelectionService is not.
		//
		// To provide information to the Properties view, an IPropertySource was
		// created for the
		// TimeZone and associated with the Platform IAdapterManager through the
		// declaration
		// in the plugin.xml file.
		// It's generally better to provide hooks declaratively in the
		// plugin.xml file rather than hooking
		// it up with the start() and stop() activator methods. That's because
		// the start on the
		// Activator may not be called until the first class is loaded from the
		// bundle; in the case of the
		// adaptor, the declarative registration can provide the information
		// before it is first required.
		// The adaptor factory provides the getAdapter() method, which wraps or
		// converts the
		// object being passed into one of the desired type. If the object is
		// already an instance of the
		// given type, it can just be returned as it is—but otherwise return a
		// POJO, proxy, or wrapper
		// object that implements the desired interface. It's quite common to
		// have a class (such as
		// TimeZonePropertySupport) whose sole job is to implement the desired
		// interface, and
		// which wraps an instance of the object (TimeZone) to provide the
		// functionality.
		// The IPropertySupport interface provides a basic means to acquire
		// properties from the
		// object, and to do so it uses an identifier for each property. These
		// can be any object type;
		// in the preceding example, new Object instances were used. Although it
		// is possible to use
		// String (plenty of other examples do), this is not recommended, since
		// the value of the
		// String has no importance and it takes up space in the JVM's PermGen
		// memory space. In
		// addition, using a plain Object means that the instance can be
		// compared with == without
		// any concerns, whereas doing so with String is likely to fail the code
		// reviews or automated
		// style checkers. (The preceding example uses the .equals() method to
		// encourage its use
		// when not using an Object, but a decent JIT will in-line
		// it—particularly since the code is
		// sending the message to a static final instance.)
		getSite().setSelectionProvider(treeViewer);

		// NOTE: page 106 also in TimeZoneTableView
		selectionListener = new TimeZoneSelectionListener(treeViewer, getSite()
				.getPart());
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);
		
		// NOTE: page 122 enable to Pop-up menu. See different impl in TimeZoneTableView
		hookContextMenu(treeViewer);
	}

	public void setFocus() {
		treeViewer.getControl().setFocus();
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
