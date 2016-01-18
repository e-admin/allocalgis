/**
 * MapasTematicosPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.ILocalGISMap;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.beans.indicadores.IndicadorEIEL;
import com.geopista.app.eiel.beans.indicadores.MapaEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.vividsolutions.jump.I18N;


public class MapasTematicosPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    
    private JLabel mapaJLabel;
    private JLabel seleccionJLabel;
	private JComboBox mapasJCBox;
    private JLabel indicadorJLabel;
	private JComboBox indicadoresEJCBox;
	private JPanel datosInformeJPanel;
	private JPanel datosSeleccionadosJPanel;
	private JPanel contenedorCentralSeleccionPanel;

	private JPanel contenedorIndicadoresPanel;

	private JButton cancelarJButton;
	private JButton mostrarMapaJButton;
	private JPanel contenedorSuperiorPestañasPanel;

	private ArrayList actionListeners= new ArrayList();


	private ILocalGISMap localgisMap;
	

	private static final Log logger = LogFactory.getLog(MapasTematicosPanel.class);
	
	
	public MapasTematicosPanel(JFrame desktop, ILocalGISMap localgisMap) throws Exception{
		aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= desktop;
        this.localgisMap=localgisMap;
        
        initComponents();
        renombrarComponentes();
	}

	private void initComponents(){		
		
		
		/*******Panel del filtro*******/
		
		contenedorSuperiorPestañasPanel= new JPanel();
		contenedorSuperiorPestañasPanel.setLayout(new java.awt.BorderLayout());
        
        //*********************************
        //Parte del medio donde aparece el nucleo y si vienen ya seleccionados los elementos
        //*********************************
		mapaJLabel = new javax.swing.JLabel();
		mapasJCBox = new javax.swing.JComboBox();
		mapasJCBox.setRenderer(new UbicacionListCellRenderer());
		seleccionJLabel = new javax.swing.JLabel();
        
		datosSeleccionadosJPanel= new JPanel();
		datosSeleccionadosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		datosSeleccionadosJPanel.add(mapaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 20));
		datosSeleccionadosJPanel.add(mapasJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 20));
		datosSeleccionadosJPanel.add(seleccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 25, 400, 20));
        
        
		contenedorCentralSeleccionPanel= new JPanel();
		contenedorCentralSeleccionPanel.setLayout(new java.awt.BorderLayout());
		contenedorCentralSeleccionPanel.add(datosSeleccionadosJPanel, BorderLayout.CENTER);

        //***************************
        //PARTE DE ABAJO DEL FILTRO
        //***************************

		indicadorJLabel = new javax.swing.JLabel();
       
        indicadoresEJCBox=new JComboBox(getIndicadoresEIEL().toArray());
        indicadoresEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
            	mostrarMapasDisponiblesJButtonActionPerformed();
            }
        });        
        indicadoresEJCBox.setRenderer(new UbicacionListCellRenderer());
        
        datosInformeJPanel= new JPanel();
        datosInformeJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosInformeJPanel.add(indicadorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 20));
        datosInformeJPanel.add(indicadoresEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 20));
        

        contenedorIndicadoresPanel= new JPanel();
        contenedorIndicadoresPanel.setLayout(new java.awt.BorderLayout());
        contenedorIndicadoresPanel.add(datosInformeJPanel, BorderLayout.CENTER);
        
        /*******Botones de Generar Informe y Salir*******/
        
        cancelarJButton= new javax.swing.JButton();
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });
        mostrarMapaJButton= new javax.swing.JButton();
        mostrarMapaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
            	mostrarMapaJButtonActionPerformed();
            }
        });
        
        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        botoneraJPanel.add(mostrarMapaJButton);
        botoneraJPanel.add(cancelarJButton);
        
        /*******Layout general*******/
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
       
    	add(contenedorIndicadoresPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 80));
    	add(contenedorCentralSeleccionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 750, 80));
    	add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 300, 50));
    	//add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 750, 50));
        
        
	}
	

	private void renombrarComponentes(){
        try{contenedorSuperiorPestañasPanel.setBorder(new javax.swing.border.TitledBorder("Filtro de Busqueda"));}catch(Exception e){}
        try{contenedorCentralSeleccionPanel.setBorder(new javax.swing.border.TitledBorder("Mapas"));}catch(Exception e){}
        try{contenedorIndicadoresPanel.setBorder(new javax.swing.border.TitledBorder("Datos del Indicador"));}catch(Exception e){}
        try{mostrarMapaJButton.setText("Mostrar Mapa");}catch(Exception e){}
        try{mapaJLabel.setText("Mapa:");}catch(Exception e){}
        try{indicadorJLabel.setText("Indicador:");}catch(Exception e){}
        try{cancelarJButton.setText("Salir");}catch(Exception e){}
	}
	
	private void cancelarJButtonActionPerformed(){
        fireActionPerformed();
    }
	
	
    private void mostrarMapaJButtonActionPerformed() {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        
            if (mapasJCBox.getSelectedIndex()==-1){
                JOptionPane.showMessageDialog(this, "No ha seleccionado ningun mapa tematico a mostrar",I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"), JOptionPane.INFORMATION_MESSAGE);	
                return;
            }
            
            String mapaSeleccionado=((MapaEIEL)mapasJCBox.getSelectedItem()).getNombreMapa();
            if (mapaSeleccionado!=null){
	            int idMapa=InitEIEL.clienteLocalGISEIEL.getIdMapa(mapaSeleccionado);
	            if (idMapa!=-1){
		            logger.info("Mapa Seleccionado:"+mapaSeleccionado+" Id:"+idMapa);
					fireActionPerformed();
		    					
		            localgisMap.showMap("workbench-properties-eiel-system.xml", idMapa);										
					LocalGISEIELUtils.setMapToReload(idMapa, true);
	            }
	            else{
	                JOptionPane.showMessageDialog(this, "No existe un mapa para el indicador seleccionado",I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"), JOptionPane.INFORMATION_MESSAGE);	
	            	
	            }
            }
            else{
                JOptionPane.showMessageDialog(this, "No existe un mapa para el indicador seleccionado",I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"), JOptionPane.INFORMATION_MESSAGE);	
            }
			
			
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,  "Error al generar el mapa temático", I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"),JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
         
    }
    
    private void mostrarMapasDisponiblesJButtonActionPerformed() {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        
            mapasJCBox.removeAllItems();
            String patronMapas=((IndicadorEIEL)indicadoresEJCBox.getSelectedItem()).getMapaSeleccionado();
            if (patronMapas!=null){
	            ArrayList listaMapas=InitEIEL.clienteLocalGISEIEL.getMapas(patronMapas,ConstantesLocalGISEIEL.Locale);
	            if (listaMapas!=null){
	            	Iterator it=listaMapas.iterator();
	            	while (it.hasNext()){
	            		MapaEIEL mapa=(MapaEIEL)it.next();
	            		mapasJCBox.addItem(mapa);
	            	}
	            	
	            }
	            else{
	                JOptionPane.showMessageDialog(this, "No existe un mapa para el indicador seleccionado",I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"), JOptionPane.INFORMATION_MESSAGE);		            	
	            }
            }
            else{
                JOptionPane.showMessageDialog(this, "No existe un mapa para el indicador seleccionado",I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"), JOptionPane.INFORMATION_MESSAGE);	
            }						
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,  "Error al generar el mapa temático", I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"),JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
         
    }
   
	
	public void actionPerformed(ActionEvent e){
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
    
   /* private void getNucleosEIEL(){
    	
    	boolean useCache=false;
    	
    	boolean existe=false;
    	if (useCache)
    		if (Estructuras.getEstructuraFromCache(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS)!=null)
    			existe=true;
    		
    	if (!existe){
			Collection<Object> c=null;
	        try {
				c = eielClient.getNucleosEIEL();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
	        //Los ordenamos alfabeticamente		
			Object[] arrayNodos= c.toArray();
	    	Arrays.sort(arrayNodos,new NodoComparatorByDenominacion());
	
	    	//Los devolvemos ordenados
	    	c.clear();
			for (int i = 0; i < arrayNodos.length; ++i) {
				LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) arrayNodos[i];
				c.add(nucleoEIEL);
			}
			
			LCGFilter.loadEstructura(c);
    	}
    	 Estructuras.cargarEstructura(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS,true);    
       
    }*/
    
    
    
    class NodoComparatorByDenominacion implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof LCGNucleoEIEL && o2 instanceof LCGNucleoEIEL){
    			LCGNucleoEIEL b1 = (LCGNucleoEIEL)o1;
    			LCGNucleoEIEL b2 = (LCGNucleoEIEL)o2;
    			String denominacion1=b1.getDenominacion();
    			String denominacion2=b2.getDenominacion();
    			
    			//Los blancos al final
    			if (denominacion1.equals(""))
    				return 1;
    			if (denominacion1.compareTo(denominacion2)>0)
    				return 1;
    			else if (denominacion1.compareTo(denominacion2)<0)
    				return -1;
    			else
    				return 0;
            	   			    	
    		}	 
        	return 0;
    	}
    }
    
    private Collection getIndicadoresEIEL(){
    	    	
		Collection<Object> c=null;
        try {
			c = InitEIEL.clienteLocalGISEIEL.getIndicadoresEIEL();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
        //Los ordenamos alfabeticamente		
		Object[] arrayNodos= c.toArray();
    	Arrays.sort(arrayNodos,new NodoComparatorByNombreIndicador());
	
    	//Los devolvemos ordenados
    	c.clear();
    	c.add(new IndicadorEIEL());
		for (int i = 0; i < arrayNodos.length; ++i) {
			IndicadorEIEL indicadorEIEL = (IndicadorEIEL) arrayNodos[i];
			c.add(indicadorEIEL);
		}
			
		return c;
       
    }
    
    class NodoComparatorByNombreIndicador implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof IndicadorEIEL && o2 instanceof IndicadorEIEL){
    			IndicadorEIEL b1 = (IndicadorEIEL)o1;
    			IndicadorEIEL b2 = (IndicadorEIEL)o2;
    			String denominacion1=b1.getNombreIndicador();
    			String denominacion2=b2.getNombreIndicador();
    			
    			//Los blancos al final
    			if (denominacion1.equals(""))
    				return 1;
    			if (denominacion1.compareTo(denominacion2)>0)
    				return 1;
    			else if (denominacion1.compareTo(denominacion2)<0)
    				return -1;
    			else
    				return 0;
            	   			    	
    		}	 
        	return 0;
    	}
    }
}
