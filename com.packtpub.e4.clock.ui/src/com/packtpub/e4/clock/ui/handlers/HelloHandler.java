package com.packtpub.e4.clock.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

// NOTE: page 112 The handler joins the processing of the command to a class that implements IHandler
// NOTE: page 112 also see plugin.xml
// NOTE: page 114 binding commands to keys. See plugin.xml
// NOTE: page 115 Changing context. Enable command & handler only on this editor. plugin.xml
public class HelloHandler extends AbstractHandler {
	// public Object execute(ExecutionEvent event) {
	// MessageDialog.openInformation(null, "Hello", "World");
	// return null;
	// }

	// NOTE: page 125: Running operation in backgroup process
	// Every action in the Eclipse UI must run on the UI thread, so if the
	// action takes a significant time to run, it will give the impression that
	// the user interface is blocked or hung. The way to avoid this is to drop
	// out of the UI thread before doing any long term work. Any updates that
	// need to be done involving the UI should be scheduled back on the UI
	// thread. The example used both Job (a mechanism for scheduling named
	// processes that can be monitored via the Progress view), as well as the
	// display's asyncExec() method to launch the resulting message dialog.
	// Both of these situations require the use of inner classes, which can
	// increase the verbosity of the code. With future versions of Java and use
	// of lambda expressions this will be significantly reduced.
	//
	// E4: Since E4 can use different renderers, the Display is not a good
	// target for submitting background UIJob. Instead, use a UISynchronize
	// instance to acquire the asyncExec() or syncExec() method.
	public Object execute(ExecutionEvent event) {
		Job job = newJobPage131();
		job.schedule();
		return null;
	}

	private Job newJobPage125() {
		return new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				// Run the Eclipse instance, and click on the Help | Hello menu
				// item. (It may be necessary to open a Java file to enable the
				// menu). Open the Progress view, and a Job will be listed with
				// About to say hello running. Unfortunately, an error dialog is
				// then shown:
				// This occurs because the Job runs on a non-UI background
				// thread, so when the MessageDialog is shown an exception
				// occurs. To fix this, instead of showing the MessageDialog
				// directly, a second Job or Runnable can be created to run
				// specifically on the UI thread. Replace the call to the
				// MessageDialog with the following code snippet:

				// MessageDialog.openInformation(null, "Hello", "World");
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						MessageDialog.openInformation(null, "Hello", "World");
					}
				});
				// Instead of scheduling the UI notification piece as a
				// Display.asyncExec() method, create it as a UIJob instead.
				// This works in exactly the same way as a Job does, but you
				// need to override the runInUIThread() method instead of the
				// run() method. This may be useful when there is more UI
				// interaction required, such as asking the user for more
				// information.

				return Status.OK_STATUS;
			}
		};
	}

	// NOTE: page 127 Reporting backgroup process status
	// Normally when a Job is running, it is necessary to periodically update
	// the user to let them know the state of progress. By default, if a Job
	// provides no information, a generic busy indicator is shown. When a Job is
	// executed, it is passed an IProgressMonitor object, which can be used to
	// notify the user of progress (and provide a way to cancel the operation).
	// A progress monitor has a number of tasks, each of which has a total unit
	// of work that it can do. For jobs that don't have a known amount of work,
	// UNKNOWN can be specified and it will be displayed in a generic busy
	// indicator.

	// When running a Job, the progress monitor can be used to indicate how much
	// work has been done. It must start with a beginTask() method—this gives
	// both the total number of work units as well as a textual name that can be
	// used to identify what's happening. If the amount of work is unknown, use
	// IProgressMonitor.UNKNOWN. The unit scale doesn't really matter; it could
	// have been 50 or 50,000. As long as the total number of work units add up,
	// and they're appropriately used, it will give the user a good idea of the
	// operation. Don't just report based on the number of lines (or tasks). If
	// there are four work items but the fifth one takes as long as the previous
	// four, then the amount of work reported needs to be balanced; for example,
	// provide a total of 8 units, with 1 unit for each of the first four and
	// then the remaining four for the fifth item. Finally, done() was called on
	// the progress monitor. This signifies that the Job has been completed, and
	// can be removed from any views that are reporting the status. This is
	// wrapped inside a finally block to ensure that the monitor is completed
	// even if the Job finishes abnormally (for example, if an exception
	// occurs).
	private Job newJobPage127() {
		return new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Preparing", 5000);
					for (int i = 0; i < 50; i++) {
						Thread.sleep(100);
						monitor.worked(100);
					}
				} catch (InterruptedException e) {
				} finally {
					monitor.done();
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						MessageDialog.openInformation(null, "Hello", "World");
					}
				});
				return Status.OK_STATUS;
			}
		};
	}

	// NOTE: page 128 Cancellation
	private Job newJobPage128() {
		return new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Preparing", 5000);
					for (int i = 0; i < 50 && !monitor.isCanceled(); i++) {
						Thread.sleep(100);
						monitor.worked(100);
					}
				} catch (InterruptedException e) {
				} finally {
					monitor.done();
				}
				if (!monitor.isCanceled()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformation(null, "Hello", "World");
						}
					});
				}
				return Status.OK_STATUS;
			}
		};
	}

	// NOTE:page 129 Using subtasks and subprocess monitors
	private Job newJobPage129() {
		return new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Preparing", 5000);
					for (int i = 0; i < 50 && !monitor.isCanceled(); i++) {
						if (i == 10) {
							monitor.subTask("Doing something");
						} else if (i == 25) {
							monitor.subTask("Doing something else");
						} else if (i == 40) {
							monitor.subTask("Nearly there");
						} else if (i == 12) {
							// NOTE: page 130 processing subtasks and sub
							// monitors
							// This will cause the process bar disappear
							// checkDozen(monitor);
							checkDozen(new SubProgressMonitor(monitor, 100));
							continue; // continue statement is used here to
										// avoid calling monitor.worked(100)
						}
						Thread.sleep(100);
						monitor.worked(100);
					}
				} catch (InterruptedException e) {
				} finally {
					monitor.done();
				}
				if (!monitor.isCanceled()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformation(null, "Hello", "World");
						}
					});
				}
				return Status.OK_STATUS;
			}
		};
	}

	// NOTE: page 130
	// The checkDozen() method took an IProgressMonitor instance, and simulated
	// a set of different tasks (with different units of work). Passing the same
	// monitor instance causes problems as the work gets missed between the two.
	// To fix this behavior, a SubProgressMonitor instance was passed in.
	// Because the SubProgressMonitor got 100 units of work from its parent,
	// when the done() method was called on the SubProgressMonitor, the parent
	// saw the completion of the 100 units of work. Importantly, this also
	// allows the child to use a completely different scale of work units and be
	// completely decoupled from the parent's use of work units.
	private void checkDozen(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Checking a dozen", 12);
			for (int i = 0; i < 12; i++) {
				Thread.sleep(10);
				monitor.worked(1);
			}
		} catch (InterruptedException e) {
		} finally {
			monitor.done();
		}
	}

	// NOTE: page 131 null process monitors and submonitors
	private void checkDozenPage131(IProgressMonitor monitor) {
		try {
			if (monitor == null)
				monitor = new NullProgressMonitor();

			monitor.beginTask("Checking a dozen", 12);
			for (int i = 0; i < 12; i++) {
				Thread.sleep(10);
				monitor.worked(1);
			}
		} catch (InterruptedException e) {
		} finally {
			monitor.done();
		}
	}

	// The NullProgressMonitor was replaced with a SubProgressMonitor with a
	// SubMonitor. To convert an arbitrary IProgessMonitor into a SubMonitor,
	// use the convert() factory method. This has the advantage of testing for
	// null (and using an embedded NullProgressMonitor if necessary) as well as
	// facilitating the construction of SubProgressMonitor instances with the
	// newChild() call. Note that the contract of SubMonitor requires the caller
	// to invoke done() on the underlying progress monitor at the end of the
	// method, so it gives an error when it's done an assignment such as monitor
	// = SubMonitor.convert(monitor) in code. Since the isCancelled() check will
	// ultimately call the parent monitor, it doesn't strictly matter whether it
	// is called on the submonitor or the parent monitor. However, if the parent
	// monitor is null, invoking it on the parent will result in a
	// NullPointerException, whereas the SubProgressMonitor will never be null.
	// In situations where there will be lots of recursive tasks, the
	// SubProgessMonitor will handle nesting better than instantiating a
	// SubProgressMonitor each time. That's because the implementation of the
	// newChild() method doesn't necessarily need to create a new SubMonitor
	// instance each time; it can keep track of how many times it has been
	// called recursively. The SubMonitor also has a setWorkRemaining() call,
	// which can be used to reset the amount of work for the outstanding
	// progress monitor. This can be useful if the job doesn't know at the start
	// how much work there is to be done, but it does become known later in the
	// process.
	private Job newJobPage131() {
		return new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					SubMonitor subMonitor = SubMonitor.convert(monitor, "Preparing", 5000);
					for (int i = 0; i < 50 && !subMonitor.isCanceled(); i++) {
						if (i == 10) {
							subMonitor.subTask("Doing something");
						} else if (i == 25) {
							subMonitor.subTask("Doing something else");
						} else if (i == 40) {
							subMonitor.subTask("Nearly there");
						} else if (i == 12) {
							checkDozen(subMonitor.newChild(100));
							continue;
						}
						Thread.sleep(100);
						subMonitor.worked(100);
					}
				} catch (InterruptedException e) {
				} finally {
					if (monitor != null)
						monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
	}

}
