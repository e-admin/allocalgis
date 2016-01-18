/**
 * ACQuery.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ACQuery implements Serializable{
    int iAction=-1;

    

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACQuery#getParams()
	 */
    
	public Hashtable getParams() {
        return htParams;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACQuery#setParams(java.util.Hashtable)
	 */
    
	public void setParams(Hashtable htParams) {
        this.htParams = htParams;
    }

    Hashtable htParams=null;

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACQuery#getAction()
	 */
    
	public int getAction() {
        return iAction;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACQuery#setAction(int)
	 */
    
	public void setAction(int iAction) {
        this.iAction = iAction;
    }

    public ACQuery(){
    }

    public static void main(String args[]){
        FilterNode fn=FilterOpBinary.and(FilterLeaf.equal("uno","UNO"),
                                         FilterOpBinary.or(FilterOpUnary.not(FilterLeaf.equal("dos","DOS")),
                                                           FilterLeaf.equal("tres","TRES")));
        System.out.println(fn.toSQL());
        List l=new ArrayList();
        fn.values(l);
        System.out.println("OK");
    }
}
