/**
 * TablaAsociarImagenesParcelasImportacionFXCC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.vividsolutions.jump.I18N;

public class TablaAsociarImagenesParcelasImportacionFXCC extends JPanel{
	
	private String etiqueta;
    private JTable tablaImagenesTable;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public TablaAsociarImagenesParcelasImportacionFXCC(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
     * que queremos.
     */
    private void inicializaPanel()
    {
    	tablaImagenesTable= new JTable();
        identificadores = new String[2];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaImagenesTable.setModel(modelo);
        tablaImagenesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaImagenesTable.setCellSelectionEnabled(false);
		tablaImagenesTable.setColumnSelectionAllowed(false);
		tablaImagenesTable.setRowSelectionAllowed(true);
        tablaScrollPanel= new JScrollPane(tablaImagenesTable);

        renombrarComponentes();
        
        this.setLayout(new GridBagLayout());
        this.add(tablaScrollPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

    }

    /**
     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. En este caso son o fincasCatastro
     * o Bienes inmuebles. Se hace un instanceof para saberlo.
     *
     * @param dirRefCatas La lista de referencias del expediente
     */
    public void cargaDatosTabla(ArrayList lstImagenes)
    {
    	String[][] datos= new String[lstImagenes.size()][6];
    	for(int i=0; i< lstImagenes.size();i++)
        {
    		ImagenCatastro imagenCatastro = (ImagenCatastro)lstImagenes.get(i);
    		
    		datos[i][0] = imagenCatastro.getNombre();
    		datos[i][1] = imagenCatastro.getTipoDocumento();
    		
        }
    	modelo.setDataVector(datos,identificadores);
        tablaImagenesTable.setModel(modelo);

    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getTablaPanel()
    {
        return this;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("Importacion",etiqueta)));
        identificadores[0] = (I18N.get("Importacion",
                        "importar.infografica.seleccion.imagen"));
        identificadores[1] = (I18N.get("Importacion",
                        "importar.infografica.seleccion.imagen.extension"));
      
    }

    /**
     * Devuelve el numero de la fila seleccionada
     *
     * @return int Numero de la fila seleccionada
     */
    public int getImagenSeleccionada()
    {
        return tablaImagenesTable.getSelectedRow();
    }

}
