/**
 * TreeRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.RegulacionesEspecificasPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentosBean;
import com.gestorfip.app.planeamiento.images.IconLoader;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;

public class TreeRenderer extends DefaultTreeCellRenderer{
   
    
    private ApplicationContext aplicacion;
    private CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacionPanelBean;
    
    public TreeRenderer() {
    	
    	aplicacion= (AppContext)AppContext.getApplicationContext();
    	lstCaracteresDeterminacionPanelBean = 
    		(CaracteresDeterminacionPanelBean[])aplicacion.getBlackboard().get(ConstantesGestorFIP.LISTA_TIPOS_CARACTER_DETERMINACION);
		
    }
    
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        
        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        
        
        if (row>=0)
        {
            StringBuffer sTitle = new StringBuffer();
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();      
            
            switch (getTypeNode(value))
            {
	            case ConstantesGestorFIP.TIPO_TRAMITE:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_TRAMITE));
	                sTitle.append(((TramitePanelBean)obj).getCodigo());  
	                this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                break;
	                
	            case ConstantesGestorFIP.TIPO_DETERMINACION:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").append(((DeterminacionPanelBean)obj).getNombre());  
	                break;
	                
	            case ConstantesGestorFIP.TIPO_REGULACION_ESPECIFICA:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_REGULACION_ESPECIFICA)); 
	                sTitle.append(((RegulacionesEspecificasPanelBean)obj).getNombre());  
	                break;
	                
	            case ConstantesGestorFIP.TIPO_ENTIDAD:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_ENTIDAD)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break;
	                
	            case ConstantesGestorFIP.TIPO_DOCUMENTO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DOCUMENTO)); 
	                sTitle.append(((DocumentosBean)obj).getNombre());  
	                break;
	                
	            case ConstantesGestorFIP.TIPO_HOJA:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_HOJA)); 
	                sTitle.append(((DocumentosBean)obj).getNombre());  
	                break;
	        
	                
	            case ConstantesGestorFIP.TIPO_DETERMINACION_ACTO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_ACTOS)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
	                				append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
	                				append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_ENUNCIADO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_ENUNCIADO)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_ENUM_COMPL:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_ENUM_COMPL)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
			        if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_ACTOS:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_GRUPO_ACTOS)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_ENTIDADES:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_GRUPO_ENTIDADES)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_USOS:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_GRUPO_USOS)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
				                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
				                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_GENERICA_GRAFICA:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_NORMA_GENERICA_GRAFICA)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_USO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_NORMA_USO)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_GENERAL_LITERAL:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_NORMA_GENERAL_LITERAL)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_REGIMEN_USO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_REGIMEN_USO)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
				                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
				                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
//	            case TIPO_DETERMINACION_REGULACION:
//	                setIcon(IconLoader.icon(ICONO_DETERMINACION_REGULACION)); 
//	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
//				                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
//				                append(((DeterminacionPanelBean)obj).getNombre());
//	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
//	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
//	                }
//	                else{
//	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//	                }
//	                break;
	                
	            case ConstantesGestorFIP.TIPO_DETERMINACION_REGIMEN_ACTO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_REGIMEN_ACTO)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
				                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
				                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;         
	            case ConstantesGestorFIP.TIPO_DETERMINACION_UNIDAD:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_UNIDAD)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
				                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
				                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_USO:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_USO)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
			                append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
			                append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                break;
	            case ConstantesGestorFIP.TIPO_DETERMINACION_VALOR_REFERENCIA:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_VALOR_REFERENCIA)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
	                		append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
	                		append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                
	                break;        
	             
	            case ConstantesGestorFIP.TIPO_DETERMINACION_ADSCRIPCION:
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_DETERMINACION_ADSCRIPCION)); 
	                sTitle.append(((DeterminacionPanelBean)obj).getApartado()).append(". ").
	                		append(((DeterminacionPanelBean)obj).getEtiqueta()).append(". ").
	                		append(((DeterminacionPanelBean)obj).getNombre());
	                if(((DeterminacionPanelBean)obj).isAplicableEntidad()){
	                	this.setBorder(BorderFactory.createLineBorder(Color.red));
	                }
	                else{
	                	this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	                }
	                
	                break;       
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_GRUPO :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_GRUPO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break;
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AMBITO :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_GRUPO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CLASE_SUELO :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CLASE_SUELO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_URBANO:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CAT_SUELO_URBANO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_URBANIZABLE :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CAT_SUELO_URBANIZABLE)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break;
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SISTEMA_POLIGONAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_SISTEMA_POLIGONAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break;
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ZONA:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_ZONA)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_POLIGONAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_PROTECCION_POLIGONAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_ACTUACION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_UNIDAD_ACTUACION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SECTOR:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_SECTOR)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AREA_REPARTO:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AREA_REPARTO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_EJECUCION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_UNIDAD_EJECUCION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_POLIGONAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AFECCION_POLIGONAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break;
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ACCION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_ACCION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
                 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SISTEMA_LINEAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_SISTEMA_LINEAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ALINEACION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_ALINEACION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_LINEAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_PROTECCION_LINEAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_PUNTUAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_PROTECCION_PUNTUAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_LINEAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AFECCION_LINEAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_PUNTUAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AFECCION_PUNTUAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_NORMALIZACION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_UNIDAD_NORMALIZACION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_URBANA:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_UNIDAD_URBANA)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_OTROS_AMBITOS :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_OTROS_AMBITOS)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AREA_MOVIMIENTO :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AREA_MOVIMIENTO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PAUTA_ORDENACION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_PAUTA_ORDENACION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AJUSTE_CARTOGRAFICO:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AJUSTE_CARTOGRAFICO)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ACTUACION_AISLADA:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_ACTUACION_AISLADA)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AMB_GESTION:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_AMB_GESTION)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL :
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
            	 case ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL:
 	            	setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)); 
	                sTitle.append(((EntidadPanelBean)obj).getNombre());  
	                break; 
	                
       
	            case ConstantesGestorFIP.TIPO_NOESPECIFICADO:
	            default:                   
	                setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_NOESPECIFICADO));                
            }            
            
            if (sTitle!=null)
            {
       	
                this.setText(sTitle.toString().replaceAll("null", " "));     
            }
            
        } else {
            setIcon(IconLoader.icon(ConstantesGestorFIP.ICONO_NOESPECIFICADO));
        }
                
        return this;
    }
    
    protected int getTypeNode(Object value) {
        
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)value;        
        
        if (node.getUserObject() instanceof DeterminacionPanelBean){
        	if( lstCaracteresDeterminacionPanelBean != null){
        		for (int i=0; i<lstCaracteresDeterminacionPanelBean.length; i++){
	    			CaracteresDeterminacionPanelBean carDet = lstCaracteresDeterminacionPanelBean[i];
	    			    
	    			if(((DeterminacionPanelBean)node.getUserObject()).getCaracter() == carDet.getId()){
	    				
	    				if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_ACTOEJECUCION_VALOR) )
	    					return ConstantesGestorFIP.TIPO_DETERMINACION_ACTO;
	    	    			
	    				else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_ENUNCIADO_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_ENUNCIADO;
			    			
	    				else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_ENUNCIADOCOMPLEMENTARIO_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_ENUM_COMPL;
			    		
	    				else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_GRUPOACTOS_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_ACTOS;
			  
	    				else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_GRUPODEENTIDADES_VALOR) )
							return ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_ENTIDADES;
			        			
	    				else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_GRUPOUSOS_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_GRUPO_USOS;
			    			
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_NORMAGENERALGRAFICA_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_GENERICA_GRAFICA;
			    		
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_NORMADEUSOS) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_USO;
			    	    			
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_NORMAGENERALLITERAL_VALOR) )
							return ConstantesGestorFIP.TIPO_DETERMINACION_NORMA_GENERAL_LITERAL;
							
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_REGIMENDEUSO) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_REGIMEN_USO;
			    			
//			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_REGULACION_VALOR) )
//			    			return TIPO_DETERMINACION_REGULACION;
//			    		
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_REGIMENDEACTOS_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_REGIMEN_ACTO;
	    				
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_UNIDAD_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_UNIDAD;
			    			
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_USO_VALOR) )
							return ConstantesGestorFIP.TIPO_DETERMINACION_USO;
			    			
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_VALORREFERENCIA_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_VALOR_REFERENCIA;
	    				
			    		else if(carDet.getCodigo().equals(ConstantesGestorFIP.CARATERDETERMINACION_ADSCRIPCION_VALOR) )
			    			return ConstantesGestorFIP.TIPO_DETERMINACION_ADSCRIPCION;
			    			
			    		else
			    			return ConstantesGestorFIP.TIPO_DETERMINACION;
	    			}
	        	} 				     	
        	}
            return ConstantesGestorFIP.TIPO_DETERMINACION;
        }

        else if (node.getUserObject() instanceof TramitePanelBean){
            return ConstantesGestorFIP.TIPO_TRAMITE;
        }
        
        else if (node.getUserObject() instanceof RegulacionesEspecificasPanelBean){
            return ConstantesGestorFIP.TIPO_REGULACION_ESPECIFICA;
        }
            
        else if (node.getUserObject() instanceof EntidadPanelBean){
    	
        	if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_GRUPO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_GRUPO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AMBITO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AMBITO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CLASE_SUELO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CLASE_SUELO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CAT_SUELO_URBANO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_URBANO;
	       	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CAT_SUELO_URBANIZABLE)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_URBANIZABLE;
	       	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE;
	       	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_SISTEMA_POLIGONAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SISTEMA_POLIGONAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_ZONA )){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ZONA;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_PROTECCION_POLIGONAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_POLIGONAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_UNIDAD_ACTUACION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_ACTUACION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_SECTOR)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SECTOR;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AREA_REPARTO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AREA_REPARTO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_UNIDAD_EJECUCION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_EJECUCION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AFECCION_POLIGONAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_POLIGONAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_ACCION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ACCION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_SISTEMA_LINEAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_SISTEMA_LINEAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_ALINEACION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ALINEACION;
	       	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_PROTECCION_LINEAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_LINEAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_PROTECCION_PUNTUAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PROTECCION_PUNTUAL;
       		}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AFECCION_LINEAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_LINEAL;
       		}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AFECCION_PUNTUAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AFECCION_PUNTUAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_UNIDAD_NORMALIZACION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_NORMALIZACION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_UNIDAD_URBANA)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_UNIDAD_URBANA;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_OTROS_AMBITOS)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_OTROS_AMBITOS;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AREA_MOVIMIENTO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AREA_MOVIMIENTO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_PAUTA_ORDENACION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_PAUTA_ORDENACION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AJUSTE_CARTOGRAGICO)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AJUSTE_CARTOGRAFICO;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_ACTUACION_AISLADA)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_ACTUACION_AISLADA;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_AMB_GESTION)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_AMB_GESTION;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL;
        	}
        	else if(((EntidadPanelBean)node.getUserObject()).getCodigoValRefCU().equals(ConstantesGestorFIP.GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)){
        		return ConstantesGestorFIP.TIPO_GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL;
        	}    	
            return ConstantesGestorFIP.TIPO_ENTIDAD;
        }
        
        else if (node.getUserObject() instanceof DocumentosBean) {
        	if (((DocumentosBean)node.getUserObject()).isHoja()){
        		return ConstantesGestorFIP.TIPO_HOJA;
        	}
        	else {
        		return ConstantesGestorFIP.TIPO_DOCUMENTO;
        	}
        }
        
        else
            return ConstantesGestorFIP.TIPO_NOESPECIFICADO;
       
    }
    
}

