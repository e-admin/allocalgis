/**
 * FileGeoreferencePanel04.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georeference.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.ui.plugin.georeference.GeoRefTableModel;
import com.geopista.ui.plugin.georeference.beans.PoliciaCoincidencias;
import com.geopista.ui.plugin.georeference.beans.PortalGeorreferenciado;
import com.geopista.ui.plugin.georeference.beans.Via;
import com.geopista.ui.plugin.georeference.beans.ViasCollector;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * @author rubengomez
 * Formulario que recibe un arrayList de localizadoMultiple y despliega una tabla
 * con los posibles valores en un comboBox, con una elección por defecto
 */
public class FileGeoreferencePanel04 extends javax.swing.JPanel implements WizardPanel {


    private WizardContext wizardContext;  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JLabel lbllayer = null;
	private String localId = null;  //  @jve:decl-index=0:
    private String nextID = null;  //  @jve:decl-index=0:
    private PlugInContext   context;
    AppContext app = (AppContext) AppContext.getApplicationContext();  //  @jve:decl-index=0:
    GeoRefTableModel panelMultiple;
	private JPanel jPanelInfo = null;
	private JPanel jPanelDatos = null;
    public FileGeoreferencePanel04(String id, String nextId, PlugInContext context2) {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
            setName(I18N.get("Georreferenciacion", "georeference.panel04.titlePanel"));
            initialize();
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }
	private void initialize(){

        lbllayer = new JLabel();
        lbllayer.setBounds(new java.awt.Rectangle(24,43,107,21));
        lbllayer.setText(I18N.get("Georreferenciacion","georeference.panel04.layerLabel"));
        
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
    		
    		jPanelDatos.add(getJScrollPane(), 
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    	}
    	return jPanelDatos;
    }
	
	private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.Info6"));
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
	
	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void enteredFromLeft(Map dataMap) {

		if (wizardContext.getData("localizadoMultiple")!= null){
		refrescarTabla(((ViasCollector) wizardContext.getData("localizadoMultiple")).getListaVias());
		}
        jTable.setEnabled(true);
        //metodoDePrueba();
	}

	public void exiting() {
		// TODO Auto-generated method stub
		
	}

	public void exitingToRight() throws Exception {
		// TODO Auto-generated method stub
		/*
		 * Almacena los valores en el arrayList localizado segun sale por la derecha
		 */
		ViasCollector localizado = (ViasCollector)wizardContext.getData("localizado");
		 
		 
		for (int i=0;i<jTable.getRowCount();i++){
			 
			jTable.getCellEditor(i,2).stopCellEditing();		
			Via valor = (Via)jTable.getValueAt(i,0);
			PoliciaCoincidencias nuevaLista = (PoliciaCoincidencias)(PoliciaCoincidencias)valor.getListaCoincidencias().get(Integer.parseInt(jTable.getValueAt(i, 2).toString()));
			valor.removeListaCoincidencias();
			valor.addListaCoincidencias(nuevaLista);
			localizado.addVia(valor);
			
		}
		wizardContext.setData("localizado", localizado);
		wizardContext.setData("localizadoMultiple", null);
		if ((((ViasCollector)wizardContext.getData("noLocalizado"))).getListaVias().size()>0){
			this.nextID = "5";
		}
		else
			this.nextID = "6";
		
	}

	public String getInstructions() {
		// TODO Auto-generated method stub
		return I18N.get("Georreferenciacion", "georeference.panel04.instructions");
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return I18N.get("Georreferenciacion", "georeference.panel04.title");
	}

	public boolean isInputValid() {
		// TODO Auto-generated method stub
		return true;
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}
	   public void setWizardContext(WizardContext wd)
	    {
	      wizardContext =wd;
	    }
	    public String getID()
	    {
	      return localId;
	    }
	    public void setNextID(String nextID)
	    {
	       this.nextID=nextID; 
	    }
	    public String getNextID()
	    {
	       
	      return nextID;
	    }
	    /*
	     * 		Actualiza la tabla con los datos de un vector de Vias.
	     * 		Vias está formado por dos string y un vector de
	     * 		PoliciaCoincidencias. Este último está formado por
	     * 		cuatro strings que almacena los datos de las calles
	     * 		coincidentes.
	     */
		private void refrescarTabla(ArrayList vias){      
		        
		       panelMultiple = new GeoRefTableModel();
		       panelMultiple.setModelData(vias);
		       TableSorted sorter = new TableSorted(panelMultiple);
		       sorter.setTableHeader(jTable.getTableHeader());
		       jTable.setModel(sorter);
		       jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		       JComboBoxNumerosPolicia renderer = new JComboBoxNumerosPolicia();
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_NOMBRE).setCellRenderer(new CellColorRenderer(Color.RED));
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_VIA).setCellRenderer(new ViaCellRenderer());
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_REFERENCIA).setCellRenderer(renderer);
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_REFERENCIA).setCellEditor(new ComboBoxTableEditor(new JComboBoxNumerosPolicia()));
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_VIA).setPreferredWidth(40);
		       jTable.getColumnModel().getColumn(GeoRefTableModel.ROW_REFERENCIA).setPreferredWidth(20);
		       jTable.setEnabled(true);
		}	    
}

class JComboBoxNumerosPolicia extends JComboBox implements TableCellRenderer
{
    public JComboBoxNumerosPolicia()
    {
        super();
        this.setRenderer(new RendererComboNumerosPolicia());
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JComboBoxNumerosPolicia aux=new JComboBoxNumerosPolicia();
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

        
        	ArrayList coincidePolicia=(ArrayList)((Via)table.getModel().getValueAt(row,GeoRefTableModel.ROW_NOMBRE)).getListaCoincidencias();

        	if (coincidePolicia==null) return aux;
        	Iterator recorreCoincidePolicia = coincidePolicia.iterator();
        	while (recorreCoincidePolicia.hasNext()){
	            aux.addItem(recorreCoincidePolicia.next());
	        }

//TODO: Revisar porque sale el fallo OUT OF BOUNDS
        if (value!=null && value instanceof Integer){
        	if ((aux.getItemCount()-1) < ((Integer)value).intValue()){
        		aux.setSelectedIndex(0);
        	}
        	else{
        		aux.setSelectedIndex(((Integer)value).intValue());
        	}
        }
        return aux;
    }
}
class RendererComboNumerosPolicia  extends JLabel implements ListCellRenderer
{
   public RendererComboNumerosPolicia()
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
       
       PoliciaCoincidencias valores = (PoliciaCoincidencias)value;
       String numero = ((PortalGeorreferenciado)valores.getDatos().get(0)).getPortal();
       setText(valores.getTipo()+" "+valores.getCalle()+" "+numero);
       return this;
   }
}
/**
 * Clase para editar los numeros de policia
 */
class ComboBoxTableEditor extends DefaultCellEditor
 {
     private JComboBox combo;
     public ComboBoxTableEditor(JComboBox combo)
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
         combo = new JComboBoxNumerosPolicia();
         if (value==null) return combo;
         ArrayList numerosPolicia = (ArrayList)((Via)table.getModel().getValueAt(rowIndex,GeoRefTableModel.ROW_NOMBRE)).getListaCoincidencias();        
         Iterator recorreNumerosPolicia = numerosPolicia.iterator();
         while (recorreNumerosPolicia.hasNext()){
              combo.addItem(recorreNumerosPolicia.next());
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
         if (combo==null) return null;
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
/**
 * Clase para pintar las vias
 */
class ViaCellRenderer  implements TableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column)
	{
		//Via via = (Via)value;
		JLabel aux = new JLabel((String)value);
		if (isSelected)
		{
			aux.setBorder(BorderFactory.createLineBorder(Color.RED));
		}
		return aux;
	}
}

/**
 * Clase para pintar las vias
 */
class CellColorRenderer  implements TableCellRenderer
{
	Color color;
	public CellColorRenderer(Color color)
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
		Via valor = (Via)value;

		if (value!=null)
			aux = new JLabel(valor.getNombre());
		if (isSelected)
		{
			aux.setBorder(BorderFactory.createLineBorder(color));
		}
		return aux;
	}

}