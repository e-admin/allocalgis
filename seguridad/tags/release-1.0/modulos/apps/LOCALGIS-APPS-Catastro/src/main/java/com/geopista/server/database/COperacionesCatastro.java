package com.geopista.server.database;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.catastro.Parcela;
import com.geopista.protocol.catastro.Via;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-dic-2004
 * Time: 13:14:49
 */
public class COperacionesCatastro {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesCatastro.class);

    public static CResultadoOperacion getParcela(String sIdParcela){

       Connection connection = null;
       PreparedStatement preparedStatement = null;
       ResultSet rs=null;
       CResultadoOperacion resultado=null;
       try{
           logger.debug("Inicio de obtener la parcela: "+sIdParcela);
           System.out.println(".............INICIO obtener parcela");
           connection = CPoolDatabase.getConnection();
           if (connection == null){
                  logger.warn("No se puede obtener la conexión");
                  return new CResultadoOperacion(false, "No se puede obtener la conexión");
           }
           try {connection.setAutoCommit(false);}catch(Exception e){};

           String sql= "SELECT id_via, id, referencia_catastral, tipo, primer_numero, primera_letra, segundo_numero, segunda_letra, " +
           "direccion_no_estructurada, codigo_postal, superficiesuelo, superficieconstruccionescargos, " +
           "ejercicioefectosibi, fecha_alta, fecha_baja, area, length FROM parcelas WHERE id="+sIdParcela;

           preparedStatement=connection.prepareStatement(sql);
           rs=preparedStatement.executeQuery();

           if (!rs.next()){
               resultado = new CResultadoOperacion(false,"Parcela no encontrada en el sistema");
           }else{
               Parcela parcela= new Parcela();
               parcela.setVia(getVia(rs.getString("id_via"),connection));
               parcela.setId(rs.getString("id"));
               parcela.setReferenciaCatastral(rs.getString("referencia_catastral"));
               parcela.setTipo(rs.getString("tipo"));
               parcela.setPrimerNumero((rs.getString("primer_numero") != null)?new Integer(rs.getInt("primer_numero")).toString():null);
               parcela.setPrimeraLetra(rs.getString("primera_letra"));
               parcela.setSegundoNumero((rs.getString("segundo_numero") != null)?new Integer(rs.getInt("segundo_numero")).toString():null);
               parcela.setSegundaLetra(rs.getString("segunda_letra"));
               parcela.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
               parcela.setCodigoPostal(rs.getString("codigo_postal"));
               parcela.setSuperficieSolar((rs.getString("superficiesuelo")!=null?new Double(rs.getDouble("superficiesuelo")):null));
               parcela.setSuperficieConstruidaTotal((rs.getString("superficieconstruccionescargos")!=null?new Double(rs.getDouble("superficieconstruccionescargos")):null));
               parcela.setAnnoAprobacion((rs.getString("ejercicioefectosibi") != null)?new Integer(rs.getInt("ejercicioefectosibi")).toString():"");
               parcela.setFechaAlta(rs.getDate("fecha_alta"));
               parcela.setFechaBaja(rs.getDate("fecha_baja"));
               parcela.setArea((rs.getString("area")!=null?new Float(rs.getFloat("area")):null));
               parcela.setLength((rs.getString("length")!=null?new Float(rs.getFloat("length")):null));

               rs.close();
               preparedStatement.close();

               sql= "select distritoscensales.nombre as nombre from parcelas, distritoscensales where " +
                    "parcelas.id_distrito = distritoscensales.id and parcelas.id="+sIdParcela;
               preparedStatement=connection.prepareStatement(sql);
               rs=preparedStatement.executeQuery();
               if (rs.next()){
                   parcela.setDistritoCensal(rs.getString("nombre"));
               }
               rs.close();
               preparedStatement.close();

               sql= "select municipios.nombreoficial as municipio, provincias.nombreoficial as provincia " +
                    "from parcelas, municipios, provincias where " +
                    "parcelas.id_municipio=municipios.id and municipios.id_provincia=provincias.id and parcelas.id="+sIdParcela;
               preparedStatement=connection.prepareStatement(sql);
               rs=preparedStatement.executeQuery();
               if (rs.next()){
                   parcela.setMunicipio(rs.getString("municipio"));
                   parcela.setProvincia(rs.getString("provincia"));
               }
               /*
               System.out.println("********** DISTRITO CENSAL="+parcela.getDistritoCensal());
               System.out.println("********** MUNICIPIO="+parcela.getMunicipio());
               System.out.println("********** PROVINCIA="+parcela.getProvincia());
               */

               resultado = new CResultadoOperacion(true,"Operacion realizada correctamente");
               Vector auxVector=new Vector();
               auxVector.add(parcela);
               resultado.setVector(auxVector);
           }
       }catch(Exception e){
          e.printStackTrace();
          java.io.StringWriter sw=new java.io.StringWriter();
          java.io.PrintWriter pw=new java.io.PrintWriter(sw);
          e.printStackTrace(pw);
          logger.error("ERROR al obtener la parcela "+sIdParcela+". Error:"+sw.toString());
          resultado=new CResultadoOperacion(false, e.getMessage());
          try {connection.rollback();} catch (Exception ex2) {}
       }finally{
          try{rs.close();} catch (Exception ex2) {}
          try{preparedStatement.close();} catch (Exception ex2) {}
          try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
       }

       System.out.println(".............FIN obtener parcela.");
       return resultado;
    }

    private static Via getVia(String sIdVia, Connection connection) throws Exception{

        PreparedStatement pS= null;
        ResultSet rs= null;
        try{

            if (sIdVia==null){
                logger.error("Error al obtener la VIA, id Via nulo");
                return null;
            }

            String sql= "SELECT codigoine, codigocatastro, tipovianormalizadocatastro, " +
                    "tipoviaine, nombreviaine, nombreviacortoine, nombrecatastro, length " +
                    //"FROM vias WHERE id="+sIdVia;
                    "FROM vias WHERE codigoine="+sIdVia;
            pS= connection.prepareStatement(sql);
            rs= pS.executeQuery();

            if (rs.next()){
                Via via= new Via();
                via.setId(sIdVia);
                via.setCogigoIne((rs.getString("codigoine") != null)?new Integer(rs.getInt("codigoine")).toString():null);
                via.setCodigoCatastro((rs.getString("codigocatastro") != null)?new Integer(rs.getInt("codigocatastro")).toString():null);
                via.setTipoViaNormalizadoCatastro(rs.getString("tipovianormalizadocatastro"));
                via.setTipoViaIne(rs.getString("tipoviaine"));
                via.setNombreViaIne(rs.getString("nombreviaine"));
                via.setNombreViaCortoIne(rs.getString("nombreviacortoine"));
                via.setNombreCatastro(rs.getString("nombrecatastro"));
                via.setLength((rs.getString("length")!=null?new Float(rs.getFloat("length")):null));
                return via;
            }else{
                return null;
            }
        }catch(Exception e){
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al buscar la via:"+sw.toString());
            throw (e);
        }finally{
          try{rs.close();} catch (Exception e) {}
          try{pS.close();} catch (Exception e) {}
        }
    }


}
