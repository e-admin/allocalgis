/*
 * 
 * Created on 31-mar-2005 by juacas
 *
 * 
 */
package ejemplosgeopista;

import javax.swing.JFrame;

import com.geopista.bean.GEOPISTABean;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class testBEAN extends JFrame
{

	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JMenuBar jJMenuBar = null;
	private javax.swing.JMenu fileMenu = null;
	private javax.swing.JMenu editMenu = null;
	private javax.swing.JMenu helpMenu = null;
	private javax.swing.JMenuItem exitMenuItem = null;
	private javax.swing.JMenuItem aboutMenuItem = null;
	private javax.swing.JMenuItem cutMenuItem = null;
	private javax.swing.JMenuItem copyMenuItem = null;
	private javax.swing.JMenuItem pasteMenuItem = null;
	private javax.swing.JMenuItem saveMenuItem = null;
	private GEOPISTABean GEOPISTABean = null;
	/**
	 * This method initializes GEOPISTABean	
	 * 	
	 * @return com.geopista.bean.GEOPISTABean	
	 */    
	private GEOPISTABean getGEOPISTABean() {
		if (GEOPISTABean == null) {
			GEOPISTABean = new GEOPISTABean();
		}
		return GEOPISTABean;
	}
  	public static void main(String[] args)
	{
		testBEAN application = new testBEAN();
		application.show();
	}

	/**
	 * This is the default constructor
	 */
	public testBEAN() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getJJMenuBar());
		this.setSize(300,200);
		this.setContentPane(getJContentPane());
		this.setTitle("Application");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getGEOPISTABean(), java.awt.BorderLayout.CENTER);  // Generated
		}
		return jContentPane;
	}
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private javax.swing.JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new javax.swing.JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new javax.swing.JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new javax.swing.JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new javax.swing.JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new javax.swing.JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new javax.swing.JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					new javax.swing.JDialog(testBEAN.this, "About", true).show();
				}
			});
		}
		return aboutMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new javax.swing.JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new javax.swing.JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new javax.swing.JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new javax.swing.JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}
}
