package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class TimeZoneViewerFilter extends ViewerFilter {
	private String pattern;

	public TimeZoneViewerFilter(String pattern) {
		this.pattern = pattern;
	}

	// NOTE: page 89 Filtering
//	The ViewerFilter class provides a filtering method,
//	confusingly called select(). (There are some filter() methods, but these are used to
//	filter the entire array; the select() method is used to determine if a specific element is
//	shown or not
	//
//	When displaying and filtering the data, the filter is called for every
//	element in the tree (including the root node)
	public boolean select(Viewer v, Object parent, Object element) {
		if (element instanceof TimeZone) {
			TimeZone zone = (TimeZone) element;
			return zone.getDisplayName().contains(pattern);
		} else {
			return true;
		}
	}
}