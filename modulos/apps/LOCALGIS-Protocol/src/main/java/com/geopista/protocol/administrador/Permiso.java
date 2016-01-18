/**
 * Permiso.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-may-2004
 * Time: 15:22:43
 */
public class Permiso implements java.io.Serializable
 {
    private String idPerm;
    private String def;
    private boolean aplica;
    private String type;
    private HashMap<String,String> hmName;
    private String idAcl;
    public Permiso()
    {}

    public Permiso (String sIdPerm, String sDef, HashMap<String,String> sHtName)
    {
         idPerm=sIdPerm;
         def=sDef;
         hmName=sHtName;
    }
    public Permiso (String sIdPerm, String sDef, HashMap<String,String> sHtName, String sIdAcl)
    {
        idPerm=sIdPerm;
        def=sDef;
        hmName=sHtName;
        idAcl=sIdAcl;
    }
    public Permiso (String sIdPerm, String sIdAcl, boolean bAplica)
    {
        idPerm=sIdPerm;
        idAcl=sIdAcl;
        aplica=bAplica;
    }
    public String getDef()
    {
        return def;
    }
    public String getIdPerm()
    {
        return idPerm;
    }

    public String getIdAcl() {
        return idAcl;
    }

    public void setIdAcl(String idAcl) {
        this.idAcl = idAcl;
    }

    public String getType() {
        return type;
    }

    public void setIdPerm(String idPerm) {
        this.idPerm = idPerm;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setHmName(HashMap<String,String> name) {
        this.hmName = name;
    }
    
    public HashMap<String,String> getHmName() {
    	return hmName;
    }

    public boolean getAplica() {
        return aplica;
    }

    public void setAplica(boolean aplica) {
        this.aplica = aplica;
    }

}
