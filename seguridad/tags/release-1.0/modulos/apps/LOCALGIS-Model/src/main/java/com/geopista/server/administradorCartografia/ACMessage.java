package com.geopista.server.administradorCartografia;

import java.io.Serializable;

public class ACMessage implements Serializable{
    byte[] query;

    public ACMessage(){
    }

    public ACMessage(byte[] query){
        this.query=query;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMessage#getQuery()
	 */
	public byte[] getQuery() {
        return query;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACMessage#setQuery(byte[])
	 */
	public void setQuery(byte[] query) {
        this.query = query;
    }

}
