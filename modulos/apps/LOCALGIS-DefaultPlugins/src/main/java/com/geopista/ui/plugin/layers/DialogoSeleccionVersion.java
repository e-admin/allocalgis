/**
 * DialogoSeleccionVersion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.layers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.GeopistaListaMapasPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;

public class DialogoSeleccionVersion extends javax.swing.JDialog
{
    private JPanel panelBasico;
    private JPanel panelAvanzado;
    private JFormattedTextField jTextHora;
    private JLabel jLabel1 = new JLabel();
    private JButton jButtonBuscar = new JButton();
    private JCheckBox jCheck = new JCheckBox();
    private JLabel jLabel3 = new JLabel();
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    com.toedter.calendar.JCalendar jCalendar1 = new com.toedter.calendar.JCalendar();
    com.toedter.calendar.JCalendar jCalendar2 = new com.toedter.calendar.JCalendar();
    private GeopistaLayer layer = null;
    private JPanel parent = null;
    private DefaultTableModel model = new DefaultTableModel () {
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return Boolean.class;
            }
            else {
                return super.getColumnClass(columnIndex);
            }
        }
        public boolean isCellEditable (int rowIndex, int columnIndex) {
        	if (columnIndex == 4 || columnIndex == 3){
        		return true;
        	}else{
        		super.isCellEditable(rowIndex, columnIndex);
        	}
        	return false;
        }
    };

  
    public void iniciarInternacionalizacion(){
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.layers.languages.Versioni18n",loc,this.getClass().getClassLoader());    	
    	I18N.plugInsResourceBundle.put("Version",bundle2);    	
    }
    
    public DialogoSeleccionVersion(Frame parent,GeopistaLayer layer ){
		super(parent, true);
		try{
		    iniciarInternacionalizacion();
		    this.layer = layer;
		    ConstantesRegistro.calendarValue = "";
		    this.setTitle(I18N.get("Version","SeleccionVersion"));
		    this.setLocation(300, 20);
		    jbInit();
		    aplicacion.getBlackboard().put("cargarFeatures", true);
		    addWindowListener(new java.awt.event.WindowAdapter() {
		    	public void windowClosing(java.awt.event.WindowEvent evt) {
		    		aplicacion.getBlackboard().put("cargarFeatures", false);
		    	}
		    });
		}
		catch(Exception e){
	            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e.getMessage());
		}
    }
    
    public DialogoSeleccionVersion(Frame frame){
		super(frame,true);
		try	{
		    iniciarInternacionalizacion();
		    ConstantesRegistro.calendarValue = "";
		    this.setTitle(I18N.get("Version","SeleccionVersion"));
		    this.setLocation(300, 20);
		    this.getContentPane().setLayout(null);
		    this.setSize(new Dimension(620, 580));
		    JTabbedPane panelCalendarios = new JTabbedPane();
		    addPanelBasico(); 
		    panelCalendarios.addTab(I18N.get("Version","Basico"), null, panelBasico, I18N.get("Version","Basico")); 
		    panelCalendarios.setBounds(0, 0, 600, 550);
		    this.getContentPane().add(panelCalendarios, null);
		    aplicacion.getBlackboard().put("cargarFeatures", true);
		    addWindowListener(new java.awt.event.WindowAdapter() {
		       	public void windowClosing(java.awt.event.WindowEvent evt) {
		       	    aplicacion.getBlackboard().put("cargarFeatures", false);
		        }
		    });
		}
		catch(Exception e){
	        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e.getMessage());
		}
    }
    public DialogoSeleccionVersion(Frame frame, JPanel parent){
    	this(frame);
		this.parent = parent;
    }

    private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(620, 580));
		JTabbedPane panelCalendarios = new JTabbedPane();
		addPanelBasico(); 
		addPanelAvanzado(); 
		panelCalendarios.addTab(I18N.get("Version","Basico"), null, panelBasico, I18N.get("Version","Basico")); 
		panelCalendarios.addTab(I18N.get("Version","Avanzado"), null, panelAvanzado, I18N.get("Version","Avanzado"));
		panelCalendarios.setBounds(0, 0, 600, 550);
		this.getContentPane().add(panelCalendarios, null);
    }

    private void addPanelBasico(){
		GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints4 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints5 = new java.awt.GridBagConstraints();
		
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.weighty = 0.2;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 0, 5);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.insets = new java.awt.Insets(45, 5, 40, 5);

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new java.awt.Insets(45, 5, 40, 5);

        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.gridwidth = 1;
        gridBagConstraints4.insets = new java.awt.Insets(5, 5, 40, 5);

        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.gridwidth = 1;
        gridBagConstraints5.insets = new java.awt.Insets(5, 5, 40, 5);

        panelBasico = new JPanel();
        panelBasico.setLayout(new java.awt.GridBagLayout());
		try{
		    MaskFormatter mascara = new MaskFormatter("##:##:##");
		    jTextHora = new JFormattedTextField(mascara);
		}catch(ParseException e1){
	            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e1.getMessage());
		}
		jTextHora.setText(getHora());
		jLabel1.setText(I18N.get("Version","Hora"));
	    jCalendar1.setBorder(new javax.swing.border.EtchedBorder());
		JButton jButton1 = new JButton();
		jButton1.setText(I18N.get("Version","btnAceptar"));
		jButton1.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		      	almacenarFecha(jCalendar1);
		       	if (jTextHora.getText().equals(""))
		       	    JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Debe introducir una hora válida");
		       	ConstantesRegistro.calendarValue += " " + jTextHora.getText();
		        dispose();
		    }
		});
		JButton jButton2 = new JButton();
		jButton2.setText(I18N.get("Version","btnCancelar"));
		jButton2.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	if (parent != null)
		    		((GeopistaListaMapasPanel)parent).setCalendario(false);
		        dispose();
		    }
		});
		panelBasico.add(jLabel1, gridBagConstraints2);
		panelBasico.add(jTextHora, gridBagConstraints3);
		panelBasico.add(jCalendar1, gridBagConstraints1);
		panelBasico.add(jButton1, gridBagConstraints4);
		panelBasico.add(jButton2, gridBagConstraints5);
    }

    private void addPanelAvanzado(){
		panelAvanzado = new JPanel();
		panelAvanzado.setLayout(new java.awt.GridBagLayout());
	
		GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints4 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints5 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints6 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints7 = new java.awt.GridBagConstraints();
		
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 0, 5);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 5);

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.weightx = 0.2;
        gridBagConstraints3.insets = new java.awt.Insets(5, 5, 0, 5);

        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 3;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.gridwidth = 2;
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints4.insets = new java.awt.Insets(5, 5, 0, 5);
		
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 0.2;
        gridBagConstraints5.gridwidth = 5;
        gridBagConstraints5.insets = new java.awt.Insets(5, 5, 0, 5);
		
        gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.gridy = 5;
        gridBagConstraints6.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints6.weightx = 1.0;
        gridBagConstraints6.gridwidth = 2;
        gridBagConstraints6.insets = new java.awt.Insets(5, 5, 15, 5);
		
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 4;
        gridBagConstraints7.gridy = 5;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.gridwidth = 2;
        gridBagConstraints7.insets = new java.awt.Insets(5, 5, 15, 5);


        //array de String's con los títulos de las columnas
        final String[] columnNames = {I18N.get("Version","Revision"), I18N.get("Version","Fecha"), I18N.get("Version","gestoreventos.autor"), I18N.get("Version","Oficial"),I18N.get("Version","Definicion")};

        //se crea la Tabla
        model.setDataVector(obtenerVersiones(""), columnNames);
        final JTable tableVersiones = new JTable(model);
        tableVersiones.setPreferredScrollableViewportSize(new Dimension(600, 70));
        tableVersiones.setDefaultRenderer(JCheckBox.class, new RenderTabla());
        this.fijarAnchoColumnas(tableVersiones.getColumnModel());

        jCalendar2.setBounds(new Rectangle(100, 70, 335, 135));       
        jCalendar2.setBorder(new javax.swing.border.EtchedBorder());
		jButtonBuscar.setText(I18N.get("Version","inventario.informes.tag2"));
		jButtonBuscar.setBounds(new Rectangle(200, 220, 100, 20));
		jButtonBuscar.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        almacenarFecha(jCalendar2);	        	
		        DefaultTableModel model = ((DefaultTableModel)tableVersiones.getModel());
		        model.setDataVector(obtenerVersiones(getFecha()), columnNames);
		        tableVersiones.setModel(model);
		    }
		});
		jLabel3.setBounds(150, 35, 160, 25);
		jLabel3.setText(I18N.get("Version","Fecha")+" "+I18N.get("Version","LegendStyle.desde")+":");
		jCheck.setText(I18N.get("Version","HabilitarFiltro"));
		jCheck.setBounds(210, 10, 160, 25);
		jCheck.addItemListener(new ItemListener(){
		    public void itemStateChanged(ItemEvent evt){
		        if (jCheck.isSelected()){
		            jButtonBuscar.setEnabled(true);
		            jLabel3.setEnabled(true);
		            jCalendar2.setEnabled(true);
		        }else{
		            jButtonBuscar.setEnabled(false);
		            jLabel3.setEnabled(false);
		            jCalendar2.setEnabled(false);
		        }
		    }
		});
        
        //Creamos un JscrollPane y le agregamos la JTable
        JScrollPane scrollPane = new JScrollPane(tableVersiones);
        scrollPane.setBounds(new Rectangle(100, 270, 365, 200));
		jCalendar2.setEnabled(false);
		jButtonBuscar.setEnabled(false);
		jLabel3.setEnabled(false);
	
		JButton jButtonCargar = new JButton();
		jButtonCargar.setText(I18N.get("Version","CargarRevision"));
		jButtonCargar.setBounds(new Rectangle(100, 480, 150, 20));
		jButtonCargar.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        if (tableVersiones.getSelectedRow() == -1)
		            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),I18N.get("Version","SeleccionarRevision"));
		        else{
		            int row = tableVersiones.getSelectedRow();
		            layer.setRevisionActual((Long)tableVersiones.getValueAt(row, 0));
		            dispose();
		        }
		    }
		});
		JButton jButtonGuardar = new JButton();
		jButtonGuardar.setText(I18N.get("Version","GuardarRevisionOficial"));
		jButtonGuardar.setBounds(new Rectangle(300, 480, 200, 20));
		jButtonGuardar.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        guardarRevisionesOficiales();
		    }
		});
		panelAvanzado.add(jCalendar2, gridBagConstraints3);
		panelAvanzado.add(scrollPane, gridBagConstraints5);
		panelAvanzado.add(jButtonBuscar, gridBagConstraints4);
		panelAvanzado.add(jCheck, gridBagConstraints1);
		panelAvanzado.add(jLabel3, gridBagConstraints2);
		panelAvanzado.add(jButtonCargar, gridBagConstraints6);
		panelAvanzado.add(jButtonGuardar, gridBagConstraints7);
    }

    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con = AppContext.getApplicationContext().getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    /**
     * Obtiene las versiones disponibles a partir de una fecha de inicio
     * @param fechaInicio
     * @return
     * @throws SQLException
     */
    private Object[][] obtenerVersiones(String fechaInicio){
    	Object[][] datosTabla = null;
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
	    	conn = getDBConnection();
    		StringBuffer consultaTable = new StringBuffer("(select c.id_table from layers l, attributes a,columns c ");
    		consultaTable = consultaTable.append("where l.id_layer = a.id_layer and a.id_column = c.id ");
    		consultaTable = consultaTable.append("and a.position = 1 and l.name = ?)");
	    	StringBuffer sbSQL = new StringBuffer("select v.revision,v.id_autor,to_char(v.fecha,'dd-MM-yyyy HH24:MI:ss') as fecha,i.name,v.oficial,v.mensaje from versiones v, iuseruserhdr i ");
	    	sbSQL = sbSQL.append("where v.id_autor=i.id ");
	    	sbSQL = sbSQL.append("and v.id_table_versionada= ");
	    	sbSQL = sbSQL.append(consultaTable.toString());
	    	if (!fechaInicio.equals(""))
	    		sbSQL = sbSQL.append("and fecha >=to_date(?,'yyyy-MM-dd') and fecha<localtimestamp");
	    	sbSQL = sbSQL.append(" order by v.revision");
	    	ps = conn.prepareStatement(sbSQL.toString());
    		ps.setString(1, this.layer.getSystemId());
	    	if (!fechaInicio.equals(""))
	    		ps.setString(2, fechaInicio);
	    	rs = ps.executeQuery();
	    	Vector vecVersiones = new Vector();
	    	while (rs.next()){
	    		JCheckBox checkBox = new JCheckBox();
	    		Object[] arrVersiones = new Object[5];
	    		arrVersiones[0]=rs.getLong("revision");
	    		arrVersiones[1]=rs.getString("fecha");
	    		arrVersiones[2]=rs.getString("name");
	    		arrVersiones[3]=new Boolean(rs.getInt("oficial")==1?true:false);
	    		arrVersiones[4]=rs.getString("mensaje");
	        	vecVersiones.add(arrVersiones);
	    	}
	    	datosTabla = (Object[][]) vecVersiones.toArray(vecVersiones.toArray(new Object[vecVersiones.size()][5]));
    	}catch(Exception e){
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e.getMessage());
	    }finally{
            aplicacion.closeConnection(conn, ps, null, rs);
	    	return datosTabla;
	    }
    }
    
    private void almacenarFecha(com.toedter.calendar.JCalendar jCalendar){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String fecha=df.format(jCalendar.getCalendar().getTime());
		ConstantesRegistro.calendarValue = fecha;
    }
    
    public String getFecha(){
    	return ConstantesRegistro.calendarValue;
    }
    
    /**
     * Obtiene la revisión más actual que tenemos para esa capa
     * @return
     */
    public long getUltimaRevision(){
    	if (this.layer != null){
	    	Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	long revisionActual = -1;
	    	try{
		    	conn = getDBConnection();
		    	ps = conn.prepareStatement("getUltimaRevision");
	    		ps.setString(1, this.layer.getSystemId());
		    	rs = ps.executeQuery();
		    	if (rs.next()){
		    		revisionActual = rs.getLong(1);
		    	}else
		    		revisionActual = -1;
	    	}catch(Exception e){
	            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e.getMessage());
		    }finally{
	            aplicacion.closeConnection(conn, ps, null, rs);
	            return revisionActual;
		    }
    	}
    	return -1;
    }
    
    /**
     * Almacena si las revisiones son oficiales o no
     * @return
     */
    public void guardarRevisionesOficiales(){
    	Connection conn = null;
    	PreparedStatement ps = null;
    	try{
	    	conn = getDBConnection();
    		StringBuffer consultaTable = new StringBuffer("(select c.id_table from layers l, attributes a,columns c ");
    		consultaTable = consultaTable.append("where l.id_layer = a.id_layer and a.id_column = c.id ");
    		consultaTable = consultaTable.append("and a.position = 1 and l.name = ?)");
	    	StringBuffer sbSQL = new StringBuffer("update versiones set oficial=?, mensaje =? where revision=? ");
	    	sbSQL = sbSQL.append("and id_table_versionada = ");
	    	sbSQL = sbSQL.append(consultaTable.toString());
	    	ps = conn.prepareStatement(sbSQL.toString());
	    	int n = model.getRowCount();
	    	for (int i=0;i<n;i++){
	    		ps.setInt(1, (Boolean)model.getValueAt(i,3)==true?1:0);
	    		ps.setString(2, (String)model.getValueAt(i, 4));
	    		ps.setLong(3, (Long)model.getValueAt(i, 0));
	    		ps.setString(4, this.layer.getSystemId());
	    		ps.executeUpdate();
	    	}
	        JOptionPane.showMessageDialog(this,I18N.get("Version","DatosRevisionGuardados"));
    	}catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
	}finally{
            aplicacion.closeConnection(conn, ps, null, null);
	}
    }
    

    /**
     * Clase que implementa TableCellRenderer para pintar el CheckBox
     */
    public class RenderTabla extends JLabel implements TableCellRenderer
    {
        /**
         * Constructor por defecto.
         */
        public RenderTabla()
       {
        }
        
        /**  Returns the component used for drawing the cell.  This method is
         *  used to configure the renderer appropriately before drawing.
         *
         * @param	table		the <code>JTable</code> that is asking the
         * 				renderer to draw; can be <code>null</code>
         * @param	value		the value of the cell to be rendered.  It is
         * 				up to the specific renderer to interpret
         * 				and draw the value.  For example, if
         * 				<code>value</code>
         * 				is the string "true", it could be rendered as a
         * 				string or it could be rendered as a check
         * 				box that is checked.  <code>null</code> is a
         * 				valid value
         * @param	isSelected	true if the cell is to be rendered with the
         * 				selection highlighted; otherwise false
         * @param	hasFocus	if true, render cell appropriately.  For
         * 				example, put a special border on the cell, if
         * 				the cell can be edited, render in the color used
         * 				to indicate editing
         * @param	row	        the row index of the cell being drawn.  When
         * 				drawing the header, the value of
         * 				<code>row</code> is -1
         * @param	column	        the column index of the cell being drawn
         *
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
           boolean isSelected, boolean hasFocus, int row, int column) 
        {
            JCheckBox chOficial = new JCheckBox();
            
            if (value instanceof Integer)
            {
                int valor = ((Integer)value).intValue();
                if (valor == 0)
                	chOficial.setSelected(false);
                else
                	chOficial.setSelected(true);
            }
            
            return chOficial;
        }
   }
   
    private String getHora(){
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String hora = "";
    	try{
    		conn = getDBConnection();
    		String sSQL = "SELECT TO_CHAR(localtimestamp, 'HH24:MI:ss')";
    		ps = conn.prepareStatement(sSQL);
    		rs = ps.executeQuery();
    		if (rs.next())
    			hora = rs.getString(1);
    	}catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());    		
	    }finally{
            aplicacion.closeConnection(conn, ps, null, rs);
            return hora;
	    }
    }
    
    private void fijarAnchoColumnas(TableColumnModel columnModel){
    	int n = columnModel.getColumnCount();
    	int ancho = 0;
    	int anchoTotal = columnModel.getTotalColumnWidth();
    	int anchoColumna = anchoTotal /100;
    	for (int i=0;i<n;i++){
    		switch (i){
    			case 0:
    				ancho = anchoColumna*5;
    				break;
    			case 1:
    				ancho = anchoColumna*20;
    				break;
    			case 2:
    				ancho = anchoColumna*20;
    				break;
    			case 3:
    				ancho = anchoColumna*5;
    				break;
    			case 4:
    				ancho = anchoColumna*50;
    		}
    		columnModel.getColumn(i).setPreferredWidth(ancho);
    		
    	}
    }
}