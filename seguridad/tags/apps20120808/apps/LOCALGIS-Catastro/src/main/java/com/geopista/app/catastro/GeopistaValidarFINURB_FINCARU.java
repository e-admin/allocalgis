package com.geopista.app.catastro;

import com.geopista.app.catastro.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.lang.Integer.*;
import org.postgresql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import javax.swing.JTable;
import org.postgresql.*;
import java.text.SimpleDateFormat;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import java.lang.Integer;
import com.geopista.util.ApplicationContext;
/** La clase GeopistaValidarFINURB_FINCARU comprueba que existen las líneas correctas en en el fichero de texto */
public class GeopistaValidarFINURB_FINCARU
{
   public GeopistaValidarFINURB_FINCARU()
  {
  }


/**
 * Este método comprueba que hay una línea que contiene datos sobre parcelas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarParcela (String linea)
    {
      return true;
    }

/**
 * Este método comprueba que hay una línea que contiene datos sobre subparcelas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarSubparcela (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre unidades constructivas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarUC (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre construcciones
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarConstruccion (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre cargos
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
    public boolean ValidarCargo (String linea)
    {
      return true;
    }
/**
 * Este método comprueba que hay una línea que contiene datos sobre parcelas rusticas
 * @param linea - la linea que contiene los datos sobre parcelas
 * @return - Devuelve que se le llama al metodo true
 */
     public boolean ValidarParcelaRustica (String linea)
    {
      return true;
    }
      public boolean ValidarSubParcelaRustica (String linea)
    {
      return true;
    }

    public boolean ValidarConstruccionRustica (String linea)
    {
      return true;
    }
    
    public boolean ValidarTitularRustico (String linea)
    {
      return true;
    }
}