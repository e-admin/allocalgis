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
 * Created on 02-may-2004
 *
 */
package ejemplosgeopista;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.geopista.app.patrimonio.PatrimonioInmueblesExtendedForm;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.BooleanDomain;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;
import com.geopista.ui.autoforms.FeatureFieldPanel;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.util.java2xml.XML2Java;

import java.awt.Image;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AutoDialogExample {
	public static void main(String [] args)
	{
		
	 
	  /**
	   * Construct the domains
	   */
		StringDomain string1Domain= new StringDomain("20[.{3,8}]","Solo 20 caracteres.");
		NumberDomain pesoDomain=new NumberDomain("[0:80]##.##","prueba");
		BooleanDomain instalado1SN= new BooleanDomain("Instalado:", "Se ha instalado correctamente. S/N");
		NumberDomain peque;
		NumberDomain grande;
		TreeDomain calibresDomain = new TreeDomain("Calibres","Diámetros de tubería");
		  	calibresDomain.addChild(peque = new NumberDomain("[0.00:12.00]##.##","Tubo pequeño."));
		  	calibresDomain.addChild(grande = new NumberDomain("[12.01:45.00]","Tubo grande."));
			
		  peque.addChild(new CodedEntryDomain("tubito","Pequeño tubo") );
		  peque.addChild(new CodedEntryDomain("tubillo","pequeñillo tubillo") );
		  grande.addChild(new CodedEntryDomain("Cacho tubo","cacho cilindro") );
		  grande.addChild(new CodedEntryDomain("Tubazo","tubería padre") );
		 
		  
		  StringDomain stringDomain= new StringDomain("20[.*]","Solo 20 caracteres.");
		  BooleanDomain instaladoSN= new BooleanDomain("Instalado:", "Se ha instalado correctamente. S/N");
		  StringDomain cascajo, sillares;
		  TreeDomain materialesDomain = new TreeDomain("Materiales","Materiales de la construcción");
		  materialesDomain.addChild(cascajo = new CodedEntryDomain("Casc","Cantería."));
		  materialesDomain.addChild(sillares = new CodedEntryDomain("Sill","Sillares."));
		  
		  cascajo.addChild(new CodedEntryDomain("CF","Cascajo fino (Zahorra)") );
		  cascajo.addChild(new CodedEntryDomain("CG","Cascajo grueso (cantos)") );
		  sillares.addChild(new CodedEntryDomain("SM","Sillar de mármol. Piedra fina tipo uno") );
		  sillares.addChild(new CodedEntryDomain("SC","Sillar de cuarcita gruesa tipo luxe.") );
		  
		  CodeBookDomain cbPropiedad=new CodeBookDomain("Propietarios","Lista de propietarios de la construcción");
		  cbPropiedad.addChild(new CodedEntryDomain("PRIV","Privada"));
		  cbPropiedad.addChild(new CodedEntryDomain("PUBL","Pública"));
		  
		  
		  DateFormat fecha= DateFormat.getDateInstance();
//		  try {
//		//	Date fe=fecha.parse("10 agosto 2003");
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		  DateDomain fechaDomain= new DateDomain("[19/04/1973:NOW]dd/MM/yy","Fecha de inicio de obras");
//		  AutoFieldDomain autoFormulaDomain=new AutoFieldDomain("FORMULA:Peso*Calibre:#.###,00", "Cálculo producto");
		  AutoFieldDomain autoFormulaDomain=new AutoFieldDomain("ENV_VAR:geopista.DefaultCityId", "Id municipio.");
			 
		  AutoFieldDomain autoAreaDomain=new AutoFieldDomain("AREA:#.###,00", "Área Feature");
		/**
		   * Construct the databasemodel
		   */
		  Table	table1= new Table("Usuarios","usuarios registrados en el sistema");
		  Table	table2= new Table("Pesos","pesos y medidas");
		  Table	table3= new Table("Materiales","Materiales de la fabricación.");
		  
		  Column nameCol= new Column("Name","Nombre_Usuario",table1,string1Domain);
		  Column pesoCol= new Column("Peso","Peso del sujeto",table2,pesoDomain);
		  Column denomCaliCol= new Column("denominacion","Denominacion del calibre.",table3,calibresDomain);
		  Column calibreCol= new Column("Calibre","Calibre tubos.",table3,calibresDomain);
		  Column restauradoCol= new Column("Restaurado","Estado de la restauración.",table3,instalado1SN);
		  calibresDomain.attachColumnToLevel(calibreCol,0);
		  calibresDomain.attachColumnToLevel(denomCaliCol,1);
		
		  Column col1= new Column("Name","Nombre_Usuario",table1,stringDomain);
		  Column fechaCol= new Column("FechaInst","Fecha instalación",table2,fechaDomain);
		  Column submaterialCol= new Column("Subtipo material","Subtipo de material utilizado.",table3,materialesDomain);
		  Column materialCol= new Column("material","Material de la edificación.",table3,materialesDomain);
		  Column col5= new Column("Instalación","Reclamaciones en la instalación.",table3,instaladoSN);
		  Column col6= new Column("Propiedad","Propiedad de la construcción (col).",table3,cbPropiedad);
		  Column col7= new Column("Area", "área de la Feature",table3, autoAreaDomain);
		  Column col8= new Column("Producto", "producto",table3, autoFormulaDomain);
		  
		  materialesDomain.attachColumnToLevel(materialCol,0);
		  materialesDomain.attachColumnToLevel(submaterialCol,1);
		  
		
		  GeopistaSchema gSchema= new GeopistaSchema();
		  gSchema.addAttribute("Peso",AttributeType.FLOAT, pesoCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Calibre",AttributeType.STRING, calibreCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("DenomCalib", AttributeType.STRING, denomCaliCol,GeopistaSchema.READ_WRITE);
		 
		  gSchema.addAttribute("Instalado",AttributeType.INTEGER, restauradoCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Campo 1",AttributeType.STRING, col1,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("fecha 2",AttributeType.DATE, fechaCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("SubtipoMatt", AttributeType.STRING, submaterialCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("TipoMatt",AttributeType.STRING, materialCol,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Restaurado",AttributeType.INTEGER, col5,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Propiedad",AttributeType.STRING, col6,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Area", AttributeType.DOUBLE, col7,GeopistaSchema.READ_WRITE);
		  gSchema.addAttribute("Producto", AttributeType.DOUBLE, col8,GeopistaSchema.READ_WRITE);
//		 
		 
	  Feature gfeature= (Feature) new GeopistaFeature(gSchema);
	  
	  gfeature.setAttribute("DenomCalib", "Tubazo");
	  gfeature.setAttribute("Calibre", "1.23");
	  gfeature.setAttribute("Peso", "4.23");
	  gfeature.setAttribute("Area", "764.23");
	  gfeature.setAttribute("Campo 1","prueba de valor");
	  gfeature.setAttribute("fecha 2", new Date());
	  gfeature.setAttribute("SubtipoMatt", "SC");
	  gfeature.setAttribute("TipoMatt", "Sill");
	  gfeature.setAttribute("Instalado", new Integer(1));
	  gfeature.setAttribute("Restaurado", new Integer(0));
	  gfeature.setAttribute("Propiedad", "PRIV");
	  StringWriter stringWriterSch = new StringWriter();
	  String result;
	  try
		{
		
		Java2XML converter = new Java2XML();
		converter.write(gSchema, "GeopistaSchema", stringWriterSch);
		result=stringWriterSch.toString();
		
		
		XML2Java  reader = new XML2Java();
		GeopistaSchema gsh=(GeopistaSchema) reader.read(result,GeopistaSchema.class);
		}
	catch (Exception e)
		{
		e.printStackTrace() ;
		}

	   // prueba panel 
//	  FeatureFieldPanel featPanel=new FeatureFieldPanel(gfeature);
//	 
//	  //featPanel.setBorder(BorderFactory.createTitledBorder("feature"));
//	  frame.getContentPane().add(featPanel);
//	  frame.show();
//	  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//	  
//	  AutoDialog d= new AutoDialog(frame, "Datos de la capa %Layer% con GEOPISTA SCHEMA", true, gfeature);
//	  d.buildDialog();//ByGroupingTables();
//	  d.setSideBarDescription("Toda la estructura del formulario, incluyendo su validación y comportamiento dinámico se obtiene de la información del sistema: esquema de datos y dominios.");
////      Image im= d.getToolkit().getImage("c:/desarrollo/eclipse/geopista/src/logo_geopista.png");
//      ImageIcon icon=new ImageIcon(im);
//        
//      d.setSideBarImage(icon);
//      d.setVisible(true);
      
	  
	  // Construye una feature básica y vampirízala
//	  FeatureSchema fschema=new FeatureSchema();
//	  fschema.addAttribute("Calibre",AttributeType.STRING);
//	  fschema.addAttribute("DenomCalib", AttributeType.STRING);
//	  fschema.addAttribute("Instalado",AttributeType.INTEGER);
//	  fschema.addAttribute("Campo 1",AttributeType.STRING);
//	  fschema.addAttribute("Campo 2",AttributeType.DOUBLE);
//	  fschema.addAttribute("SubtipoMatt", AttributeType.STRING);
//	  fschema.addAttribute("TipoMatt",AttributeType.STRING);
//	  fschema.addAttribute("Restaurado",AttributeType.INTEGER);
////	 
//	  Feature feature=new BasicFeature(fschema);
//	  feature.setAttribute("DenomCalib", "Tubazo");
//	  feature.setAttribute("Calibre", "1.23");
//	  feature.setAttribute("Campo 1","prueba de valor");
//	  feature.setAttribute("Campo 2", new Double(0.0323));
//	  feature.setAttribute("SubtipoMatt", "SC");
//	  feature.setAttribute("TipoMatt", "Sill");
//	  feature.setAttribute("Instalado", new Integer(1));
//	  feature.setAttribute("Restaurado", new Integer(1));
//	  GeopistaFeature geoFeature= GeopistaSchema.vampiriceSchema(feature);

//	  AutoDialog d1= new AutoDialog(frame, "Datos de la capa %Layer% Con FEATURESCHEMA", true, geoFeature);
//	  d1.buildDialogByTables(true);
//	  
//
//        d1.setSideBarDescription("Prueba de decoración lateral.");
//       
//        ImageIcon icon=IconLoader.icon("logo_geopista.png");
//        d1.setSideBarImage(icon);
//        d1.setVisible(true);
	
//	LookAndFeelInfo[] lf= UIManager.getInstalledLookAndFeels();
	try{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	}catch(Exception e){};
	JFrame frame=new JFrame("pryuebs");
	frame.setLocation(200,300);
	frame.setSize(500,300);
	
	  frame.show();
	  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	   
    FeatureDialog fd=new FeatureDialog(frame,"prueba",true,gfeature);
   
    //fd.setFeature(gfeature);
    
    ImageIcon icon=IconLoader.icon("logo_geopista.png");
 fd.setSideBarImage(icon);
 
   fd.setSideBarDescription("<b>Toda la estructura del formulario</b>, incluyendo su validación y comportamiento dinámico se obtiene de la información del sistema:" +
   		"<ul><li>esquema de datos</li><li>dominios.</li></ul>");
    
  // featPanel.checkPanel(true);
   fd.pack();
   fd.addExtendedForm(new PatrimonioInmueblesExtendedForm());
   fd.buildDialog();
   fd.setVisible(true);
   
   exit(0);
}

	/**
	 * @param i
	 */
	private static void exit(int i) {
		// TODO Auto-generated method stub
		
	}
}
