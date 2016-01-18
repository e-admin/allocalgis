package com.geopista.ui.plugin.georeference.panel;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.ui.plugin.georeference.CallejeroTableModel;
import com.geopista.ui.plugin.georeference.NoGeoRefTableModel;
import com.geopista.ui.plugin.georeference.beans.PoliciaCoincidencias;
import com.geopista.ui.plugin.georeference.beans.PortalGeorreferenciado;
import com.geopista.ui.plugin.georeference.beans.Via;
import com.geopista.ui.plugin.georeference.beans.ViasCollector;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.WKTReader;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author rubengomez
 * Formulario que permite a las entradas del arrayList noLocalizado
 * asignarle una geometría por medio del callejero completo, y almacenarlo 
 * en el arrayList localizado
 * Aquellos no georreferenciados se perderán
 */
public class FileGeoreferencePanel05 extends javax.swing.JPanel implements WizardPanel {

	public static Icon iconoCalle= null;
	private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext app = (AppContext) AppContext.getApplicationContext();  //  @jve:decl-index=0:
	private JTable jTableViasBaseDatos = null;
	private JTable jTableViasSinReferenciar = null;
	private JPanel jPanelTabla = new JPanel();
	private JPanel jPanelResultados = new JPanel();
	private JButton bAsociar = new JButton();
	private JButton bEliminar = new JButton();
	private JList jListViasAsociadas = new JList();
	private WizardContext wizardContext = null;  //  @jve:decl-index=0:
	private CallejeroTableModel modelCallejero;
	private NoGeoRefTableModel modelOrigen;
	private ArrayList asociaciones = new ArrayList();  //  @jve:decl-index=0:
	private ArrayList viasSinReferenciar;
	private Via elementListSelected;
	private JPanel jPanelInfo = null;
	private JPanel jPanelDatos = null;
	public FileGeoreferencePanel05(String id, String nextId, PlugInContext context2) {
        
		Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
        setName(I18N.get("Georreferenciacion", "georeference.panel05.titlePanel"));
            initialize();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }
    private void initialize() {
    	try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            iconoCalle=new javax.swing.ImageIcon(cl.getResource("img/calle.jpg"));
            
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
        
    }
    
    private JPanel getJPanelDatos(){
    	
    	if (jPanelDatos == null){
    		
    		jPanelDatos   = new JPanel();
    		jPanelDatos.setLayout(new GridBagLayout());
    		
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.panel02.selectreferences"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		
            jPanelTabla.setLayout(new GridBagLayout());
            
            jTableViasBaseDatos = new JTable();
            
            jTableViasBaseDatos.setRowSelectionAllowed(true);
            jTableViasBaseDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            JScrollPane jScrollPaneSinReferenciar = new JScrollPane(jTableViasBaseDatos);

            jScrollPaneSinReferenciar.setBorder(new TitledBorder(I18N.get("Georreferenciacion","georeference.panel05.tableNoReferences")));
            actualizarModeloTabla(null);
            jTableViasSinReferenciar= new JTable();
            
            JScrollPane jScrollPaneViasOrigen = new JScrollPane(jTableViasSinReferenciar);
            jScrollPaneViasOrigen.setBorder(new TitledBorder(I18N.get("Georreferenciacion", "georeference.panel05.tableSource")));
          
            jPanelTabla.add(jScrollPaneViasOrigen, 
    				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
            jPanelTabla.add(jScrollPaneSinReferenciar, 
    				new GridBagConstraints(2,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
            jPanelResultados.setLayout(new GridBagLayout());
            
            bAsociar.setText(I18N.get("Georreferenciacion", "georeference.panel05.buttonLabelAssociate"));
            bAsociar.setEnabled(true);
            bAsociar.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    asociarVias();
                }
            });
            
            jListViasAsociadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListViasAsociadas.setCellRenderer(new RendererAsociacionesPanelFinal());        
            ListSelectionModel row = jListViasAsociadas.getSelectionModel();
            row.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                	escogerAsociacion();
                }
            });
            jListViasAsociadas.setEnabled(true);
            JScrollPane scrollViasAsociadas = new JScrollPane(jListViasAsociadas);
            scrollViasAsociadas.setBorder(new TitledBorder(I18N.get("Georreferenciacion","georeference.panel05.listAssociatedRoutes")));
            
            bEliminar.setText(I18N.get("Georreferenciacion","georeference.panel05.buttonLabelDelete"));
            bEliminar.setEnabled(false);
            bEliminar.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                   eliminarAsociacion();
                }
            });
            
            jPanelTabla.add(bAsociar, 
    				new GridBagConstraints(1,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));

            jPanelResultados.add(scrollViasAsociadas, 
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
            jPanelResultados.add(bEliminar, 
    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));


            jPanelDatos.add(jPanelTabla,
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
            jPanelDatos.add(jPanelResultados, 
    				new GridBagConstraints(0,1,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
    		
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.Info5"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}
    
    /*
     * ArrayList de PoliciaCoincidencias 
     * El Bean PoliciaCoincidencias guarda los datos del callejero, con el nombre y tipo de calle
     * y un hashtable con todos los números de policia y sus coordenadas. 
     */
    private ArrayList obtenerViasBD()
    {
           Connection con=null;
           ArrayList vias= new ArrayList();
           try {
                con = getDBConnection();
                PreparedStatement ps = con.prepareStatement("loadGeometryViasNumPolicia");
                ps.setString(1,com.geopista.security.SecurityManager.getIdMunicipio());
                ResultSet rs=ps.executeQuery();
                PoliciaCoincidencias auxPolicia=null;
                int idVias = 0;               
                while (rs.next()) {
                	if(idVias != rs.getInt("id_vias")){
                		idVias = rs.getInt("id_vias");
                		String nombreVia =  rs.getString("nombreviaine")!=null&&rs.getString("nombreviaine").length()>0?rs.getString("nombreviaine"):rs.getString("nombrecatastro");
                		auxPolicia = new PoliciaCoincidencias(nombreVia,rs.getString("tipoviaine"));
                		if(rs.getString("rotulo") != null && rs.getString("rotulo") !="" && nombreVia != null && nombreVia != ""){
                			Point point = getGeometry(rs.getString("geometria"));
                			point.setSRID(rs.getInt("srid"));
                			auxPolicia.setDatos(rs.getString("rotulo"), point);
                        	vias.add(auxPolicia);
                		}
                	}
                	else{ 
                		rs.getString("rotulo");
                		if( rs.getString("id_numeros_policia")!=null && rs.getString("id_numeros_policia").length()>0 && idVias == rs.getInt("id_vias") && rs.getString("rotulo") != null && rs.getString("rotulo") !=""){ 
                			Point point = getGeometry(rs.getString("geometria"));
	            			point.setSRID(rs.getInt("srid"));
	            			auxPolicia.setDatos(rs.getString("rotulo"), point);
	            		}
                	}
               }
           } catch (Exception e)
           {
                e.printStackTrace();
           }
           finally
           {
                app.closeConnection(con, null, null, null);
           }
           return vias;
   }
    /*
     * Conexión a la base de datos
     */
    public Connection getDBConnection()
    {
        Connection conn = null;
        try
        {
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = app.getString("geopista.conexion.url");
            conn = DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }

        return conn;

   }
    /*
     * Inserta los datos del callejero
     */
    private void actualizarModeloTabla(ArrayList policia)
    {
    	modelCallejero = new CallejeroTableModel();
        modelCallejero.setModelData(policia);
        TableSorted sorter = new TableSorted(modelCallejero);
        sorter.setTableHeader(jTableViasBaseDatos.getTableHeader());
        jTableViasBaseDatos.setModel(sorter);
        jTableViasBaseDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JComboBoxPolicia renderer = new JComboBoxPolicia();
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_TIPO).setCellRenderer(new CellColorRendererFinal(Color.RED));
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_VIA).setCellRenderer(new ViaCellRendererFinal());
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_PORTAL).setCellRenderer(renderer);
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_PORTAL).setCellEditor(new ComboBoxTEditor(new JComboBoxPolicia()));
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_TIPO).setPreferredWidth(40);     
        jTableViasBaseDatos.getColumnModel().getColumn(CallejeroTableModel.ROW_PORTAL).setPreferredWidth(20);
        jTableViasBaseDatos.setEnabled(true);
    }
    /*
     * Inserta los datos a georreferenciar
     */
    private void actualizarModeloOrigen(ArrayList vias){
    	modelOrigen = new NoGeoRefTableModel();
    	modelOrigen.setModelData(vias);
    	TableSorted sorter = new TableSorted(modelOrigen);
        sorter.setTableHeader(jTableViasSinReferenciar.getTableHeader());
        jTableViasSinReferenciar.setModel(sorter);
        jTableViasSinReferenciar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableViasSinReferenciar.getColumnModel().getColumn(NoGeoRefTableModel.ROW_NOMBRE).setCellRenderer(new RendererOrigenNombre(Color.RED));
        jTableViasSinReferenciar.getColumnModel().getColumn(NoGeoRefTableModel.ROW_DIRECCION).setCellRenderer(new ViaCellRendererFinal());
        jTableViasSinReferenciar.setEnabled(true);
    }
    /*
     * Selecciona los elementos de las dos listas (Elementos no georreferenciados
     * y callejero) y los asocia.
     */
    private void asociarVias()
    {
        if(jTableViasBaseDatos.getSelectedRow() < 0 || jTableViasSinReferenciar.getSelectedRow() < 0) return;

	    int rowCallejero = jTableViasBaseDatos.getSelectedRow();
        PoliciaCoincidencias auxVia = (PoliciaCoincidencias)jTableViasBaseDatos.getValueAt(rowCallejero, CallejeroTableModel.ROW_TIPO);
        PoliciaCoincidencias auxViaCloned = new PoliciaCoincidencias(auxVia.getCalle(),auxVia.getTipo());
       
        int rowViaOrigen = jTableViasSinReferenciar.getSelectedRow();
        Via datos = (Via) jTableViasSinReferenciar.getValueAt(rowViaOrigen,NoGeoRefTableModel.ROW_NOMBRE);
        jTableViasBaseDatos.getCellEditor(rowCallejero, CallejeroTableModel.ROW_PORTAL).getCellEditorValue();

        jTableViasBaseDatos.getCellEditor(rowCallejero, CallejeroTableModel.ROW_PORTAL).stopCellEditing();
        String i = jTableViasBaseDatos.getModel().getValueAt(rowCallejero, CallejeroTableModel.ROW_PORTAL).toString();
        PortalGeorreferenciado nuevoPortal = (PortalGeorreferenciado)auxVia.getDatos().get(Integer.parseInt(i));
        auxViaCloned.setDatos(nuevoPortal.getPortal(),nuevoPortal.getGeometria());
        
        datos.addListaCoincidencias(auxViaCloned);
        asociaciones.add(datos);
        eliminar(datos,rowViaOrigen);
        DefaultListModel listModel = new DefaultListModel();
        for(Iterator it=asociaciones.iterator(); it.hasNext();)
            listModel.addElement(it.next());
        jListViasAsociadas.setModel(listModel);
        bEliminar.setEnabled(true);
    }
    /*
     * Selecciona en la lista de elementos asociados
     */
    private void escogerAsociacion()
    {
        int selectedRow = jListViasAsociadas.getMinSelectionIndex();
        if(selectedRow < 0) return;
        ListModel auxList = jListViasAsociadas.getModel();
        elementListSelected = (Via)auxList.getElementAt(selectedRow);

    }
    /*
     * Elimina la entrada en la tabla origen
     */
    public void eliminar(Via  datos,int row)
    {
        if(datos == null) return;
	    viasSinReferenciar.remove(datos);
        datos = null;
        actualizarModeloOrigen(viasSinReferenciar);
	    if(viasSinReferenciar.size() == 0)
	        bAsociar.setEnabled(false);
    }


    public void eliminarAsociacion()
    {
        if(elementListSelected == null) return;
        Via devuelve = (Via)jListViasAsociadas.getSelectedValue();
        devuelve.dropListaCoincidencias();
        viasSinReferenciar.add(devuelve);
        actualizarModeloOrigen(viasSinReferenciar);
        asociaciones.remove(elementListSelected);
        DefaultListModel listModel = new DefaultListModel();
        for(Iterator it=asociaciones.iterator(); it.hasNext();)
            listModel.addElement(it.next());
        jListViasAsociadas.setModel(listModel);
	    if(asociaciones.isEmpty())
	        bEliminar.setEnabled(false);
	    else
	    	bEliminar.setEnabled(true);
        if (viasSinReferenciar.size()>0)
            bAsociar.setEnabled(true);
	    elementListSelected = null;
    }

    public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	} 

    /*
     * Extrae los datos de un punto de un string con formato
     * Point(x,y)
     */
    public Point getGeometry(String data){
    	WKTReader wktReader=new WKTReader();
        FeatureCollection fc = null;
		try {
			fc = wktReader.read(new StringReader(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Feature jumpFeature=(Feature)fc.iterator().next();
		return (Point) jumpFeature.getGeometry();
    	
    }

	public void enteredFromLeft(Map dataMap) {
		// TODO Auto-generated method stub
		
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(app.getMainFrame(), 
                null);

        progressDialog.setTitle(I18N.get("Georreferenciacion","georeference.panel05.progressDialog.dataProcessLabel"));
        progressDialog.report(I18N.get("Georreferenciacion","georeference.panel05.progressDialog.reportLabel"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                { 
                                	ArrayList vias = obtenerViasBD();
                                	actualizarModeloTabla(vias);
                                	viasSinReferenciar = ((ViasCollector)wizardContext.getData("noLocalizado")).getListaVias();
                                	actualizarModeloOrigen(viasSinReferenciar);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
	}

	public void exiting() {
		// TODO Auto-generated method stub
		
	}

	public void exitingToRight() throws Exception {
		// TODO Auto-generated method stub
		jListViasAsociadas.getModel().getSize();
		if (!asociaciones.isEmpty()){
			ViasCollector localizado = (ViasCollector)wizardContext.getData("localizado");
			Iterator recorreAsociaciones = asociaciones.iterator();
			while (recorreAsociaciones.hasNext())
				localizado.addVia((Via)recorreAsociaciones.next());
			wizardContext.setData("localizado", localizado);
		}
		this.setNextID("6");
	}

	public String getID() {
		// TODO Auto-generated method stub
		return localId;
	}

	public String getInstructions() {
		// TODO Auto-generated method stub
		return I18N.get("Georreferenciacion","georeference.panel05.instructions");
	}

	public String getNextID() {
		// TODO Auto-generated method stub
		return nextID;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return I18N.get("Georreferenciacion","georeference.panel05.title");
	}

	public boolean isInputValid() {
		// TODO Auto-generated method stub
		return true;
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(WizardContext wd) {
		// TODO Auto-generated method stub
		wizardContext =wd;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */

	/**
	 * This method initializes jTableSinReferenciar	
	 * 	
	 * @return javax.swing.JTable	
	 */


}
class JComboBoxPolicia extends JComboBox implements TableCellRenderer
{
    public JComboBoxPolicia()
    {
        super();
        this.setRenderer(new RendererComboPolicia());
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JComboBoxPolicia aux=new JComboBoxPolicia();
        if (isSelected)
        {
            aux.setForeground(table.getSelectionForeground());
            aux.setBackground(Color.RED);
            aux.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        else
        {
            aux.setForeground(table.getForeground());
            aux.setBackground(table.getBackground());
        }

        	
        	ArrayList coincidePolicia=(ArrayList)((PoliciaCoincidencias)table.getModel().getValueAt(row,CallejeroTableModel.ROW_TIPO)).getDatos();
        	if (coincidePolicia==null) return aux;
        	coincidePolicia = ordenarCalles(coincidePolicia);
        	Iterator lista = coincidePolicia.iterator();
        	
        	while (lista.hasNext()){
	            aux.addItem(lista.next());
	        }


        if (value!=null && value instanceof Integer){
            aux.setSelectedIndex(((Integer)value).intValue());
        }
        return aux;
    }

	private ArrayList ordenarCalles(ArrayList coincidePolicia) {
		// TODO Auto-generated method stub
		Comparator comp = new CoincidePoliciaComparator(); 
		Collections.sort(coincidePolicia, comp);
		return coincidePolicia;
	}
public static class CoincidePoliciaComparator implements Comparator {
  public int compare(Object element1, Object element2) {
    String lower1 = ((PortalGeorreferenciado)element1).getPortal().toLowerCase();
    String lower2 = ((PortalGeorreferenciado)element2).getPortal().toLowerCase();
    return lower1.compareTo(lower2);
  }
}

}
class RendererComboPolicia  extends JLabel implements ListCellRenderer
{
   public RendererComboPolicia()
   {
       setOpaque(true);
       setHorizontalAlignment(CENTER);
       setVerticalAlignment(CENTER);
   }

   /* listamos los numeros de policia en el combo */
   public Component getListCellRendererComponent(
                                      JList list,
                                      Object value,
                                      int index,
                                      boolean isSelected,
                                      boolean cellHasFocus)
   {
       if (value==null) return this;

      if (isSelected)
       {
           setBackground(list.getSelectionBackground());
           setForeground(list.getSelectionForeground());
           //setBorder(BorderFactory.createLineBorder(Color.red,2));
       }
       else
       {
           setBackground(list.getBackground());
           setForeground(list.getForeground());
       }
       setHorizontalAlignment(LEFT);
       setText(((PortalGeorreferenciado)value).getPortal());
       return this;
   }
}
/**
 * Clase para editar los numeros de policia
 */
class ComboBoxTEditor extends DefaultCellEditor
 {
     private JComboBox combo;
     public ComboBoxTEditor(JComboBox combo)
     {
         super(combo);
     }

     public Component getTableCellEditorComponent(JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  int rowIndex,
                                                  int vColIndex)
     {
         // 'value' is value contained in the cell located at (rowIndex, vColIndex)
         combo = new JComboBoxPolicia();
         if (value==null) return combo;
         Iterator numerosPolicia = ((PoliciaCoincidencias)table.getModel().getValueAt(rowIndex,CallejeroTableModel.ROW_TIPO)).getDatos().iterator();        
         while (numerosPolicia.hasNext()){
              combo.addItem(numerosPolicia.next());
         }
         return combo;
     }

     public Component getEditorComponent()
     {
        return (JComboBox)this.getComponent();
     }

     /**
      * Devuelve el item seleccionado
      * @return
      */
     public Object getCellEditorValue()
     {
         if (combo==null) 
        	 return null;
         return new Integer(combo.getSelectedIndex());
     }

     public void setEditable(boolean b)
     {
        JComboBox combo = (JComboBox)this.getComponent();
        combo.setEditable(b);
     }

     public void setEnabled(boolean b)
     {
        JComboBox combo = (JComboBox)this.getComponent();
        combo.setEnabled(b);
     }

}
//Renderer para pintar las asociaciones
class RendererAsociacionesPanelFinal extends DefaultListCellRenderer
{

   public Component getListCellRendererComponent(JList l,
          Object value, int i, boolean s, boolean f)
   {
        JLabel label = new JLabel();
		Via valor = (Via)value;

		ArrayList poli = (ArrayList)valor.getListaCoincidencias();
		PoliciaCoincidencias resultado = (PoliciaCoincidencias)poli.get(0);
		String result=resultado.getTipo()+" "+resultado.getCalle()+" "+((PortalGeorreferenciado)resultado.getDatos().get(0)).getPortal();
		label.setText(valor.getNombre()+" "+valor.getCalle()+" =====> "+result);
        if (FileGeoreferencePanel05.iconoCalle!=null)
            label.setIcon(FileGeoreferencePanel05.iconoCalle);
        label.setBackground(Color.ORANGE);
        if(s)
        	label.setBorder(BorderFactory.createLineBorder(Color.RED));
	   		
        return label;
   }
}
class RendererGeorreferenciadasFinal extends DefaultListCellRenderer
{

public Component getListCellRendererComponent(JList l,
       Object value, int i, boolean s, boolean f)
{
		JLabel label = new JLabel(); 
		Via valor = (Via)value;
		label.setText(valor.getNombre()+" "+valor.getCalle());
        if (FileGeoreferencePanel05.iconoCalle!=null)
            label.setIcon(FileGeoreferencePanel05.iconoCalle);
        label.setBackground(Color.ORANGE);
	   		
        return label;
}
}
/*
 * Dibuja el contenido de la columna nombre
 * La columna nombre contiene un tipo via
 */
class RendererOrigenNombre implements TableCellRenderer{
	Color color;
    public RendererOrigenNombre(Color color)
    {
        this.color=color;
    }
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column){
				JLabel aux = new JLabel("");
				Via valor = (Via)value;
				
				if (value!=null)
					aux = new JLabel(valor.getNombre());
				if (isSelected)
					aux.setBorder(BorderFactory.createLineBorder(color));
				return aux;
    }
}
class CellColorRendererFinal  implements TableCellRenderer
{
    Color color;
    public CellColorRendererFinal(Color color)
    {
        this.color=color;
    }
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JLabel aux = new JLabel("");
        PoliciaCoincidencias valor = (PoliciaCoincidencias)value;
        
        if (value!=null)
            aux = new JLabel(valor.getTipo());
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(color));
         }
        return aux;
    }
}
class ViaCellRendererFinal  implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JLabel aux = new JLabel((String)value);
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
    }
}