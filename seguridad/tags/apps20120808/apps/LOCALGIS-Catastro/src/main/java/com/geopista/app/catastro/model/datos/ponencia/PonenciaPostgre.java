package com.geopista.app.catastro.model.datos.ponencia;

import java.sql.*;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;


public class PonenciaPostgre
{
	public Connection conn = null;

	public PonenciaPostgre()
	{
		try
		{
			conn = getDBConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Connection getDBConnection () throws SQLException
	{
		AppContext app =(AppContext) AppContext.getApplicationContext();
		Connection con=  app.getConnection();
		con.setAutoCommit(false);
		return con;
	}  

	public boolean insertPonenciaCNT(PonenciaCNT ponenciaCNT){

		String ID = null;

		try
		{
			PreparedStatement s = null;
			PreparedStatement f = null;
			ResultSet r = null;		 

			f = conn.prepareStatement("MCfindIDPonenciaCNT");
			NumberFormat formatterCod2 = new DecimalFormat("00");	 
			NumberFormat formatterCod3 = new DecimalFormat("000");	   

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaCNT.getCodDelegacionMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaCNT.getCodMunicipioMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaCNT.getCodProvinciaINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaCNT.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaCNT.getAnioAprobacion());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getTipoPonencia());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaCNT.getAnioEfectos());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getAnioAprobacionTotal());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getAnioEfectosTotal());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getAnioNormas());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getAnioCuadroMarco());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getAplicFormula());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaCNT.getInfraedificacion());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaCNT.getAntiguedad());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaCNT.getSinDesarrollar());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaCNT.getRuinoso());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaCNT.getPropVertical());

			tiposSQL.add(new Integer(Types.VARCHAR));

			setParametersSQL(f, datosSQL, tiposSQL);

			if (f.execute())
			{
				r = f.getResultSet();

				if (r.next())
				{                
					ID = r.getString(1);
				}
			}


			if(ID==null){
				s = conn.prepareStatement("MCinsertPonenciaCNT");

			}
			else{
				ponenciaCNT.setIDentificador(String.valueOf(ID));        	 
				s = conn.prepareStatement("MCupdatePonenciaCNT");
				s.setString(19, ponenciaCNT.getIDentificador());
			}

			NumberFormat formatterCodigo2 = new DecimalFormat("00");	
			NumberFormat formatterCodigo3 = new DecimalFormat("000");

			ArrayList datosSQL2 = new ArrayList();
			ArrayList tiposSQL2 = new ArrayList();

			datosSQL2.add(formatterCodigo2.format(Double.valueOf(ponenciaCNT.getCodDelegacionMEH())));

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(formatterCodigo3.format(Double.valueOf(ponenciaCNT.getCodMunicipioMEH())));

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(formatterCodigo2.format(Double.valueOf(ponenciaCNT.getCodProvinciaINE())));

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(formatterCodigo3.format(Double.valueOf(ponenciaCNT.getCodMunicipioINE())));

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(ponenciaCNT.getAnioAprobacion());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getTipoPonencia());

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(ponenciaCNT.getAnioEfectos());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getAnioAprobacionTotal());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getAnioEfectosTotal());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getAnioNormas());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getAnioCuadroMarco());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getAplicFormula());

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(ponenciaCNT.getInfraedificacion());

			tiposSQL2.add(new Integer(Types.FLOAT));
			datosSQL2.add(ponenciaCNT.getAntiguedad());

			tiposSQL2.add(new Integer(Types.INTEGER));
			datosSQL2.add(ponenciaCNT.getSinDesarrollar());

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(ponenciaCNT.getRuinoso());

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(ponenciaCNT.getPropVertical());

			tiposSQL2.add(new Integer(Types.VARCHAR));
			datosSQL2.add(formatterCodigo2.format(Double.valueOf(ponenciaCNT.getCodProvinciaINE()))+formatterCodigo3.format(Double.valueOf(ponenciaCNT.getCodMunicipioINE())));

			tiposSQL2.add(new Integer(Types.VARCHAR));

			setParametersSQL(s, datosSQL2, tiposSQL2);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}

	private void setParametersSQL(PreparedStatement f, ArrayList datosSQL, ArrayList tiposSQL) throws SQLException {

		Iterator j = datosSQL.iterator();
		int index=0;
		Integer tipo;
		Object paramSQL;

		for(Iterator i=tiposSQL.iterator();i.hasNext();){

			index++;
			tipo = (Integer)i.next();
			paramSQL = j.next();

			switch (tipo.intValue()){
			case Types.VARCHAR:
				if(paramSQL!=null){
					f.setString(index,(String) paramSQL);
				}
				else{
					f.setNull(index, Types.VARCHAR);
				}
				break;
			case Types.INTEGER:
				if(paramSQL!=null){
					f.setInt(index,((Integer) paramSQL).intValue());
				}
				else{
					f.setNull(index, Types.INTEGER);
				}
				break;
			case Types.FLOAT:
				if(paramSQL!=null){
					f.setFloat(index,((Float) paramSQL).floatValue());
				}
				else{
					f.setNull(index, Types.FLOAT);
				}
				break;
			case Types.DOUBLE:
				if(paramSQL!=null){
					f.setDouble(index,((Double) paramSQL).doubleValue());
				}
				else{
					f.setNull(index, Types.DOUBLE);
				}
				break;
			}

		}
	}

	public boolean insertPonenciaPoligono(PonenciaPoligono ponenciaPoligono){

		String CodPoligono = null;

		try
		{
			PreparedStatement s = null;
			PreparedStatement f = null;
			ResultSet r = null;		 

			f = conn.prepareStatement("MCfindIDPonenciaPoligono");		 
			f.setString(1,ponenciaPoligono.getCodPoligono());


			if (f.execute())
			{
				r = f.getResultSet();

				if (r.next())
				{                
					CodPoligono = r.getString(1);
				}
			}

			if(CodPoligono==null){		 
				s = conn.prepareStatement("MCinsertPonenciaPoligono");
			}
			else{
				s = conn.prepareStatement("MCupdatePonenciaPoligono");
				s.setString(22, ponenciaPoligono.getCodPoligono());
			}

			NumberFormat formatterCod2 = new DecimalFormat("00");
			NumberFormat formatterCod3 = new DecimalFormat("000");	    

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaPoligono.getCodDelegacionMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaPoligono.getCodMunicipioMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaPoligono.getCodProvinciaINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaPoligono.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getAnioAprobacion());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaPoligono.getCodPoligono());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getImporteMBC());

			tiposSQL.add(new Integer(Types.DOUBLE));
			datosSQL.add(ponenciaPoligono.getImporteMBR());

			tiposSQL.add(new Integer(Types.DOUBLE));
			datosSQL.add(ponenciaPoligono.getCodMBCPlan());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaPoligono.getGrupoPlan());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaPoligono.getUsoPlan());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getModPlan());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaPoligono.getCoefCoordPlan());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaPoligono.getEdifPlan());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaPoligono.getVrb());

			tiposSQL.add(new Integer(Types.DOUBLE));
			datosSQL.add(ponenciaPoligono.getFlGB());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaPoligono.getZonaVUB());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getZonaVRB());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getDiseminado());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaPoligono.getFlGBUni());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaPoligono.getCodProvinciaINE()))+formatterCod3.format(Double.valueOf(ponenciaPoligono.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));

			setParametersSQL(s, datosSQL, tiposSQL);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}

	public boolean insertPonenciaUrbanistica(PonenciaUrbanistica ponenciaUrbanistica){

		String CodZona = null;

		try
		{

			PreparedStatement s = null;
			PreparedStatement f = null;
			ResultSet r = null;		 

			f = conn.prepareStatement("MCfindIDPonenciaUrbanistica");		 
			f.setString(1,ponenciaUrbanistica.getCodZona());		 

			if (f.execute())
			{
				r = f.getResultSet();

				if (r.next())
				{                
					CodZona = r.getString(1);
				}
			}

			if(CodZona==null){		 
				s = conn.prepareStatement("MCinsertPonenciaUrbanistica");
			}
			else{
				s = conn.prepareStatement("MCupdatePonenciaUrbanistica");
				s.setString(28, ponenciaUrbanistica.getCodZona());
			}


			NumberFormat formatterCod2 = new DecimalFormat("00");	
			NumberFormat formatterCod3 = new DecimalFormat("000");

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();

			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaUrbanistica.getCodDelegacionMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaUrbanistica.getCodMunicipioMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaUrbanistica.getCodProvinciaINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaUrbanistica.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getAnioAprobacion());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaUrbanistica.getCodZona());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getDenominacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getCodCalificacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getCodZonificacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getCodOcupacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getCodOrdenacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaUrbanistica.getLongitud());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaUrbanistica.getFondo());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaUrbanistica.getSupMinima());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaUrbanistica.getNumPlantas());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getNumPlantasSolatInt());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getUsoComercial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getUsoResidencial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getUsoOficinas());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getUsoIndustrial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getUsoTuristico());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getOtrosUsos1());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getOtrosUsos2());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getOtrosUsos3());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getZonaVerde());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaUrbanistica.getEdificabilidad().getEquipamientos());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaUrbanistica.getCodProvinciaINE()))+formatterCod3.format(Double.valueOf(ponenciaUrbanistica.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));

			setParametersSQL(s, datosSQL, tiposSQL);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}

	public boolean insertDatosPonenciaTramos(PonenciaTramos ponenciaTramos){

		String CodTramo = null;
		try
		{

			PreparedStatement s = null;
			PreparedStatement f = null;
			ResultSet r = null;		 

			f = conn.prepareStatement("MCfindIDPonenciaTramos");		 
			f.setString(1,ponenciaTramos.getCodTramo());		 

			if (f.execute())
			{
				r = f.getResultSet();

				if (r.next())
				{                
					CodTramo = r.getString(1);
				}
			}

			if(CodTramo==null){		 
				s = conn.prepareStatement("MCinsertPonenciaTramos");
			}
			else{
				s = conn.prepareStatement("MCupdatePonenciaTramos");
				s.setString(40, ponenciaTramos.getCodTramo());
			}

			NumberFormat formatterCod2 = new DecimalFormat("00");	
			NumberFormat formatterCod3 = new DecimalFormat("000");

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaTramos.getCodDelegacionMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaTramos.getCodMunicipioMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaTramos.getCodProvinciaINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaTramos.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getAnioAprobacion());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaTramos.getCodVia());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getCodTramo());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getDenominacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getPonPoligono().getCodPoligono());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getPonUrbanistica().getCodZona());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getSituacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getMaxPar());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaTramos.getCMaxPar());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getMinPar());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaTramos.getCMinPar());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getMaxImpar());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaTramos.getCMaxImpar());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getMinImpar());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaTramos.getCMinImpar());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getValorUnitario());

			tiposSQL.add(new Integer(Types.DOUBLE));
			datosSQL.add(ponenciaTramos.getBanda().getUsoComercial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getUsoResidencial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getUsoOficinas());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getUsoIndustrial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getUsoTuristico());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getOtrosUsos1());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getOtrosUsos2());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getBanda().getOtrosUsos3());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getCorrApDepSuelo());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getCorrApDepConst());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaTramos.getValorSuelo());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getAgua());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getElectricidad());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getAlumbrado());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getDesmonte());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getPavimentacion());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getAlcantarillado());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaTramos.getCostesUrbanizacion());

			tiposSQL.add(new Integer(Types.DOUBLE));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaTramos.getCodProvinciaINE()))+formatterCod3.format(Double.valueOf(ponenciaTramos.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));

			setParametersSQL(s, datosSQL, tiposSQL);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}

	public boolean insertPonenciaTramos(PonenciaTramos ponenciaTramos)
	{
		PreparedStatement s = null;
		ResultSet rPonenciaPoligono = null;
		ResultSet rPonenciaUrbanistica = null;
		boolean error = false;

		try{
			//Buscar el siguiente identificador de habitante (secuencial)            
			s = conn.prepareStatement("MCselectPonenciaPoligono");
			if(ponenciaTramos.getPonPoligono().getCodPoligono()!=null){
				s.setString(1, ponenciaTramos.getPonPoligono().getCodPoligono());
			}
			else{
				s.setNull(1, Types.VARCHAR);
			}

			if (s.execute())
			{
				rPonenciaPoligono = s.getResultSet();
			}

		}catch (SQLException ex)
		{
			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return false;
		} 
		catch (Exception ex)
		{
			return false;
		}

		if (rPonenciaPoligono!=null)
		{   
			try{
				//Buscar el siguiente identificador de habitante (secuencial)            
				s = conn.prepareStatement("MCselectPonenciaUrbanistica");
				if(ponenciaTramos.getPonUrbanistica().getCodZona()!=null){
					s.setString(1,ponenciaTramos.getPonUrbanistica().getCodZona());
				}
				else{
					s.setNull(1, Types.VARCHAR);
				}

				if (s.execute())
				{
					rPonenciaUrbanistica = s.getResultSet();
				}

			}catch (SQLException ex)
			{
				AppContext app =(AppContext) AppContext.getApplicationContext();
				ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
				return false;
			} 
			catch (Exception ex)
			{
				return false;
			}

			if(rPonenciaUrbanistica!=null){

				error = insertDatosPonenciaTramos(ponenciaTramos);
			}

		}  
		return error;


	}

	public boolean insertPonenciaZonaValor(PonenciaZonaValor ponenciaZonaValor){

		String CodZonaValor = null;

		try
		{

			PreparedStatement s = null;
			PreparedStatement f = null;
			ResultSet r = null;		 

			f = conn.prepareStatement("MCfindIDPonenciaZonaValor");		 
			f.setString(1,ponenciaZonaValor.getCodZonaValor());

			if (f.execute())
			{
				r = f.getResultSet();

				if (r.next())
				{                
					CodZonaValor = r.getString(1);
				}
			}

			if(CodZonaValor==null){		 
				s = conn.prepareStatement("MCinsertPonenciaZonaValor");
			}
			else{
				s = conn.prepareStatement("MCupdatePonenciaZonaValor");
				s.setString(22, ponenciaZonaValor.getCodZonaValor());
			}

			NumberFormat formatterCod2 = new DecimalFormat("00");	
			NumberFormat formatterCod3 = new DecimalFormat("000");	

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaZonaValor.getCodDelegacionMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaZonaValor.getCodMunicipioMEH())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaZonaValor.getCodProvinciaINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(formatterCod3.format(Double.valueOf(ponenciaZonaValor.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaZonaValor.getAnioAprobacion());

			tiposSQL.add(new Integer(Types.INTEGER));
			datosSQL.add(ponenciaZonaValor.getCodZonaValor());

			tiposSQL.add(new Integer(Types.VARCHAR));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getUsoComercial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getUsoResidencial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getUsoOficinas());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getUsoIndustrial());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getUsoTuristico());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getOtrosUsos1());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getOtrosUsos2());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getOtrosUsos3());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getZonaVerde());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getImportesZonaValor().getEquipamientos());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getValorUnitario());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getValorZonaVerde());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getValorEquipamientos());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(ponenciaZonaValor.getValorSinDesarrollar());

			tiposSQL.add(new Integer(Types.FLOAT));
			datosSQL.add(formatterCod2.format(Double.valueOf(ponenciaZonaValor.getCodProvinciaINE()))+formatterCod3.format(Double.valueOf(ponenciaZonaValor.getCodMunicipioINE())));

			tiposSQL.add(new Integer(Types.VARCHAR));

			setParametersSQL(s, datosSQL, tiposSQL);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}

	public boolean insertRuEvaluatorio(RUEvaluatorio ruEvaluatorio){

		try
		{

			PreparedStatement s = null;

			s = conn.prepareStatement("MCinsertRuEvaluatorio");	

			ArrayList datosSQL = new ArrayList();
			ArrayList tiposSQL = new ArrayList();

			datosSQL.add(ruEvaluatorio.getCodDelegacionMEH());		 
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getCodMunicipioMEH());		 
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getCodMunicipioINE());		 
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getCodMunicipioAgregado().toString());		 
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getCodCalificacionCatastral());		
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getCodIntensidadProduccion());		 
			tiposSQL.add(new Integer(Types.INTEGER));

			datosSQL.add(ruEvaluatorio.getImporteTipoEvaluatorio());		
			tiposSQL.add(new Integer(Types.DOUBLE));

			datosSQL.add(ruEvaluatorio.getIndicadorVueloPiesSueltos());		
			tiposSQL.add(new Integer(Types.VARCHAR));

			datosSQL.add(ruEvaluatorio.getIntensidadProductivaBOE());		
			tiposSQL.add(new Integer(Types.INTEGER));

			setParametersSQL(s, datosSQL, tiposSQL);

			s.executeUpdate();
			conn.commit();
			s.close();
			conn.close();

			return true;
		}
		catch (SQLException ex)
		{	            
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     
	}


}