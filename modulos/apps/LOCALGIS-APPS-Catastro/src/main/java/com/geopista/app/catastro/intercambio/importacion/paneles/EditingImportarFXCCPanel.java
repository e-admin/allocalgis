/**
 * EditingImportarFXCCPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.intercambio.importacion.utils.UtilImportacion;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelMapa;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;

public class EditingImportarFXCCPanel extends JPanel{
	
	private JFrame desktop;
	private JPanel jPanelBotoneraInfoCatastral = null;
	private JPanel jPanelBusquedaCatastral = null;
	private JPanel jPanelTablaInfoCatastral = null;
	private JPanel jPanelTablaBotonesCatastral = null;
	private TablaAsociarParcelasImportacionFXCC refCatasTablaBienesInmuebles;
	

	private PanelDatosAsociarParcelasImportarFXCC panelDatosAsociarParcelasImportarFXCC;
	
	//private PanelMapa editorMapaPanel;
	private ArrayList referenciasCatastrales = new ArrayList();
	
	
	private JButton parcelaMapaATablaJButton;
    private JButton parcelaTablaAMapeJButton;
    private JButton elimarParcelaTablaJButton;
	
 
	/**
     * Constructor de la clase. 
     *
     */
    public EditingImportarFXCCPanel()
    {
    	super();
    	UtilRegistroExp.inicializarIconos();
        inicializaPanel();

    }
    
    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     *
     * @param numExp Numero de expediente
     */
    private void inicializaPanel()
    {
    	referenciasCatastrales= new ArrayList();
    	
    	this.setLayout(new GridBagLayout());
    	
    	this.setBorder(new TitledBorder(I18N.get("Importacion","importar.fichero.fxcc.panelMapa.informacionAlfanumerica")));
    	this.add(getJPanelBusquedaCatastral(), 
    			new GridBagConstraints(0, 0, 1, 1, 1, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));
    	
    	this.add(getJPanelTablaBotonesCatastral(), 
    			new GridBagConstraints(0, 1, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));
    	
    	refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
    	
    }
    
    
    public JPanel getJPanelTablaBotonesCatastral()
    {
    	if(jPanelTablaBotonesCatastral == null){
    		
    		jPanelTablaBotonesCatastral = new JPanel(new GridBagLayout());
    		
    		jPanelTablaBotonesCatastral.setPreferredSize(new Dimension(250, 400));
    		jPanelTablaBotonesCatastral.add(getJPanelTablaInfoCatastral(),
    				new GridBagConstraints(0, 0, 1, 0, 1, 1,
                            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
    		
    		jPanelTablaBotonesCatastral.add(getJPanelBotonesAsociarTablaMapa(), 
        			new GridBagConstraints(1, 0, 1, 1, 0, 0,
                            GridBagConstraints.NORTH, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));
                            
    	}
    	
    	return jPanelTablaBotonesCatastral;
    }
    /**
     * This method initializes jPanelInfoCatastral	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelTablaInfoCatastral()
    {
    	if (jPanelTablaInfoCatastral == null)
        {
    		jPanelTablaInfoCatastral = new JPanel(new GridBagLayout());

    		refCatasTablaBienesInmuebles = new TablaAsociarParcelasImportacionFXCC("importar.fichero.fxcc.panelBusqueda.parcelas");
    		refCatasTablaBienesInmuebles.setPreferredSize(new Dimension(250, 400));
   		
    		jPanelTablaInfoCatastral.add(refCatasTablaBienesInmuebles, 
    				new GridBagConstraints(0, 0, 1, 1, 1, 1,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
    	
        }
    	
    	return jPanelTablaInfoCatastral;
    }
    
    
    /**
     * This method initializes jPanelBusquedaCatastral	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelBusquedaCatastral()
    {
    	 if (jPanelBusquedaCatastral == null)
         {
    		jPanelBusquedaCatastral = new JPanel(new GridBagLayout());
    		panelDatosAsociarParcelasImportarFXCC = new PanelDatosAsociarParcelasImportarFXCC("importar.fichero.fxcc.panelBusqueda.etiqueta");
    		
    		jPanelBusquedaCatastral.add(panelDatosAsociarParcelasImportarFXCC, 
    				new GridBagConstraints(0, 0, 0, 0, 1, 0,
    	                     GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
    	                     new Insets(0, 0, 0, 0), 0, 0));
         }
    	
    	 return jPanelBusquedaCatastral;
    }

    /**
     * Devuelve el panel.
     *
     * @return this
     */
     public JPanel getDatosPanel()
     {
         return this;
     }

    
     
     /**
      * Comprueba que la referencia que se desea asociar no este ya asociada a este expediente.
      *
      * @param refNuevas
      * @return boolean
      */    
     private boolean checkeaRefAniadiadas(ArrayList refNuevas)
     {
         for(int i = 0;i<refNuevas.size();i++)
         {
             FincaCatastro auxFinca = (FincaCatastro)refNuevas.get(i);
             for(int j= 0; j<referenciasCatastrales.size();j++)
             {
                 if(auxFinca.getRefFinca().getRefCatastral().equalsIgnoreCase
                         (((FincaCatastro)referenciasCatastrales.get(j)).getRefFinca().getRefCatastral()))
                 {
                     return true;
                 }
             }
         }
         return false;
     }
     
     
     /**
      * Evento asociado al botón de búsqueda de referencias catastrales. Abre el diálogo de búsqueda de
      * referencias catastrales.
      */
     public void linkBotonBuscarRefCatastralActionPerformed(PanelMapa editorMapaPanel)
     {
    	DialogoBuscarRefCatastralesImportarFXCC dialogo = new DialogoBuscarRefCatastralesImportarFXCC(desktop,true);
 		dialogo.setLocation(this.getWidth()-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
 		dialogo.setResizable(false);
 		dialogo.show();
         if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
         {
             if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
             {
                 referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                 refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                 FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                 editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
             }
             else
             {
                 JOptionPane.showMessageDialog(desktop,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.mensajeRefRepetida"));
             }
         }
     }
     
     /**
      * Evento asociado al botón de búsqueda de referencias catastrales por dirección. Abre un diálogo con la
      * búsqueda de referencias catastrales por dirección.
      */
     public void linkBotonBuscarRefCatastralPorDirActionPerformed(PanelMapa editorMapaPanel)
     {
    	 DialogoBuscarRefCatastralesPorDirImportarFXCC dialogo = new DialogoBuscarRefCatastralesPorDirImportarFXCC(desktop,true);
 		dialogo.setLocation(this.getWidth()-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
 		dialogo.setResizable(false);
 		dialogo.show();
         if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
         {
             if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
             {
                 referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                 refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                 FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                 editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
             }
             else
             {
                 JOptionPane.showMessageDialog(desktop,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.mensajeRefRepetida"));
             }
         }
     }
     
     /**
      * Evento asociado al botón de búsqueda de referencias por poligono. Abre un diálogo con la
      * búsqueda de referencias catastrales por poligono.
      */
     public void linkBotonBuscarRefCatastralPorPoligonoActionPerformed(PanelMapa editorMapaPanel)
     {
    	DialogoBuscarRefCatastralPorPoligonoImportarFXCC dialogo = new DialogoBuscarRefCatastralPorPoligonoImportarFXCC(desktop,true);
 		dialogo.setLocation(this.getWidth()-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
 		dialogo.setResizable(false);
 		dialogo.show();
         if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
         {
             if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
             {
                 referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                 refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                 FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                 editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
             }
             else
             {
                 JOptionPane.showMessageDialog(desktop,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.mensajeRefRepetida"));
             }
         }
     }

     /**
      * Inicializa los botones del panel
      */    
     private JPanel getJPanelBotonesAsociarTablaMapa()
     {
    	 if(jPanelBotoneraInfoCatastral == null){
    		 UtilImportacion.inicializarIconos();
    		 jPanelBotoneraInfoCatastral = new JPanel(new GridBagLayout());
      	 
	         parcelaMapaATablaJButton = new JButton();
	         parcelaMapaATablaJButton.setToolTipText(I18N.get("Importacion",
	                         "importar.fichero.fxcc.AsociarParcelas.parcelaMapaATablaJButton.hint"));
	         parcelaMapaATablaJButton.setIcon(UtilImportacion.iconoFlechaIzquierda);
	         
	         
	         parcelaTablaAMapeJButton = new JButton();
	         parcelaTablaAMapeJButton.setToolTipText(I18N.get("Importacion",
	                         "importar.fichero.fxcc.AsociarParcelas.parcelaTablaAMapeJButton.hint"));
	         parcelaTablaAMapeJButton.setIcon(UtilImportacion.iconoFlechaDerecha);
	        
	         elimarParcelaTablaJButton = new JButton();
	         elimarParcelaTablaJButton.setToolTipText(I18N.get("Importacion",
	                         "importar.fichero.fxcc.AsociarParcelas.elimarParcelaTablaJButton.hint"));
	         elimarParcelaTablaJButton.setIcon(UtilImportacion.iconoDelete);
	       
	         parcelaMapaATablaJButton.setPreferredSize(new Dimension(20, 20));
	         parcelaTablaAMapeJButton.setPreferredSize(new Dimension(20, 20));
	         elimarParcelaTablaJButton.setPreferredSize(new Dimension(20, 20));
	         
	         
	         jPanelBotoneraInfoCatastral.add(parcelaMapaATablaJButton, 
	        		 new GridBagConstraints(0, 0, 1, 1, 0, 0,
	                         GridBagConstraints.EAST, GridBagConstraints.BOTH,
	                         new Insets(0, 0, 0, 0), 0, 0));

	         jPanelBotoneraInfoCatastral.add(parcelaTablaAMapeJButton, 
	        		 new GridBagConstraints(0, 1, 1, 1, 0, 0,
	                         GridBagConstraints.EAST, GridBagConstraints.BOTH,
	                         new Insets(0, 0, 0, 0), 0, 0));

	         jPanelBotoneraInfoCatastral.add(elimarParcelaTablaJButton, 
	        		 new GridBagConstraints(0, 2, 1, 1, 0, 0,
	                         GridBagConstraints.EAST, GridBagConstraints.BOTH,
	                         new Insets(0, 0, 0, 0), 0, 0));
	
    	 }
    	 
         return jPanelBotoneraInfoCatastral;

     }
     
     
     /**
      * Evento asociado al botón eliminarParcela que permite eliminar la asociacion, de la parcela seleccionada en la
      * tabla, con el expediente que se esta trabajando.
      */
     public void eliminarParcelaTablaJButtonActionPerformed( PanelMapa editorMapaPanel, int parcelaSeleccionada)
     {  	 
         if(parcelaSeleccionada>=0)
         {
             String msg1= I18N.get("Importacion",
                             "importar.fichero.fxcc.AsociarParcelas.msg1");
             String msg2= I18N.get("Importacion",
                             "importar.fichero.fxcc.AsociarParcelas.msg2");
             String msg3= I18N.get("Importacion",
                             "importar.fichero.fxcc.AsociarParcelas.msg5");
             String msg4= I18N.get("Importacion",
                             "importar.fichero.fxcc.AsociarParcelas.msg4");
             Object[] options = {msg1, msg2};

             if (JOptionPane.showOptionDialog(this, msg3, msg4, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                     null, //don't use a custom Icon
                     options, //the titles of buttons
                     options[1])!=JOptionPane.OK_OPTION)
             {
                 return;
             }
             referenciasCatastrales.remove(parcelaSeleccionada);
             refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
             if(referenciasCatastrales.size()>0)
             {
                 FincaCatastro parcela = (FincaCatastro)referenciasCatastrales.get(0);
                                              
                 editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
             }
         }
         else
         {
             JOptionPane.showMessageDialog(this, I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.msgEliminar"));
         }
     }
     
     /**
      * Evento asociado al botón parcelaTablaAMapa que permite hacer zoom sobre la parcela de la tabla seleccionada.
      */
     public void parcelaTablaAMapeJButtonActionPerformed(PanelMapa editorMapaPanel, int parcelaSeleccionada)
     {
         if(parcelaSeleccionada>=0)
         {
             editorMapaPanel.actualizarFeatureSelection("parcelas",((FincaCatastro)referenciasCatastrales.get(parcelaSeleccionada)).getRefFinca().getRefCatastral());
         }
         else
         {
             JOptionPane.showMessageDialog(this,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.msgZoom"));
         }
     }

     
     /**
      * Evento asociado al botón parcelaMapaATabla que borra las referencias que tuviera el expediente y asocia las
      * parcelas seleccionadas en el mapa.
      */
     public void parcelaMapaATablaJButtonActionPerformed(PanelMapa editorMapaPanel)
     {
    	 
         Collection aux = editorMapaPanel.getFeaturesSelecionados();
         if(aux.size()>=1)
         {
             Iterator it = aux.iterator();
             ArrayList refCatas = new ArrayList();
             ArrayList refCatasAux = new ArrayList();
             ArrayList referenciasCatastralesFinales = new ArrayList();
             while (it.hasNext())
             {
                 Feature feature = (Feature) it.next();
                 if (feature == null)
                 {
                     continue;
                 }
                 String referencia= UtilRegistroExp.checkNull(feature.getAttribute(2));
                 refCatas.clear();
                 refCatas.add(referencia);
                 try
                 {
                     refCatasAux.clear();
                     refCatasAux = (ArrayList)ConstantesRegExp.clienteCatastro.getFincaCatastroPorReferenciaCatastral(refCatas);
                 }
                 catch(Exception e)
                 {
                     e.printStackTrace();
                 }
                 if ((refCatasAux != null) && (refCatasAux.size() > 0))
                 {
                     /** Recogemos los valores de las tablas parcelas y vias */
                     //Todo en licencias se añade a otro objeto mas, por si hay null, yo creo que no puede dar problema.
                     FincaCatastro parcela= (FincaCatastro) refCatasAux.get(0);
                     referenciasCatastralesFinales.add(parcela);

                 }
                 else
                 {
                     /** Recogemos los atributos del schema (attributeNameToIndexMap) de la feature */
                     FincaCatastro parcela = new FincaCatastro();
                     ReferenciaCatastral refCatastral = new ReferenciaCatastral(referencia);
                     parcela.setRefFinca(refCatastral);
                     DireccionLocalizacion dir = new DireccionLocalizacion();
                     dir.setNombreVia(UtilRegistroExp.checkNull(feature.getAttribute(14)));
                     dir.setTipoVia("");
                     dir.setPrimerNumero(Integer.parseInt(UtilRegistroExp.checkNull(feature.getAttribute(8))));
                     parcela.setDirParcela(dir);
                     referenciasCatastralesFinales.add(parcela);

                 }
             }
             if(!checkeaRefAniadiadas(referenciasCatastralesFinales))
             {
                 referenciasCatastrales.addAll(referenciasCatastralesFinales);
                 if(referenciasCatastrales.size()>0)
                 {
                     refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                 }
             }
             else
             {
                 JOptionPane.showMessageDialog(desktop,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.mensajeRefRepetida"));
             }            

         }
         else
         {
             JOptionPane.showMessageDialog(this,I18N.get("Importacion",
                         "importar.fichero.fxcc.AsociarParcelas.msgSeleccionMapa"));
         }
     }
     
    public JButton getParcelaMapaATablaJButton() {
 		return parcelaMapaATablaJButton;
 	}

 	public void setParcelaMapaATablaJButton(JButton parcelaMapaATablaJButton) {
 		this.parcelaMapaATablaJButton = parcelaMapaATablaJButton;
 	}

 	public JButton getParcelaTablaAMapeJButton() {
 		return parcelaTablaAMapeJButton;
 	}

 	public void setParcelaTablaAMapeJButton(JButton parcelaTablaAMapeJButton) {
 		this.parcelaTablaAMapeJButton = parcelaTablaAMapeJButton;
 	}

 	public JButton getElimarParcelaTablaJButton() {
 		return elimarParcelaTablaJButton;
 	}

 	public void setElimarParcelaTablaJButton(JButton elimarParcelaTablaJButton) {
 		this.elimarParcelaTablaJButton = elimarParcelaTablaJButton;
 	}
 	
 	public TablaAsociarParcelasImportacionFXCC getRefCatasTablaBienesInmuebles() {
		return refCatasTablaBienesInmuebles;
	}

	public void setRefCatasTablaBienesInmuebles(
			TablaAsociarParcelasImportacionFXCC refCatasTablaBienesInmuebles) {
		this.refCatasTablaBienesInmuebles = refCatasTablaBienesInmuebles;
	}
	
	public ArrayList getReferenciasCatastrales() {
		return referenciasCatastrales;
	}

	public void setReferenciasCatastrales(ArrayList referenciasCatastrales) {
		this.referenciasCatastrales = referenciasCatastrales;
	}
	
	
	
	
	public JButton getLinkBotonBuscarRefCatastral() {
 		return panelDatosAsociarParcelasImportarFXCC.getReferenciaCatastralJButton();
 	}
	
	public JButton getLinkBotonBuscarRefCatastralPorDir() {
 		return panelDatosAsociarParcelasImportarFXCC.getDirTipoViaNombreViaJButton();
 	}
	
	public JButton getLinkBotonBuscarRefCatastralPorPoligono() {
 		return panelDatosAsociarParcelasImportarFXCC.getPoligonoJButton();
 	}
	
}
