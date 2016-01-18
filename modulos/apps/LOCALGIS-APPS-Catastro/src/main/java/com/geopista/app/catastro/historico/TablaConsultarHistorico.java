/**
 * TablaConsultarHistorico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.historico;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.Fichero;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-feb-2007
 * Time: 11:33:22
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones. Esta clase implementa un panel que tiene una tabla y carga los datos en ella.
 */

public class TablaConsultarHistorico extends JPanel implements IMultilingue {
    private String etiqueta;
    private JTable tablaConsultarHistoricoTabel;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public TablaConsultarHistorico(String label)
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
        tablaConsultarHistoricoTabel= new JTable();
        identificadores = new String[5];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaConsultarHistoricoTabel.setModel(modelo);
		tablaConsultarHistoricoTabel.setCellSelectionEnabled(false);
		tablaConsultarHistoricoTabel.setColumnSelectionAllowed(false);
		tablaConsultarHistoricoTabel.setRowSelectionAllowed(false);
        tablaConsultarHistoricoTabel.getTableHeader().setReorderingAllowed(true);
        tablaScrollPanel= new JScrollPane(tablaConsultarHistoricoTabel);

        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tablaScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 280));
    }

   /**
     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. En este caso son ficheros de los
     * que se recogera la informacion necesaria y se mostrara, aquellos que coincidan en codigo de envio van en tuplas
     * de dos y se muestran en la misma linea como envio y recepcion.
     *
     * @param ficheros La lista de referencias del expediente
     */
    public void cargaDatosTabla(ArrayList ficheros)
    {
        String[][] datos= new String[0][5];
        int posTabla = 0;
        for(int i=0; i< ficheros.size();i++)
        {

            Fichero ficheroEntGen= (Fichero)ficheros.get(i);
            if(i+1<ficheros.size()&&((Fichero)ficheros.get(i+1)).getCodigoEnvio()!=null&& ficheroEntGen.getCodigoEnvio()!=null
                    &&((Fichero)ficheros.get(i+1)).getCodigoEnvio().equalsIgnoreCase(ficheroEntGen.getCodigoEnvio()))
            {
                datos = aumentaTamArray(datos);
                i++;
                Fichero ficheroCatastro = (Fichero)ficheros.get(i);
                if(ficheroEntGen.getIdTipoFichero()==Fichero.FIN_ENTRADA || ficheroEntGen.getIdTipoFichero()==Fichero.VARPAD)
                {
                    datos[posTabla][0] = ficheroEntGen.getNombre();
                    datos[posTabla][1] = String.valueOf(ficheroEntGen.getFechaIntercambio());
                    datos[posTabla][2] = "Si";
                    datos[posTabla][3] = ficheroCatastro.getNombre();
                    datos[posTabla][4] = String.valueOf(ficheroCatastro.getFechaGeneracion());
                }
                else
                {
                    datos[posTabla][0] = ficheroCatastro.getNombre();
                    datos[posTabla][1] = String.valueOf(ficheroCatastro.getFechaIntercambio());
                    datos[posTabla][2] = "Si";
                    datos[posTabla][3] = ficheroEntGen.getNombre();
                    datos[posTabla][4] = String.valueOf(ficheroEntGen.getFechaGeneracion());
                }
                posTabla++;
            }
            else
            {
                datos = aumentaTamArray(datos);                
                if(ficheroEntGen.getIdTipoFichero()==Fichero.FIN_ENTRADA || ficheroEntGen.getIdTipoFichero()==Fichero.VARPAD)
                {

                    datos[posTabla][0] = ficheroEntGen.getNombre();
                    datos[posTabla][1] = String.valueOf(ficheroEntGen.getFechaIntercambio());
                    datos[posTabla][2] = "No";
                }
                else
                {
                    datos[posTabla][2] = "No";
                    datos[posTabla][3] = ficheroEntGen.getNombre();
                    datos[posTabla][4] = String.valueOf(ficheroEntGen.getFechaGeneracion());
                }
                posTabla++;
            }
        }
        modelo.setDataVector(datos,identificadores);
        tablaConsultarHistoricoTabel.setModel(modelo);
    }

    /**
     * Funcion auxiliar que aumenta el tamaño del array que carga los datos cuando es necesario.
     *
     * @param datos Array que ha que aumentar y duplicar.
     * @return String[][] Array aumentado y duplicado.
     * */
    private String[][] aumentaTamArray(String[][] datos)
    {
        String[][] datos2 = datos;
        datos = new String[datos2.length +1][5];
        for(int j=0;j<datos2.length;j++)
        {
            datos[j]=datos2[j];
        }
        return datos;
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
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaConsultarHistorico.FicheroEnviadoLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaConsultarHistorico.fechaEnviadoLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaConsultarHistorico.confirmacionLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaConsultarHistorico.ficheroRecibidoLabel"));
        identificadores[4] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaConsultarHistorico.fechaRecibidoLabel"));
    }    
}
