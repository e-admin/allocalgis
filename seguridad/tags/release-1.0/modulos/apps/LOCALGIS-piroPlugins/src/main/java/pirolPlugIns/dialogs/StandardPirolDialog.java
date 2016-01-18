/*
 * Created on 02.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/StandardPirolDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author <strong>Carsten Schulze</strong>
 * @author <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck
 * <br>Project: PIROL (2005)
 * <br>Subproject: Daten- und Wissensmanagement
 */
public abstract class StandardPirolDialog extends JDialog implements
        ActionListener, WindowListener {

    private int height = 10;
    private int width = 10;
    
    /**
     * Constructor.
     * @param owner the owner of this dialog.
     * @param contentPanel the content to be centered.
     * @param wspace the width
     * @param hspace the heigth
     */
    public StandardPirolDialog(JDialog owner, JPanel contentPanel, int wspace, int hspace){
        super(owner, true);
        init(contentPanel,wspace,hspace);
    }
    
    /**
     * Constructor.
     * @param owner the owner of this dialog.
     * @param contentPanel the content to be centered.
     * @param wspace the width
     * @param hspace the heigth
     */
    public StandardPirolDialog(JFrame owner, JPanel contentPanel, int wspace, int hspace){
        super(owner, true);
        init(contentPanel,wspace,hspace);
    }
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent arg0) {}
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent arg0) {}
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent arg0) {}
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent arg0) {}
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent arg0) {}
    
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent arg0) {}

    
    private void init(JPanel contentPanel, int wspace, int hspace){
        if(wspace >= 0) height = wspace;
        if(hspace >= 0) width = wspace;
        
        getContentPane().setLayout(new BorderLayout(0,0));

        getContentPane().add(Box.createRigidArea(
                new Dimension(width,height)),BorderLayout.NORTH);
        getContentPane().add(Box.createRigidArea(
                new Dimension(width,height)),BorderLayout.SOUTH);
        getContentPane().add(Box.createRigidArea(
                new Dimension(width,height)),BorderLayout.EAST);
        getContentPane().add(Box.createRigidArea(
                new Dimension(width,height)),BorderLayout.WEST);
        
        getContentPane().add(contentPanel,BorderLayout.CENTER);
    }
}
