package com.geopista.app.reports;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.geopista.app.AppContext;
import com.geopista.app.help.HelpLoader;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.geopista.app.reports.GeopistaObjetoOrdenCampos;

 




public class GeopistaGeneradorListadosUnionesTablas extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
  
  private Connection con = null; 
  
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private JPanel jPanel1 = new JPanel();
//  private GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();
  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();
  private JComboBox cmbTabla = new JComboBox();
  private JLabel lblCampo = new JLabel();
  private JComboBox cmbCampo = new JComboBox();
  private JPanel jPanel2 = new JPanel();
  private JLabel lblNombreTabla = new JLabel();
  private JLabel lblCamposInforme = new JLabel();
  private JLabel lblTablaOrigen = new JLabel();
  private JPanel jPanel4 = new JPanel();
  private ArrayList aCamposOrdenados;

  private DefaultTableModel model = new DefaultTableModel()
    {
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }
    };
  
  private JTable tablaCampos = new JTable(model);

  private JButton btnQuitarUnion = new JButton();
  private int filas = 0;

  public GeopistaGeneradorListadosUnionesTablas()
  {
    try
    {
       //blackboardInformes.put("hola","Saludos desde el primer panel");
       jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  
  private void jbInit() throws Exception
  {
    con=getDBConnection();
    this.setLayout(null);
    this.setSize(new Dimension(760, 611));
    jPanel1.setBounds(new Rectangle(5, 5, 750, 580));
    jPanel1.setLayout(null);
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
     DefaultListModel modeloList = new DefaultListModel();
    //Cargamos la lista de capas
     ArrayList capas = geopistaListados.capasGeopista();
     Iterator i = capas.iterator();
    cmbTabla.setBounds(new Rectangle(75, 35, 210, 20));

    try {
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      
    HelpSet hs = HelpLoader.getHelpSet(helpHS);
    HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda
    hb.enableHelpKey(this,"generadorInformeUnionesTablas",hs);
    }catch (Exception e){e.printStackTrace();}
    
    lblCampo.setText(aplicacion.getI18nString("generador.app.reports.uniones.campo"));
    lblCampo.setBounds(new Rectangle(15, 65, 50, 15));
    cmbCampo.setBounds(new Rectangle(75, 60, 210, 20));
    jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel2.setLayout(null);
    
    lblNombreTabla.setText(aplicacion.getI18nString("generador.app.reports.uniones.tabla"));
    lblNombreTabla.setBounds(new Rectangle(15, 35, 40, 20));
    
    lblCamposInforme.setText(aplicacion.getI18nString("generador.app.reports.uniones.relaciones.tablas"));
    lblTablaOrigen.setText(aplicacion.getI18nString("generador.app.reports.uniones.tabla.origen"));
    lblTablaOrigen.setBounds(new Rectangle(15, 10, 95, 25));


    
    jPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel4.setLayout(null);
    tablaCampos.setBounds(new Rectangle(5, 5, 525, 170));
    //btnQuitarUnion.setText(aplicacion.getI18nString("generador.app.reports.uniones.eliminar"));
    btnQuitarUnion.setIcon(IconLoader.icon("borrar_campo.gif"));
    
     while (i.hasNext())
     {
        modeloList.addElement(i.next());
     }
        


     //Creamos la lista con el modelo anteriormente definido      
    jPanel1.setSize(new Dimension(750, 500));

    
    
    //Ponemos a mano los Módulos , prueba

    //Ponemos a mano la horientacion
    
    
    jPanel2.add(btnAnadirUnion, null);
    jPanel2.add(cmbCamposDestino, null);
    jPanel2.add(cmbTablaDestino, null);
    jPanel2.add(lblTablaDestino, null);
    jPanel2.add(lblTablaOrigen, null);
    jPanel2.add(lblNombreTabla, null);
    jPanel2.add(cmbTabla, null);
    jPanel2.add(cmbCampo, null);
    jPanel2.add(lblCampo, null);
    jPanel1.add(lblImagen, null);
    jPanel1.add(jSeparator1, null);
    jPanel1.add(chkgrupo, null);
    jPanel1.add(lblTituloGrupo, null);
    jPanel1.add(txtTitulo, null);
    jPanel1.add(lblGrupo, null);
    jPanel1.add(cmbGrupo, null);
    jPanel1.add(jPanel4, null);
    jPanel1.add(lblCamposInforme, null);
    jPanel1.add(jPanel2, null);
    jPanel1.add(btnQuitarUnion, null);
    this.add(jPanel1, null);

  

      tablaCampos.addMouseListener(new java.awt.event.MouseAdapter() { 
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		int x;
      		int y;
      		String valor;
      		String p1;
      		String p2;
      		String p3;
      		String p4;
      		x = tablaCampos.getSelectedRow();
      		valor = (String) tablaCampos.getValueAt(x,1);
      		String[] division=valor.split("\\.");
      		String[] division2=division[1].split("=");
      			p1 = division[0];
      			p4=division[2];
      			p2=division2[0];
      			p3=division2[1];
      	
      	cmbTablaDestino.setSelectedItem((String) p3.trim());
      	cmbCamposDestino.setSelectedItem((String) p4.trim());
      	cmbTabla.setSelectedItem((String) p1.trim());
      	cmbCampo.setSelectedItem((String) p2.trim());
      	
      	}
      });
      cmbTabla.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
          //Recogemos el tipo de inmuebles
          //JComboBox combo =(JComboBox) evt.getSource();

              cmbCampo.removeAllItems();
              String  nombre = (String) cmbTabla.getSelectedItem();
              ArrayList capas =  geopistaListados.camposDeUnaTabla(nombre);
              Iterator i = capas.iterator();
              while (i.hasNext()){
                  String Campo = (String)i.next();
                  cmbCampo.addItem(Campo);
              }
     
 

            }
         });  
 


   // añadimos en el de operacion la suma y longitud

   // en el cruce de las tablas Contener e Interseccion

    /** Creamos la tabla vacía  **/ 
    
     
    // Creamos las columnas

   JTableHeader header = tablaCampos.getTableHeader();
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen.setBounds(new Rectangle(10, 10, 110, 500));
    this.setBounds(new Rectangle(10, 10, 750, 600));
    jSeparator1.setBounds(new Rectangle(0, 340, 740, 2));
    lblCamposInforme.setBounds(new Rectangle(140, 10, 180, 15));
    jPanel2.setBounds(new Rectangle(140, 30, 590, 90));
    jPanel4.setBounds(new Rectangle(145, 155, 540, 180));
    btnQuitarUnion.setBounds(new Rectangle(700, 300, 35, 30));
    chkgrupo.setBounds(new Rectangle(145, 355, 180, 25));
    lblGrupo.setBounds(new Rectangle(145, 380, 85, 15));
    cmbGrupo.setBounds(new Rectangle(245, 380, 410, 20));
    lblTituloGrupo.setBounds(new Rectangle(145, 410, 95, 15));
    txtTitulo.setBounds(new Rectangle(245, 405, 410, 20));
    chkgrupo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkgrupo_actionPerformed(e);
        }
      });
   
    txtTitulo.setEnabled(false);
    cmbGrupo.setEnabled(false);
    chkgrupo.setText(aplicacion.getI18nString("generador.app.reports.agrupar"));
    lblTituloGrupo.setText(aplicacion.getI18nString("generador.app.reports.titulo.grupo"));
    txtTitulo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtTitulo_actionPerformed(e);
        }
      });
    lblGrupo.setText(aplicacion.getI18nString("generador.app.reports.agrupar.por"));
    btnAnadirUnion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAnadirUnion_actionPerformed(e);
        }
      });


    
     
    
    
    btnAnadirUnion.setBounds(new Rectangle(545, 50, 35, 30));
    btnAnadirUnion.setIcon(IconLoader.icon("anadir_campo.gif"));
    cmbCamposDestino.setBounds(new Rectangle(295, 60, 205, 20));
    cmbTablaDestino.setBounds(new Rectangle(295, 35, 205, 20));
    lblTablaDestino.setBounds(new Rectangle(320, 10, 180, 25));
    lblTablaDestino.setText(aplicacion.getI18nString("generador.app.reports.uniones.tabla.destino"));

    cmbTablaDestino.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
          //Recogemos el tipo de inmuebles
          //JComboBox combo =(JComboBox) evt.getSource();

              cmbCamposDestino.removeAllItems();
              String  nombre = (String) cmbTablaDestino.getSelectedItem();
              ArrayList capas = geopistaListados.camposDeUnaTabla(nombre);
              Iterator i = capas.iterator();
              while (i.hasNext()){
                  String Campo = (String)i.next();
                  cmbCamposDestino.addItem(Campo);
              }
     
 

            }
         });  
 
   
   
    btnQuitarUnion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
  
   jPanel4.setLayout(new BorderLayout());
    // Add header in NORTH slot
    jPanel4.add(tablaCampos, null);
    jPanel4.add(header, BorderLayout.NORTH);
    // Add table itself to CENTER slot
    jPanel4.add(tablaCampos, BorderLayout.CENTER);
    model.addColumn(aplicacion.getI18nString("generador.app.reports.uniones.union"));
   model.addColumn(aplicacion.getI18nString("generador.app.reports.uniones.etiqueta"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.uniones.campo"));

     //quitamos el orden y el campo
     tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(1));

  }
 
  public void enteredFromLeft(Map dataMap)
  {
      // Pasamos el list del blackBoard al combo
      cmbTabla.removeAllItems();
      cmbTablaDestino.removeAllItems();
      ArrayList listaCampos = new ArrayList();
      GeopistaObjetoInformeCampos objetoCampos = new GeopistaObjetoInformeCampos();
      
      listaCampos = (ArrayList) blackboardInformes.get("listaCampos");
      
      
      
      Iterator i = listaCampos.iterator();
      
      while (i.hasNext())
        {
          
          objetoCampos = (GeopistaObjetoInformeCampos) i.next();
          String tabla = objetoCampos.getTabla();

          if (tabla.indexOf("/")!=-1){
             StringTokenizer tipoDescripcionToken = new StringTokenizer(tabla, "/");
             String tabla1  = tipoDescripcionToken.nextToken();
             String tabla2 = tipoDescripcionToken.nextToken();

             if (!tabla.equals("")){
                 if (cmbTabla.getItemCount()==0){
                  if (tabla1.equals(tabla2)){
                    cmbTabla.addItem(tabla1);
                  }else{
                    cmbTabla.addItem(tabla1);
                      cmbTabla.addItem(tabla2);
                  }
                 }else{
                     boolean encontrado=false;
                      for (int di=0; di<cmbTabla.getItemCount();di++)
                      {
                        if (cmbTabla.getItemAt(di).equals(tabla1)){
                        encontrado=true;
                        break;
                        }
                      }
                      if (!encontrado){
                        cmbTabla.addItem((String) tabla1);
                        cmbTablaDestino.addItem((String) tabla1);
                
                      }
                      encontrado=false;
                      for (int di=0; di<cmbTabla.getItemCount();di++)
                      {
                        if (cmbTabla.getItemAt(di).equals(tabla2)){
                        encontrado=true;
                        break;
                        }
                      }
                      if (!encontrado){
                        cmbTabla.addItem((String) tabla2);
                        cmbTablaDestino.addItem((String) tabla2);
                
                      }

                 } //de Exisitir filas
             }
          }//Del token
          else{
          if (!tabla.equals("")){
            if (cmbTabla.getItemCount()==0){
                cmbTabla.addItem((String) tabla);
                cmbTablaDestino.addItem(tabla);
            }else{
              boolean encontrado=false;
              for (int di=0; di<cmbTabla.getItemCount();di++)
              {
                if (cmbTabla.getItemAt(di).equals(tabla)){
                encontrado=true;
                break;
                }
              }
              if (!encontrado){
                cmbTabla.addItem((String) tabla);
                cmbTablaDestino.addItem((String) tabla);
                
              }
            }
         
          }
          }
        }

        //recoger los campos de las tablas.
//--------
       //recuperamos la lista de campos y solo los que son N.
              ArrayList lista = (ArrayList) blackboardInformes.get("listaCampos");
              //Para todos los campos hay que hacer 
              Iterator it = lista.iterator();
              int p =1;
              cmbGrupo.removeAllItems();
              cmbGrupo.addItem("--");
              while (it.hasNext())
              {
                GeopistaObjetoInformeCampos objetoCampos2 = new GeopistaObjetoInformeCampos();    
                objetoCampos2 = (GeopistaObjetoInformeCampos) it.next();
                String tipo = objetoCampos2.getTipo();
                if ( tipo.equals("N"))
                {
                      cmbGrupo.addItem((String) objetoCampos2.getTabla()+"."+(String)objetoCampos2.getCampo());
                }
              }
// -------
      }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      ArrayList alistaUniones = new ArrayList();
      ArrayList arrayDatosDominios = (ArrayList) blackboardInformes.get("camposConDominio");
        for (int i=0;i<model.getRowCount();i++)
          {
          GeopistaObjetoInformeUniones objetoUniones = new GeopistaObjetoInformeUniones();
/*          objetoUniones.setUnion((String) tablaCampos.getValueAt(i,0));
          objetoUniones.setEtiqueta((String) tablaCampos.getValueAt(i,1));
          objetoUniones.setCampos((String) tablaCampos.getValueAt(i,2));*/
          objetoUniones.setUnion((String) tablaCampos.getModel().getValueAt(i,0));
          objetoUniones.setEtiqueta((String) tablaCampos.getModel().getValueAt(i,1));
          objetoUniones.setCampos((String) tablaCampos.getModel().getValueAt(i,2));
          
          alistaUniones.add(objetoUniones);
          } 

         if (arrayDatosDominios.size()!=0){
          GeopistaObjetoInformeUniones objetoUniones = new GeopistaObjetoInformeUniones();
          objetoUniones.setUnion(String.valueOf(alistaUniones.size()+1));
          objetoUniones.setEtiqueta("--");
          objetoUniones.setCampos("domainnodes.id_description=dictionary.id_vocablo");
          alistaUniones.add(objetoUniones);
          GeopistaObjetoInformeUniones objetoUniones2 = new GeopistaObjetoInformeUniones();
          objetoUniones2.setUnion(String.valueOf(alistaUniones.size()+1));
          objetoUniones2.setEtiqueta("--");
          objetoUniones2.setCampos("domainnodes.id_domain=domains.id");
/*La de Arriba antes del cambio*/
//          objetoUniones2.setCampos("domainnodes.parentdomain=domains.id");
          alistaUniones.add(objetoUniones2);
         } 
        //Almacenar en el blackboard el ArrayList
        blackboardInformes.put("listaUniones",alistaUniones);


        //A partir de aquí empezaré a crear el informe XML.
        SAXBuilder builder = new SAXBuilder(false);
        Document doc=builder.build(new File(aplicacion.getPath("generador.app.reports.plantilla"))); 

        //Saco el raiz
        Element raiz = doc.getRootElement();



        
        //Primer Paso recuperar Datos del BlackBoard Panel 1 y poner:
          //. Nombre Fichero
          //. Orientación
          //. Descripción del Fichero
          //. Título del Informe

          GeopistaObjetoInformeDatosGenerales objetoDatosGenerales = new GeopistaObjetoInformeDatosGenerales();
          objetoDatosGenerales = (GeopistaObjetoInformeDatosGenerales) blackboardInformes.get("datosGenerales");
          String modulo = objetoDatosGenerales.getModulo();
          String nombreFichero = objetoDatosGenerales.getNombreFichero();
          String tituloFichero = objetoDatosGenerales.getTitulo();
          String descripcionFichero = objetoDatosGenerales.getDescripcion();
          String orientacionFichero = objetoDatosGenerales.getOrientacion();

          // Ponemos los datos de la base de datos
          Element source = raiz.getChild("source");
          Element baseDatos = source.getChild("database");
          
          baseDatos.setAttribute("driverClassName","org.postgresql.Driver");
          baseDatos.setAttribute("connInfo","jdbc:postgresql://" + aplicacion.getString("geopista.conexion.serverIP") + ":5432/geopista");
          baseDatos.setAttribute("name","public");
          baseDatos.setAttribute("username","postgres");
           
          //Ponemos el sort de los campos que se introducen	
          Element consulta = source.getChild("query");
          
          //recorrer el array list 
          aCamposOrdenados = (ArrayList) blackboardInformes.get("camposOrdenadosInformes");
          GeopistaObjetoOrdenCampos objetoDatosOrdenados = new GeopistaObjetoOrdenCampos();
          Iterator i3 = aCamposOrdenados.iterator();
          while (i3.hasNext())
          {
              objetoDatosOrdenados = (GeopistaObjetoOrdenCampos)i3.next();
              Element ordenar = new Element("sort");    
              //Ahora se escribe en el fichero xml.
              ordenar.setAttribute("order","asc");
              ordenar.setAttribute("groupable-id",objetoDatosOrdenados.getCampoOrdenado());
              ordenar.setAttribute("groupable-type","column");
              consulta.addContent(ordenar);
          }//Fin de crear los sort del report
          
          
          
          Element descripcion = raiz.getChild("description");
          //Creamos el data para poder poner > como textos.
          CDATA data = new CDATA(descripcionFichero);
          descripcion.addContent(data);
        // Orientación
          if (orientacionFichero.equals("Horizontal"))
          {
               orientacionFichero="landscape";
           }else{
               orientacionFichero="portrait";
           }
           
           Element paper = raiz.getChild("paper");
           paper.setAttribute("orientation",orientacionFichero);

           //Nombre y Titulo Fichero
           raiz.setAttribute("name",nombreFichero);
           raiz.setAttribute("title",tituloFichero);
           

          //Ventana 2. Logotipos, Campos Identificación e Imagen Listado.
           GeopistaObjetoInformeEncabezado objetoEncabezado = new GeopistaObjetoInformeEncabezado();
           objetoEncabezado = (GeopistaObjetoInformeEncabezado) blackboardInformes.get("datosEncabezado");

           String logotipo = objetoEncabezado.getLogo();
           String tituloInforme = objetoEncabezado.getTituloInforme();
           String titulo2 = objetoEncabezado.getSubTituloInforme();
           String tabla = objetoEncabezado.getTablaEncabezado();
           String etiquetaNombre = objetoEncabezado.getEtiquetaEncabezado();
           String campoNombre = objetoEncabezado.getCampoEncabezado();
           String campoDescripcion = objetoEncabezado.getCampoDescripcion();
           String etiquetaDescripcion = objetoEncabezado.getEtiquetaDescripcion();
           String tituloImagen = objetoEncabezado.getTituloImagen();
           String crearImagen = objetoEncabezado.getCrearImagen();
           String imagenEnListado = objetoEncabezado.getImagenEnInforme(); 

           //Comprobar para quitar el campo nombre y descripcion
           if (campoNombre==null) {
              if (campoDescripcion==null){
                //Descripcin
                quitarCampo(raiz,"1004",false);
                quitarCampo(raiz,"1006",true);
                //nombre
                quitarCampo(raiz,"1003",false);
                quitarCampo(raiz,"1005",true);
              }else{
                //nombre
                quitarCampo(raiz,"1003",false);
                quitarCampo(raiz,"1005",true);

              }
           }else{
              if (campoDescripcion==null){
                //Descripcin
                quitarCampo(raiz,"1004",false);
                quitarCampo(raiz,"1006",true);

              }else{
                  //no quitar ninguno
              }
           };
          



            
          
          //Comprobar para quitar la imagen y titulo de la misma en el informe.
            if (tituloImagen.equals("")){
                if (crearImagen.equals("N")) {
                      quitarImagen(raiz,true,true);          
                }else{
                      quitarImagen(raiz,true,false);          
                }
            }else{
                if (crearImagen.equals("N")){
                    quitarImagen(raiz,false,true);          
                }else{
                    quitarImagen(raiz,false,false);
                }
            }

            Element header = raiz.getChild("headers");
            Element sectionHeader = header.getChild("section");
            java.util.List campos=sectionHeader.getChildren("field");

            
            
            //Si existe el logotipo se pone
            if (logotipo.length()!=0){
              cambiarAtributoElementos(campos,"1000","value",logotipo);
            }
            //titulo del informe
            cambiarAtributoElementosCDATA(campos,"1001","",tituloInforme);

            //titulo 2
            cambiarAtributoElementosCDATA(campos,"1002","",titulo2);
            //Etiqueta Campo 1 y 2
            cambiarAtributoElementosCDATA(campos,"1003","",etiquetaNombre);
            cambiarAtributoElementosCDATA(campos,"1004","",etiquetaDescripcion);

            //Campos de Nombre y Descripcion.
            cambiarAtributoElementos(campos,"1005","value","public."+tabla+"."+campoNombre);
            cambiarAtributoElementos(campos,"1006","value","public."+tabla+"."+campoDescripcion);
            // Pongo el titulo de la imagen y la imagen si esta
            cambiarAtributoElementosCDATA(campos,"1007","",tituloImagen);
           
            if (crearImagen.equals("N")){
              borrarMapa(campos,"1008","","");
            }else{
                if (imagenEnListado!=null) cambiarAtributoElementos(campos,"1008","value",imagenEnListado);
            }

            //Siguiente paso Crear los Detalles, de los campos.

//            
              boolean hayGrupo=false;
              ArrayList lista = (ArrayList) blackboardInformes.get("listaCampos");
              int elementos = lista.size();
              //Ancho de cada columna
              int izda = 10; //Para el primer elemento
              double ancho = (Math.ceil(574.0 / elementos));

              //Para todos los campos hay que hacer 
              Iterator it = lista.iterator();
              int p =1;

              while (it.hasNext())
              {
                GeopistaObjetoInformeCampos objetoCampos = new GeopistaObjetoInformeCampos();    
                objetoCampos = (GeopistaObjetoInformeCampos) it.next();
                String tipo = objetoCampos.getTipo();
                if (!tipo.equals("N"))
                {
                  // Sólo si es con grupo y crear la entrada en grupo.
                  // Si es un Campo Normal con Grupo
                  if ((tipo.equals("X")) || (tipo.equals("G")))
                    {hayGrupo=true;}
                    
                  if (!tipo.equals("N")){
                      //Crear un objeto UserCol
                      crearUserCol(String.valueOf(5000+p),raiz,objetoCampos);
                      crearCampoCabecera(String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(3000+p),objetoCampos);
                      crearCampoDetalleColumnaUsuario(String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(2000+p),objetoCampos,String.valueOf(5000+p));
                      if (objetoCampos.getSubtotales().equals("S")){      
                        crearCampoSubtotal(String.valueOf(4000+p),String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(2000+p),objetoCampos);
                      }
                      
                  }else{
                      //Son espaciales, se tratan de la misma forma.
                  }
                  
                }
                // Independientemente si se tiene que crear el Detalle y Page Footer.
                if (objetoCampos.getTipo().equals("N")){  
                    crearCampoDetalle(String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(2000+p),objetoCampos);
                    crearCampoCabecera(String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(3000+p),objetoCampos);
                      if (objetoCampos.getSubtotales().equals("S")){      
                          crearCampoSubtotal(String.valueOf(4000+p),String.valueOf(ancho),String.valueOf(izda+ancho*(p-1)),raiz,String.valueOf(2000+p),objetoCampos);
                      }

                }

                //Colocamos los subtotales que se hayan especificado.
                
                p++;
              }

              if (hayGrupo) {
                Iterator i2 = lista.iterator();
                while (i2.hasNext()){
                GeopistaObjetoInformeCampos objetoCampos = new GeopistaObjetoInformeCampos();    
                objetoCampos = (GeopistaObjetoInformeCampos) i2.next();
                String tipo = objetoCampos.getTipo();
                if (tipo.equals("N")){
                  //Pongo un grupo a un campo normal.
                    ponerGrupoCampoNormal(raiz,objetoCampos);
                  
                }else{
                  if (tipo.equals("E")){
                    //Pongo el grupo a un userCols.
                    ponerGrupoCampoEspacial(raiz,objetoCampos);
                  }
                }

                }
                hayGrupo=false;
              }              
            
            //Colocamos las uniones de las tablas,
        //Almacenar en el blackboard el ArrayList
        ArrayList listaUnionesJoins = new ArrayList();
        listaUnionesJoins = (ArrayList) blackboardInformes.get("listaUniones");
        Iterator uniones = listaUnionesJoins.iterator();

        while (uniones.hasNext()){
          //Acceder a cada union y crear una entrada en el xml.
         GeopistaObjetoInformeUniones sentenciasUnion = (GeopistaObjetoInformeUniones) uniones.next();
         StringTokenizer tipoDescripcionToken = new StringTokenizer(sentenciasUnion.getCampos(), "=");
         String tabla1  = tipoDescripcionToken.nextToken();
         String tabla2 = tipoDescripcionToken.nextToken();


         crearUniones(raiz,tabla1,tabla2);
          //
          }
           String sentencia = (String) blackboardInformes.get("SentenciaTransformada");
           crearWheres(raiz,sentencia,arrayDatosDominios,"es_ES");
           if (arrayDatosDominios.size()!=0){
          //Existen datos dominios
           crearFormulaLocale(raiz);
           crearFormulasColUsuarios(raiz,arrayDatosDominios);
           modificarAspectoCampos(raiz,arrayDatosDominios);
          

          }

          //Crear el grupo del campo recogido del cmbGrupo
          String campoGrupo = (String) cmbGrupo.getSelectedItem();
          if (campoGrupo.equals("--")){
          }else{
            //Se ha seleccionadon un valor, hay que bucarlo den details
              Element detalles = raiz.getChild("details");
              Element seccionDetalles= detalles.getChild("section");
              java.util.List camposDetalles = seccionDetalles.getChildren("field");
              Iterator itDetalles = camposDetalles.iterator();
              boolean encontrado =false;
              while (itDetalles.hasNext()){
                Element tmp = (Element) itDetalles.next();
                String id = tmp.getAttributeValue("id");
                String nombreDetalles = tmp.getAttributeValue("value");
                String campoVisible = tmp.getAttributeValue("visible");
                if ((nombreDetalles.equals("public."+campoGrupo))&&(campoVisible.equals("true"))){
                    //Una vez localizado el grupo y tipo

                      crearGrupo(raiz,"column",campoGrupo,id,campoVisible,txtTitulo.getText());
                      encontrado=true;
                      break;
                }
              }

            if(encontrado){
                //Es un campo column

            }else{
                //buscamos si es un campo User col
              Element usercols = raiz.getChild("usercols");
              java.util.List camposUserCols = usercols.getChildren("usercol");
              Iterator itUsercols = camposUserCols.iterator();
              while (itUsercols.hasNext()){
                Element tmp2 = (Element)itUsercols.next();
                String nameUserCol = (String) tmp2.getAttributeValue("name");
                if (nameUserCol.equals(campoGrupo)){
                    //Es el valor buscado en user col para crear el grupo.
                    String id = tmp2.getAttributeValue("id");
                    crearGrupo(raiz,"usercol",id,id,"false",txtTitulo.getText());
                break;
                }
              }

            }
          }  
          

  
           XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
           File fileToSave = new File(aplicacion.getPath("informes.guardados"),modulo+"_"+nombreFichero);
           
           if(!fileToSave.getParentFile().exists())
           {
               fileToSave.getParentFile().mkdirs();
           }
           FileOutputStream file = new FileOutputStream(fileToSave);
           out.output(doc,file);
           
           
           blackboardInformes.put("fichero",fileToSave.getAbsoluteFile());

           
           
        
       }

      

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {     
    }

    public void remove(InputChangedListener listener)
    {     
    }

    public String getTitle()
    {
      return " ";
    }

    public String getID()
    {
      return "6";
    }
  private WizardContext wizardContext;
  private JLabel lblTablaDestino = new JLabel();
  private JComboBox cmbTablaDestino = new JComboBox();
  private JComboBox cmbCamposDestino = new JComboBox();
  private JButton btnAnadirUnion = new JButton();




  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  private BorderLayout borderLayout1 = new BorderLayout();

  private JComboBox cmbGrupo = new JComboBox();
  private JLabel lblGrupo = new JLabel();
  private JTextField txtTitulo = new JTextField();
  private JLabel lblTituloGrupo = new JLabel();
  private JCheckBox chkgrupo = new JCheckBox();
  private JSeparator jSeparator1 = new JSeparator();
  
    public void setWizardContext(WizardContext wd)
    {
      wizardContext =wd;

    }
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
     return true;
    }


  private String nextID="7";
  private JLabel lblImagen = new JLabel();
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
      return nextID;
    }

  private void cmbCapas_actionPerformed(ActionEvent e)
  {

  }

  private void cmbModulos_actionPerformed(ActionEvent e)
  {
  }

  private void cmbHorientacion_actionPerformed(ActionEvent e)
  {
  }

  private void jButton2_actionPerformed(ActionEvent e)
  {
    if (tablaCampos.getSelectedRow() ==-1)
      {

      }else{
        model.removeRow(tablaCampos.getSelectedRow());
      }
      wizardContext.inputChanged();
    
  }




  


  private void btnAnadirUnion_actionPerformed(ActionEvent e)
  {
  //Añadimos los valores a la tabla.
    if ((cmbTabla.getSelectedItem().equals(cmbTablaDestino.getSelectedItem())) && 
       (cmbCampo.getSelectedItem().equals(cmbCamposDestino.getSelectedItem())))
       {
        JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.uniones.error.relacion"));
       }else{
          //Es correcto, añadir la unión.
           Object[] cadena = new Object[3];
            int filas = model.getRowCount();
            cadena[0]="Union " + filas;
            cadena[1]= cmbTabla.getSelectedItem() + " y " + cmbTablaDestino.getSelectedItem();
            cadena[2]= cmbTabla.getSelectedItem()+"."+ cmbCampo.getSelectedItem() + " = " + cmbTablaDestino.getSelectedItem() +"." + cmbCamposDestino.getSelectedItem();
            model.insertRow(filas, cadena);
            wizardContext.inputChanged();
       }
  }

  
  
/**
 * Cambio de valores dentro del fichero XML
 * @param List campos, la lista de campos
 * @param String idCampo, el valor por el que se  va a preguntar
 * @param String nombreCampo campo del que se va a cambiar el atributo
 * @param String valor nuevo valor a cambiar
 */
 
public  void cambiarAtributoElementos(java.util.List campos,String idCampo,String nombreCampo, String valor)
{
          Iterator i = campos.iterator();
            while (i.hasNext()){
                Element e= (Element)i.next();
                //primer hijo que tenga como nombre club
                  Attribute club =e.getAttribute("id"); 
                  if (club.getValue().equals(idCampo)) {
                      e.setAttribute(nombreCampo,valor);
                      break;
                    }   
            }
}

/**
 * Cambio de valores dentro del fichero XML, en una sección CDATA del fichero XML
 * @param List campos, la lista de campos
 * @param String idCampo, el valor por el que se  va a preguntar
 * @param String nombreCampo campo del que se va a cambiar el atributo
 * @param String valor nuevo valor a cambiar

 **/
public  void cambiarAtributoElementosCDATA(java.util.List campos,String idCampo,String nombreCampo, String valor)
{
          Iterator i = campos.iterator();
            while (i.hasNext()){
                Element e= (Element)i.next();
                //primer hijo que tenga como nombre club
                  Attribute club =e.getAttribute("id"); 
                  if (club.getValue().equals(idCampo)) {
                      Element textos = e.getChild("text");
                      CDATA nuevoCdata = new CDATA(valor);
                      textos.addContent(nuevoCdata);
                      break;
                    }   
            }
} 

/**
 * Borra la sección del mapa, cuando no se desea imprimir
 * @param List campos, lista de campos
 * @param String idCampo, identificador del campo
 * @param String valor, valor del campo a borrar
 **/
public  void borrarMapa(java.util.List campos,String idCampo,String nombreCampo, String valor)
{
          Iterator i = campos.iterator();
            while (i.hasNext()){
                Element e= (Element)i.next();
                //primer hijo que tenga como nombre club
                  Attribute club =e.getAttribute("id"); 
                  if (club.getValue().equals(idCampo)) {
                       e.getParent().removeContent(e);
                      break;
                    }   
            }
}    
/**
 * Crea un campo en el detalle del informe
 * @param String ancho, Ancho del campo
 * @param String x, fila donde se colocará el campo
 * @param Element raiz, Raiz del fichero XML
 * @param String idCampo, valor identificador del campo
 * @param GeopistaObjetoInformeCampos objetoCampos, listado de campos
 **/
 
public  void crearCampoDetalle(String ancho,String x, Element raiz,String idCampo, GeopistaObjetoInformeCampos objetoCampos)
{
    Element details = raiz.getChild("details");
    Element sectionDetails = details.getChild("section");
    Element fieldDetails = new Element("field");
    Element bounds = new Element("bounds");

    fieldDetails.setAttribute("id",idCampo);
    fieldDetails.setAttribute("type","column");
    fieldDetails.setAttribute("visible","true");
    fieldDetails.setAttribute("value","public." + objetoCampos.getTabla()+ "." + objetoCampos.getCampo());
    bounds.setAttribute("y","2.0"); //Esta siempre fija
    bounds.setAttribute("x",x);
    bounds.setAttribute("width",ancho);
    bounds.setAttribute("height","16.0");
    fieldDetails.addContent(bounds);
    sectionDetails.addContent(fieldDetails);

    
}    

/**
 * Añade campos en la cabecera del informe
 * @param String ancho, Ancho del campo
 * @param String x, fila
 * @param Element raiz, Raiz del fichero XML
 * @param String idCampo, Identificador del campo
 * @param GeopistaObjetoInformeCampos objetoCampos, listado de campos
 **/
 
public  void crearCampoCabecera(String ancho,String x, Element raiz,String idCampo, GeopistaObjetoInformeCampos objetoCampos)
{
    Element pagina = raiz.getChild("page");
    Element cabecera = pagina.getChild("headers");
    Element sectionPagina = cabecera.getChild("section");
    Element fieldDetails = new Element("field");
    Element bounds = new Element("bounds");
    Element texto = new Element("text");
    Element format = new Element("format");
    CDATA datos = new CDATA(objetoCampos.getEtiquetas());
    texto.addContent(datos);
 
    fieldDetails.setAttribute("id",idCampo);
    fieldDetails.setAttribute("type","text");
    bounds.setAttribute("y","56.0"); //Esta siempre fija
    bounds.setAttribute("x",x);
    bounds.setAttribute("width",ancho);
    bounds.setAttribute("height","16.0");
    format.setAttribute("font","Arial");
    format.setAttribute("size","11.0");
    format.setAttribute("bold","true");
    fieldDetails.addContent(bounds);
    fieldDetails.addContent(texto);
    fieldDetails.addContent(format);
    sectionPagina.addContent(fieldDetails);

    
}    

/**
 * Crea en el detalle del informe un campo definido por el usuario
 * @param ancho, ancho del campo
 * @param String x, fila dentro del detalle
 * @param Element raiz, Raiz del fichero Xml
 * @param String idCampo, valor interno del campo
 * @param  GeopistaObjetoInformeCampos objetoCampos, listado de campos
 * @param String campoUsuario, valor del campo de usuario
 */
public  void crearCampoDetalleColumnaUsuario(String ancho,String x, Element raiz,String idCampo, GeopistaObjetoInformeCampos objetoCampos,String campoUsuario)
{
    Element details = raiz.getChild("details");
    Element sectionDetails = details.getChild("section");
    Element fieldDetails = new Element("field");
    Element bounds = new Element("bounds");
    fieldDetails.setAttribute("id",idCampo);
    fieldDetails.setAttribute("type","usercol");
    fieldDetails.setAttribute("value",campoUsuario);
    bounds.setAttribute("y","2.0"); //Esta siempre fija
    bounds.setAttribute("x",x);
    bounds.setAttribute("width",ancho);
    bounds.setAttribute("height","16.0");
    fieldDetails.addContent(bounds);
    sectionDetails.addContent(fieldDetails);
  
  }    
/**
 * Crea un campo definido por el usuario
 * Stirng id, Identificador Interno
 * @param Element raiz, Raiz del fichero XML
 * @param GeopistaObjetoInformeCampos campos, listado de campos

 */
public void crearUserCol(String id, Element raiz, GeopistaObjetoInformeCampos objetoCampos){
    Element columnas = raiz.getChild("usercols");
    Element columna = new Element("usercol");
    CDATA valor = new CDATA(objetoCampos.getCampo());
//    columna.setAttribute("id",id);
    columna.setAttribute("name","col"+id);
//    columna.setAttribute("name",objetoCampos.getEtiquetas());
    columna.setAttribute("id",id);
    columna.addContent(valor);
    columnas.addContent(columna);

  }

/**
 * Crea una entrada de agrupamiento para un campo Normal.
 * @param Element raiz, Raiz del fichero XML
 * @param GeopistaObjetoInformeCampos campos, listado de campos

 */
public void ponerGrupoCampoNormal(Element raiz,GeopistaObjetoInformeCampos campos){
    Element grupos = raiz.getChild("groups");
    Element grupo = new Element("group");
    grupo.setAttribute("groupable-id","public." + campos.getTabla()+ "." + campos.getCampo());
    grupo.setAttribute("groupable-type","column");
    grupo.setAttribute("sort-order","asc");
    grupos.addContent(grupo);

  }

// Crea una entrada de agrupamiento para un campo userCol.
/**
 * Crea un grupo para un campo Espacial.
 * @param Element raiz, Raiz del fichero XML
 * @param GeopistaObjetoInformeCampos campos, listado de campos
 */
public void ponerGrupoCampoEspacial(Element raiz,GeopistaObjetoInformeCampos campos){
  try {
    Element grupos = raiz.getChild("groups");
    Element grupo = new Element("group");
    //Tengo que leer la sección de usercols y quedarme con el id
    //que corresponda al getCampo.
    Element usercols = raiz.getChild("usercols");
    java.util.List columna = usercols.getChildren("usercol");
    Iterator i = columna.iterator();
    boolean encontrado=false;
    
    while (i.hasNext()) 
  {
    // procesamos tosas las clasificaciones del tema
    Element valor = (Element) i.next();
    
    String  dato = valor.getText();
 
    if (dato.equals(campos.getCampo())){
            String id = valor.getAttributeValue("id");
            grupo.setAttribute("groupable-id",id);
            grupo.setAttribute("groupable-type","usercol");
            grupo.setAttribute("sort-order","asc");
            grupos.addContent(grupo);
            break;
    }
    
    
  }//del while
  }catch(Exception e){e.printStackTrace();} 

  }//del método

/**
 * Método para crear un campo con subtotales
 * @param String id, valor interno del campo a crear
 * @param String ancho, Ancho del campo
 * @param String x,
 * @param Element raiz, Elemento Raiz del fichero XML
 * @param String idCampo , identificador del campo sobre el que se realiza el subtotal
 * @param GeopistaObjetoInformeCampos objetoCampos, listado de campos
 */
public void crearCampoSubtotal(String id, String ancho,String x, Element raiz,String idCampo, GeopistaObjetoInformeCampos objetoCampos){

    Element footers = raiz.getChild("footers");
    Element sectionFooters = footers.getChild("section");
    Element campos = new Element("field");
    Element bounds = new Element("bounds");
    Element format = new Element("format");
    format.setAttribute("size","11");
    format.setAttribute("bold","true");
    format.setAttribute("format","#,##0.00");

    bounds.setAttribute("x",x);
    bounds.setAttribute("y","3");
    bounds.setAttribute("width",ancho);
    bounds.setAttribute("height","16");
    campos.setAttribute("id",id);
    campos.setAttribute("type","subtotal");
    campos.setAttribute("value",idCampo);
    campos.addContent(format);
    campos.addContent(bounds);
    sectionFooters.addContent(campos);

  }

/**
 * Crea la sección de join de la query del xml.
 * @param Element raiz, Elmento Raiz del fichero XML
 * @param String tabla1, Nombre de la tabla 
 * @param String tabla2, Nombre de la 2 tabla para hacer la unión
 */
public void crearUniones(Element raiz, String tabla1, String tabla2){
  Element source = raiz.getChild("source");
  Element query = source.getChild("query");
  Element join = new Element("join");
  join.setAttribute("from","public."+tabla1.trim());
  join.setAttribute("relation","=");
  join.setAttribute("to", "public." + tabla2.trim());
  query.addContent(join);

  }

/**
 * Crea la sentencia Where de la query del fichero XML
 * @param Element raiz, Raiz del fichero XML
 * @param String sentencia, sentencia del Where
 * @param ArrayList arrayDatosDominios, datos que tienen dominios
 * @param String localidad, variable locale para obtener la traducción de los valores
 */
public void crearWheres(Element raiz, String sentencia ,ArrayList arrayDatosDominios,String localidad){
  Element source = raiz.getChild("source");
  Element query = source.getChild("query");
  Element wheres = new Element("where");
  CDATA datos=null;
  if (arrayDatosDominios.size()==0){

  }else{
     //hay que modificar la sentencia para hacer el join con la domains y dictionary
    if (sentencia.equals("")){
      sentencia = (sentencia) + " (public.dictionary.locale='" + localidad +"' and ("; 
    }else{
    sentencia = "(" + sentencia +")" + "  and (public.dictionary.locale='" + localidad +"' and ("; 
    }
    //recorremos el ArrayLista para sacar los distintos dominios
    String parteFin = " ";
    Iterator iter = arrayDatosDominios.iterator();
    while (iter.hasNext())
    {
      GeopistaDatosDominios datosDominios = new GeopistaDatosDominios();
      datosDominios = (GeopistaDatosDominios) iter.next();
      parteFin = parteFin +" public.domainnodes.id_domain = " +datosDominios.getDominio() + " or " ;
    }
      parteFin = parteFin.substring(1,parteFin.length()-3) +"))";
      sentencia = sentencia + parteFin;
  } 
  
  datos = new CDATA(sentencia);
  wheres.addContent(datos);
  query.addContent(wheres);
}


/**
 * Devuevle la conexion para acceso a la base de datos
 * @return conexion para acceder a la base de datos
 */

public static Connection getDBConnection() throws SQLException
{
 /* AppContext app = (AppContext) AppContext.getApplicationContext();
  Connection conn= app.getConnection();*/
  GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
  Connection  conn=null;
  try {
  conn = geopistaListados.abrirConexion();
  conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}


/**
 * Creación de campo formula para mete rel valor del lenguaje
 * @param Element raiz, raiz del fichero xml
 * 
 */
public void crearFormulaLocale(Element raiz){
       Element formulas = raiz.getChild("formulas");
       Element formula = new Element("formula");
       formula.setAttribute("id","9500");
       formula.setAttribute("name","localidad");
       formula.setAttribute("visible","false");
      //Creamos el data para poder poner > como textos.
       CDATA dataLocale = new CDATA("\"es_ES\"");
       formula.addContent(dataLocale);
       formulas.addContent(formula);
  }

/**
 * Crea las Fórmulas de los dominiios, con el tipo de ColUsers
 * @param Element raiz, Raiz del fichero XML
 * @param ArrayList, listado de campos de dominios
 * 
 */
public void crearFormulasColUsuarios(Element raiz,ArrayList arrayDatosDominios){


       Element formulas = raiz.getChild("formulas");
       Element usercols = raiz.getChild("usercols");
       Iterator it = arrayDatosDominios.iterator();

       while (it.hasNext()){
        Element formula = new Element("formula");
        Element usercol = new Element("usercol");
        GeopistaDatosDominios geo = new GeopistaDatosDominios();
        geo = (GeopistaDatosDominios) it.next();

        int orden  = 10000 + geo.getOrden();
        int orden2 = 15000 + geo.getOrden();
        
        formula.setAttribute("id",String.valueOf(orden));
        formula.setAttribute("name","Dominio"+String.valueOf(orden));
        CDATA datoDominio = new CDATA(geo.getDominio());
        formula.addContent(datoDominio);
        formulas.addContent(formula);

        //Procesamos las columnas de los usuario.
        usercol.setAttribute("id", String.valueOf(orden2));
//xxxx  usercol.setAttribute("name","userCol"+String.valueOf(orden2));
        usercol.setAttribute("name",geo.getTabla()+"."+geo.getCampo());
/*        String sql = " (select distinct public.dictionary.traduccion " +
                     " from   public.domains, public.dictionary, public.domainnodes "+
                     " where public.domainnodes.pattern="+ geo.getTabla() +"."+ geo.getCampo() +" AND domainnodes.id_domain={@" +  String.valueOf(orden) + "}  and " +
                     " public.domains.id = public.domainnodes.id_domain and public.domainnodes.id_description = public.dictionary.id_vocablo and " +
                     " public.dictionary.locale={@9500})"; //9500 Fija el idioma*/
                     /*La de arriba correcta hasta el cambio*/
        String sql = " (select distinct public.dictionary.traduccion " +
                     " from   public.domains, public.dictionary, public.domainnodes "+
                     " where public.domainnodes.pattern="+ geo.getTabla() +"."+ geo.getCampo() +" AND domainnodes.parentdomain={@" +  String.valueOf(orden) + "}  and " +
                     " public.domains.id = public.domainnodes.parentdomain and public.domainnodes.id_description = public.dictionary.id_vocablo and " +
                     " public.dictionary.locale={@9500})"; //9500 Fija el idioma*/
                     
        CDATA userColData = new CDATA(sql);
        usercol.addContent(userColData);
        usercols.addContent(usercol);
       }
  }

/**
 * Método que leerá los datos de los campos tipo = column 
 * Cogerá sus datos de tamaño, posición y los ocultara
 * Cargará la fórmula del dominio poniendola visible a false
 * Cargará el campo columna de usuario en la posición del campo correspondiente
 * @param Element raiz, Raiz del fichero XML
 * @param ArrayList, Contiene los campos que pertenecen a algun domnio
 */
public void modificarAspectoCampos(Element raiz,ArrayList arrayDatosDominios){

  Iterator itDominios = arrayDatosDominios.iterator();

  while (itDominios.hasNext()){
    GeopistaDatosDominios geo = new GeopistaDatosDominios();
    geo = (GeopistaDatosDominios) itDominios.next();

    //Recupero los campos de la sección de detalle.
      Element detalle = raiz.getChild("details");
      Element seccion = detalle.getChild("section");
      java.util.List campos=seccion.getChildren("field");
      Iterator it  = campos.iterator();
        while (it.hasNext()){
            Element campo = (Element) it.next();
            int numero = geo.getOrden()+1;
            String resultado = "200"+ String.valueOf(numero);
            if(campo.getAttributeValue("id").equals(resultado)){
                //Coinciden
                //Sacamos los valores x,y,
                campo.setAttribute("visible","false");
                Element bounds = campo.getChild("bounds");
                Element campoFormula = new Element("field");
                campoFormula.setAttribute("id","1100"+geo.getOrden());
                campoFormula.setAttribute("type","formula");
                campoFormula.setAttribute("value","1000"+geo.getOrden());
                campoFormula.setAttribute("visible","false");
                //ponemos las coordenadas
                Element bounds2 = new Element("bounds");
                bounds2.setAttribute("x",bounds.getAttributeValue("x"));
                bounds2.setAttribute("y",bounds.getAttributeValue("y"));
                bounds2.setAttribute("width",bounds.getAttributeValue("width"));
                bounds2.setAttribute("height",bounds.getAttributeValue("height"));
                campoFormula.addContent(bounds2);
                seccion.addContent(campoFormula);

                //Añadimos los campos usuario
                Element campoUsuario = new Element("field");
                campoUsuario.setAttribute("id","800"+geo.getOrden());
                campoUsuario.setAttribute("type","usercol");
                campoUsuario.setAttribute("value","1500"+geo.getOrden());
                 Element bounds3 = new Element("bounds");
                bounds3.setAttribute("x",bounds.getAttributeValue("x"));
                bounds3.setAttribute("y",bounds.getAttributeValue("y"));
                bounds3.setAttribute("width",bounds.getAttributeValue("width"));
                bounds3.setAttribute("height",bounds.getAttributeValue("height"));
                campoUsuario.addContent(bounds3);
                seccion.addContent(campoUsuario);
                break;
            }
        }
      }//fin del iterator de los dominios

  }//fin del método

  private void txtTitulo_actionPerformed(ActionEvent e)
  {
  }

/**
 * Crea un grupo en el informe
 * @param Element raiz, Raiz del fichero XML
 * @param String tipo, Tipo del grupo (Columna o No) 
 * @param String valor, Valor por el que se va a agrupar
 * @param String id, Valor Interno del grupo
 * @param boolean visible Si será visible o no el grupo
 * @param String Titulo, Titulo que aparecerá en el grupo 
 * 
 */
 
public void crearGrupo(Element raiz, String tipo, String valor,String id, String visible,String titulo){

       Element grupos = raiz.getChild("groups");
       Element grupo = new Element("group");
       int temporal = Integer.parseInt(id);
       int temporal2 =0;
       Element headers = new Element("headers");
       Element section = new Element("section");
       section.setAttribute("height","20.0");
       Element field = new Element("field");
       Element footer = new Element("footers");
       Element sectionfooter = new Element("section");
       sectionfooter.setAttribute("height","20.0");
       
       //Debemos sacar la sección de footers del raiz y ponerla a la section footers
       Element footersRaiz = raiz.getChild("footers");
       Element seccionRaiz = footersRaiz.getChild("section");
       java.util.List hijos = seccionRaiz.getChildren("field");
       Iterator itHijos = hijos.iterator();

       Element bounds = new Element("bounds");
       bounds.setAttribute("x","0.0");
       bounds.setAttribute("y","0.0");
       bounds.setAttribute("width","287.0");
       bounds.setAttribute("height","16.0");
       
       
       if ((tipo.equals("column"))&&(visible=="true")){
           grupo.setAttribute("groupable-id","public."+ valor);
           grupo.setAttribute("groupable-type",tipo);
           grupo.setAttribute("sort-order","asc");
           temporal2=18000+temporal;
           field.setAttribute("id",String.valueOf(temporal2));
           field.setAttribute("value","public."+valor);
           field.setAttribute("type","column");
           
           
       }else{
           grupo.setAttribute("groupable-id",valor);
           grupo.setAttribute("groupable-type",tipo);
           grupo.setAttribute("sort-order","asc");
           temporal2 = 11000+temporal;
           field.setAttribute("id",String.valueOf(temporal2));
           field.setAttribute("value",valor);
           field.setAttribute("type","usercol");
       }
            field.addContent(bounds);
            section.addContent(field);

            // Comprobar si el titulo es vacio,
            if (!titulo.equals("")){
              //Crear el nuevo valor 
              Element fieldtitulo = new Element("field");
              fieldtitulo.setAttribute("id",String.valueOf(temporal2+1));
              fieldtitulo.setAttribute("type","text");
              Element textoTitulo = new Element("text");
              CDATA cdataTitulo = new CDATA(titulo);
              textoTitulo.addContent(cdataTitulo);
              Element bondsTitulo = new Element("bounds");
              bondsTitulo.setAttribute("x","3.0");
              bondsTitulo.setAttribute("y","1.0");
              bondsTitulo.setAttribute("width","90.0");
              bondsTitulo.setAttribute("height","16.0");
              fieldtitulo.addContent(bondsTitulo);
              fieldtitulo.addContent(textoTitulo);
              section.addContent(fieldtitulo);

            }
            headers.addContent(section);
            grupo.addContent(headers);

            //recorrer la sección de footers del raiz
            if (hijos.size()!=0){
                while (itHijos.hasNext()){
                  //Sacar el elemento
                  Element hijoRaiz = (Element)itHijos.next();
                  String idHijo = hijoRaiz.getAttributeValue("id");
                  String tipoHijo ="subtotal";
                  String valorHijo = hijoRaiz.getAttributeValue("value");
                  Element formatoHijo = hijoRaiz.getChild("format");
                  String  tamanhoFormato = formatoHijo.getAttributeValue("size");
                  String  negritaFormato = formatoHijo.getAttributeValue("bold");
                  String  formatoFormato = formatoHijo.getAttributeValue("format");
                  Element limitesHijo = hijoRaiz.getChild("bounds");
                  String  limiteX = limitesHijo.getAttributeValue("x");
                  String  limiteY = limitesHijo.getAttributeValue("y");
                  String  limiteAncho = limitesHijo.getAttributeValue("width");
                  String limiteAlto = limitesHijo.getAttributeValue("height");

                  Element grupoLimites = new Element("bounds");
                  grupoLimites.setAttribute("x",limiteX);
                  grupoLimites.setAttribute("y",limiteY);
                  grupoLimites.setAttribute("width",limiteAncho);
                  grupoLimites.setAttribute("height",limiteAlto);

                  Element grupoFormato = new Element("format");
                  grupoFormato.setAttribute("size",tamanhoFormato);
                  grupoFormato.setAttribute("bold",negritaFormato);
                  grupoFormato.setAttribute("format",formatoFormato);

                  Element grupoCampos = new Element("field");
                  grupoCampos.setAttribute("type",tipoHijo);
                  grupoCampos.setAttribute("value",valorHijo);
                  int resultado = Integer.parseInt(idHijo) +(25000-Integer.parseInt(idHijo));
                  grupoCampos.setAttribute("id",String.valueOf(resultado));
                  grupoCampos.addContent(grupoFormato);
                  grupoCampos.addContent(grupoLimites);
                  sectionfooter.addContent(grupoCampos);

                }
            }
            footer.addContent(sectionfooter);
            grupo.addContent(footer);
            grupos.addContent(grupo);

            

  }
/**
 * Método que elimina de la plantilla el titulo o la imagen y reajusta la sección.
 * @param Element raiz Raiz del fichero Xml
 * @param boolean si quita la imagen o no
 **/
  public void quitarImagen(Element raiz, boolean  quitarTitulo, boolean quitarImagen){

    int quitarAltura=0;
    double x= 0.0;

        Element cabecera = raiz.getChild("headers");
        Element seccion = cabecera.getChild("section");
        x = Double.parseDouble(seccion.getAttributeValue("height"));
        java.util.List listaCampos = seccion.getChildren("field");
        Iterator it = listaCampos.iterator();
        while (it.hasNext()){
          Element tipo = (Element)it.next();
          if (tipo.getAttribute("id").equals("1007")){
              //comprobamos si quitamos el titulo
                if (quitarTitulo){
                    //borrar el campo
                    x = x + Double.parseDouble(tipo.getAttributeValue("height"));
                    tipo.getParent().removeContent(tipo);
                }
          }else{
              if (tipo.getAttribute("id").equals("1008")){
                if (quitarImagen){
                   x = x + Double.parseDouble(tipo.getAttributeValue("height"));
                   //quitamos
                   tipo.getParent().removeContent(tipo);
                }
                
              }
          };


        }
          if (x!=0){
             seccion.setAttribute("height",String.valueOf((Double.parseDouble(seccion.getAttributeValue("height"))-x)));
          }
  
  }

/**
 * Método que elimina el titulo y subtitulo del informe
 * @param Element raiz Raiz del fichero Xml
 * @param String campo a borrar
 * @param boolean si se quita la seccion o no
 */
  public void quitarCampo(Element raiz, String campo, boolean quitarAlturaSeccion){

    int quitarAltura=0;
    double x= 0.0;

        Element cabecera = raiz.getChild("headers");
        Element seccion = cabecera.getChild("section");
        java.util.List listaCampos = seccion.getChildren("field");
        Iterator it = listaCampos.iterator();
        while (it.hasNext()){
          Element tipo = (Element)it.next();
          if (tipo.getAttributeValue("id").equals(campo)){
              tipo.getParent().removeContent(tipo);
              break;
          }
 
        }
          if (quitarAlturaSeccion){
       //      seccion.setAttribute("height",String.valueOf((Double.parseDouble(seccion.getAttributeValue("height"))-x)));
          }
  
  }

  private void chkgrupo_actionPerformed(ActionEvent e)
  {
      if (chkgrupo.isSelected()){
         cmbGrupo.setEnabled(true);
         txtTitulo.setEnabled(true);
         txtTitulo.setText(null);
      }else{
        cmbGrupo.setSelectedIndex(-1);
         cmbGrupo.setEnabled(false);
         txtTitulo.setText(null);
         txtTitulo.setEnabled(false);

      }
  }


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

  


}  // de la clase.





