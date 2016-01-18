/**
 * MuebleJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.app.inventario.panel.lotes.LotePanel;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 28-ago-2006
 * Time: 12:56:51
 * To change this template use File | Settings | File Templates.
 */
public class MuebleJDialog extends JDialog implements BienJDialog{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(MuebleJDialog.class);

     private JPanel datosGenerales2JPanel;
     private DatosGeneralesComunesJPanel datosGenerales1JPanel;
     private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
     private ApplicationContext aplicacion;

     private MuebleBean mueble;

     private ArrayList actionListeners= new ArrayList();
     private javax.swing.JPanel datosGeneralesJPanel;
     private javax.swing.JTabbedPane datosMuebleJTabbedPane;
     private DatosSegurosJPanel datosSegurosJPanel;
     private ObservacionesJPanel observacionesJPanel;
     private GestionDocumentalJPanel documentosJPanel;
     private DatosAmortizacionJPanel datosAmortizacionJPanel;
     private LotePanel lotePanel;
 	
     private String operacion;
     private String tipo;
     private String locale;

     /**
      * Método que genera el dialogo que muestra los datos de un bien mueble
      * @param desktop
      * @param locale
      * @param tipo del bien mueble a mostrar. En funcion de uno u otro, se mostrara el correspondiente panel de datos generales.
      */
     public MuebleJDialog(JFrame desktop, String locale, String tipo) throws Exception{
         super(desktop);
         this.aplicacion = (AppContext) AppContext.getApplicationContext();
         this.locale=locale;
         getContentPane().setLayout(new BorderLayout());
         renombrarComponentes();
         setModal(true);
         this.tipo=tipo;

         datosMuebleJTabbedPane= new javax.swing.JTabbedPane();
         datosMuebleJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         datosMuebleJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

         datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
//         /** Mostramos un panel u otro en funcion del tipo */
         if (tipo.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
            datosGenerales2JPanel= new DatosGeneralesMuebleArtisticoJPanel(desktop, locale);
         }else if (tipo.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
            datosGenerales2JPanel= new DatosGeneralesBienMuebleJPanel(locale);
         }

         /** Montamos el panel de datos generales */
         datosGeneralesJPanel= new JPanel();
         datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));
         datosGeneralesJPanel.setLayout(new BorderLayout());
         datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
         datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

         datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);

//         if (tipo.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
        	 
			// Añadmos Listener para detectar los cambios en los valores
			if (datosGenerales2JPanel instanceof DatosGeneralesMuebleArtisticoJPanel) {
				((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
						.getCosteAdquisicionJTField().addFocusListener(
								new ValorAdqFocusListener());
				((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
						.getfAdquisicionJTField().addFocusListener(
								new FechaAdqFocusListener());
			} else if (datosGenerales2JPanel instanceof DatosGeneralesBienMuebleJPanel) {
				((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
						.getCosteAdquisicionJTField().addFocusListener(
								new ValorAdqFocusListener());
				((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
						.getfAdquisicionJTField().addFocusListener(
								new FechaAdqFocusListener());
			}

             datosAmortizacionJPanel= new DatosAmortizacionJPanel(desktop, locale);
             datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab5"), datosAmortizacionJPanel);
//         }

         datosSegurosJPanel= new DatosSegurosJPanel();
         datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

         observacionesJPanel= new ObservacionesJPanel();
         datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

         documentosJPanel= new GestionDocumentalJPanel(false);
         datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
         if ((operacion!= null) && (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))){
             documentosJPanel.modificarJButtonSetEnabled(false);
         }

         
         botoneraAceptarCancelarJPanel= new BotoneraAceptarCancelarJPanel();
         botoneraAceptarCancelarJPanel.addActionListener(new java.awt.event.ActionListener(){
             public void actionPerformed(ActionEvent e){
                 botoneraAceptarCancelarJPanel_actionPerformed();
             }
         });

         addWindowListener(new java.awt.event.WindowAdapter() {
             public void windowClosing(java.awt.event.WindowEvent evt) {
                 exitForm(evt);
             }
         });

         getContentPane().add(datosMuebleJTabbedPane, BorderLayout.NORTH);
         getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
         //setSize(470, 710);
         setSize(570, 710);
         GUIUtil.centreOnWindow(this);
         addAyudaOnline();
     }
 	/**
 	 * Ayuda Online
 	 * 
 	 */
 	private void addAyudaOnline() {
 		
 		getRootPane()
 				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
 				.put(KeyStroke.getKeyStroke("F1"), "action F1");

 		getRootPane().getActionMap().put("action F1", new AbstractAction() {
 			public void actionPerformed(ActionEvent ae) {
 				int indice = datosMuebleJTabbedPane.getSelectedIndex();
 				String uriRelativa = "";
 				if (tipo.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
 	 				switch (indice) {
 	 				case 0:
 	 					uriRelativa = "/Geocuenca:Inventario:Muebles_Históricos_Artísticos#Datos_Generales";
 	 					break;
 	 				case 1:
 	 					uriRelativa = "/Geocuenca:Inventario:Muebles_Históricos_Artísticos#Datos_Seguros";
 	 					break;
 	 				case 2:
 	 					uriRelativa = "/Geocuenca:Inventario:Muebles_Históricos_Artísticos#Observaciones";
 	 					break;
 	 				case 3:
 	 					uriRelativa = "/Geocuenca:Inventario:Muebles_Históricos_Artísticos#Documentos";
 	 					break;
 	 				default:
 	 					break;
 	 				}
 		         }else if (tipo.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
  	 				switch (indice) {
 	 				case 0:
 	 					uriRelativa = "/Geocuenca:Inventario:Bienes_Muebles#Datos_Generales";
 	 					break;
 	 				case 1:
 	 					uriRelativa = "/Geocuenca:Inventario:Bienes_Muebles#Datos_Amortizaci.C3.B3n";
 	 					break;
 	 				case 2:
 	 					uriRelativa = "/Geocuenca:Inventario:Bienes_Muebles#Datos_Seguros";
 	 					break;
 	 				case 3:
 	 					uriRelativa = "/Geocuenca:Inventario:Bienes_Muebles#Observaciones";
 	 					break;
 	 				case 4:
 	 					uriRelativa = "/Geocuenca:Inventario:Bienes_Muebles#Documentos";
 	 					break;
 	 				default:
 	 					break;
 	 				}
 		         } 				
 				GeopistaBrowser.openURL(aplicacion
 						.getString("ayuda.geopista.web")
 						+ uriRelativa);
 			}
 		});

 	}

     /**
      * Método que actualiza la operacion que se esta realizando desde el panel padre
      * @param s operacion
      */
     public void setOperacion(String s){
         this.operacion= s;
     }

     /**
      * Método que carga un bien mueble (HISTORICO_ART, MUEBLE) en la ventana de dialogo
      * @param bien a cargar
      */
     public void load(MuebleBean bien, boolean editable) throws Exception {
         setMueble(bien);
         if(operacion == null) return;
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
             bien.setLote(new Lote());
             lotePanel= new LotePanel(bien.getLote(),true);
             datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.lote"), lotePanel);
         }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
             if (bien.getLote()!=null){
                 lotePanel= new LotePanel(bien.getLote(),true);
                 lotePanel.setEnabled(false);
                 datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.lote"), lotePanel);
             }
         }
         datosGenerales1JPanel.setEnabled(editable);
         datosGenerales2JPanel.setEnabled(editable);
         datosGenerales1JPanel.load(bien);

         if (bien.getTipo()!= null && (bien.getTipo().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)))
            ((DatosGeneralesMuebleArtisticoJPanel)datosGenerales2JPanel).load(bien);
         else if (bien.getTipo()!= null && (bien.getTipo().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)))
            ((DatosGeneralesBienMuebleJPanel)datosGenerales2JPanel).load(bien);

         /** cargamos los datos de amortizacion */
         if (datosAmortizacionJPanel != null){
             datosAmortizacionJPanel.load(bien);
             datosAmortizacionJPanel.setEnabled(editable);
         }

         /** cargamos el seguro */
         datosSegurosJPanel.load(bien);
         datosSegurosJPanel.setEnabledDatos(false);
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) datosSegurosJPanel.setEnabledBotonera(false);
         else{
             datosSegurosJPanel.addJButtonSetEnabled(true);
             datosSegurosJPanel.buscarJButtonSetEnabled(true);
             datosSegurosJPanel.editarJButtonSetEnabled(bien!=null && bien.getSeguro()!=null);
             datosSegurosJPanel.borrarJButtonSetEnabled(bien!=null && bien.getSeguro()!=null);
         }


         /** cargamos las observaciones */
         observacionesJPanel.load(bien);
         observacionesJPanel.setEnabled(editable);
         observacionesJPanel.setOperacion(operacion);
		
         /** cargamos los documentos */
         documentosJPanel.load(bien);
         documentosJPanel.setEnabled(editable);
         
         /**añadimos los bienes revertibles*/
 		if (bien.getBienesRevertibles()!=null && bien.getBienesRevertibles().size()>0){
         	 JTabbedPane auxJPanel=new JTabbedPane();
         	datosMuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
         	 for (Iterator <BienRevertible>it=bien.getBienesRevertibles().iterator();it.hasNext();)
 	       	 {
 	       		 BienesRevertiblesPanel bienesJPanel= new BienesRevertiblesPanel((BienRevertible)it.next(),locale);
 	       		 bienesJPanel.setEnabled(false);
 	       		 auxJPanel.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienrevertible") , bienesJPanel);
 	       	 }
         }
     }

     public void renombrarComponentes(){
         try{datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));}catch(Exception e){}
     }

     public void botoneraAceptarCancelarJPanel_actionPerformed(){
         if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
            (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false))
             mueble= null;
         else{
             datosGenerales1JPanel.actualizarDatosGeneralesComunes((BienBean)mueble);
             if (((MuebleBean)mueble).getTipo()!= null && (((MuebleBean)mueble).getTipo().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)))
                ((DatosGeneralesMuebleArtisticoJPanel)datosGenerales2JPanel).actualizarDatosGenerales((MuebleBean)mueble);
             else if ((((MuebleBean)mueble).getTipo()!= null && (((MuebleBean)mueble).getTipo().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES))))
                ((DatosGeneralesBienMuebleJPanel)datosGenerales2JPanel).actualizarDatosGenerales((MuebleBean)mueble);

             if (datosAmortizacionJPanel != null)
                datosAmortizacionJPanel.actualizarDatos((BienBean)mueble);

             datosSegurosJPanel.actualizarDatos((BienBean)mueble);
             observacionesJPanel.actualizarDatos((BienBean)mueble);
             documentosJPanel.actualizarDatos((BienBean)mueble);
             if (lotePanel!=null)
            	 ((MuebleBean)mueble).setLote(lotePanel.getLote());
         }
         fireActionPerformed();
     }
     public LotePanel getLotePanel(){
    	 return lotePanel;
     }

     /**
      * Método que abre una ventana de confirmacion sobre la operacion que se esta llevando a cabo
      */
     private boolean confirmOption(){
         int ok= -1;
         ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.optionpane.tag1"), aplicacion.getI18nString("inventario.optionpane.tag2"), JOptionPane.YES_NO_OPTION);
         if (ok == JOptionPane.NO_OPTION){
             return false;
         }
         return true;
     }

     public void addActionListener(ActionListener l) {
         this.actionListeners.add(l);
     }

     public void removeActionListener(ActionListener l) {
         this.actionListeners.remove(l);
     }

     private void fireActionPerformed() {
         for (Iterator i = actionListeners.iterator(); i.hasNext();) {
             ActionListener l = (ActionListener) i.next();
             l.actionPerformed(new ActionEvent(this, 0, null));
         }
     }

     public void setMueble(MuebleBean mueble){
         this.mueble= mueble;
     }

     public Object getMueble(){
         return mueble;
     }

    public void setDatosGenerales2JPanel(JPanel panel){
        this.datosGenerales2JPanel= panel;
    }

    public JPanel getDatosGenerales2JPanel(){
        return datosGenerales2JPanel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        setMueble(null);
        fireActionPerformed();
    }

    public GestionDocumentalJPanel getDocumentosJPanel() {
        return documentosJPanel;
    }

    public void pmsChecked(){
    	try{((DatosGeneralesMuebleArtisticoJPanel)datosGenerales2JPanel).patrimonioChecked();}catch(Exception ex){}
    }
	@Override
	public BienBean getBien() {
		return mueble;
	}


	class ValorAdqFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

		}

		@Override
		public void focusLost(FocusEvent e) {
			Number valor = 0.0;
			try {
				// Cambiamos el valor de adquisicion
				if (datosGenerales2JPanel instanceof DatosGeneralesMuebleArtisticoJPanel) {
					if (((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
							.getCosteAdquisicionJTField().getText() != null
							&& !((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
									.getCosteAdquisicionJTField().getText()
									.equalsIgnoreCase("")) {
						valor = ((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
								.getCosteAdquisicionJTField().getNumber();
					}
				} else if (datosGenerales2JPanel instanceof DatosGeneralesBienMuebleJPanel) {
					if (((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
							.getCosteAdquisicionJTField().getText() != null
							&& !((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
									.getCosteAdquisicionJTField().getText()
									.equalsIgnoreCase("")) {
						valor = ((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
								.getCosteAdquisicionJTField().getNumber();
					}
				}

				datosAmortizacionJPanel.setCosteAdquisicion((Double) valor);
				if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
					datosAmortizacionJPanel.calcularTotalAmortizado(null,mueble.getFechaAdquisicion(), mueble.getCosteAdquisicion(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	class FechaAdqFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

		}

		@Override
		public void focusLost(FocusEvent e) {
			String valor = null;
			try {

				// Cambiamos la fecha de adquisicion
				if (datosGenerales2JPanel instanceof DatosGeneralesMuebleArtisticoJPanel) {
					if (((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
							.getfAdquisicionJTField().getText() != null
							&& !((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
									.getfAdquisicionJTField().getText()
									.equalsIgnoreCase("")) {

						valor = ((DatosGeneralesMuebleArtisticoJPanel) datosGenerales2JPanel)
								.getfAdquisicionJTField().getText().trim();
						datosAmortizacionJPanel
								.setFechaAdquisicion(Constantes.df.parse(valor));
						if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
							datosAmortizacionJPanel.calcularTotalAmortizado(null,mueble.getFechaAdquisicion(), mueble.getCosteAdquisicion(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
					}
				} else if (datosGenerales2JPanel instanceof DatosGeneralesBienMuebleJPanel) {
					if (((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
							.getfAdquisicionJTField().getText() != null
							&& !((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
									.getfAdquisicionJTField().getText()
									.equalsIgnoreCase("")) {
						valor = ((DatosGeneralesBienMuebleJPanel) datosGenerales2JPanel)
								.getfAdquisicionJTField().getText().trim();
						datosAmortizacionJPanel
								.setFechaAdquisicion(Constantes.df.parse(valor));
						if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
							datosAmortizacionJPanel.calcularTotalAmortizado(null,mueble.getFechaAdquisicion(), mueble.getCosteAdquisicion(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
					}
				}

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

}
