/**
 * CompareJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.inventario.edit.compare.geometries;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.util.config.UserPreferenceStore;

public class CompareJPanel extends JDialog {

	Logger logger = Logger.getLogger(CompareJPanel.class);
	private AppContext aplicacion;
	private InventarioClient inventarioClient;

	private static final int TAM_Y = 500;
	private static final int TAM_X = 800;

	private ResourceBundle literales;
	private JFrame desktop;
	private JButton salirJButton;
	private CompareBienJTable compareJTable;
	private JScrollPane compareJScrollPane;
	private CompareTableModel compareTM;
	private TableSorted compareSorted;
	private String locale;
	private ArrayList<InmuebleBean> bienes;
	private ArrayList<Double> features;
	private double tolerancia;

	public CompareJPanel(JFrame main, double tol,
			ArrayList<InmuebleBean> bienesFiltrados,
			ArrayList<Double> featuresFiltrados, ResourceBundle literales) {
		// TODO Auto-generated constructor stub
		super(main, true);
		this.literales = literales;
		this.desktop = main;
		this.bienes = bienesFiltrados;
		this.tolerancia = tol;
		this.features = featuresFiltrados;
		aplicacion = (AppContext) AppContext.getApplicationContext();
		this.locale = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)
						+ ServletConstants.INVENTARIO_SERVLET_NAME);

		initComponents();

	}

	private void initComponents() {

		JPanel jPanelBotonera = getBotonera();
		JPanel jPanelTabla = getPanelTabla();

		Dimension d = new Dimension(TAM_X, TAM_Y);
		this.setSize(d);
		this.setPreferredSize(d);
		this.setMaximumSize(this.getPreferredSize());
		this.setMinimumSize(this.getPreferredSize());

		this.setLayout(new GridBagLayout());
		this.add(jPanelTabla, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0));
		this.add(jPanelBotonera, new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets(0, 10, 0, 10), 0, 0));
		pack();

	}

	private JPanel getBotonera() {

		salirJButton = new JButton("Salir");
		salirJButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				salirJButtonMouseClicked();
			}
		});

		JPanel jPanel = new JPanel();
		Dimension d = new Dimension(TAM_X, 50);
		jPanel.setSize(d);
		jPanel.setPreferredSize(d);
		jPanel.setLayout(new GridBagLayout());
		jPanel.add(salirJButton, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,
						0, 3, 2), 0, 0));

		return jPanel;
	}

	private JPanel getPanelTabla() {

		compareJTable = new CompareBienJTable();

		try {
			// String[] columnNamesEventos = {
			// literales.getString("CBienesPreALta.bienesTableModel.text.column1"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column2"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column3"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column4"),
			// literales.getString("CBienesPreALta.bienesTableModel.text.column5"),
			// "HIDDEN" };

			String[] columnNames = { "Nº Bien", "Superf. Catastral",
					"Superf. Real", "Superf. Registral", "Superf. Geometrica",
					"HIDDEN" };

			// bienesTM.setColumnNames(columnNames);

			compareTM = new CompareTableModel(columnNames, new boolean[] {
					false, false, false, false, false, false }, locale, true);
		} catch (Exception e) {
			logger.error("Error al poner el titulo de las tablas.", e);
		}
		compareSorted = new TableSorted(compareTM);
		compareSorted.setTableHeader(compareJTable.getTableHeader());
		compareJTable.setModel(compareSorted);
		compareTM.setTableSorted(compareSorted);
		compareTM.setTable(compareJTable);

		setInvisible(compareTM.getColumnCount() - 1, compareJTable);

		compareJTable.initRenderer();

		compareJScrollPane = new JScrollPane();
		compareJScrollPane.setViewportView(compareJTable);
		Dimension d = new Dimension(800, 500);
		compareJScrollPane.setSize(d);
		compareJScrollPane.setPreferredSize(d);
		compareJScrollPane.setBorder(new TitledBorder(
				"Comparacion de Geometrías"));

		JPanel bienesJPanel = new JPanel(new GridLayout());

		bienesJPanel.add(compareJScrollPane, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(3, 0, 3, 0), 0, 0));

		return bienesJPanel;
	}

	/**
	 * Método que hace un columna de la tabla no visible
	 */
	private void setInvisible(int column, JTable jTable) {
		/** columna hidden no visible */
		TableColumn col = jTable.getColumnModel().getColumn(column);
		col.setResizable(false);
		col.setWidth(0);
		col.setMaxWidth(0);
		col.setMinWidth(0);
		col.setPreferredWidth(0);
	}

	public boolean rellenaTabla() {

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (bienes.size() == 0) {
			dispose();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return false;
		}

		double superfCat;
		double superfReal;
		double superfReg;
		double superfGeom;

		InmuebleBean b;
		for (int i = 0; i < bienes.size(); i++) {
			superfGeom = features.get(i);
			DecimalFormat df1 = new DecimalFormat("##0.00");
			String geomFormat = df1.format(superfGeom);
			b = bienes.get(i);

			superfCat = b.getSuperficieCatastralSuelo();
			superfReal = b.getSuperficieRealSuelo();
			superfReg = b.getSuperficieRegistralSuelo();

			try {
//				compareTM.annadirRow(b.getNumInventario(), superfCat,
//						superfReal, superfReg,
//						Double.valueOf(geomFormat.replace(",", ".")), i);
				
				compareTM.annadirRow(b.getNumInventario(), df1.format(superfCat),
						df1.format(superfReal), df1.format(superfReg),
						df1.format(superfGeom), i);
			} catch (Exception e) {
				logger.error("Error al cargar las bienes en precarga pendientes ");
				e.printStackTrace();
			}

		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;

	}

	protected void salirJButtonMouseClicked() {
		// TODO Auto-generated method stub
		dispose();

	}

	public void renombrarComponentes(ResourceBundle literales) {
		// TODO Auto-generated method stub

	}

	public class CompareTableModel extends DefaultTableModel {
		public String[] columnNames;
		public boolean[] columnEditables;
		private String locale;
		private Hashtable rows = new Hashtable();
		private TableSorted tableSorted;
		private JTable tabla;
		private boolean showSeleccion = false;

		public CompareTableModel(String[] colNames, boolean[] colEditables,
				String locale, boolean showSeleccion) {
			columnNames = colNames;
			columnEditables = colEditables;
			this.showSeleccion = showSeleccion;
			this.locale = locale;
			new DefaultTableModel(columnNames, 0);

		}

//		public void annadirRow(String numInventario, double superfCat,
//				double superfReal, double superfReg, double suferfGeom,
//				int indice) {
		public void annadirRow(String numInventario, String superfCat,
				String superfReal, String superfReg, String suferfGeom,
					int indice) {
			if (tableSorted == null)
				return;
			// rows.put((((BienPreAltaBean)obj).getId()), obj);
			Object[] rowData = { numInventario, superfCat, superfReal,
					superfReg, suferfGeom };
			rows.put(numInventario, rowData);
			tableSorted.addRow(rowData);

		}

		public void setTableSorted(TableSorted tableSorted) {
			this.tableSorted = tableSorted;
		}

		public TableSorted getTableSorted() {
			return this.tableSorted;
		}

		public void setColumnNames(String[] colNames) {
			columnNames = colNames;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (columnEditables[col])
				return true;
			return false;
		}

		public void setTable(javax.swing.JTable t) {
			this.tabla = t;
		}

		public void clearModel() {
			if (tableSorted == null)
				return;
			for (int i = 0; i < tableSorted.getRowCount();) {
				tableSorted.removeRow(i);
			}
		}

		public double getObjetAt(int r, int c) {
			return (Double) tableSorted.getValueAt(r, c);

		}

		public Object[] getValueAt(int row) {
			if (row == -1)
				return null;
			if (tableSorted == null)
				return null;
			Object[] result = null;
			try {
				result = (Object[]) rows.get(""
						+ (String) tableSorted.getValueAt(row, 0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		}

	}

	public class CompareBienJTable extends JTable {

		private int bienesHiddenCol = 6;

		public CompareBienJTable() {
			this.setBorder(new LineBorder(new Color(0, 0, 0)));
			this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			this.setFocusCycleRoot(true);
			this.setSurrendersFocusOnKeystroke(true);
			this.getTableHeader().setReorderingAllowed(false);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		}

		public void initRenderer() {
			MyRenderer miRender = new MyRenderer();

			for (int i = 0; i < (compareTM.getColumnCount()); i++) {
				if (i == 0)
					compareJTable.setDefaultRenderer(String.class, miRender);
				else
					compareJTable.setDefaultRenderer(Double.class, miRender);
			}
			// TableColumn column = null;
			// int headerWidth = 0;
			// int cellWidth = 0;
			// Component comp = null;
			//
			// TableCellRenderer headerRenderer = compareJTable.getTableHeader()
			// .getDefaultRenderer();
			// for (int i = 1; i < 5; i++) {
			// column = compareJTable.getColumnModel().getColumn(i);
			//
			// comp = headerRenderer.getTableCellRendererComponent(null,
			// column.getHeaderValue(), false, false, 0, 0);
			// headerWidth = comp.getPreferredSize().width;
			//
			// comp = compareJTable
			// .getDefaultRenderer(compareTM.getColumnClass(i))
			// .getTableCellRendererComponent(compareJTable, null, false,
			// false, 0, i);
			// cellWidth = comp.getPreferredSize().width;
			// column.setPreferredWidth(Math.max(headerWidth, cellWidth));
			// }
		}

		// @Override
		// public Component prepareRenderer(TableCellRenderer renderer, int row,
		// int column){
		// Component returnComp = null;
		//
		// try{
		// returnComp = super.prepareRenderer(renderer, row,column);
		//
		// double superfCat = (Double) getValueAt(row,this.getColumnCount()-5);
		// double superfReal = (Double) getValueAt(row,this.getColumnCount()-4);
		// double superfReg = (Double) getValueAt(row,this.getColumnCount()-3);
		// double superfGeom = (Double) getValueAt(row,this.getColumnCount()-2);
		//
		//
		// if(returnComp == null) return null;
		//
		// double superMax = superfGeom*(1+tolerancia);
		// double superMin = superfGeom*(1-tolerancia);
		//
		//
		// if (superfCat < superMin || superfCat > superMax
		// || superfReal < superMin || superfReal > superMax
		// || superfReg < superMin || superfReg > superMax) {
		// returnComp.setBackground(Color.RED);
		// returnComp.setForeground(Color.WHITE);
		// }
		// if (superfCat<superMin || superfCat>superMax){
		// Component cell1 = (Component) super.getCellRenderer(row, 2);
		// cell1.setBackground(Color.RED);
		// cell1.setForeground(Color.WHITE);
		// }
		//
		// if (superfReal<superMin || superfReal>superMax){
		// Component cell2 = (Component) super.getCellRenderer(row, 3);
		// cell2.setBackground(Color.BLUE);
		// cell2.setForeground(Color.WHITE);
		// }
		//
		// if (superfReg<superMin || superfReg>superMax){
		// Component cell3 = (Component) super.getCellRenderer(row, 4);
		//
		// cell3.setBackground(Color.GREEN);
		// cell3.setForeground(Color.WHITE);
		// }

		// }catch(Exception e){
		// logger.error("prepareRenderer() - Error obteniendo datos de celda");
		// e.printStackTrace();
		// }
		// return returnComp;
		//
		// }

	}

	class MyRenderer extends DefaultTableCellRenderer {

		//Seleccionado Normal
		private final Color SELECTED_BG = Color.BLUE;
		private final Color SELECTED_FG = Color.WHITE;
		//No Seleccionado Normal
		private final Color NSELECTED_BG = Color.WHITE;
		private final Color NSELECTED_FG = Color.BLACK;
		
		//Seleciconado Erronea
		private final Color SELECTED_ERROR_BG = Color.CYAN;
		private final Color SELECTED_ERROR_FG = Color.BLACK;
		//No Seleccionado Erronea
		private final Color NSELECTED_ERROR_BG = Color.RED;
		private final Color NSELECTED_ERROR_FG = Color.WHITE;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			
			Object[] obj = compareTM.getValueAt(row);

			String geomString = (String)obj[4];
			double geom = Double.valueOf(geomString.replace(",", "."));
			
			double max = geom * (1 + tolerancia);
			double min =  geom * (1 - tolerancia);
			double valor = 0;

			if ((column > 0 && column < 4)) {
				valor = Double.valueOf(((String)value).replace(",", "."));
				if ((valor < min) || (valor > max)) {
					if (isSelected){
						this.setBackground(SELECTED_ERROR_BG);
						this.setForeground(SELECTED_ERROR_FG);						
					}else{
						this.setBackground(NSELECTED_ERROR_BG);
						this.setForeground(NSELECTED_ERROR_FG);
					}
				} else {
					
					if (isSelected){
						this.setBackground(SELECTED_BG);
						this.setForeground(SELECTED_FG);						
					}else{
						this.setBackground(NSELECTED_BG);
						this.setForeground(NSELECTED_FG);
					}
				}
			} else {
				if (isSelected){
					this.setBackground(SELECTED_BG);
					this.setForeground(SELECTED_FG);						
				}else{
					this.setBackground(NSELECTED_BG);
					this.setForeground(NSELECTED_FG);
				}
			}

			return this;
		}

	}

}
