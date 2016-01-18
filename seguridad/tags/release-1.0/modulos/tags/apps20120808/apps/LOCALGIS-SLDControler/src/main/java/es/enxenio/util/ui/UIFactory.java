/*
 * Created on 16-jun-2004
 */
package es.enxenio.util.ui;

import java.awt.Frame;

import es.enxenio.util.ui.impl.PanelContainerDialogImpl;
import es.enxenio.util.ui.impl.PanelContainerFrameImpl;

/**
 * @author luaces
 */
public class UIFactory {

	public static PanelContainer createPanelContainer(Frame frame, boolean modal) {
		if (modal) 
			return new PanelContainerDialogImpl(frame, modal);
		else
			return new PanelContainerFrameImpl(); 		
	}

}
