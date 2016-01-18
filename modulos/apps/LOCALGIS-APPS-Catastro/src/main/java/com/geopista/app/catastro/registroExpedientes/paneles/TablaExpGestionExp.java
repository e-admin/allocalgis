/**
 * TablaExpGestionExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 19-dic-2006
 * Time: 18:14:30
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones. Esta clase implementa un panel que tiene una tabla y carga los datos en ella.
 */

public class TablaExpGestionExp extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JTable tablaExpTabel;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     * @param dirRefCatas Las referencias del expediente, pueden ser objetos FincaCatastro o BienInmuebleCatastro
     */
    public TablaExpGestionExp(String label, ArrayList dirRefCatas)
    {
        etiqueta= label;
        inicializaPanel();
        if(dirRefCatas!=null&&dirRefCatas.size()>0)
        {
            cargaDatosTabla(dirRefCatas);
        }
    }

    /**
     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
     * que queremos.
     */
    private void inicializaPanel()
    {
        tablaExpTabel= new JTable();
        identificadores = new String[4];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaExpTabel.setModel(modelo);
		tablaExpTabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaExpTabel.setCellSelectionEnabled(false);
		tablaExpTabel.setColumnSelectionAllowed(false);
		tablaExpTabel.setRowSelectionAllowed(true);
        tablaExpTabel.getTableHeader().setReorderingAllowed(false);
        tablaScrollPanel= new JScrollPane(tablaExpTabel);

        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tablaScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 80));

    }

   /**
     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. En este caso son o fincasCatastro
     * o Bienes inmuebles. Se hace un instanceof para saberlo.
     *
     * @param refCatas La lista de referencias del expediente
     */
    public void cargaDatosTabla(ArrayList refCatas)
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();         
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        String[][] datos= new String[refCatas.size()][4];
        for(int i=0; i< refCatas.size();i++)
        {
            DireccionLocalizacion dir = null;
            if(refCatas.get(i) instanceof FincaCatastro)
            {
                datos[i][0] = ((FincaCatastro)refCatas.get(i)).getRefFinca().getRefCatastral();
                dir = ((FincaCatastro)refCatas.get(i)).getDirParcela();
            }
            else if(refCatas.get(i) instanceof BienInmuebleCatastro)
            {
                datos[i][0] = ((BienInmuebleCatastro)refCatas.get(i)).getIdBienInmueble().getIdBienInmueble();
                dir = ((BienInmuebleCatastro)refCatas.get(i)).getDomicilioTributario();
            }

            if(Estructuras.getListaTipos().getDomainNode(dir.getTipoVia())!=null)
            {
                datos[i][1] = Estructuras.getListaTipos().getDomainNode(dir.getTipoVia())
                        .getTerm(app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"));
            }
            else
            {
                datos[i][1] = "";
            }
            datos[i][2] = dir.getNombreVia();
            datos[i][3] = String.valueOf(dir.getPrimerNumero());
        }
        modelo.setDataVector(datos,identificadores);
        tablaExpTabel.setModel(modelo);
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getTablaEepPanel()
    {
        return this;
    }

   /**
    * Devuelve la tabla.
    *
    * @return JTable tablaExpTabel
    * */
    public JTable getTablaExpTabel()
    {
        return tablaExpTabel;
    }

    /**
     * Devuelve el numero de la fila seleccionada
     *
     * @return int Numero de la fila seleccionada
     */    
    public int getParcelaSeleccionada()
    {
        return tablaExpTabel.getSelectedRow();
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
                        "Catastro.RegistroExpedientes.TablaExpGestionExp.RefCatastralLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaExpGestionExp.tipoViaLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaExpGestionExp.NombreViaLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaExpGestionExp.NumeroLabel"));
    }
}
