package com.packtpub.e4.clock.ui.views;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

// NOTE: page 79
//The key to implementing a ITreeContentProvider is to remember to keep the
//implementation of the getChildren() and hasChildren() methods in sync.
//One way of doing this is to implement the hasChildren() method in terms of
//getChildren() returning an empty array, but this may be less performant if the
//getChildren() is an expensive operation.
//

// NOTE: pag 80
//To unpack the data structure, the ITreeContentProvider interface provides two key
//methods: hasChildren() and getChildren(). These allow the data structure to be
//walked on demand as the user opens and closes nodes in the tree. The rationale for having
//two separate methods is that the calculation for getChildren() may be expensive; the
//hasChildren() is used to show the expandable icon in the node, but the getChildren()
//is deferred until the user opens that node in the tree.
public class TimeZoneContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	// NOTE: page 79
	//Since a TreeViewer can have multiple roots, there is a method to get the array of
	//roots from the input element object. A bug in the JFace framework prevents the
	//getElements() argument containing its own value. It is therefore, conventional
	//to pass in an array (containing a single element) and return it. This method will be
	//identical for every TreeContentProvider that you're ever likely to write:
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		} else {
			return new Object[0];
		}
	}

	// The getChildren() implementation recurses into a Map, Collection, or Map.
	// Entry following the same pattern. Since the return of this function is an
	// Object[], the built-in functionality of Map is used to convert the
	// contents via an
	// entrySet() to an array:
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Map) {
			return ((Map) parentElement).entrySet().toArray();
		} else if (parentElement instanceof Map.Entry) {
			return getChildren(((Map.Entry) parentElement).getValue());
		} else if (parentElement instanceof Collection) {
			return ((Collection) parentElement).toArray();
		} else {
			return new Object[0];
		}
	}

	// The hasChildren() will return true, if a Map or Collection is passed to
	// it, and
	// it's not empty; otherwise, if a Map.Entry is passed, recurse. For trees
	// based on
	// nested Map or Collection, the hasChildren() method will look identical:
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Map) {
			return !((Map) element).isEmpty();
		} else if (element instanceof Map.Entry) {
			return hasChildren(((Map.Entry) element).getValue());
		} else if (element instanceof Collection) {
			return !((Collection) element).isEmpty();
		} else {
			return false;
		}

	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}
