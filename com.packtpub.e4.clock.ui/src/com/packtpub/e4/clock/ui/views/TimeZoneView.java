// NOTE: page 67 generated by editing plugin.xml and click class
package com.packtpub.e4.clock.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.ClockWidget;
import com.packtpub.e4.clock.ui.internal.TimeZoneComparator;

import java.util.*;
import java.util.Map.Entry;

public class TimeZoneView extends ViewPart {

	public TimeZoneView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// NOTE: page 68 A TimeZoneView that contains available timezone tabs
		Map<String, Set<TimeZone>> timeZones = TimeZoneComparator
				.getTimeZones();
		CTabFolder tabs = new CTabFolder(parent, SWT.BOTTOM);
		Iterator<Entry<String, Set<TimeZone>>> regionIterator = timeZones
				.entrySet().iterator();
		while (regionIterator.hasNext()) {
			Entry<String, Set<TimeZone>> region = regionIterator.next();
			// NOTE: CTabItem->Composite
			CTabItem item = new CTabItem(tabs, SWT.NONE);
			item.setText(region.getKey());
			// NOTE: page 69 how multiple ClockWidgets
			//Composite clocks = new Composite(tabs, SWT.NONE);
			//
			//item.setControl(clocks);
			// NOTE: page 70 Use ScrolledComposite to make contents scrollable
			ScrolledComposite scrolled = new ScrolledComposite(tabs,SWT.H_SCROLL | SWT.V_SCROLL);
			Composite clocks = new Composite(scrolled, SWT.NONE);
			clocks.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			item.setControl(scrolled);
			scrolled.setContent(clocks);
			clocks.setLayout(new RowLayout()); // without this line the clocks won't show
			
			RGB rgb = new RGB(128, 128, 128);
			TimeZone td = TimeZone.getDefault();
			Iterator<TimeZone> timezoneIterator = region.getValue().iterator();
			while (timezoneIterator.hasNext()) {
				TimeZone tz = timezoneIterator.next();
				// NOTE: CTabItem->Composite->ClockWidget
				//ClockWidget clock = new ClockWidget(clocks, SWT.NONE, rgb);
				//clock.setOffset((tz.getOffset(System.currentTimeMillis()) - td.getOffset(System.currentTimeMillis())) / 3600000);
				// NOTE: CTabItem->Composite->Group->ClockWidget
				// NOTE: page 70
				Group group = new Group(clocks,SWT.SHADOW_ETCHED_IN);
				group.setText(tz.getID().split("/")[1]);
//				Since the default layout manager for general Composite classes is null, Groups
//				don't have a layout manager and thus, the clocks are not getting sized appropriately.
//				This can be fixed by setting a layout manager explicitly:
				group.setLayout(new FillLayout());
				ClockWidget clock = new ClockWidget(group, SWT.NONE, rgb);
				clock.setOffset((tz.getOffset(System.currentTimeMillis()) - td
						.getOffset(System.currentTimeMillis())) / 3600000);
			}
			// NOTE: page 71.19
			//The problem is that ScrolledComposite has no minimum size. This can be
			//calculated from the clocks container. Add this to the bottom of the while
			//loop, after the contents of ScrolledComposite have been created:
			// NOTE: It has to be here after above while loop
			Point size = clocks.computeSize(SWT.DEFAULT,SWT.DEFAULT);
			scrolled.setMinSize(size);
			scrolled.setExpandHorizontal(true);
			scrolled.setExpandVertical(true);
		}
		tabs.setSelection(0);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
