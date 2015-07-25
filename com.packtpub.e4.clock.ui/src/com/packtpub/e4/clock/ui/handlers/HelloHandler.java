package com.packtpub.e4.clock.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

// NOTE: page 112 The handler joins the processing of the command to a class that implements IHandler
// NOTE: page 112 also see plugin.xml
// NOTE: page 114 binding commands to keys. See plugin.xml
// NOTE: page 115 Changing context. Enable command & handler only on this editor. plugin.xml
public class HelloHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) {
		MessageDialog.openInformation(null, "Hello", "World");
		return null;
	}
}
