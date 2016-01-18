package com.geopista.app.catastro;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Hashtable;
import java.util.Iterator;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;




public class PadronHabitantesPostgre
{

	public Connection conn = null;

	public static AppContext app =(AppContext) AppContext.getApplicationContext();
	Blackboard Identificadores = app.getBlackboard();

	public PadronHabitantesPostgre()
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
		Connection con=  app.getConnection();
		con.setAutoCommit(false);
		return con;
	}  


	public Hashtable DatosPoblacion(int idHabitante)
	{
		Hashtable Datos = new Hashtable(); 

		try
		{    
			//String query = "select codigoprovincia, codigomunicipio from habitantes where id_habitante=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("padrondatospoblacion");
			s.setInt(1, idHabitante);
			r = s.executeQuery();  
			while (r.next())
			{
				Datos.put("CodProvincia",r.getString("codigoprovincia"));
				Datos.put("CodMunicipio",r.getString("codigomunicipio"));
			}     


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();


			return Datos;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return null;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return null;
		} 


	}


	public String findMunicipioById(String idProvincia, String idMunicipio)
	{

		String nombreMunicipio="";

		NumberFormat formatter = new DecimalFormat("00");
		idProvincia = formatter.format(Integer.parseInt(idProvincia));
		formatter = new DecimalFormat("000");
		idMunicipio = formatter.format(Integer.parseInt(idMunicipio));


		try
		{    
			//String query = "select nombreoficial from municipios where id_provincia=? and id_ine=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("buscarnombremunicipio");
			s.setString(1, idProvincia);
			s.setString(2, idMunicipio);
			r = s.executeQuery();  
			while (r.next())
			{
				nombreMunicipio= r.getString("nombreoficial");
			}    


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return nombreMunicipio;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return null;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return null;
		} 
	}

	public String findProvinciaById(String idProvincia)
	{

		String nombreProvincia="";

		NumberFormat formatter = new DecimalFormat("00");
		idProvincia = formatter.format(Integer.parseInt(idProvincia));


		try
		{    
			//String query = "select nombreoficial from provincias where id=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("buscarnombreprovincia");
			s.setString(1, idProvincia);
			r = s.executeQuery();  
			while (r.next())
			{
				nombreProvincia= r.getString("nombreoficial");
			}    


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return nombreProvincia;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return null;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return null;
		} 


	}


	public ArrayList DatosHabitante(int idHabitante)
	{
		ArrayList Datos = new ArrayList(); 



		try
		{    
			//String query = "select * from habitantes where id_habitante=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("padrondatoshabitante");
			s.setInt(1, idHabitante);
			r = s.executeQuery();  
			while (r.next())
			{
				//Datos.add(r.getString("codigoprovincia")); 
				//Datos.add(r.getString("codigomunicipio")); 
				Datos.add(r.getString("nombre")); 
				Datos.add(r.getString("sexo")); 
				Datos.add(r.getString("part_apell1")); 
				Datos.add(r.getString("apellido1")); 
				Datos.add(r.getString("part_apell2")); 
				Datos.add(r.getString("apellido2")); 
				Datos.add(r.getString("nacprovincia")); 
				Datos.add(r.getString("nacmunicipio")); 
				Datos.add(r.getString("nacfecha")); 


				//Tipo de documento:
				//0 Sin documento acreditativo, 1 DNI, 2 Pasaporte, 3 Tarjeta de residencia
				int tipoDocumento = r.getInt("tipodocumento");
				if (tipoDocumento==1)
				{
					Datos.add("1");
					Datos.add(r.getString("dni")); 
					Datos.add(r.getString("letradni")); 
					Datos.add("");Datos.add("");  
				}
				else if (tipoDocumento==0 || tipoDocumento==2 || tipoDocumento==3){
					Datos.add(String.valueOf(tipoDocumento));
					Datos.add("");Datos.add(""); 
					Datos.add(r.getString("dni")); 
					Datos.add(r.getString("letradni")); 	             
				}
				else{
					Datos.add("0");
					Datos.add("");Datos.add(""); 
					Datos.add(r.getString("dni")); 
					Datos.add(r.getString("letradni")); 	
				}




				//Datos adicionales
				Datos.add(r.getString("hojapadron")); 
				Datos.add(r.getString("ocupacion"));

				Datos.add(r.getString("id_domicilio")); 


			}     


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return Datos;

		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return null;
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		} 


	}



	public ArrayList DatosDomicilio(String idProvincia, String idMunicipio, int idDomicilio)
	{
		ArrayList Datos = new ArrayList(); 

		try
		{    
			//String query = "select * from domicilio where id_domicilio=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("padrondatosdomicilio");
			s.setInt(1, idDomicilio);
			r = s.executeQuery();  
			while (r.next())
			{
				//Distrito
				Hashtable codDistrito = findCodigoDistritoById(r.getInt("id_distrito"));
				Datos.add(codDistrito.get("INE"));

				//Sección
				Hashtable codSeccion = findCodigoSeccionById(r.getInt("id_seccion"));
				Datos.add(codSeccion.get("INE"));

				//Entidad colectiva
				Hashtable codEntidadColectiva = findCodigoEntidadColectivaById(r.getInt("id_entidadcolectiva"));
				Datos.add(codEntidadColectiva.get("INE"));
				Datos.add(codEntidadColectiva.get("Nombre"));

				//Entidad singular
				Hashtable codEntidadSingular = findCodigoEntidadSingularByIneColectiva(idProvincia, idMunicipio, codEntidadColectiva.get("INE").toString()); 
				Datos.add(codEntidadSingular.get("INE"));
				Datos.add(codEntidadSingular.get("Nombre"));


				//Núcleo
				Hashtable codNucleo = findCodigoNucleoById(r.getInt("id_nucleo"));
				Datos.add(codNucleo.get("INE"));
				Datos.add(codNucleo.get("Nombre"));

				//Vía      
				Datos.add(r.getString("codigovia")); 
				Datos.add(r.getString("tipovia")); 
				Datos.add(r.getString("nombrevia"));


				//Pseudovía
				Datos.add(""); //codigo
				Datos.add(""); //nombre


				//Número
				Datos.add(r.getString("tiponumero")); 
				Datos.add(r.getString("numero")); 
				Datos.add(r.getString("calificadornumero")); 
				Datos.add(r.getString("numerosuperior")); 
				Datos.add(r.getString("calificadornumerosuperior")); 

				Datos.add(r.getString("kilometro")); 
				Datos.add(r.getString("hectometro"));
				Datos.add(r.getString("bloque"));          
				Datos.add("");//Portal?
				Datos.add(r.getString("id_escalera")); 
				Datos.add(r.getString("planta")); 
				Datos.add(r.getString("puerta")); 
				Datos.add(r.getString("tipolocal")); 

				Datos.add(r.getString("id_ayuntamiento")); 


			}     


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return Datos;

		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return null;
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}    
	}

	public Hashtable findCodigoDistritoById(int idDistrito)
	{
		return findCodigoIneById(idDistrito, "buscarcodigodistrito");
	} 

	public Hashtable findCodigoSeccionById(int idSeccion)
	{
		return findCodigoIneById(idSeccion, "buscarcodigoseccion");
	}

	public Hashtable findCodigoEntidadColectivaById(int idEntidad)
	{
		return findCodigoIneById(idEntidad, "buscarcodigoentidadcolectiva");
	} 

	public Hashtable findCodigoEntidadSingularByIneColectiva(String idProvincia, String idMunicipio, String codEntidadColectiva)
	{   
		Hashtable Datos = new Hashtable();
		Datos.put("INE", "");
		Datos.put("Nombre", "");

		NumberFormat formatter = new DecimalFormat("00");
		idProvincia = formatter.format(Integer.parseInt(idProvincia));

		formatter = new DecimalFormat("000");
		idMunicipio = formatter.format(Integer.parseInt(idMunicipio));


		try
		{    
			//String query = "select codigoine,nombreoficialcorto from entidadessingulares where id_municipio=? and codigo_entidadcolectiva=?";
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("buscarcodigoentidadsingular");
			s.setString(1, idProvincia+idMunicipio);
			s.setString(2, codEntidadColectiva);
			r = s.executeQuery();  
			while (r.next())
			{
				Datos.put("INE", r.getString(1));
				Datos.put("Nombre", r.getString(2));
			}    


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return Datos;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return Datos;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return Datos;
		} 

	} 


	public Hashtable findCodigoNucleoById(int idNucleo)
	{   
		return findCodigoIneById(idNucleo, "buscarcodigonucleo");
	}


	private Hashtable findCodigoIneById(int id, String querySQL)
	{  
		Hashtable Datos = new Hashtable();
		Datos.put("INE", "");
		Datos.put("Nombre", "");

		try
		{    
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement(querySQL);
			s.setInt(1, id);
			r = s.executeQuery();  
			while (r.next())
			{
				Datos.put("INE", r.getString(1));
				Datos.put("Nombre", r.getString(2));

			}    


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return Datos;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return Datos;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return Datos;
		} 
	}
	/**
	 * Busca un domicilio en la tabla domicilios
	 * 
	 * @param datos del domicilio a buscar
	 *             
	 * @return int Identificador del domicilio buscado, 0 en caso de no encontrarlo, -1 si hay error
	 * 
	 */
	public int findDomicilio(GeopistaDatosImportarPadron datos)
	{
		int idDomicilio = 0;
		int error = -1;

		try
		{
			PreparedStatement s = null;
			ResultSet r = null;

			s = conn.prepareStatement("buscarIdDomicilio");
			s.setString(1, datos.getIdentifAyuntamientoDV());
			s.setInt(2, datos.getIdDistrito());
			s.setInt(3, datos.getIdSeccion());
			s.setInt(4, datos.getIdEntidadColectiva());
			s.setInt(5, datos.getIdNucleo());
			s.setInt(6, datos.getIdVia());            
			s.setInt(7, Integer.parseInt(datos.getCodVia()));
			s.setString(8, datos.getTipoVia());
			s.setString(9, datos.getNombreCortoVia());
			s.setString(10, datos.getTipoNumero());
			s.setInt(11, Integer.parseInt(datos.getNumero()));
			s.setString(12, datos.getCalificador());
			s.setInt(13, Integer.parseInt(datos.getNumeroSuperior()));
			s.setString(14, datos.getCalificadorSuperior());
			s.setInt(15, Integer.parseInt(datos.getKilometro()));
			s.setInt(16, Integer.parseInt(datos.getHectometro()));
			s.setString(17, datos.getBloque());
			s.setString(18, datos.getPlanta());
			s.setString(19, datos.getPuerta());
			s.setString(20, datos.getIdEscalera());
			s.setString(21, datos.getTipoLocal());          

			if (s.execute())
			{
				r = s.getResultSet();

				if (r.next())
				{
					//en este caso el resultado es el id_domicilio en caso de que exista
					idDomicilio = r.getInt(1);
				}
			}


			return idDomicilio;

		}
		catch (SQLException ex){

			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return error;

		}        
		catch(Exception ex){

			return error;

		}

	}

	/**
	 * Inserta un nuevo domicilio en la tabla domicilios
	 * 
	 * @param datos del domicilio
	 *             
	 * @return int Identificador del domicilio insertado, o -1 en caso de error
	 * 
	 */
	public int insertDomicilio(GeopistaDatosImportarPadron datos)
	{
		PreparedStatement s = null;
		ResultSet r = null;
		int idDomicilio = 0;
		int error = -1;
		boolean tablaModificada = false;
		String aux = "";

		try{
			//Buscar el siguiente identificador de domicilio (secuencial)
			s = conn.prepareStatement("buscarSecIdDomicilio");
			if (s.execute())
			{
				r = s.getResultSet();

				if (r.next())
				{
					aux = String.valueOf(r.getInt(1));
					if (aux != null)            
						idDomicilio = r.getInt(1);        

				}  
			}

		}catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return error;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return error;
		}


		if (idDomicilio>0)
		{
			tablaModificada = modificaDatosDomicilio(idDomicilio, "insertarDomicilio", datos);
		}       


		if (tablaModificada) 
		{
			return idDomicilio;
		}
		else
		{
			return error;
		}  
	}


	/**
	 * Inserta un nuevo habitante en la tabla habitantes
	 * 
	 * @param datos del habitante
	 *             
	 * @return int Identificador del habitante insertado, o -1 en caso de error
	 * 
	 */
	public int insertHabitante(GeopistaDatosImportarPadron datos)
	{
		PreparedStatement s = null;
		ResultSet r = null;
		int idHabitante = 0;
		int error = -1;
		String aux="";
		boolean tablaModificada = false;

		try{
			//Buscar el siguiente identificador de habitante (secuencial)            
			s = conn.prepareStatement("buscarSecIdHabitante");
			if (s.execute())
			{
				r = s.getResultSet();
				if (r.next())
				{   
					aux = String.valueOf(r.getInt(1));
					if (aux != null)            
						idHabitante = r.getInt(1);   

				}
			}

		}catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return error;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return error;
		}

		if (idHabitante>0)
		{
			tablaModificada = modificaDatosHabitantes(idHabitante, "insertarHabitante", datos);
			if (tablaModificada) 
			{
				return idHabitante;
			} 

		}  
		return error;


	}

	public int findIdDistritoByIne(int idMunicipioCompleto, String codDistrito)
	{

		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idMunicipioCompleto));
		Tipos.add("0");    
		Valores.add(codDistrito);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdDistrito");

	}

	public int findIdSeccionByIne (int idDistrito, String codSeccion)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idDistrito));
		Tipos.add("0");  
		Valores.add(codSeccion);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdSeccion");
	}

	public int findIdEntidadColectivaByIne (int idMunicipioCompleto, String codEntidadColectiva)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idMunicipioCompleto));
		Tipos.add("0");    
		Valores.add(codEntidadColectiva);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdEntidadColectiva");

	}

	public int findIdEntidadSingularByIne (int idMunicipioCompleto, String codEntidadColectiva, String codEntidadSingular)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idMunicipioCompleto));
		Tipos.add("0");    
		Valores.add(codEntidadColectiva);
		Tipos.add("1");
		Valores.add(codEntidadSingular);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdEntidadSingular");


	}

	public int findIdNucleoByIne (int idEntidadSingular, String codNucleo)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idEntidadSingular));
		Tipos.add("0");    
		Valores.add(codNucleo);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdNucleo");


	}

	public int findIdViaByIne (int idMunicipioCompleto, String codVia)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idMunicipioCompleto));
		Tipos.add("0");    
		Valores.add(codVia);
		Tipos.add("0");

		return findIdByValues(Valores, Tipos, "buscarIdVia");
	}

	public int findIdPseudoViaByIne (int idMunicipioCompleto, String codPseudoVia)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros y 1 para cadenas
		Valores.add(Integer.toString(idMunicipioCompleto));
		Tipos.add("0");    
		Valores.add(codPseudoVia);
		Tipos.add("1");

		return findIdByValues(Valores, Tipos, "buscarIdPseudovia");
	}

	private int findIdByValues(ArrayList Valores, ArrayList Tipos, String querySQL)
	{
		int identificador=0;
		int error= -1;

		try
		{    
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement(querySQL);

			Iterator Datos = Valores.iterator();
			Iterator TipoDatos = Tipos.iterator();

			int n=0;

			while (Datos.hasNext())
			{
				
				n++;

				switch (Integer.parseInt(TipoDatos.next().toString())){
				case 0:
					String num =Datos.next().toString();

					if (!num.equals("")){
						s.setInt(n,Integer.parseInt(num));
					}else
					{
						s.setNull(n, 0);
					}
					break;

				case 1:
					String Dat=Datos.next().toString();	               
					if (!Dat.equals("")){
						s.setString(n,Dat);
					}else
					{
						s.setNull(n, 1);
					}
					break;


				case 2:
					Date date = (Date)Datos.next();
					if (!date.equals("")){
						s.setDate(n,date);
					}else
					{
						s.setNull(n, 91);
					}
					break;


				default:
					break;

				}    
			}       

			r = s.executeQuery();  
			if (r.next())
			{
				identificador= r.getInt(1);           
			}    


			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return identificador;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return error;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return error;
		}
	}

	public boolean deleteHabitante(GeopistaDatosImportarPadron datos)
	{
		try
		{
			PreparedStatement s = null;
			s = conn.prepareStatement("padronBajaHabitante");


			s.setInt(1, Integer.parseInt(datos.getCodProvincia()));
			s.setInt(2, Integer.parseInt(datos.getCodMunicipio()));
			s.setString(3, datos.getNombreHabitante());
			s.setString(4, datos.getParticulaPrimerApellido());
			s.setString(5, datos.getPrimerApellido());
			s.setString(6, datos.getParticulaSegundoApellido());
			s.setString(7, datos.getSegundoApellido());	 
			s.setInt(8, Integer.parseInt(datos.getCodProvinciaNacimiento()));	 
			s.setInt(9, Integer.parseInt(datos.getCodMunicipioNacimiento()));	 

			//Date date = new Date(Integer.parseInt(datos.getAnioNacimiento().toString()),
					//        Integer.parseInt(datos.getMesNacimiento().toString()), 
			//        Integer.parseInt(datos.getDiaNacimiento().toString()));
			//s.setDate(10, date);

			String fecha = datos.getAnioNacimientoDV()+"/"+datos.getMesNacimientoDV()+"/"+datos.getDiaNacimientoDV();
			s.setString(10, fecha);


			//Si es español vienen rellenos los datos: identificador y codigo de control
			//Si es extranjero, vienen rellenos los datos: numero del documento y letra documento
			if (!datos.getIdentificador().trim().equals("") && !datos.getCodigoControl().trim().equals("")){
				//Español
				s.setString(11, datos.getIdentificador());
				s.setString(12, datos.getCodigoControl());
			}
			else if(!datos.getNumeroDocumento().trim().equals("") ){
				//Extranjero
				s.setString(11, datos.getNumeroDocumento());
				s.setString(12, datos.getLetraDocumentoExtranjeros());
			}	                    

			s.setInt (13, Integer.parseInt(datos.getTipoIdentificador()));

			s.executeUpdate();  
			conn.commit();

			s.close();
			conn.close();
			return true;

		}catch (SQLException ex)
		{

			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}

	}


	public int findIdHabitanteByDatosPadron (GeopistaDatosImportarPadron datos)
	{   
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();


		//Tipo es 0 para enteros, 1 para cadenas y 2 para fechas
		Valores.add(datos.getCodProvincia());
		Tipos.add("0");    
		Valores.add(datos.getCodMunicipio());
		Tipos.add("0");    
		Valores.add(datos.getNombreHabitante());
		Tipos.add("1");    
		Valores.add(datos.getParticulaPrimerApellido());
		Tipos.add("1");    
		Valores.add(datos.getPrimerApellido());
		Tipos.add("1");    
		Valores.add(datos.getParticulaSegundoApellido());
		Tipos.add("1");    
		Valores.add(datos.getSegundoApellido());
		Tipos.add("1");    
		Valores.add(datos.getCodProvinciaNacimiento());
		Tipos.add("0");    
		Valores.add(datos.getCodMunicipioNacimiento());
		Tipos.add("0");    

		//fecha de nacimiento

		//Date date = new Date(Integer.parseInt(datos.getAnioNacimiento().toString()),
		//        Integer.parseInt(datos.getMesNacimiento().toString()), 
		//        Integer.parseInt(datos.getDiaNacimiento().toString()));

		String strFecha = datos.getAnioNacimiento().toString()+"/"+datos.getMesNacimiento().toString()+"/"+datos.getDiaNacimiento().toString();
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date fecha = null;
		java.sql.Date date = null;
		try {

			fecha = formatoDelTexto.parse(strFecha);
			date= new java.sql.Date(fecha.getTime());

		} catch (ParseException ex) {
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		}

		Valores.add(date);
		Tipos.add("2");    

		//Identificación
		//Si es español vienen rellenos los datos: identificador y codigo de control
		//Si es extranjero, vienen rellenos los datos: numero del documento y letra documento
		if (!datos.getIdentificador().trim().equals("") && !datos.getCodigoControl().trim().equals("")){
			//Español
			Valores.add(datos.getIdentificador());
			Tipos.add("1");    
			Valores.add(datos.getCodigoControl());
			Tipos.add("1");    
		}else if (!datos.getIdentificadorDV().trim().equals("") && !datos.getCodigoControlDV().trim().equals("")){
			//Español
			Valores.add(datos.getIdentificadorDV());
			Tipos.add("1");    
			Valores.add(datos.getCodigoControlDV());
			Tipos.add("1");    
		}
		else if(!datos.getNumeroDocumento().trim().equals("") ){
			//Extranjero
			Valores.add(datos.getNumeroDocumento());
			Tipos.add("1");
			Valores.add(datos.getLetraDocumentoExtranjeros());
			Tipos.add("1");
		}
		else if(!datos.getNumeroDocumentoDV().trim().equals("") ){
			//Extranjero
			Valores.add(datos.getNumeroDocumentoDV());
			Tipos.add("1");
			Valores.add(datos.getLetraDocumentoExtranjerosDV());
			Tipos.add("1");
		}
		else{
			Valores.add("");
			Tipos.add("1");    
			Valores.add("");
			Tipos.add("1");
		}

		//Tipo de documento
		if (!datos.getIdentificador().trim().equals("") && !datos.getCodigoControl().trim().equals(""))
			Valores.add(datos.getTipoIdentificador());
		else
			Valores.add(datos.getTipoIdentificadorDV());
		Tipos.add("0");


		return findIdByValues(Valores, Tipos, "buscarIdHabitante");


	}

	public boolean updateHabitante (int idHabitante, GeopistaDatosImportarPadron datos)
	{     
		if (findIdHabitanteByDatosPadron(datos) > 0){
			return modificaDatosHabitantes(idHabitante, "padronModificacionHabitante", datos);	
		} else{
			return (insertHabitante(datos) != -1);
		}

	}


	public boolean modificaDatosHabitantes (int idHabitante, String querySQL, GeopistaDatosImportarPadron datos) 
	{
		try{

			PreparedStatement s = null;

			s = conn.prepareStatement(querySQL);
			s.setInt(1, Integer.parseInt(datos.getCodProvincia()));
			s.setInt(2, Integer.parseInt(datos.getCodMunicipio()));
			s.setString(3, datos.getNombreDV());
			s.setString(4, datos.getParticula1DV());
			s.setString(5, datos.getApellido1DV());
			s.setString(6, datos.getParticula2DV());
			s.setString(7, datos.getApellido2DV());	 
			s.setInt(8, Integer.parseInt(datos.getProvinciaNacimientoDV()));	 
			s.setInt(9, Integer.parseInt(datos.getMunicipioNacimientoDV()));	 

			//Date date = new Date(Integer.parseInt(datos.getAnioNacimientoDV().toString()),
					//        Integer.parseInt(datos.getMesNacimientoDV().toString()), 
			//        Integer.parseInt(datos.getDiaNacimientoDV().toString()));
			//s.setDate(10, date);
			String fecha = datos.getAnioNacimientoDV()+"/"+datos.getMesNacimientoDV()+"/"+datos.getDiaNacimientoDV();
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd"); 
			java.util.Date d=sdf.parse(fecha); 
			java.sql.Date date= new java.sql.Date(d.getTime());

			s.setDate(10, (java.sql.Date)date);

			//Si es español vienen rellenos los datos: identificador y codigo de control
			//Si es extranjero, vienen rellenos los datos: numero del documento y letra documento
			if (!datos.getIdentificadorDV().trim().equals("") && !datos.getCodigoControlDV().trim().equals("")){
				//Español
				s.setString(11, datos.getIdentificadorDV());
				s.setString(12, datos.getCodigoControlDV());
			}
			else if(!datos.getNumeroDocumentoDV().trim().equals("") ){
				//Extranjero
				s.setString(11, datos.getNumeroDocumentoDV());
				s.setString(12, datos.getLetraDocumentoExtranjerosDV());
			}
			else{
				System.out.println("habitante no localizado. Ni español ni extranjero.");
				return false;
			}

			s.setInt(13, Integer.parseInt(datos.getTipoIdentificadorDV()));


			s.setString(14, datos.getSexo());
			s.setString(15, datos.getCodigoNivelEstudios());
			s.setInt(16, datos.getIdDomicilio());
			s.setString(17, datos.getNumeroHojaPadronal());
			/*s.setString(18, datos.getCodigoNivelEstudios());
			s.setInt(19, idHabitante);*/
			s.setInt(18, idHabitante);


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
		catch (ParseException ex)
		{

			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return false;
		}     

	} 

	public int findIdDomicilioByIdHabitante(int idHabitante)
	{
		ArrayList Valores= new ArrayList();
		ArrayList Tipos= new ArrayList();

		//Tipo es 0 para enteros, 1 para cadenas y 2 para fechas
		Valores.add(String.valueOf(idHabitante));
		Tipos.add("0");    

		return findIdByValues(Valores, Tipos, "buscarDomicilioHabitante");
	}

	public boolean updateDomicilio(GeopistaDatosImportarPadron datos)
	{
		return modificaDatosDomicilio(datos.getIdDomicilio(), "padronModificacionDomicilio", datos);
	}

	private boolean modificaDatosDomicilio (int idDomicilio, String querySQL, GeopistaDatosImportarPadron datos)
	{	     
		try
		{   
			PreparedStatement s = null;

			s = conn.prepareStatement(querySQL);
			s.setString(1, datos.getIdentifAyuntamientoDV());
			s.setInt(2, datos.getIdDistrito());
			s.setInt(3, datos.getIdSeccion());
			s.setInt(4, datos.getIdEntidadColectiva());
			s.setInt(5, datos.getIdNucleo());
			s.setInt(6, datos.getIdVia());
			s.setInt(7, Integer.parseInt(datos.getCodVia()));
			s.setString(8, datos.getTipoVia());
			s.setString(9, datos.getNombreCortoVia());
			s.setString(10, datos.getTipoNumero());
			s.setInt(11, Integer.parseInt(datos.getNumero()));
			s.setString(12, datos.getCalificador());
			s.setInt(13, Integer.parseInt(datos.getNumeroSuperior()));
			s.setString(14, datos.getCalificadorSuperior());
			s.setInt(15, Integer.parseInt(datos.getKilometro()));
			s.setInt(16, Integer.parseInt(datos.getHectometro()));
			s.setString(17, datos.getBloque());
			s.setString(18, datos.getPlanta());
			s.setString(19, datos.getPuerta());
			s.setString(20, datos.getIdEscalera());
			s.setString(21, datos.getTipoLocal());   
			s.setInt(22, idDomicilio);

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

	private String deleteFirstZeroValues(String stringWithZeroValues){
		if(stringWithZeroValues.length() > 0 ){
			if(stringWithZeroValues.substring(0, 1).equals("0"))
				stringWithZeroValues = deleteFirstZeroValues(stringWithZeroValues.substring(1,stringWithZeroValues.length()));
		}
		return stringWithZeroValues;
	}
	
	public int findIdPortal(int idVia,String portal) {
		// TODO Auto-generated method stub
		String query = "buscarIdPortalDomicilio" ;
		System.out.println("Id de via : " + idVia + " Num Portal " + portal);
		portal = deleteFirstZeroValues(portal);
		int idPortal = -1;
		try{   
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement(query);
			s.setInt(1, idVia);
			s.setString(2, portal);
			r = s.executeQuery();  

			while (r.next())
			{
				// Se ha encontrado un id_portal que coincide con los datos
				idPortal = r.getInt("id");
			}
			System.out.println("Portal = " + idPortal);
			if (idPortal > -1){
				return idPortal;
			}else{

			}

		} catch (SQLException ex){
			ex.printStackTrace();

			AppContext app =(AppContext) AppContext.getApplicationContext();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));

			return -1;
		}     
		return -1;
	}

	public ArrayList listaHabitantes (int idNumPolicia)
	{

		ArrayList Datos = new ArrayList();

		try
		{    
			PreparedStatement s = null;
			ResultSet r = null;
			s = conn.prepareStatement("padronHabitantesPolicia");
			s.setInt(1, idNumPolicia);
			r = s.executeQuery();  
			while (r.next())
			{

				Datos.add(r.getString("id_habitante"));
				Datos.add(r.getString("dni"));
				Datos.add(r.getString("letradni"));
				Datos.add(r.getString("part_apell1"));
				Datos.add(r.getString("apellido1"));
				Datos.add(r.getString("part_apell2"));
				Datos.add(r.getString("apellido2"));
				Datos.add(r.getString("nombre"));	        		         

			}    		     

			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			return Datos;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return Datos;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return Datos;
		} 


	}


	public ArrayList[] getListaHabitantes (int idNumPolicia, String orden)
	{  
		ArrayList Ident = new ArrayList();
		ArrayList Datos = new ArrayList();
		ArrayList[] habitantes = new ArrayList[2];

		try
		{    
			PreparedStatement s = null;
			ResultSet r = null;

			//orden puede valer: dni o apellidos
			String querySQL= "padronHabitantesPoliciaOrden"+orden; 
			s = conn.prepareStatement(querySQL);
			s.setInt(1, idNumPolicia);
			r = s.executeQuery();  
			while (r.next())
			{		  
				Ident.add(r.getString("id_habitante"));
				String data = r.getString("dni").trim()+" "+ r.getString("letradni")+ " - ";
				String part_apell1= r.getString("part_apell1");
				if (part_apell1.trim().length()!=0) 
					data= data+ part_apell1+" ";		         
				data= data+ r.getString("apellido1").trim()+ " ";

				String part_apell2= r.getString("part_apell2");
				if (part_apell2.trim().length()!=0) 
					data= data+ part_apell2+" ";

				data=data + r.getString("apellido2").trim()+ ", "+ r.getString("nombre").trim();	
				Datos.add(data);		   

			}    		     

			s.close();
			r.close(); 
			s = null;
			r = null;
			conn.close();

			habitantes[0]=Ident;
			habitantes[1]=Datos;

			return habitantes;

		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return habitantes;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return habitantes;
		} 


	}

	public boolean updateIdPortalAtDomicilio(int idPortal, int idDomicilio) {
		// TODO Auto-generated method stub
		try
		{    
			PreparedStatement s = null;
			s = conn.prepareStatement("updateDomicilioPortal");
			s.setInt(1, idPortal);
			s.setInt(2, idDomicilio );
			int r = s.executeUpdate();  
			s.close();
			s = null;
			conn.close();
		}
		catch (SQLException ex)
		{
			//ex.printStackTrace();
			ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
			return false;
		} 
		catch (Exception ex)
		{
			//ex.printStackTrace();
			return false;
		} 
		return true;
	}

}