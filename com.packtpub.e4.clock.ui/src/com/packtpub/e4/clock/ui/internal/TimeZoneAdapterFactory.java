package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

// NOTE: page 96 hook property source into the Properties view
// NOTE: pate 97 to register the adapter factory with Eclipse, add it to plugin.xml
public class TimeZoneAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class
				&& adaptableObject instanceof TimeZone) {
			return new TimeZonePropertySource((TimeZone) adaptableObject);
		} else {
			return null;
		}
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
