package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

// NOTE: page 87 sorting
//The TreeViewer is already showing data in a sorted format, but this is not a view-imposed
//sort. Because the data is stored in a TreeMap, the sort ordering is created by the TreeMap
//itself, which in turn is sorting on the value of toString(). To use a different ordering
//(say, based on offset) the choices are either to modify the TreeMap to add a Comparator
//and sort the data at creation time, or add a sorter to the TreeViewer. The first choice
//is applicable if the data is only used by a single view, or if the data is coming from a large
//external data store, which can perform the sorting more efficiently (such as a relational
//database). For smaller data sets, the sorting can be done in the viewer itself.
public class TimeZoneViewerComparator extends ViewerComparator {
	public int compare(Viewer viewer, Object o1, Object o2) {
		int compare;
		if (o1 instanceof TimeZone && o2 instanceof TimeZone) {
			compare = ((TimeZone) o2).getOffset(System.currentTimeMillis())
					- ((TimeZone) o1).getOffset(System.currentTimeMillis());
		} else {
			compare = o1.toString().compareTo(o2.toString());
		}
		// return compare;
		// NOTE: page 88 sorting
//		To add a viewer-specific sort, modify the compare() method of
//		TimeZoneViewerComparator to get a REVERSE key from the
//		viewer's data. Use that to invert the results of the sort:
		boolean reverse = Boolean.parseBoolean(String.valueOf(viewer
				.getData("REVERSE")));
		return reverse ? -compare : compare;
	}
}
