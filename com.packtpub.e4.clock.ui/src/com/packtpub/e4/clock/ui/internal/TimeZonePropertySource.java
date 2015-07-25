package com.packtpub.e4.clock.ui.internal;

import java.util.Date;
import java.util.TimeZone;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

// NOTE: page 95 add package to MANIFEST in order to see IPropertySource
//The only methods that need to be implemented are getPropertyValue()
//and getPropertyDescriptors(). (The other methods, such as
//getEditableValue() and isPropertySet(), can be ignored, because they only
//get invoked when performing edit operations. These should be left empty or return
//null/false.) The accessors are called with an identifier; while the latter returns an
//array of PropertyDescriptors combining pairs of identifiers and a displayable name.

// NOTE: page 96
//To hook this property source into the Properties view, an adapter is used. It can
//be specified via a generic IAdaptable interface, which allows a class to virtually
//implement an interface. Since the TimeZone cannot implement the IAdaptable
//interface directly, an IAdapterFactory is needed. See TimeZoneAdapterFactory class
public class TimeZonePropertySource implements IPropertySource {
	private TimeZone timeZone;

	public TimeZonePropertySource(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	private static final Object ID = new Object();
	private static final Object DAYLIGHT = new Object();
	private static final Object NAME = new Object();

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(ID, "Time Zone"),
				new PropertyDescriptor(DAYLIGHT, "Daylight Savings"),
				new PropertyDescriptor(NAME, "Name") };
	}

	public Object getPropertyValue(Object id) {
		if (ID.equals(id)) {
			return timeZone.getID();
		} else if (DAYLIGHT.equals(id)) {
			return timeZone.inDaylightTime(new Date());
		} else if (NAME.equals(id)) {
			return timeZone.getDisplayName();
		} else {
			return null;
		}
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}
}
