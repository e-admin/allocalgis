/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.utils.enumerados;

import java.io.Serializable;

public enum TiposFuente implements Serializable {
	SHAPEFILE("Fichero shapefile.","1","jsp.fuente.tipo.shapefile"),
    WFS("Servicio WFS.","2","jsp.fuente.tipo.wfs"),
    GML("Fichero GML","3","jsp.fuente.tipo.gml"),
    CSV("Fichero CSV","4","jsp.fuente.tipo.csv"),
    ODBC("Fuente de datos ODBC","5","jsp.fuente.tipo.odbc" ),
    BDESPACIAL("Fuente de datos BD Espacial (PostGis)","6","jsp.fuente.tipo.bdespacial"),
	MYSQL("Fuente de datos Mysql","7","jsp.fuente.tipo.mysql"),
	ORACLE("Fuente de datos Oracle","8","jsp.fuente.tipo.oracle");
    
	private String descripcion;
    private String id;
    private String internacionalizacion;

    private TiposFuente(String descripcion, String id, String internacionalizacion) {
        this.descripcion = descripcion;
        this.id = id;
        this.internacionalizacion=internacionalizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public String getId() {
        return this.id;
    }
    public String getInternacionalizacion() {
		return internacionalizacion;
	}

	@Override
    public String toString() {
        if (this == SHAPEFILE) return SHAPEFILE.getId();
        else if (this == WFS) return WFS.getId();
        else if (this == GML) return GML.getId();
        else if (this == CSV) return CSV.getId();
        else if (this == ODBC) return ODBC.getId();
        else if (this == BDESPACIAL) return BDESPACIAL.getId();
        else if (this==MYSQL)return MYSQL.getId();
        else if (this==ORACLE)return ORACLE.getId();
        else return "";
    }
    
    public static TiposFuente recuperarFuentePorId(Integer tipo){
    	switch(tipo){
    	case 1:
    		return SHAPEFILE;
    	case 2:	
    		return WFS;
    	case 3:
    		return GML;
    	case 4:
    		return CSV;
    	case 5:
    		return ODBC;
    	case 6:
    		return BDESPACIAL;
    	case 7:
    		return MYSQL;
    	case 8:
    		return ORACLE;
    	}
    	return null;
    }
}