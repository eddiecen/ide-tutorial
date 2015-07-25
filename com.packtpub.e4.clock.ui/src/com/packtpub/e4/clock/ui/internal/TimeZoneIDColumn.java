package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

// NOTE: page 103
public class TimeZoneIDColumn extends TimeZoneColumn {
	public String getText(Object element) {
		if (element instanceof TimeZone) {
			return ((TimeZone) element).getID();
		} else {
			return "";
		}
	}

	public String getTitle() {
		return "ID";
	}
}
