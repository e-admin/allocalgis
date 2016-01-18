/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 08-oct-2004 by juacas
 *
 * 
 */
package com.geopista.app.alptolocalgis.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.alptolocalgis.beans.OperacionAlp;
import com.geopista.protocol.catastro.Via;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.java2xml.XML2Java;

public class LoadDatosPanel extends JPanel
{

	private JPanel jPanelVia = null;
	private JPanel jPanelNumeroPolicia = null;

	private JLabel jLabelDatosVia = null;

	private JPanel jPanelDatosViaIdentificacion = null;

	private JLabel jLabelDatosViaIdentificacion = null;

	private JLabel jLabelViaIdentificadorLocalGIS = null;

	private JLabel jLabelViaIdentificadorALP = null;

	private JLabel jLabelViaTextIdentificadorALP = null;

	private JLabel jLabelViaTextIdentificadorLocalGIS = null;

	private JPanel jPanelDatosViaINE = null;

	private JLabel jLabelDatosViaINE = null;

	private JLabel jLabelViaINETipo = null;

	private JLabel jLabelViaINENombre = null;

	private JLabel jLabelViaINENombreCorto = null;

	private JLabel jLabelViaINECodigo = null;

	private JLabel jLabelViaTextTipoINE = null;

	private JLabel jLabelViaTextNombre = null;

	private JLabel jLabelViaTextNombreCorto = null;

	private JLabel jLabelViaTextCodigoINE = null;

	private JPanel jPanelDatosViaFechas = null;

	private JLabel jLabelDatosViaFechas = null;

	private JLabel jLabelViaFechaGabacionCierre = null;

	private JLabel jLabelViaFechaGrabacionAyto = null;

	private JLabel jLabelViaFechaEjecucion = null;

	private JLabel jLabelViaTextFechaGrabacionCierre = null;

	private JLabel jLabelViaTextFechaGrabacionAyto = null;

	private JLabel jLabelViaTextFechaEjecucion = null;

	private JPanel jPanelDatosViaCatastro = null;

	private JLabel jLabelDatosViaCatastro = null;

	private JLabel jLabelViaNombreCatastro = null;

	private JLabel jLabelViaCodigoCatastro = null;

	private JLabel jLabelViaTextNombreCatastro = null;

	private JLabel jLabelViaTextCodigoCatastro = null;

	private JLabel jLabelDatosNumeroPolicia = null;

	private JPanel jPanelDatosNumeroPoliciaIdentificacion = null;

	private JLabel jLabelDatosNumeroPoliciaIdentificacion = null;

	private JLabel jLabelNumeroPoliciaIdentificadorLocalGIS = null;

	private JLabel jLabelNumeroPoliciaIdentificadorALP = null;

	private JLabel jLabelNumeroPoliciaTextIdentificadorLocalGIS = null;

	private JLabel jLabelNumeroPoliciaTextIdentificadorALP = null;

	private JPanel jPanelDatosNumeroPoliciaINE = null;

	private JLabel jLabelDatosNumeroPoliciaINE = null;

	private JLabel jLabelNumeroPoliciaRotulo = null;

	private JLabel jLabelNumeroPoliciaCalificador = null;

	private JLabel jLabelNumeroPoliciaNumero = null;

	private JLabel jLabelNumeroPoliciaTextRotulo = null;

	private JLabel jLabelNumeroPoliciaTextCalificador = null;

	private JLabel jLabelNumeroPoliciaTextNumero = null;

	private JPanel jPanelDatosNumeroPoliciaVia = null;

	private JLabel jLabelDatosNumeroPoliciaVia = null;

	private JLabel jLabelNumeroPoliciaIdLocalGIS = null;

	private JLabel jLabelNumeroPoliciaTipoVia = null;

	private JLabel jLabelNumeroPoliciaNombreVia = null;

	private JLabel jLabelNumeroPoliciaTextIdLocalGISVia = null;

	private JLabel jLabelNumeroPoliciaTextTipoVia = null;

	private JLabel jLabelNumeroPoliciaTextNombreVia = null;

	private JPanel jPanelDatosNumeroPoliciaFechas = null;

	private JLabel jLabelDatosNumeroPoliciaFechas = null;

	private JLabel jLabelNumeroPoliciaFechaEjecucion = null;

	private JLabel jLabelNumeroPoliciaTextFechaEjecucion = null;
	
	private JPanel jPanelCabeceraPanelVia = null;
	
	private JPanel jPanelCabeceraViaIdentificacion = null;
	
	private JPanel jPanelCabeceraPanelViaINE = null;
	
	private JPanel jPanelDatosVia = null;
	
	private JPanel jPanelCabeceraPanelViaCatastro = null;
	
	private JPanel jPanelCabeceraPanelViaFechas = null;
	
	private JPanel jPanelCabeceraNumeroPolicia = null;
	
	private JPanel jPanelDatosNumeroPolicia = null;
	
	private JPanel jPanelCabeceraNumeroPoliciaIdentificacion = null;
	
	private JPanel jPanelCabeceraNumeroPoliciaINE = null;
	
	private JPanel jPanelCabeceraNumeroPoliciaVia = null;
	
	private JPanel jPanelCabeceraNumeroPoliciaFechas = null;
	
	private static AppContext app =(AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = app.getBlackboard();
    
	private JPanel getJPanelCabeceraPanelVia(){
		
		if (jPanelCabeceraPanelVia  == null){
			
			jPanelCabeceraPanelVia = new JPanel();
			jPanelCabeceraPanelVia.setLayout(new GridBagLayout());
			jPanelCabeceraPanelVia.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
			jLabelDatosVia  = new JLabel("", JLabel.CENTER);
    		jLabelDatosVia.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infovia.title"));
    		jLabelDatosVia.setForeground(Color.white);
    		jLabelDatosVia.setFont(new Font(null, Font.BOLD, 12));
    		
    		jPanelCabeceraPanelVia.add(jLabelDatosVia, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraPanelVia.setBackground(Color.gray);
    		jPanelCabeceraPanelVia.setBorder(BorderFactory.createLineBorder(Color.white));
    		jPanelCabeceraPanelVia.setBorder(BorderFactory.createRaisedBevelBorder());
    		
		}
		return jPanelCabeceraPanelVia;
	}
	
	private JPanel getJPanelDatosVia(){
		
		if (jPanelDatosVia  == null){
	    	
			jPanelDatosVia  = new JPanel();
			jPanelDatosVia.setLayout(new GridBagLayout());
			
			jPanelDatosVia.add(getJPanelCabeceraViaIdentificacion(), 
            		new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
			jPanelDatosVia.add(getJPanelDatosViaIdentificacion(), 
            		new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
			jPanelDatosVia.add(getJPanelCabeceraPanelViaINE(), 
            		new GridBagConstraints(0,3,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		    		
			jPanelDatosVia.add(getJPanelDatosViaINE(), 
            		new GridBagConstraints(0,4,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
			jPanelDatosVia.add(getJPanelCabeceraPanelViaCatastro(), 
            		new GridBagConstraints(0,5,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		    		
			jPanelDatosVia.add(getJPanelDatosViaCatastro(), 
            		new GridBagConstraints(0,6,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
			jPanelDatosVia.add(getJPanelCabeceraViaFechas(), 
            		new GridBagConstraints(0,7,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		    		
			jPanelDatosVia.add(getJPanelDatosViaFechas(), 
            		new GridBagConstraints(0,8,1,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
			
			jPanelDatosVia.setBorder(BorderFactory.createLineBorder(Color.black));
			jPanelDatosVia.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		return jPanelDatosVia;
	}
	
    private JPanel getJPanelVia(){
    	
    	if (jPanelVia  == null){
    	
    		jPanelVia = new JPanel();
    		jPanelVia.setLayout(new GridBagLayout());
    		
    		jPanelVia.add(getJPanelCabeceraPanelVia(), 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelVia.add(getJPanelDatosVia(), 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelVia;
    }
    
    private JPanel getJPanelCabeceraNumeroPolicia(){
    	
    	if (jPanelCabeceraNumeroPolicia == null){
    		
    		jPanelCabeceraNumeroPolicia  = new JPanel();
    		jPanelCabeceraNumeroPolicia.setLayout(new GridBagLayout());
    		
    		jLabelDatosNumeroPolicia   = new JLabel("", JLabel.CENTER);
    		jLabelDatosNumeroPolicia.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.title"));
    		jLabelDatosNumeroPolicia.setForeground(Color.white);
    		jLabelDatosNumeroPolicia.setFont(new Font(null, Font.BOLD, 12));
    		
    		jPanelCabeceraNumeroPolicia.add(jLabelDatosNumeroPolicia, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraNumeroPolicia.setBackground(Color.gray);
    		jPanelCabeceraNumeroPolicia.setBorder(BorderFactory.createRaisedBevelBorder());
    		
    	}
    	return jPanelCabeceraNumeroPolicia;
    }    
    
    private JPanel getJPanelDatosNumeroPolicia(){
    	
    	if (jPanelDatosNumeroPolicia == null){
    	
    		jPanelDatosNumeroPolicia  = new JPanel();
    		jPanelDatosNumeroPolicia.setLayout(new GridBagLayout());
    		
    		jPanelDatosNumeroPolicia.add(getJPanelCabeceraNumeroPoliciaIdentificacion(), 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelDatosNumeroPoliciaIdentificacion(), 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelCabeceraNumeroPoliciaINE(), 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelDatosNumeroPoliciaINE(), 
            		new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelCabeceraNumeroPoliciaVia(), 
            		new GridBagConstraints(0,4,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelDatosNumeroPoliciaVia(), 
            		new GridBagConstraints(0,5,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelCabeceraNumeroPoliciaFechas(), 
            		new GridBagConstraints(0,6,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.add(getJPanelDatosNumeroPoliciaFechas(), 
            		new GridBagConstraints(0,7,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,10,5,10),0,0));
    		
    		jPanelDatosNumeroPolicia.setBorder(BorderFactory.createLineBorder(Color.black));
    		jPanelDatosNumeroPolicia.setBorder(BorderFactory.createRaisedBevelBorder());
		
    	}
    	return jPanelDatosNumeroPolicia;
    }
    
    private JPanel getJPanelNumeroPolicia(){
    	
    	if (jPanelNumeroPolicia  == null){
    	
    		jPanelNumeroPolicia = new JPanel();
    		jPanelNumeroPolicia.setLayout(new GridBagLayout());
    		
    		jPanelNumeroPolicia.add(getJPanelCabeceraNumeroPolicia(), 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelNumeroPolicia.add(getJPanelDatosNumeroPolicia(), 
            		new GridBagConstraints(0,1,1,4,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelNumeroPolicia;
    }
    
    private JLabel getJLabelViaTextIdentificadorLocalGIS(){
    	
    	if (jLabelViaTextIdentificadorLocalGIS == null){
    		
    		jLabelViaTextIdentificadorLocalGIS  = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextIdentificadorLocalGIS.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextIdentificadorLocalGIS;
    }
    
    private JLabel getJLabelNumeroPoliciaTextIdentificadorLocalGIS(){
    	
    	if (jLabelNumeroPoliciaTextIdentificadorLocalGIS  == null){
    		
    		jLabelNumeroPoliciaTextIdentificadorLocalGIS  = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextIdentificadorLocalGIS.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextIdentificadorLocalGIS;
    }
    
    private JLabel getJLabelNumeroPoliciaTextIdLocalGISVia(){
    	
    	if (jLabelNumeroPoliciaTextIdLocalGISVia  == null){
    		
    		jLabelNumeroPoliciaTextIdLocalGISVia  = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextIdLocalGISVia.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextIdLocalGISVia;
    }
    
    private JLabel getJLabelViaTextIdentificadorALP(){
    	
    	if (jLabelViaTextIdentificadorALP == null){
    		    		
    		jLabelViaTextIdentificadorALP = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextIdentificadorALP.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextIdentificadorLocalGIS;
    }
    
    private JLabel getJLabelNumeroPoliciaTextIdentificadorALP(){
    	
    	if (jLabelNumeroPoliciaTextIdentificadorALP  == null){
    		    		
    		jLabelNumeroPoliciaTextIdentificadorALP = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextIdentificadorALP.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextIdentificadorALP;
    }
    
    private JPanel getJPanelCabeceraViaIdentificacion(){
    	
    	if (jPanelCabeceraViaIdentificacion == null){
    		
    		jPanelCabeceraViaIdentificacion  = new JPanel();
    		jPanelCabeceraViaIdentificacion.setLayout(new GridBagLayout());
    		jPanelCabeceraViaIdentificacion.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
    		jLabelDatosViaIdentificacion   = new JLabel("", JLabel.CENTER);
    		jLabelDatosViaIdentificacion.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infovia.id.title"));
    		jLabelDatosViaIdentificacion.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraViaIdentificacion.add(jLabelDatosViaIdentificacion, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraViaIdentificacion.setBackground(Color.lightGray);
    	}
    	return jPanelCabeceraViaIdentificacion;
    }
    
    private JPanel getJPanelDatosViaIdentificacion(){
    	
    	if (jPanelDatosViaIdentificacion  == null){
    		
    		jPanelDatosViaIdentificacion = new JPanel();
    		jPanelDatosViaIdentificacion.setLayout(new GridBagLayout());
    		
    		jLabelViaIdentificadorLocalGIS  = new JLabel("", JLabel.LEFT);
    		jLabelViaIdentificadorLocalGIS.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.id.idlocalgis"));
    		jLabelViaIdentificadorLocalGIS.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaIdentificadorLocalGIS.setForeground(Color.gray);
    		
    		jLabelViaIdentificadorALP   = new JLabel("", JLabel.LEFT);
    		jLabelViaIdentificadorALP.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.id.idalp"));
    		jLabelViaIdentificadorALP.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaIdentificadorALP.setForeground(Color.gray);
    		
    		jPanelDatosViaIdentificacion.add(jLabelViaIdentificadorLocalGIS, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),50,0));
    		
    		jPanelDatosViaIdentificacion.add(getJLabelViaTextIdentificadorLocalGIS(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaIdentificacion.add(getJLabelViaTextIdentificadorALP(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaIdentificacion.add(jLabelViaIdentificadorALP, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaIdentificacion.setBorder(BorderFactory.createLineBorder(Color.black));
    		jPanelDatosViaIdentificacion.setBackground(Color.white);
    	}
    	return jPanelDatosViaIdentificacion;
    }
    
    private JPanel getJPanelCabeceraNumeroPoliciaIdentificacion(){
    	
    	if (jPanelCabeceraNumeroPoliciaIdentificacion == null){
    	
    		jPanelCabeceraNumeroPoliciaIdentificacion  = new JPanel();
    		jPanelCabeceraNumeroPoliciaIdentificacion.setLayout(new GridBagLayout());
    		jPanelCabeceraNumeroPoliciaIdentificacion.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    		
    		jLabelDatosNumeroPoliciaIdentificacion = new JLabel("", JLabel.CENTER);
    		jLabelDatosNumeroPoliciaIdentificacion.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.id.title"));
    		jLabelDatosNumeroPoliciaIdentificacion.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraNumeroPoliciaIdentificacion.add(jLabelDatosNumeroPoliciaIdentificacion, 
            		new GridBagConstraints(0,0,2,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		    		
    		jPanelCabeceraNumeroPoliciaIdentificacion.setBackground(Color.lightGray);
    	}
    	return jPanelCabeceraNumeroPoliciaIdentificacion;
    }
        
    private JPanel getJPanelDatosNumeroPoliciaIdentificacion(){
    	
    	if (jPanelDatosNumeroPoliciaIdentificacion  == null){
    		
    		jPanelDatosNumeroPoliciaIdentificacion = new JPanel();
    		jPanelDatosNumeroPoliciaIdentificacion.setLayout(new GridBagLayout());
    		
    		jLabelDatosNumeroPoliciaIdentificacion    = new JLabel("", JLabel.LEFT);
    		jLabelDatosNumeroPoliciaIdentificacion.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.id.title"));
    		jLabelDatosNumeroPoliciaIdentificacion.setForeground(Color.gray);
    		jLabelDatosNumeroPoliciaIdentificacion.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaIdentificadorLocalGIS   = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaIdentificadorLocalGIS.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.id.idlocalgis"));
    		jLabelNumeroPoliciaIdentificadorLocalGIS.setForeground(Color.gray);
    		jLabelNumeroPoliciaIdentificadorLocalGIS.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaIdentificadorALP    = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaIdentificadorALP.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.id.idalp"));
    		jLabelNumeroPoliciaIdentificadorALP.setForeground(Color.gray);
    		jLabelNumeroPoliciaIdentificadorALP.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelDatosNumeroPoliciaIdentificacion.add(jLabelNumeroPoliciaIdentificadorLocalGIS, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaIdentificacion.add(getJLabelNumeroPoliciaTextIdentificadorLocalGIS(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaIdentificacion.add(jLabelNumeroPoliciaIdentificadorALP, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaIdentificacion.add(getJLabelNumeroPoliciaTextIdentificadorALP(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		    	
    		jPanelDatosNumeroPoliciaIdentificacion.setBorder(BorderFactory.createLineBorder(Color.black));
    		jPanelDatosNumeroPoliciaIdentificacion.setBackground(Color.white);
    	}
    	return jPanelDatosNumeroPoliciaIdentificacion;
    }
    
    private JLabel getJLabelViaTextTipoINE(){
    	
    	if (jLabelViaTextTipoINE == null){    		

    		jLabelViaTextTipoINE  = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextTipoINE.setFont(new Font(null, Font.BOLD, 10));
    	}
    	return jLabelViaTextTipoINE;
    }
    
    private JLabel getJLabelNumeroPoliciaTextTipoVia(){
    	
    	if (jLabelNumeroPoliciaTextTipoVia  == null){    		

    		jLabelNumeroPoliciaTextTipoVia  = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextTipoVia.setFont(new Font(null, Font.BOLD, 10));
    	}
    	return jLabelNumeroPoliciaTextTipoVia;
    }
    
    private JLabel getJLabelViaTextNombre(){
    	
    	if (jLabelViaTextNombre == null){    		

    		jLabelViaTextNombre = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextNombre.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextNombre;
    }
    
    private JLabel getJLabelNumeroPoliciaTextNombreVia(){
    	
    	if (jLabelNumeroPoliciaTextNombreVia == null){    		

    		jLabelNumeroPoliciaTextNombreVia  = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextNombreVia.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextNombreVia;
    }
    
    private JLabel getJLabelViaTextNombreCorto(){
    	
    	if (jLabelViaTextNombreCorto == null){    		

    		jLabelViaTextNombreCorto = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextNombreCorto.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextNombreCorto;
    }
    
    private JLabel getJLabelViaTextCodigoINE(){
    	
    	if (jLabelViaTextCodigoINE == null){    		

    		jLabelViaTextCodigoINE  = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextCodigoINE.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextCodigoINE;
    }
    
    private JPanel getJPanelCabeceraPanelViaINE(){
    	
    	if (jPanelCabeceraPanelViaINE == null){
    		
    		jPanelCabeceraPanelViaINE  = new JPanel();
    		jPanelCabeceraPanelViaINE.setLayout(new GridBagLayout());
    		jPanelCabeceraPanelViaINE.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
    		jLabelDatosViaINE = new JLabel("", JLabel.CENTER);
    		jLabelDatosViaINE.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infovia.ine.title"));
    		jLabelDatosViaINE.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraPanelViaINE.add(jLabelDatosViaINE, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraPanelViaINE.setBackground(Color.lightGray);    		
    		
    	}
    	return jPanelCabeceraPanelViaINE;
    }
    
    private JPanel getJPanelDatosViaINE(){
    	
    	if (jPanelDatosViaINE  == null){
    		
    		jPanelDatosViaINE = new JPanel();
    		jPanelDatosViaINE.setLayout(new GridBagLayout());
    		
    		jLabelViaINETipo = new JLabel("", JLabel.LEFT);
    		jLabelViaINETipo.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.ine.type"));
    		jLabelViaINETipo.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaINETipo.setForeground(Color.gray);
    		
    		jLabelViaINENombre = new JLabel("", JLabel.LEFT);
    		jLabelViaINENombre.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.ine.name"));
    		jLabelViaINENombre.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaINENombre.setForeground(Color.gray);
    		
    		jLabelViaINENombreCorto = new JLabel("", JLabel.LEFT);
    		jLabelViaINENombreCorto.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.ine.shortname"));
    		jLabelViaINENombreCorto.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaINENombreCorto.setForeground(Color.gray);
    		
    		jLabelViaINECodigo = new JLabel("", JLabel.LEFT);
    		jLabelViaINECodigo.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.ine.cod"));
    		jLabelViaINECodigo.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaINECodigo.setForeground(Color.gray);
    		
    		jPanelDatosViaINE.add(jLabelViaINETipo, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaINE.add(getJLabelViaTextTipoINE(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaINE.add(jLabelViaINENombre, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaINE.add(getJLabelViaTextNombre(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaINE.add(jLabelViaINENombreCorto, 
            		new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaINE.add(getJLabelViaTextNombreCorto(), 
            		new GridBagConstraints(1,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaINE.add(jLabelViaINECodigo, 
            		new GridBagConstraints(0,4,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaINE.add(getJLabelViaTextCodigoINE(), 
            		new GridBagConstraints(1,4,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		    		   		
    		jPanelDatosViaINE.setBackground(Color.white);
    		jPanelDatosViaINE.setBorder(BorderFactory.createLineBorder(Color.black));
    		
    	}
    	return jPanelDatosViaINE;
    }
    
    private JPanel getJPanelCabeceraNumeroPoliciaVia(){
    	
    	if (jPanelCabeceraNumeroPoliciaVia == null){
    		
    		jPanelCabeceraNumeroPoliciaVia  = new JPanel();
    		jPanelCabeceraNumeroPoliciaVia.setLayout(new GridBagLayout());
    		jPanelCabeceraNumeroPoliciaVia.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    		
    		jLabelDatosNumeroPoliciaVia  = new JLabel("", JLabel.CENTER);
    		jLabelDatosNumeroPoliciaVia.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.via.title"));
    		jLabelDatosNumeroPoliciaVia.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraNumeroPoliciaVia.add(jLabelDatosNumeroPoliciaVia, 
            		new GridBagConstraints(0,0,2,1,1,1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraNumeroPoliciaVia.setBackground(Color.lightGray);
    	}
    	return jPanelCabeceraNumeroPoliciaVia;
    }
    
    private JPanel getJPanelDatosNumeroPoliciaVia(){
    	
    	if (jPanelDatosNumeroPoliciaVia   == null){
    		
    		jPanelDatosNumeroPoliciaVia = new JPanel();
    		jPanelDatosNumeroPoliciaVia.setLayout(new GridBagLayout());
    		
    		jLabelNumeroPoliciaIdLocalGIS  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaIdLocalGIS.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.via.id"));
    		jLabelNumeroPoliciaIdLocalGIS.setForeground(Color.gray);
    		jLabelNumeroPoliciaIdLocalGIS.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaTipoVia  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaTipoVia.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.via.type"));
    		jLabelNumeroPoliciaTipoVia.setForeground(Color.gray);
    		jLabelNumeroPoliciaTipoVia.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaNombreVia  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaNombreVia.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.name"));
    		jLabelNumeroPoliciaNombreVia.setForeground(Color.gray);
    		jLabelNumeroPoliciaNombreVia.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelDatosNumeroPoliciaVia.add(jLabelNumeroPoliciaIdLocalGIS, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.add(getJLabelNumeroPoliciaTextIdLocalGISVia(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.add(jLabelNumeroPoliciaTipoVia, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.add(getJLabelNumeroPoliciaTextTipoVia(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.add(jLabelNumeroPoliciaNombreVia, 
            		new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.add(getJLabelNumeroPoliciaTextNombreVia(), 
            		new GridBagConstraints(1,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaVia.setBorder(BorderFactory.createLineBorder(Color.black));
    		jPanelDatosNumeroPoliciaVia.setBackground(Color.white);
    	}
    	return jPanelDatosNumeroPoliciaVia;
    }
    
    private JPanel getJPanelCabeceraNumeroPoliciaINE(){
    	
    	if (jPanelCabeceraNumeroPoliciaINE == null){
    		
    		jPanelCabeceraNumeroPoliciaINE = new JPanel();
    		jPanelCabeceraNumeroPoliciaINE .setLayout(new GridBagLayout());
    		jPanelCabeceraNumeroPoliciaINE.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    		
    		jLabelDatosNumeroPoliciaINE  = new JLabel("", JLabel.CENTER);
    		jLabelDatosNumeroPoliciaINE.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.number.title"));
    		jLabelDatosNumeroPoliciaINE.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraNumeroPoliciaINE .add(jLabelDatosNumeroPoliciaINE, 
            		new GridBagConstraints(0,0,2,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraNumeroPoliciaINE.setBackground(Color.lightGray);
    		
    	}
    	return jPanelCabeceraNumeroPoliciaINE;
    }
    
    private JPanel getJPanelDatosNumeroPoliciaINE(){
    	
    	if (jPanelDatosNumeroPoliciaINE  == null){
    		
    		jPanelDatosNumeroPoliciaINE = new JPanel();
    		jPanelDatosNumeroPoliciaINE.setLayout(new GridBagLayout());
    		    		
    		jLabelNumeroPoliciaRotulo  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaRotulo.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.number.rot"));
    		jLabelNumeroPoliciaRotulo.setForeground(Color.gray);
    		jLabelNumeroPoliciaRotulo.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaCalificador  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaCalificador.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.number.calif"));
    		jLabelNumeroPoliciaCalificador.setForeground(Color.gray);
    		jLabelNumeroPoliciaCalificador.setFont(new Font(null, Font.BOLD, 11));
    		
    		jLabelNumeroPoliciaNumero  = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaNumero.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.number.number"));
    		jLabelNumeroPoliciaNumero.setForeground(Color.gray);
    		jLabelNumeroPoliciaNumero.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelDatosNumeroPoliciaINE.add(jLabelNumeroPoliciaRotulo, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaINE.add(getJLabelNumeroPoliciaTextRotulo(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaINE.add(jLabelNumeroPoliciaCalificador, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaINE.add(getJLabelNumeroPoliciaTextCalificador(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosNumeroPoliciaINE.add(jLabelNumeroPoliciaNumero, 
            		new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaINE.add(getJLabelNumeroPoliciaTextNumero(), 
            		new GridBagConstraints(1,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		    	
    		jPanelDatosNumeroPoliciaINE.setBackground(Color.white);
    		jPanelDatosNumeroPoliciaINE.setBorder(BorderFactory.createLineBorder(Color.black));
    		
    	}
    	return jPanelDatosNumeroPoliciaINE;
    }
    
    private JLabel getJLabelViaTextNombreCatastro(){
    	
    	if (jLabelViaTextNombreCatastro == null){    		

    		jLabelViaTextNombreCatastro   = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextNombreCatastro.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextNombreCatastro;
    }
    
    private JLabel getJLabelViaTextCodigoCatastro(){
    	
    	if (jLabelViaTextCodigoCatastro == null){    		

    		jLabelViaTextCodigoCatastro  = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextCodigoCatastro.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextCodigoCatastro;
    }
    
    private JPanel getJPanelCabeceraPanelViaCatastro(){
    	
    	if (jPanelCabeceraPanelViaCatastro == null){
    		
    		jPanelCabeceraPanelViaCatastro  = new JPanel();
    		jPanelCabeceraPanelViaCatastro.setLayout(new GridBagLayout());
    		jPanelCabeceraPanelViaCatastro.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
    		jLabelDatosViaCatastro    = new JLabel("", JLabel.CENTER);
    		jLabelDatosViaCatastro.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infovia.catastro.title"));
    		jLabelDatosViaCatastro.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraPanelViaCatastro.add(jLabelDatosViaCatastro, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraPanelViaCatastro.setBackground(Color.lightGray);  
    	}
    	return jPanelCabeceraPanelViaCatastro;
    }
    
    private JPanel getJPanelDatosViaCatastro(){
    	
    	if (jPanelDatosViaCatastro  == null){
    		
    		jPanelDatosViaCatastro  = new JPanel();
    		jPanelDatosViaCatastro.setLayout(new GridBagLayout());
    		    		    		
    		jLabelViaNombreCatastro   = new JLabel("", JLabel.LEFT);
    		jLabelViaNombreCatastro.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.catastro.name"));
    		jLabelViaNombreCatastro.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaNombreCatastro.setForeground(Color.gray);
    		
    		jLabelViaCodigoCatastro    = new JLabel("", JLabel.LEFT);
    		jLabelViaCodigoCatastro.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.catastro.cod"));
    		jLabelViaCodigoCatastro.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaCodigoCatastro.setForeground(Color.gray);
    		
    		jPanelDatosViaCatastro.add(jLabelViaNombreCatastro, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaCatastro.add(getJLabelViaTextNombreCatastro(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaCatastro.add(getJLabelViaTextCodigoCatastro(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaCatastro.add(jLabelViaCodigoCatastro, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaCatastro.setBackground(Color.white);
    		jPanelDatosViaCatastro.setBorder(BorderFactory.createLineBorder(Color.black));
    	}
    	return jPanelDatosViaCatastro;
    }
    
    private JLabel getJLabelViaTextFechaGrabacionCierre(){
    	
    	if (jLabelViaTextFechaGrabacionCierre == null){    		

    		jLabelViaTextFechaGrabacionCierre  = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextFechaGrabacionCierre.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextFechaGrabacionCierre;
    }
    
    private JLabel getJLabelViaTextFechaGrabacionAyto(){
    	
    	if (jLabelViaTextFechaGrabacionAyto == null){    		

    		jLabelViaTextFechaGrabacionAyto = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextFechaGrabacionAyto.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextFechaGrabacionAyto;
    }
    
    private JLabel getJLabelViaTextFechaEjecucion(){
    	
    	if (jLabelViaTextFechaEjecucion == null){    		

    		jLabelViaTextFechaEjecucion = new JLabel("", JLabel.RIGHT);
    		jLabelViaTextFechaEjecucion.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelViaTextFechaEjecucion;
    }
    
    private JLabel getJLabelNumeroPoliciaTextFechaEjecucion(){
    	
    	if (jLabelNumeroPoliciaTextFechaEjecucion == null){    		

    		jLabelNumeroPoliciaTextFechaEjecucion  = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextFechaEjecucion.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextFechaEjecucion;
    }
    
    private JLabel getJLabelNumeroPoliciaTextRotulo(){
    	
    	if (jLabelNumeroPoliciaTextRotulo  == null){    		

    		jLabelNumeroPoliciaTextRotulo = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextRotulo.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextRotulo;
    }
    
    private JLabel getJLabelNumeroPoliciaTextCalificador(){
    	
    	if (jLabelNumeroPoliciaTextCalificador   == null){    		

    		jLabelNumeroPoliciaTextCalificador = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextCalificador.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextCalificador;
    }
    
    private JLabel getJLabelNumeroPoliciaTextNumero(){
    	
    	if (jLabelNumeroPoliciaTextNumero  == null){    		

    		jLabelNumeroPoliciaTextNumero = new JLabel("", JLabel.RIGHT);
    		jLabelNumeroPoliciaTextNumero.setFont(new Font(null, Font.BOLD, 10));
    		
    	}
    	return jLabelNumeroPoliciaTextNumero;
    }
    
    private JPanel getJPanelCabeceraViaFechas(){
    	
    	if (jPanelCabeceraPanelViaFechas == null){
    		
    		jPanelCabeceraPanelViaFechas  = new JPanel();
    		jPanelCabeceraPanelViaFechas.setLayout(new GridBagLayout());
    		jPanelCabeceraPanelViaFechas.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
    		jLabelDatosViaFechas  = new JLabel("", JLabel.CENTER);
    		jLabelDatosViaFechas.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infovia.dates.title"));
    		jLabelDatosViaFechas.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelCabeceraPanelViaFechas.add(jLabelDatosViaFechas, 
            		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraPanelViaFechas.setBackground(Color.lightGray);
    	}
    	return jPanelCabeceraPanelViaFechas;
    }
    
    private JPanel getJPanelDatosViaFechas(){
    	
    	if (jPanelDatosViaFechas  == null){
    		
    		jPanelDatosViaFechas = new JPanel();
    		jPanelDatosViaFechas.setLayout(new GridBagLayout());
    		
    		jLabelViaFechaGabacionCierre  = new JLabel("", JLabel.LEFT);
    		jLabelViaFechaGabacionCierre.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.dates.datesaveclose"));
    		jLabelViaFechaGabacionCierre.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaFechaGabacionCierre.setForeground(Color.gray);
    		
    		jLabelViaFechaGrabacionAyto = new JLabel("", JLabel.LEFT);
    		jLabelViaFechaGrabacionAyto.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.dates.datesaveay"));
    		jLabelViaFechaGrabacionAyto.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaFechaGrabacionAyto.setForeground(Color.gray);
    		
    		jLabelViaFechaEjecucion  = new JLabel("", JLabel.LEFT);
    		jLabelViaFechaEjecucion.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infovia.dates.dateexecute"));
    		jLabelViaFechaEjecucion.setFont(new Font(null, Font.BOLD, 11));
    		jLabelViaFechaEjecucion.setForeground(Color.gray);
    		
    		jPanelDatosViaFechas.add(jLabelViaFechaGabacionCierre, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaFechas.add(getJLabelViaTextFechaGrabacionCierre(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaFechas.add(jLabelViaFechaGrabacionAyto, 
            		new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaFechas.add(getJLabelViaTextFechaGrabacionAyto(), 
            		new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		
    		jPanelDatosViaFechas.add(jLabelViaFechaEjecucion, 
            		new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosViaFechas.add(getJLabelViaTextFechaEjecucion(), 
            		new GridBagConstraints(1,3,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		   		    		
    		jPanelDatosViaFechas.setBackground(Color.white);
    		jPanelDatosViaFechas.setBorder(BorderFactory.createLineBorder(Color.black));

    	}
    	return jPanelDatosViaFechas;
    }
    
    private JPanel getJPanelCabeceraNumeroPoliciaFechas(){
    	
    	if (jPanelCabeceraNumeroPoliciaFechas == null){
    		
    		jPanelCabeceraNumeroPoliciaFechas  = new JPanel();
    		jPanelCabeceraNumeroPoliciaFechas.setLayout(new GridBagLayout());
    		jPanelCabeceraNumeroPoliciaFechas.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    		
    		jLabelDatosNumeroPoliciaFechas   = new JLabel("", JLabel.CENTER);
    		jLabelDatosNumeroPoliciaFechas.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.info.infonumber.dates.title"));
    		jLabelDatosNumeroPoliciaFechas.setFont(new Font(null, Font.BOLD, 11));
    		
    		
    		jPanelCabeceraNumeroPoliciaFechas .add(jLabelDatosNumeroPoliciaFechas, 
            		new GridBagConstraints(0,0,2,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		
    		jPanelCabeceraNumeroPoliciaFechas.setBackground(Color.lightGray);
    	}
    	return jPanelCabeceraNumeroPoliciaFechas;
    }
    
    private JPanel getJPanelDatosNumeroPoliciaFechas(){
    	
    	if (jPanelDatosNumeroPoliciaFechas  == null){
    		
    		jPanelDatosNumeroPoliciaFechas  = new JPanel();
    		jPanelDatosNumeroPoliciaFechas.setLayout(new GridBagLayout());
    		
    		jLabelNumeroPoliciaFechaEjecucion   = new JLabel("", JLabel.LEFT);
    		jLabelNumeroPoliciaFechaEjecucion.setText(I18N.get("AlpToLocalGIS", 
			"alptolocalgis.editinginfo.info.infonumber.dates.dateexecute"));
    		jLabelNumeroPoliciaFechaEjecucion.setForeground(Color.gray);
    		jLabelNumeroPoliciaFechaEjecucion.setFont(new Font(null, Font.BOLD, 11));
    		
    		jPanelDatosNumeroPoliciaFechas.add(jLabelNumeroPoliciaFechaEjecucion, 
            		new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,30,5,0),0,0));
    		
    		jPanelDatosNumeroPoliciaFechas.add(getJLabelNumeroPoliciaTextFechaEjecucion(), 
            		new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,0,5,30),0,0));
    		    		
    		jPanelDatosNumeroPoliciaFechas.setBackground(Color.white);
    		jPanelDatosNumeroPoliciaFechas.setBorder(BorderFactory.createLineBorder(Color.black));

    	}
    	return jPanelDatosNumeroPoliciaFechas;
    }
    
    
    /**
     * This is the default constructor
     */
    public LoadDatosPanel(){
    	
    	Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.alptolocalgis.languages.AlpToLocalGISi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("AlpToLocalGIS",bundle);
    }
    
    private void loadJPanelVia(Via via){
    	
    	DateFormat df = DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    	
    	getJLabelViaTextIdentificadorLocalGIS().setText(via.getId());
    	getJLabelViaTextIdentificadorALP().setText(via.getIdalp());
    	getJLabelViaTextTipoINE().setText(via.getTipoViaIne());
    	getJLabelViaTextNombre().setText(via.getNombreViaIne());
    	getJLabelViaTextNombreCorto().setText(via.getNombreViaCortoIne());
    	getJLabelViaTextCodigoINE().setText(via.getCodigoIne());
    	getJLabelViaTextNombreCatastro().setText(via.getNombreCatastro());
    	getJLabelViaTextCodigoCatastro().setText(via.getCodigoCatastro());
    	
    	if (via.getFechaGrabacionCierre()!=null){
    		getJLabelViaTextFechaGrabacionCierre().setText(df.format(via.getFechaGrabacionCierre()));
    		
    	}
    	if (via.getFechaGrabacionAyto()!=null){
    		getJLabelViaTextFechaGrabacionAyto().setText(df.format(via.getFechaGrabacionAyto()));
    		
    	}
    	if (via.getFechaEjecucion()!=null){
    		getJLabelViaTextFechaEjecucion().setText(df.format(via.getFechaEjecucion()));
    		
    	}
    	
    }
    
    private void loadJPanelNumeroPolicia(NumeroPolicia numeroPolicia){
    	
    	DateFormat df = DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    	
    	getJLabelNumeroPoliciaTextIdentificadorLocalGIS().setText(numeroPolicia.getId());
    	getJLabelNumeroPoliciaTextIdentificadorALP().setText(numeroPolicia.getIdalp());
    	getJLabelNumeroPoliciaTextIdLocalGISVia().setText(numeroPolicia.getId_via());
    	getJLabelNumeroPoliciaTextTipoVia().setText(numeroPolicia.getTipovia());
    	getJLabelNumeroPoliciaTextNombreVia().setText(numeroPolicia.getNombrevia());
    	getJLabelNumeroPoliciaTextCalificador().setText(numeroPolicia.getCalificador());
    	getJLabelNumeroPoliciaTextNumero().setText(numeroPolicia.getNumero());
    	
    	if (numeroPolicia.getFechaEjecucion()!=null){
    		getJLabelNumeroPoliciaTextFechaEjecucion().setText(df.format(numeroPolicia.getFechaEjecucion()));
    		
    	}    	
    }
   
    public JPanel getLoadDatosPanel(Object value)
    {
    	OperacionAlp operacion = (OperacionAlp)value;
    	
    	try {

    		XML2Java converter = new XML2Java();    	

    		if (value != null){
    			if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA)||
    					operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)||
    					operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA)){

    				converter.removeCustomConverter(java.util.Date.class);
    				converter.addCustomConverter(java.sql.Date.class, Via.getDateCustomConverter());
    				Via via = (Via)converter.read(operacion.getXml(),Via.class);

    				blackboard.put("ElementoSeleccionado", via);
    				
    				this.setLayout(new GridBagLayout()); 
    				this.removeAll();
    				this.add(getJPanelVia(), 
    						new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
    								GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    				loadJPanelVia(via);    			

    			}
    			else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA)||
    					operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)||
    					operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA)){

    				converter.removeCustomConverter(java.util.Date.class);
    				converter.addCustomConverter(Date.class, NumeroPolicia.getDateCustomConverter());
    				NumeroPolicia numeroPolicia = (NumeroPolicia)converter.read(operacion.getXml(),NumeroPolicia.class);

    				blackboard.put("ElementoSeleccionado", numeroPolicia);
    				
    				this.setLayout(new GridBagLayout());
    				this.removeAll();
    				this.add(getJPanelNumeroPolicia(), 
    						new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
    								GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    				loadJPanelNumeroPolicia(numeroPolicia);

    			}
    		}    		

    	} catch (Exception e) {

    		e.printStackTrace();
    	}

        return this;
    }
} 
