package com.geopista.server.administradorCartografia;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

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
