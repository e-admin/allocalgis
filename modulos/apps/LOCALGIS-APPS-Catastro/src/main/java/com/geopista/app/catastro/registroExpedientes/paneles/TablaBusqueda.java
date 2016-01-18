/**
 * TablaBusqueda.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 22-ene-2007
 * Time: 15:18:43
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones. Esta clase implementa un panel que tiene una tabla y carga los datos en ella.
 */

public class TablaBusqueda extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JTable expedientesTable;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;
    private ArrayList editors;
    private boolean sorted = false;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama. Se pasan los expediente y una hashtable con key el
     * id de usuario y value el nombre.
     *
     * @param label Etiqueta del borde del panel
     * @param expedientes Los expedientes a mostrar en la tabla
     * @param usuarios Los nombres de los usuarios de los expediente, ya que el expediente solo guarda el id usuario.
     */
    public TablaBusqueda(String label, ArrayList expedientes, Hashtable usuarios)
    {
        etiqueta= label;
        inicializaTabla();
        if(expedientes!=null&&expedientes.size()>0)
        {
            cargaDatosTabla(expedientes, usuarios);
        }
    }

    /**
     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
     * que queremos. Se sobreescribe el TableCellEditor para que devuelva el array de referencias del expediente de la
     * fila.
     */
    private void inicializaTabla()
    {

        expedientesTable= new JTable(){
            public TableCellEditor getCellEditor(int row, int column){
                if (column == 7)
                    return (TableCellEditor)editors.get(row);
                else
                    return super.getCellEditor(row, column);
            }
        };

        editors = new ArrayList();
        identificadores = new String[10];

        modelo= new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                if(column==7)
                    return true;
                else
                    return false;
            }
        };

        renombrarComponentes();

        expedientesTable.setModel(modelo);
        expedientesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		expedientesTable.setCellSelectionEnabled(false);
		expedientesTable.setColumnSelectionAllowed(false);
        expedientesTable.setRowSelectionAllowed(true);
        expedientesTable.getTableHeader().setReorderingAllowed(false);
        expedientesTable.getTableHeader().setResizingAllowed(true);
        expedientesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Disable autoCreateColumnsFromModel otherwise all the column customizations
        // and adjustments will be lost when the model data is sorted
        expedientesTable.setAutoCreateColumnsFromModel(false);

        expedientesTable.getTableHeader().addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {

                int columna = expedientesTable.columnAtPoint(e.getPoint());
                sorted = !sorted;
                sortAllRowsBy(modelo, columna, sorted);//e.getClickCount()%2 == 0? false : true);

            }

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}
        });

        tablaScrollPanel= new JScrollPane(expedientesTable);
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tablaScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 640, 170));

    }

    /**
     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. Se cargan ciertos datos de los
     * expedientes pasados por parametro. Para ver las referencias se hace un instanceof
     *
     * @param expedientes Los expedientes a mostrar en la tabla
     * @param usuarios Los nombres de los usuarios de los expediente, ya que el expediente solo guarda el id usuario.
     */
    public void cargaDatosTabla(ArrayList expedientes, Hashtable usuarios)
    {
        editors.clear();
        String[][] datos= new String[expedientes.size()][10];
        for(int i=0; i< expedientes.size();i++)
        {
            Expediente expAux= (Expediente)expedientes.get(i);
            datos[i][0] = String.valueOf(expAux.getNumeroExpediente());
            datos[i][1] = expAux.getTipoExpediente().getCodigoTipoExpediente();
            if(expAux.isTipoTramitaExpSitFinales()){
            	// expediente de situaciones finales
            	 datos[i][2] = (I18N.get("RegistroExpedientes",
                 				"Catastro.RegistroExpedientes.TablaBusqueda.tipoDeExpediente.situacionesFinales"));
            }
            else{
            	//Expediente de variaciones
            	 datos[i][2] = (I18N.get("RegistroExpedientes",
            	 				"Catastro.RegistroExpedientes.TablaBusqueda.tipoDeExpediente.variaciones"));
            }
           
            
            datos[i][3] = Estructuras.getListaEstadosExpediente().getDomainNode(String.valueOf(expAux.getIdEstado()))
                .getTerm(ConstantesCatastro.Locale);
            datos[i][4] = (String)usuarios.get(String.valueOf(expAux.getIdTecnicoCatastro()));
            datos[i][5] = String.valueOf(expAux.getFechaRegistro());
            datos[i][6] = String.valueOf(expAux.getAnnoExpedienteAdminOrigenAlteracion());
            datos[i][7] = String.valueOf(expAux.getEntidadGeneradora().getCodigo());
            ArrayList refCatas = expAux.getListaReferencias();
            if(refCatas!=null && refCatas.size()>0)
            {
                if(refCatas.get(0) instanceof FincaCatastro)
                {
                    datos[i][8]=(((FincaCatastro)refCatas.get(0)).getRefFinca().getRefCatastral());
                }
                else if(refCatas.get(0) instanceof BienInmuebleCatastro)
                {
                    datos[i][8]=(((BienInmuebleCatastro)refCatas.get(0)).getIdBienInmueble().getIdBienInmueble());
                }
            }
            rellenaRefCatas(expAux);
            datos[i][9] = expAux.getNifPresentador();
        }

        modelo.setDataVector(datos,identificadores);
        expedientesTable.setModel(modelo);

    }

// Regardless of sort order (ascending or descending), null values always appear last.
    // colIndex specifies a column in model.
    public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
        Vector data = model.getDataVector();
        Collections.sort(data, new ColumnSorter(colIndex, ascending));
        model.fireTableStructureChanged();
    }

    // This comparator is used to sort vectors of data
    public class ColumnSorter implements Comparator {
        int colIndex;
        boolean ascending;
        ColumnSorter(int colIndex, boolean ascending) {
            this.colIndex = colIndex;
            this.ascending = ascending;
        }
        public int compare(Object a, Object b) {
            Vector v1 = (Vector)a;
            Vector v2 = (Vector)b;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);

            // Treat empty strains like nulls
            if (o1 instanceof String && ((String)o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String)o2).length() == 0) {
                o2 = null;
            }

            // Sort nulls so they appear last, regardless
            // of sort order
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if (o1 instanceof Comparable) {
                if (ascending) {
                    return ((Comparable)o1).compareTo(o2);
                } else {
                    return ((Comparable)o2).compareTo(o1);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareTo(o1.toString());
                }
            }
        }
    }

    /**
     * Metodo que rellena un array con las referencias del expediente que se esta añadiendo a la tabla y que pone el
     * array en u comoboBox. Cuando el usuario pincha en la celda se ha reescrito para que devuelva el comboBox.
     *
     * @param exp El expediente que se esta tratando.
     */
    private void rellenaRefCatas(Expediente exp)
    {
        ArrayList refCatas = exp.getListaReferencias();
        JComboBox comboBox = new JComboBox();
        if(refCatas!=null)
        {
            for(int i= 0; i < refCatas.size();i++)
            {
                if(refCatas.get(i) instanceof FincaCatastro)
                {
                    comboBox.addItem(((FincaCatastro)refCatas.get(i)).getRefFinca().getRefCatastral());
                }
                else if(refCatas.get(i) instanceof BienInmuebleCatastro)
                {
                    comboBox.addItem(((BienInmuebleCatastro)refCatas.get(i)).getIdBienInmueble().getIdBienInmueble());                    
                }
            }
        }
        DefaultCellEditor dce = new DefaultCellEditor(comboBox);
        editors.add(dce);        
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */    
    public JPanel getPanel()
    {
        return this;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.numExpedienteLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.tipoDeExpedienteLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
        				"Catastro.RegistroExpedientes.TablaBusqueda.tipoTramitacionLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.estadoLabel"));
        identificadores[4] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.usuarioLabel"));
        identificadores[5] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.fechaAperturaLabel"));
        identificadores[6] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.annoExpedienteAdminOrigenAlt_EjercicioLabel"));
        identificadores[7] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.codigoEntidadGeneradora_ControlLabel"));
        identificadores[8] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.referenciaCatastralLabel"));
        identificadores[9] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaBusqueda.nifPresentadorLabel"));
        modelo.setColumnIdentifiers(identificadores);

    }

    /**
     * Devuelve el numero de la fila seleccionada
     *
     * @return int Numero de la fila seleccionada
     */
    public String getExpedienteSeleccionado()
    {
        Vector fila =(Vector) modelo.getDataVector().elementAt(expedientesTable.getSelectedRow()) ;
        String numExp = (String)fila.elementAt(0);
        return numExp;
    }

    public JTable getExpedientesTable() {
		return expedientesTable;
	}

	public void setExpedientesTable(JTable expedientesTable) {
		this.expedientesTable = expedientesTable;
	}
}

/*class TableHeaderCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)  {
        JLabel etiqueta = new JLabel();

        etiqueta.setOpaque(true);
        etiqueta.setText(((String)value));
        etiqueta.setForeground(new Color(0, 64, 128));
       // etiqueta.setFont(new Font());
        return etiqueta;
    }

}*/

