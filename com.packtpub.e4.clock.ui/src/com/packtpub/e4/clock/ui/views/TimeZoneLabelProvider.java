package com.packtpub.e4.clock.ui.views;

import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

//The IStyledLabelProvider is used to style the representation of the tree viewer, as used
//by the Java outline viewer for displaying the return type of the method, and by the team's
//decorator when showing when changes have occurred.
// To change the Font used by the view, the TimeZoneLabelProvider needs to implement the IFontProvider interface.
public class TimeZoneLabelProvider extends LabelProvider implements
		IStyledLabelProvider, IFontProvider {
	// NOTE page 85
	private final FontRegistry fr;
	// NOTE: page 82
	private final ImageRegistry ir;
	
	public TimeZoneLabelProvider(ImageRegistry ir, FontRegistry fr) {
		this.ir = ir;
		this.fr = fr;
	}

	public Font getFont(Object element) {
		Font italic = fr.getItalic(JFaceResources.DEFAULT_FONT);
		return italic;
	}

	// public TimeZoneLabelProvider(ImageRegistry ir) {
	// this.ir = ir;
	// }

	// NOTE: page 84
	public StyledString getStyledText(Object element) {
		String text = getText(element);
		StyledString ss = new StyledString(text);
		// This adds the time zone offset to the tree view when a node is
		// TimeZone
		if (element instanceof TimeZone) {
			int offset = -((TimeZone) element).getOffset(0);
			ss.append(" (" + offset / 3600000 + "h)",
					StyledString.DECORATIONS_STYLER);
		}
		return ss;
	}

	public String getText(Object element) {
		if (element instanceof Map) {
			// Since a TreeViewer can have multiple roots, the instanceof Map
			// test is used to
			// represent the top of the tree, called Time Zones.
			return "Time Zones";
		} else if (element instanceof Map.Entry) {
			return ((Map.Entry) element).getKey().toString();
		} else if (element instanceof TimeZone) {
			return ((TimeZone) element).getID().split("/")[1];
		} else {
			return "Unknown type: " + element.getClass();
		}
	}

	// NOTE: page 81 change tree view node icons to use Workbench's
	// ImaageRegistry to provide the folder icon
	// public Image getImage(Object element) {
	// if (element instanceof Map.Entry) {
	// return PlatformUI.getWorkbench().getSharedImages()
	// .getImage(ISharedImages.IMG_OBJ_FOLDER);
	// } else {
	// return super.getImage(element);
	// }
	// }
	// NOTE: page 82 use image registry
	public Image getImage(Object element) {
		if (element instanceof Map.Entry) {
			return ir.get("sample");
		} else if (element instanceof TimeZone) {
			return ir.get("sample");
		} else {
			return super.getImage(element);
		}
	}

}
