/**
 * HistoricoTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Historico;
import com.geopista.protocol.administrador.dominios.DomainNode;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-abr-2005
 * Time: 11:25:17
 */
public class HistoricoTableModel  extends DefaultTableModel {
    public static final int IND_APUNTE=4;
    public static final int IND_ID=7;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HistoricoTableModel.class);
    private static String[] tableModelNames = new String[] {
        CMainContaminantes.messages.getString("historicoTableModel.FECHA"),
        CMainContaminantes.messages.getString("historicoTableModel.USUARIO"),
        CMainContaminantes.messages.getString("historicoTableModel.TIPO"),
        CMainContaminantes.messages.getString("historicoTableModel.ACCION"),
        CMainContaminantes.messages.getString("historicoTableModel.APUNTE"),
        CMainContaminantes.messages.getString("historicoTableModel.SISTEMA"),
        CMainContaminantes.messages.getString("historicoTableModel.ID_ELEMENTO"),
        CMainContaminantes.messages.getString("historicoTableModel.ID")};
    public static int idIndex=0;
    public void setModelData(Vector listaHistoricos) {
        try
        {
            if (listaHistoricos==null)return;
            for (Enumeration e=listaHistoricos.elements();e.hasMoreElements();) {
                try
                {
			        Historico aux =(Historico) e.nextElement();
                    SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
                    String sFecha =df.format(aux.getFecha());
                    String sTipo="";
                    DomainNode nodeTipo=com.geopista.app.contaminantes.Estructuras.getListaTipoMedioambiental().getDomainNode(new Integer(aux.getTipo_medioambiental()).toString());
                    if (nodeTipo!=null)
                      sTipo=nodeTipo.getTerm(com.geopista.app.contaminantes.Constantes.Locale);
                    String sAccion="";
                    DomainNode nodeAccion=com.geopista.app.contaminantes.Estructuras.getListaTipoAccion().getDomainNode(new Integer(aux.getAccion()).toString());
                    if (nodeAccion!=null)
                        sAccion=nodeAccion.getTerm(com.geopista.app.contaminantes.Constantes.Locale);
                    Object row[] = new Object[] { sFecha, aux.getNombre_Usuario(),
                                              sTipo,sAccion,
                                              aux.getApunte(),new Boolean(aux.getSistema()==1),new Long(aux.getId_elemento()), new Long(aux.getId_historico())};
			        this.addRow(row);
                }catch(Exception ex){}
	        }
            fireTableDataChanged();
        }catch(Exception e)
        {
            logger.error("Error al poner la lista de Historicos: "+ e.toString());
        }
	}



	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return tableModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

    public static void setColumnNames(String[] colNames){
        tableModelNames= colNames;
    }
    public Class getColumnClass(int column)
    {
      try
      {
         return getValueAt(0, column).getClass();
      }catch(Exception e){
          return String.class;
      }
    }




}
