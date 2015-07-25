package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

// NOTE: page 103
public class TimeZoneOffsetColumn extends TimeZoneColumn {
	public String getText(Object element) {
		if (element instanceof TimeZone) {
			return String.valueOf(((TimeZone) element).getOffset(0));
		} else {
			return "";
		}
	}

	public String getTitle() {
		return "Offset";
	}
}
