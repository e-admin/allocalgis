/**
 * PermisosTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.KeyListaPermisos;
import com.geopista.protocol.administrador.ListaAcl;
import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.Permiso;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 14-mar-2005
 * Time: 20:09:00
 */
public class PermisosTableModel extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConexionesTableModel.class);
    private static String[] tableModelNames = new String[] { "ACL", "PERMISO"};

    public void setModelData(ListaPermisos listaPermisosUsuario, ListaAcl listaAcl) {
        try
        {
            if (listaPermisosUsuario==null)return;
            
            
            for (int j=0;j<listaPermisosUsuario.gethPermisosSorted().size();j++){
				Permiso permisoUsuario = (Permiso) listaPermisosUsuario.gethPermisosSorted().get(j);
				
            //for (Enumeration e=listaPermisosUsuario.gethPermisos().elements();e.hasMoreElements();) {

                /*
			    Permiso permisoUsuario =(Permiso) e.nextElement();
                Acl acl=(Acl)listaAcl.gethAcls().get(permisoUsuario.getIdAcl());
                Permiso permisoGeneral=(Permiso)acl.getPermisos().gethPermisos().get(permisoUsuario.getIdPerm());
                */
                
                /** Incidencia [308] - acl distintos con los mismos idperm */
                //Permiso permisoUsuario =(Permiso) e.nextElement();
                Acl acl=(Acl)listaAcl.gethAcls().get(permisoUsuario.getIdAcl());
                KeyListaPermisos k= new KeyListaPermisos(permisoUsuario.getIdPerm(), acl.getId());
                Permiso permisoGeneral=(Permiso)acl.getPermisos().gethPermisos().get(k);

                Object row[] = new Object[] { acl.getNombre(), permisoGeneral.getDef()};
                this.addRow(row);

	    }
        fireTableDataChanged();
        }catch(Exception e)
        {
            logger.error("Error al poner la lista de permisos: "+ e.toString());
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





}
