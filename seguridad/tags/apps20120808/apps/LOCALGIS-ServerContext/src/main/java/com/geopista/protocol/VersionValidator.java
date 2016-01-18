package com.geopista.protocol;

import java.math.BigDecimal;

import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.ACLayer;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.SQLParser;

/**
 * En esta clase se realizan las modificaciones necesarias para facilitar el versionado de las capas. Así, al realizar una consulta, la select
 * se modificará para obtener las features posteriores a una fecha. Al insertar una feature se insertará ésta con la revisión 
 * adecuada. Al actualizarla se dará de baja la feature actual y se insertará con la nueva revisión.
 * @author miriamperez
 *
 */
public class VersionValidator extends AbstractValidator{

	private String fechaActual = "";
	private String revisionExpiradaNula = "";
	private String revisionExpiradaNoNula = "";
	private String versionActual = "";
	private String versionAnteriorAFechaActual = "";
	private String restriccionAnteriorAFechaActual = "";
	private String restriccionFeaturesBaja = "";
	private String restriccionActual = "";
	private String nuevaVersion = "";
	private String restriccionMaxRevisionBaja = "";
	private boolean versionado = true;

	Version version = new Version();
	
	public VersionValidator(){
		revisionExpiradaNula = " and "+Const.REVISION_EXPIRADA+" =9999999999 ";
		revisionExpiradaNoNula = " and "+Const.REVISION_EXPIRADA+" <>9999999999 ";
		versionActual = "(select coalesce(max(revision)+1,0) from versiones where fecha<localtimestamp)";
		versionAnteriorAFechaActual = "(select coalesce(max(revision),0) from versiones where fecha<localtimestamp)";
		restriccionFeaturesBaja = " and "+Const.REVISION_EXPIRADA+" =-1 ";
		restriccionAnteriorAFechaActual = revisionExpiradaNula+" and "+Const.REVISION_ACTUAL+" <= "+ versionAnteriorAFechaActual;
		restriccionActual = revisionExpiradaNula+" and "+Const.REVISION_ACTUAL+" <=  "+ versionActual;
		restriccionMaxRevisionBaja = " and "+Const.REVISION_ACTUAL+" = (select max("+Const.REVISION_ACTUAL+" ) ";
		nuevaVersion = "(select coalesce(max(revision)+1,1) from versiones where fecha<localtimestamp)";
	}
	

    public void setVersion(Version version){
		this.version = version;
		if (version != null){
			versionado = version.isCrearVersionado();
			if (version.getFecha().equals(""))
				fechaActual = "localtimestamp";
			else
				fechaActual = "to_timestamp('"+version.getFecha()+"','yyyy-MM-dd HH24:MI:ss')";
			if (version.getRevisionActual() != -1)
				versionAnteriorAFechaActual = String.valueOf(version.getRevisionActual());
			else
				versionAnteriorAFechaActual = "(select coalesce(max(revision),0) from versiones where fecha<"+fechaActual+" and id_table_versionada="+version.getIdTable()+")";
			versionActual = "(select coalesce(max(revision)+1,0) from versiones where fecha<"+fechaActual+" and id_table_versionada="+version.getIdTable()+")";
			if (version.isFeaturesActivas()){
				restriccionAnteriorAFechaActual = " and "+Const.REVISION_EXPIRADA+">"+versionAnteriorAFechaActual+" and "+Const.REVISION_ACTUAL+" <= "+ versionAnteriorAFechaActual;
				restriccionActual = " and "+Const.REVISION_EXPIRADA+">"+versionActual+" and "+Const.REVISION_ACTUAL+" <=  "+ versionActual;
			}else{
				restriccionAnteriorAFechaActual = revisionExpiradaNoNula+" and "+Const.REVISION_ACTUAL+" <= "+ versionAnteriorAFechaActual;
				restriccionActual = revisionExpiradaNoNula+" and "+Const.REVISION_ACTUAL+" <=  "+ versionActual;
			}
		}else{
			versionActual = "(select coalesce(max(revision)+1,0) from versiones where fecha<localtimestamp and id_table_versionada="+version.getIdTable()+")";
			versionAnteriorAFechaActual = "(select coalesce(max(revision),0) from versiones where fecha<localtimestamp and id_table_versionada="+version.getIdTable()+")";
			restriccionAnteriorAFechaActual = revisionExpiradaNula+" and "+Const.REVISION_ACTUAL+" <= "+ versionAnteriorAFechaActual;
			restriccionActual = revisionExpiradaNula+" and "+Const.REVISION_ACTUAL+" <=  "+ versionActual;
		}
	}
	
	public void afterInsert(ACFeatureUpload uploadFeature, ACLayer acLayer){
	}
	
	public void beforeInsert(ACFeatureUpload uploadFeature, ACLayer acLayer){
		String sSQL = annadirTemporalInsert(acLayer,false);
		acLayer.setInsertQuery(sSQL);
		insertarNuevaRevision(acLayer);
		version = null;
	}
	
	/**
	 * Para elementos de la EIEL temporales
	 */
	public void beforeInsertTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		String sSQL = annadirTemporalVersion(acLayer,estadoValidacion);
		acLayer.setInsertQuery(sSQL);
		version = null;
	}
	
	public void afterDelete(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeDelete(ACFeatureUpload uploadFeature, ACLayer acLayer){
		if (versionado){
					
			boolean encontradoElementoTemporal=false;
			try{
				String atributorevisionExpirada=String.valueOf((BigDecimal)uploadFeature.getAttValues()[uploadFeature.getAttValues().length-1]);
				if ((atributorevisionExpirada!=null) && ((atributorevisionExpirada.equals(Const.REVISION_TEMPORAL)) || 
										(atributorevisionExpirada.equals(Const.REVISION_PUBLICABLE)))){
					beforeDeleteTemporal(uploadFeature,acLayer);
					acLayer.setInsertQuery("");
					encontradoElementoTemporal=true;
					
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
			if (!encontradoElementoTemporal){
		    	String sSQL = annadirTemporalUpdate(acLayer,true);
				acLayer.setUpdateQuery(sSQL);
		    	sSQL = annadirTemporalInsertDelete(acLayer);
		    	sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+uploadFeature.getId()+"' ");
				acLayer.setInsertQuery(sSQL);
				insertarNuevaRevision(acLayer);
			}
		}else{
			acLayer.setInsertQuery("");
		}
		version = null;
	}
	
	public void beforeDeleteTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer){
		if (versionado){
			String sSQL=borrarVersionTemporal(acLayer);
			acLayer.setUpdateQuery(sSQL);			    	
		}else{
			acLayer.setInsertQuery("");
		}
		version = null;
	}
	
	public void afterUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer){
		
	}
	
	public void beforeUpdate(ACFeatureUpload uploadFeature, ACLayer acLayer){
		if (versionado && acLayer.getUpdateQuery() != null && acLayer.getInsertQuery() != null &&!(acLayer.getUpdateQuery().equals("")&&acLayer.getInsertQuery().equals(""))){
	    	String sSQL = "";
	    	if (version.isbRecuperarFeatures() == false){
	    		sSQL = annadirTemporalUpdate(acLayer,true);
	    	}
			acLayer.setUpdateQuery(sSQL);
	    	sSQL = annadirTemporalInsert(acLayer,false);
	    	sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+uploadFeature.getId()+"' ");
			acLayer.setInsertQuery(sSQL);
			insertarNuevaRevision(acLayer);
		}else{
			acLayer.setInsertQuery("");
		}
		version = null;
	}
	
	public void beforeUpdateTemporal(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		if (versionado){
			String sSQL=borrarVersionTemporal(acLayer);
			acLayer.setUpdateQuery(sSQL);
			
			sSQL = annadirTemporalVersion(acLayer,estadoValidacion);		
			sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+uploadFeature.getId()+"' ");
			acLayer.setInsertQuery(sSQL);
		}
		else{
			acLayer.setInsertQuery("");
		}
		version = null;		
	}
	
	/**
	 * Actualizacion antes de actualizar de forma publicable
	 */
	public void beforeUpdatePublicable(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		if (versionado && acLayer.getUpdateQuery() != null && acLayer.getInsertQuery() != null &&!(acLayer.getUpdateQuery().equals("")&&acLayer.getInsertQuery().equals(""))){			
			
			String sSQL=null;
			
			//Insertamos la nueva revision lo primero para que se genere el nextval, sino es 
			//posible que no se pueda generar correctamente.
			String nuevaRevision=insertarNuevaRevision(acLayer,true);
			
			if (version.isbRecuperarFeatures() == false){
	    		sSQL = annadirTemporalUpdate(acLayer);	 
	    		acLayer.setUpdateQuery(sSQL);
			}
			
			sSQL=borrarVersionTemporal(acLayer);
			String nuevoSQL=nuevaRevision+""+acLayer.getUpdateQuery()+";"+sSQL;
    		acLayer.setUpdateQuery(nuevoSQL);
			
			
	    	sSQL = annadirTemporalInsert(acLayer,false);
	    	sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+uploadFeature.getId()+"' ");
	    	acLayer.setInsertQuery(sSQL);
			
		}else{
			acLayer.setInsertQuery("");
		}
		version = null;
	}
	
	/**
	 * 
	 * Actualizacion antes de actualizar de forma temporal
	 */
	public void beforeUpdateAutoPublicable(ACFeatureUpload uploadFeature, ACLayer acLayer,int estadoValidacion){
		if (versionado && acLayer.getUpdateQuery() != null && acLayer.getInsertQuery() != null &&!(acLayer.getUpdateQuery().equals("")&&acLayer.getInsertQuery().equals(""))){
			
			String sSQL=null;
			
			//Insertamos la nueva revision lo primero para que se genere el nextval, sino es 
			//posible que no se pueda generar correctamente.
			String nuevaRevision=insertarNuevaRevision(acLayer,true);
			
			if (version.isbRecuperarFeatures() == false){
	    		sSQL = annadirTemporalUpdate(acLayer);	  
	    		acLayer.setUpdateQuery(sSQL);
			}
			
			sSQL=borrarVersionAutoPublicable(acLayer);
			String nuevoSQL=nuevaRevision+""+acLayer.getUpdateQuery()+";"+sSQL;
    		acLayer.setUpdateQuery(nuevoSQL);
    		
			
	    	sSQL = annadirTemporalInsert(acLayer,false);
	    	sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+uploadFeature.getId()+"' ");
	    	acLayer.setInsertQuery(sSQL);

		}else{
			acLayer.setInsertQuery("");
		}
		version = null;
	}
			
	public void afterRead(ACFeature feature, ACLayer acLayer){
		
	}
	
	public void beforeRead(ACLayer acLayer){
		String sSQL = annadirTemporalSelect(acLayer);
		acLayer.setSelectQuery(sSQL);
	}
	
	/**
	 * Se añade la restricción temporal a la sentencia de inserción
	 * @param acLayer
	 */
	public String annadirTemporalVersion(ACLayer acLayer,int estadoValidacion){
    	StringBuffer sb = new StringBuffer("");
    	if (!acLayer.getInsertQuery().equals("")){
    		
    		//A veces se meten saltos de linea en las queries y eso provoca que falle el split
    		//Los quitamos
    		String querySinSaltosDeLinea = acLayer.getInsertQuery().replaceAll("[\n\r]","");

		    String[] cadTroz = querySinSaltosDeLinea.split("[)]");
		    sb = sb.append(cadTroz[0]);
		    sb = sb.append(","+Const.REVISION_ACTUAL+","+Const.REVISION_EXPIRADA+")");
		    int n = cadTroz.length-1;
		    for (int i=1;i<n;i++){
		    	sb = sb.append(cadTroz[i]);
			    sb = sb.append(")");
		    }
		    
		    //Añade hasta el ultimo sin contar el parentesis final
	    	sb = sb.append(cadTroz[n]);
	    	if (estadoValidacion==Const.ESTADO_TEMPORAL)
	    		sb = sb.append(",-1,"+Const.REVISION_TEMPORAL);
	    	else if (estadoValidacion==Const.ESTADO_A_PUBLICAR)
	    		sb = sb.append(",-1,"+Const.REVISION_PUBLICABLE);
	    	else if (estadoValidacion==Const.ESTADO_PUBLICABLE_MOVILIDAD)
	    		sb = sb.append(",-2,"+Const.REVISION_PUBLICABLE_PDA);
	    	
	    	sb = sb.append(")");
    	}
	    return sb.toString();
	}
	
	
	/**
	 * Se añade la restricción temporal a la sentencia de inserción
	 * @param acLayer
	 */
	public String annadirTemporalInsert(ACLayer acLayer,boolean generateNextVal){
    	StringBuffer sb = new StringBuffer("");
    	if (!acLayer.getInsertQuery().equals("")){
    		
    		//A veces se meten saltos de linea en las queries y eso provoca que falle el split
    		//Los quitamos
    		String querySinSaltosDeLinea = acLayer.getInsertQuery().replaceAll("[\n\r]","");
    		
		    String[] cadTroz = querySinSaltosDeLinea.split("[)]");
		    sb = sb.append(cadTroz[0]);
		    sb = sb.append(","+Const.REVISION_ACTUAL+")");
		    int n = cadTroz.length-1;
		    for (int i=1;i<n;i++){
		    	sb = sb.append(cadTroz[i]);
			    sb = sb.append(")");
		    }
	    	sb = sb.append(cadTroz[n]);
	    	if (version.isbRecuperarFeatures() || (acLayer.getUpdateQuery()== null || acLayer.getUpdateQuery().equals("")))
	    		sb = sb.append(",nextval('\"seq_version_");
	    	else{
	    		if (generateNextVal)
	    			sb = sb.append(",nextval('\"seq_version_");
	    		else
	    			sb = sb.append(",currval('\"seq_version_");
	    	}
	    	sb = sb.append(this.version.getIdTable());
	    	sb = sb.append("\"'))");
    	}
	    return sb.toString();
	}
    
	/**
	 * Se añade la restricción temporal a la sentencia de borrado
	 * @param acLayer
	 */
	public String annadirTemporalInsertDelete(ACLayer acLayer){
    	StringBuffer sb = new StringBuffer("");
    	
    	//A veces se meten saltos de linea en las queries y eso provoca que falle el split
		//Los quitamos
		String querySinSaltosDeLinea = acLayer.getInsertQuery().replaceAll("[\n\r]","");
		
	    String[] cadTroz = querySinSaltosDeLinea.split("[)]");
	    sb = sb.append(cadTroz[0]);
	    sb = sb.append(","+Const.REVISION_ACTUAL+","+Const.REVISION_EXPIRADA+")");
	    int n = cadTroz.length-1;
	    for (int i=1;i<n;i++){
	    	sb = sb.append(cadTroz[i]);
		    sb = sb.append(")");
	    }
    	sb = sb.append(cadTroz[n]);
    	sb = sb.append(",currval('\"seq_version_");
   		sb = sb.append(this.version.getIdTable());
    	sb = sb.append("\"'),currval('\"seq_version_");
   		sb = sb.append(this.version.getIdTable());
    	sb = sb.append("\"'))");
	    return sb.toString();
	}

	/**
	 * Se añade la restricción temporal a la sentencia de consulta
	 * @param acLayer
	 */
	public String annadirTemporalSelect(ACLayer acLayer){
    	StringBuffer sb = new StringBuffer();
       	int indice = acLayer.getSelectQuery().toLowerCase().indexOf("from");
	    String cadTroz0 = acLayer.getSelectQuery().substring(0,indice-1);
       	int indice2 = acLayer.getSelectQuery().toLowerCase().indexOf("group by");
       	String cadTroz1 = "";
       	String cadTroz2 = "";
       	if (indice2 != -1){
		    cadTroz1 = acLayer.getSelectQuery().substring(indice-1, indice2-1);
		    cadTroz2 = acLayer.getSelectQuery().substring(indice2-1);
       	}else{
		    cadTroz1 = acLayer.getSelectQuery().substring(indice-1);       		
       	}
	    sb = sb.append(cadTroz0);
	    sb = sb.append(","+Const.REVISION_ACTUAL);
	    sb = sb.append(","+Const.REVISION_EXPIRADA);
    	sb = sb.append(cadTroz1);
	    if (version == null || version.isFeaturesActivas()){
	    	sb = sb.append(restriccionAnteriorAFechaActual);
	    }else{
		    sb = sb.append(restriccionFeaturesBaja);
	    }
       	if (indice2 != -1){
	    	sb = sb.append(cadTroz2);
		    sb = sb.append(","+Const.REVISION_ACTUAL);
		    sb = sb.append(","+Const.REVISION_EXPIRADA);
       	}
	    return sb.toString();
	}
	
	/**
	 * Se añade la restricción temporal a la sentencia de borrado
	 * @param acLayer
	 */
	public String annadirTemporalDelete(ACLayer acLayer){
    	StringBuffer sb = new StringBuffer();
    	
    	//A veces se meten saltos de linea en las queries y eso provoca que falle el split
    	//Los quitamos
    	String querySinSaltosDeLinea = acLayer.getUpdateQuery().replaceAll("[\n\r]","");
    			
    	int indice = querySinSaltosDeLinea.toLowerCase().indexOf("where");
	    String cadTroz0 = querySinSaltosDeLinea.substring(0,indice-1);
	    String cadTroz1 = querySinSaltosDeLinea.substring(indice-1);
	    sb = sb.append(cadTroz0);
   		sb = sb.append(","+Const.REVISION_EXPIRADA+"="+versionActual);
       	sb = sb.append(cadTroz1);
       	sb = sb.append(restriccionAnteriorAFechaActual);
	    return sb.toString();
	}
	
	private String addNextValSentence(ACLayer acLayer){
		StringBuffer sb = new StringBuffer();
    	sb = sb.append("select ");
    	sb = sb.append("nextval('\"seq_version_");
    	sb = sb.append(this.version.getIdTable());
    	sb = sb.append("\"')");
	    return sb.toString();
	}

	/**
	 * Se añade la restricción temporal a la sentencia de actualización
	 * @param acLayer
	 * @param useNextVal 
	 */
	public String annadirTemporalUpdate(ACLayer acLayer){
		return annadirTemporalUpdate(acLayer,false);
	}
	public String annadirTemporalUpdate(ACLayer acLayer, boolean useNextVal){
    	StringBuffer sb = new StringBuffer();
		if (!acLayer.getUpdateQuery().equals("")){
	    	if (versionado){
	    		
	    		String querySinSaltosDeLinea = acLayer.getUpdateQuery().replaceAll("[\n\r]","");
	    		
	        	int indice1 = querySinSaltosDeLinea.toLowerCase().indexOf("set");
	        	int indice2 = querySinSaltosDeLinea.toLowerCase().indexOf("where");
	    	    String cadTroz0 = querySinSaltosDeLinea.substring(0,indice1);
	    	    String cadTroz1 = querySinSaltosDeLinea.substring(indice2-1);
		    	sb = sb.append(cadTroz0);
		    	sb = sb.append("SET ");
		    	sb = sb.append(Const.REVISION_EXPIRADA);
		    	sb = sb.append("=");
		    	if (useNextVal)
		    		sb = sb.append	("nextval('\"seq_version_");		    	
		    	else
		    		sb = sb.append	("currval('\"seq_version_");		    	
		    		
		    	sb = sb.append(this.version.getIdTable());
		    	sb = sb.append("\"')");
		    	sb = sb.append(cadTroz1);
	    	}else{
		    	sb = sb.append(acLayer.getUpdateQuery());
	    	}
	       	sb = sb.append(revisionExpiradaNula);
		}
	    return sb.toString();
	}
	
	public String borrarVersionTemporal(ACLayer acLayer){
    	StringBuffer sb = new StringBuffer();
		if (!acLayer.getDeleteQuery().equals("")){				
			sb.append(acLayer.getDeleteQuery());
			sb.append(" and ("+Const.REVISION_EXPIRADA+"="+Const.REVISION_TEMPORAL+" or "+Const.REVISION_EXPIRADA+"="+Const.REVISION_PUBLICABLE+")");
		}
	    return sb.toString();
	}
	
	public String borrarVersionAutoPublicable(ACLayer acLayer){
    	StringBuffer sb = new StringBuffer();
		if (!acLayer.getDeleteQuery().equals("")){				
			sb.append(acLayer.getDeleteQuery());
			sb.append(" and ("+Const.REVISION_EXPIRADA+"="+Const.REVISION_PUBLICABLE_PDA+")");
		}
	    return sb.toString();
	}
	

	/**
	 * 	
	 * Inserta una nueva revisión en la base de datos
	 * @param acLayer
	 */
	private void insertarNuevaRevision(ACLayer acLayer){
		insertarNuevaRevision(acLayer,false);
	}
	private String insertarNuevaRevision(ACLayer acLayer,boolean useNextval){
    	StringBuffer sbSQL = new StringBuffer("insert into versiones (revision,id_autor,fecha,id_table_versionada) values (");
    	if (useNextval)
    		sbSQL = sbSQL.append("nextval('\"seq_version_");
    	else
    		sbSQL = sbSQL.append("currval('\"seq_version_");
   		sbSQL = sbSQL.append(this.version.getIdTable());
    	sbSQL = sbSQL.append("\"'),");
    	if (version != null && version.getIdUsuario() != -1)
    		sbSQL = sbSQL.append(version.getIdUsuario());
    	else{
    		int usuario = acLayer.getIdUsuario();
    		sbSQL = sbSQL.append(usuario);
    	}
    	sbSQL = sbSQL.append(",");
    	sbSQL = sbSQL.append("(select date_trunc('second', localtimestamp)),");
   		sbSQL = sbSQL.append(this.version.getIdTable());
    	sbSQL = sbSQL.append(");");
    	
    	//En el caso de usar el nextval vamos a actualizar la sentencia de actualizacion en lugar
    	//de la de insercion
    	if (useNextval)
    		return sbSQL.toString();
    	else{
    		acLayer.setInsertQuery(acLayer.getInsertQuery()+";"+sbSQL.toString());
    		return null;
    	}
    }

}
