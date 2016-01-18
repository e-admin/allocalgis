package com.geopista.app.inventario.panel.bienesRevertibles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.panel.BienesJPanel;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;



public class BienesRevertiblesBienesJPanel extends JPanel {
	private static final Log logger = LogFactory.getLog(BienesRevertiblesBienesJPanel.class);
	private static final long serialVersionUID = 1L;
	private String locale="es_ES";
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private JFrame frame;
    BienesJPanel listaBienes;
    private String tipoBien=null;
	public BienesRevertiblesBienesJPanel (String locale, JFrame frame, BienRevertible bienRevertible, String tipoBien){
		this.locale=locale;
		this.frame=frame;
		this.tipoBien=tipoBien;
		initComponents();
		try{
			listaBienes.loadListaBienes(bienRevertible==null?null:bienRevertible.getBienes());
			}  catch (Exception e) {
			logger.error("Error al cargar el listado de bienes", e);
		}
	}
	private void initComponents(){
		addJButton= new JButton();
	    addJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
	    addJButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				pulsarAdd(tipoBien);
			}
			
		});
	    removeJButton= new JButton();
	    removeJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBorrar);
	    removeJButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				pulsarRemove();
			}
			
		});
		 listaBienes = new BienesJPanel(locale,false,false);
		this.setLayout(new BorderLayout());
		
		this.add(listaBienes,BorderLayout.NORTH);
		JPanel botonera = new JPanel();
		botonera.setLayout(new FlowLayout());
		
		botonera.add(addJButton);
		botonera.add(removeJButton);
        this.add(botonera, BorderLayout.EAST);
		
	}
	public void setEnabled(boolean enabled){
		addJButton.setEnabled(enabled);
		removeJButton.setEnabled(enabled);
	}
	private void pulsarAdd(String tipoBien){
		  MostrarBienesWizard mbw = new MostrarBienesWizard(locale);
          
		  WizardDialog d = new WizardDialog(frame,
				  aplicacion.getI18nString("inventario.bienesrevertibles.anadirbienes"), null);
		          if (tipoBien==null){
		        	  d.init(new WizardPanel[] {
		        			  new ElegirTipoBienWizard(locale),
		        			  new BuscarBienWizard(frame,locale),
		        			  mbw}
		        	  );
		        	
		           }else {
		        	   d.init(new WizardPanel[] {
			        			  new BuscarBienWizard(frame,locale,tipoBien),
			        			  mbw}
			       );
		           }
	               d.setSize(800,450);
	               //Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	               //d.setLocation(dim.width/2 - 800/2, dim.height/2 - 450/2);
	               GUIUtil.centreOnWindow(d);
	               d.setVisible(true);
	      if (mbw.getBienesSeleccionados()!=null){
	    	  listaBienes.addBienes(mbw.getBienesSeleccionados());
	      }
	}
	
	private void pulsarRemove(){
		if (listaBienes.getBienSeleccionado()!=null)
			listaBienes.deleteBienTabla((BienBean)listaBienes.getBienSeleccionado());
		
	}
	/***
	 * Devuelve la lista de bienes
	 * @return
	 */
	public Collection<BienBean> getListaBienes(){
		return listaBienes.getListaBienes();
	}
	
    private javax.swing.JButton addJButton;
    private javax.swing.JButton removeJButton;
    

}
