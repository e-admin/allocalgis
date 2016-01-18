/*
 * Created on 03.07.2005
 *
 * CVS information:
 *  $Author: miriamperez $
 *  $Date: 2009/07/03 12:31:31 $
 *  $ID$
 *  $Revision: 1.1 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ChangeRasterImageStyleDialog.java,v $
 *  $Log: ChangeRasterImageStyleDialog.java,v $
 *  Revision 1.1  2009/07/03 12:31:31  miriamperez
 *  Rama única LocalGISDOS
 *
 *  Revision 1.1  2006/05/18 08:23:26  jpcastro
 *  Pirol RasterImage
 *
 *  Revision 1.3  2006/01/09 11:39:11  orahn
 *  Unbennenungen...
 *
 *  Revision 1.2  2006/01/05 14:55:42  orahn
 *  Einige grundlegende Klassen nach utilities/RasterImageSupport verschoben
 *
 *  Revision 1.1  2006/01/04 18:11:04  orahn
 *  neues Model für RasterImage-Support - erfordert u.U. noch Kern-Patch
 *
 *  Revision 1.6  2005/08/02 16:06:22  orahn
 *  +i18n - erster Wurf
 *
 *  Revision 1.5  2005/07/27 11:06:57  orahn
 *  +speed regler eingebaut (s. bug #68)
 *  +kleinere optimierungen
 *
 *  Revision 1.4  2005/07/05 16:31:33  orahn
 *  + labels für transparenz slider
 *
 *  Revision 1.3  2005/07/05 16:12:21  orahn
 *  - verschiedene Verbesserungen
 *  - verminderte Warnings
 *  - volle (und einigermaßen vernünftige) Kontrolle der Transparenzen
 *  - allgemeiner Einsatz des EnableChecks für RasterImage Layer
 *
 *  Revision 1.2  2005/07/05 15:23:48  orahn
 *  Steuerung der RasterImage Transparenzen durch Kontext-Menü
 *
 *  Revision 1.1  2005/07/04 08:42:32  orahn
 *  Vorbereitung: Dialog zum Einstellen der Transparenzen bei RasterImages
 *
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;

import pirolPlugIns.dialogs.DialogTools;
import pirolPlugIns.dialogs.OKCancelListener;
import pirolPlugIns.dialogs.OkCancelButtonPanel;
import pirolPlugIns.dialogs.ValueChecker;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;

/**
 * 
 * Dialog that show controlls to customize the appearance og a
 * RasterImage layer. 
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class ChangeRasterImageStyleDialog extends JDialog {

    private static final long serialVersionUID = -8476427365953412168L;

    private javax.swing.JPanel jContentPane = null;
	
	protected OkCancelButtonPanel okCancelPanel = new OkCancelButtonPanel();
	protected OKCancelListener okCancelListener = null;

    private RasterImageLayer rasterImageLayer = null;
    
	/**
	 * This is the default constructor
	 */
	public ChangeRasterImageStyleDialog(RasterImageLayer rasterImageLayer, Frame parent, String title, boolean modal) {
		super(parent, title, modal);
		this.rasterImageLayer = rasterImageLayer;
		initialize();
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(500,530);
		this.setContentPane(getJContentPane());
		
		okCancelListener = new OKCancelListener(this);
		this.okCancelPanel.addActionListener(this.okCancelListener);
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(15);
		this.jContentPane.setLayout(borderLayout);
		
		this.jContentPane.add(DialogTools.getPanelWithLabels(PirolPlugInMessages.getString("Change-RasterImage-Style-Dialog-intro-text"), 65), BorderLayout.NORTH); //$NON-NLS-1$
		
		JPanel controllsPanel = new RasterImageLayerControllPanel(this.rasterImageLayer);
		this.jContentPane.add(controllsPanel, BorderLayout.CENTER);
		this.jContentPane.add(this.okCancelPanel, BorderLayout.SOUTH);
		this.okCancelListener.addValueChecker((ValueChecker)controllsPanel);
		
		this.jContentPane.doLayout();
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			BorderLayout bl = new BorderLayout();
			bl.setHgap(15);
			bl.setVgap(15);
			jContentPane.setLayout(bl);
		}
		return jContentPane;
	}
	
	/**
	 *@see OKCancelListener#wasOkClicked()
	 */
    public boolean wasOkClicked() {
        return okCancelListener.wasOkClicked();
    }
}
