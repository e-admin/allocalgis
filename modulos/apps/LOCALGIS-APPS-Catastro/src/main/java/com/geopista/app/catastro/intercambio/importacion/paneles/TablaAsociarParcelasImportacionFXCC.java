/**
 * TablaAsociarParcelasImportacionFXCC.java
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

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

public class TablaAsociarParcelasImportacionFXCC extends JPanel{
	
	private String etiqueta;
    private JTable tablaRefCatasTabel;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public TablaAsociarParcelasImportacionFXCC(String label)
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
        tablaRefCatasTabel= new JTable();
        identificadores = new String[6];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaRefCatasTabel.setModel(modelo);
		tablaRefCatasTabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaRefCatasTabel.setCellSelectionEnabled(false);
		tablaRefCatasTabel.setColumnSelectionAllowed(false);
		tablaRefCatasTabel.setRowSelectionAllowed(true);
        tablaScrollPanel= new JScrollPane(tablaRefCatasTabel);

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
    public void cargaDatosTabla(ArrayList dirRefCatas)
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();        
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        String[][] datos= new String[dirRefCatas.size()][6];
        for(int i=0; i< dirRefCatas.size();i++)
        {
            DireccionLocalizacion auxDir = null;
            if(dirRefCatas.get(i) instanceof FincaCatastro)
            {
                FincaCatastro parcela = (FincaCatastro)dirRefCatas.get(i);
                auxDir = parcela.getDirParcela();
                datos[i][0] = parcela.getRefFinca().getRefCatastral();
            }
            else if(dirRefCatas.get(i) instanceof BienInmuebleCatastro)
            {
                BienInmuebleCatastro bien = (BienInmuebleCatastro)dirRefCatas.get(i);
                auxDir = bien.getDomicilioTributario();
                datos[i][0] = bien.getIdBienInmueble().getIdBienInmueble();
            }
            if(auxDir!=null &&Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())!=null)
            {
                datos[i][1] = Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())
                        .getTerm(app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"));
            }
            else
            {
                datos[i][1] = "";    
            }
            if(auxDir!=null)
            {
                datos[i][2] = auxDir.getNombreVia();
                datos[i][3] = String.valueOf(auxDir.getPrimerNumero());
                datos[i][4] = auxDir.getCodPoligono();
                datos[i][5] = auxDir.getCodParcela();
            }
        }
                          
        modelo.setDataVector(datos,identificadores);
        tablaRefCatasTabel.setModel(modelo);
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
                        "importar.fichero.fxcc.TablaAsociarParcelas.RefCatastralLabel"));
        identificadores[1] = (I18N.get("Importacion",
                        "importar.fichero.fxcc.TablaAsociarParcelas.tipoViaLabel"));
        identificadores[2] = (I18N.get("Importacion",
                        "importar.fichero.fxcc.TablaAsociarParcelas.NombreViaLabel"));
        identificadores[3] = (I18N.get("Importacion",
                        "importar.fichero.fxcc.TablaAsociarParcelas.NumeroLabel"));
        identificadores[4] = (I18N.get("Importacion",
                        "importar.fichero.fxcc.TablaAsociarParcelas.codigoPoligono"));
        identificadores[5] = (I18N.get("Importacion",
                        "importar.fichero.fxcc.TablaAsociarParcelas.codigoParcela"));        
    }

    /**
     * Devuelve el numero de la fila seleccionada
     *
     * @return int Numero de la fila seleccionada
     */
    public int getParcelaSeleccionada()
    {
        return tablaRefCatasTabel.getSelectedRow();
    }

}
