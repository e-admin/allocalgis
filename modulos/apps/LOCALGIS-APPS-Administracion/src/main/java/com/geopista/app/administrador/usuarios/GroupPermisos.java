/**
 * GroupPermisos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.Permiso;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-jun-2004
 * Time: 10:49:06
 */
public class GroupPermisos extends ButtonGroup {
    private final String IDPERM_CODE="IDPERM";
    private String idPerm;
    private JRadioButton botonPermitir;
    private JRadioButton botonDenegar;
    private JRadioButton botonRol;
    public GroupPermisos(String sIdPerm, boolean bEnabled)
    {
        super();
        idPerm =sIdPerm;
        botonPermitir= new JRadioButton();
        botonPermitir.setBackground(new java.awt.Color(255, 255, 255));
        botonPermitir.setText("Permitir");
        botonPermitir.putClientProperty(IDPERM_CODE,idPerm);
        botonPermitir.setEnabled(bEnabled);
        add(botonPermitir);

        botonDenegar= new JRadioButton();
        botonDenegar.setBackground(new java.awt.Color(255, 255, 255));
        botonDenegar.putClientProperty(IDPERM_CODE,idPerm);
        botonDenegar.setText("Denegar");
        botonDenegar.setEnabled(bEnabled);
        add(botonDenegar);


        botonRol= new JRadioButton();
        botonRol.setBackground(new java.awt.Color(255, 255, 255));
        botonRol.setText("Rol");
        botonRol.putClientProperty(IDPERM_CODE,idPerm);
        botonRol.setSelected(bEnabled);
        botonRol.setVisible(false);
        add(botonRol);

    }
    public void inicializaGroupPermisos(ListaPermisos listaPermisos, String sIdAcl)
    {
          //Permiso auxPermiso=listaPermisos.get(idPerm);
         /** Incidencia [308] - acl distintos con los mismos idperm */
          Permiso auxPermiso=listaPermisos.get(idPerm, sIdAcl);
          if ((auxPermiso==null)||(!auxPermiso.getIdAcl().equals(sIdAcl)))
          {
              botonRol.setSelected(true);
              return;
          }
          if (auxPermiso.getAplica())
              botonPermitir.setSelected(true);
          else
              botonDenegar.setSelected(true);
    }
     public String getIdPerm() {
        return idPerm;
    }

    public JRadioButton getBotonPermitir() {
        return botonPermitir;
    }

    public JRadioButton getBotonDenegar() {
        return botonDenegar;
    }

    public JRadioButton getBotonRol() {
        return botonRol;
    }
}
