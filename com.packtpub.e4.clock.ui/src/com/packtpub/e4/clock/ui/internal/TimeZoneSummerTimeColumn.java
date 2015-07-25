package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

// NOTE: page 103
public class TimeZoneSummerTimeColumn extends TimeZoneColumn {
	public String getText(Object element) {
		if (element instanceof TimeZone) {
			boolean isSummerTime = ((TimeZone) element).useDaylightTime();
			return isSummerTime ? "Yes" : "No";
		} else {
			return "";
		}
	}

	public String getTitle() {
		return "Summer Time";
	}
}
