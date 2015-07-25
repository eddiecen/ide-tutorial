package com.packtpub.e4.clock.ui;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

// NOTE: page 45
//Although the ClockView shows an animated clock, creating an independent widget will
//allow the clock to be reused in other places.
public class ClockWidget extends Canvas {

	private final Color color;
	private int offset;
	
	public ClockWidget(Composite parent, int style, RGB rgb) {
		super(parent, style);
		// NOTE: color is leaked!
		this.color = new Color(parent.getDisplay(),rgb);
		// NOTE: color leak is fixed by adding DisposeListener but not overriding dispose method
		//		The only time dispose() is called is at the top level
		//		Shell (or ViewPart), and if there are no registered listeners then the dispose method is
		//		not called on any components beneath.
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (color != null && !color.isDisposed())
					color.dispose();
			}
		});

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				ClockWidget.this.paintControl(e);
			}
		});

		// this will allow the ClockWidget to operate independently
		new Thread("TickTock") {
			public void run() {
				while (!ClockWidget.this.isDisposed()) {
					// directly invoking this.paintControl() or paintControl() would have ended up in an infinite loop
					ClockWidget.this.getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (!ClockWidget.this.isDisposed())
								ClockWidget.this.redraw();
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
	}

	public void paintControl(PaintEvent e) {

		// draws the circle
		e.gc.drawArc(e.x, e.y, e.width - 1, e.height - 1, 0, 360);
		
		// draw second hand
		int seconds = new Date().getSeconds();
		int arc = (15 - seconds) * 6 % 360;
		Color blue = e.display.getSystemColor(SWT.COLOR_BLUE);
		e.gc.setBackground(blue);
		e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 1, 2);
		
		// draw hour hand
		e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK));
		int hours = new Date().getHours() + offset;
		arc = (3 - hours) * 30 % 360;
		e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 5, 10);
	}

	
//	Add a computeSize() method to allow the clock to have a square appearance that
//	is the minimum of the width and height. Note that SWT.DEFAULT may be passed
//	in, which has the value -1, so this needs to be handled explicitly:
	public Point computeSize(int w, int h, boolean changed) {
		int size;
		if (w == SWT.DEFAULT) {
			size = h;
		} else if (h == SWT.DEFAULT) {
			size = w;
		} else {
			size = Math.min(w, h);
		}
		if (size == SWT.DEFAULT)
			size = 50;
		return new Point(size, size);
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Color getColor() {
		return color;
	}

	public int getOffset() {
		return offset;
	}
	
	// NOTE: this dispose won't fix leak issue
//	@Override
//	public void dispose() {
//		if (color != null && !color.isDisposed())
//			color.dispose();
//		super.dispose();
//	}
}
