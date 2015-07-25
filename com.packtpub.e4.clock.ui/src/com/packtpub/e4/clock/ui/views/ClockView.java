package com.packtpub.e4.clock.ui.views;


import java.util.Date;
import java.util.TimeZone;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import com.packtpub.e4.clock.ui.ClockWidget;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ClockView extends ViewPart {
	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Combo timezones;
	
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ClockView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 * NOTE: page 39
	 */
//	public void createPartControl(Composite parent) {
//		final Canvas clock = new Canvas(parent,SWT.NONE);
//		clock.addPaintListener(new PaintListener() {
//		public void paintControl(PaintEvent e) {
//		e.gc.drawArc(e.x,e.y,e.width-1,e.height-1,0,360);
//		}
//		});
//	}
	// NOTE: page 41
	@SuppressWarnings("unused")
	public void createPartControl(Composite parent) {
// NOTE: page 46: change to instantiate ClockWidget

		//final ClockWidget clock = new ClockWidget(parent,SWT.NONE);
//		final Canvas clock = new Canvas(parent, SWT.NONE);
//		clock.addPaintListener(new PaintListener() {
// NOTE: page 45: moved to ClockWidget			
//			public void paintControl(PaintEvent e) {
//				int seconds = new Date().getSeconds();
//				int arc = (15 - seconds) * 6 % 360;
//				Color blue = e.display.getSystemColor(SWT.COLOR_BLUE);
//				e.gc.setBackground(blue);
//				e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 1, 2);
//			}
//		});

		// NOTE: page 42: make the sends hand run automatically
// NOTE: page 45 moved to ClockWidget		
//		new Thread("TickTock") {
//			public void run() {
//				while (!clock.isDisposed()) {
//					// NOTE: page 44 clock.redraw(); // the second hand only
//					// update when resize the view
//					// now the seconds hand will be updated automatically
//					clock.getDisplay().asyncExec(new Runnable() {
//						public void run() {
//							if (clock != null && !clock.isDisposed()) {
//								clock.redraw();
//							}
//						}
//
//					});
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						return;
//					}
//				}
//			}
//		}.start();
		

		
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		parent.setLayout(layout);
		
		final ClockWidget clock1 = new ClockWidget(parent, SWT.NONE, new RGB(255,0,0));
		final ClockWidget clock2 = new ClockWidget(parent, SWT.NONE, new RGB(0, 255,0));
		final ClockWidget clock3 = new ClockWidget(parent, SWT.NONE, new RGB(0, 0, 255));
		
		clock1.setLayoutData(new RowData(20, 20));
		clock3.setLayoutData(new RowData(100, 100));
		
		// NOTE: interact with user chapter 2
		// TODO: timezones leak?
		String[] ids = TimeZone.getAvailableIDs();
		timezones = new Combo(parent, SWT.SIMPLE);
		timezones.setVisibleItemCount(5);
		for (int i = 0; i < ids.length; i++) {
			timezones.add(ids[i]);
		}
		// change hour when user select timezones
		timezones.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String z = timezones.getText();
				TimeZone tz = z == null ? null : TimeZone.getTimeZone(z);
				TimeZone dt = TimeZone.getDefault();
				int offset = tz == null ? 0 : (tz.getOffset(System
						.currentTimeMillis()) - dt.getOffset(System
						.currentTimeMillis())) / 3600000;
				clock3.setOffset(offset);
				clock3.redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				clock3.setOffset(0);
				clock3.redraw();
			}
		});
//		It is necessary to know how many resources are allocated in order to know if the leak has
//		been plugged or not. Fortunately, SWT provides a mechanism to do this via the Display
//		and the DeviceData classes. Normally, this is done by a separate plug-in, but in this
//		example ClockView will be modified to show this behavior.
		// NOTE: discover leaks at ClockWidget FIXME
		// page 53 update debug configuration to see the count increase
		checkLeak(parent);
	}

	private void checkLeak(Composite parent) {
		Object[] oo=parent.getDisplay().getDeviceData().objects;
		int c = 0, d = 0;
		for (int i = 0; i < oo.length; i++) {
			if (oo[i] instanceof Color) {
				c++;
			}
			if (oo[i] instanceof Combo) {
				d++;
			}
		}
		System.err.println("There are " + c + " Color instances");
		System.err.println("There are " + d + " Combo stances");
		
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ClockView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Clock View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		timezones.setFocus();
	}
}