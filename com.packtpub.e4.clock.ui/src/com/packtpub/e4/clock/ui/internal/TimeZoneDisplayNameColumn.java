package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

// NOTE: page 103
public class TimeZoneDisplayNameColumn extends TimeZoneColumn {
	public String getText(Object element) {
		if (element instanceof TimeZone) {
			return ((TimeZone) element).getDisplayName();
		} else {
			return "";
		}
	}

	public String getTitle() {
		return "Display Name";
	}
}
