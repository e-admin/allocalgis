package com.geopista.app.utilidades;

import java.io.*;
import java.util.*;

//import com.satec.util.alertas.*;


/**
 * Clase IniFile para manejar ficheros de configuracion.
 *
 * @author SATEC
 * @version 1.0
 */

public class IniFile {
	// Siglas que pueden acompañar a los campos de tipo entero que se
	// refieren a periodos temporales
	private String HORAS = "hh";
	private String MINUTOS = "mi";
	private String SEGUNDOS = "ss";
	private String MILISEGUNDOS = "ml";

	/**
	 * Constructor de la clase IniFile.
	 *
	 * @return Este método no devuelve ningún valor.
	 */
	public IniFile() {
		m_sections = new Map();
	}

	/**
	 * Constructor de la clase IniFile.
	 *
	 * @param s Nombre del fichero de configuracion.
	 * @return Este método no devuelve ningún valor.
	 */
	public IniFile(String s)
			throws FileNotFoundException {
		m_sections = new Map();
		readStream(new FileInputStream(s));
	}

	/**
	 * Constructor de la clase IniFile.
	 *
	 * @param inputstream fichero stream
	 * @return Este método no devuelve ningún valor.
	 */
	public IniFile(InputStream inputstream) {
		m_sections = new Map();
		readStream(inputstream);
	}

	/**
	 * Este metodo obtiene el valor de un campo String en una
	 * seccion dentro de un fichero de configuracion.
	 *
	 * @param s  seccion.
	 * @param s1 campo.
	 * @return Este método devuelve el valor del campo
	 *         dentro del fichero de configuracion o null en caso
	 *         contrario.
	 */
	public synchronized String getProfileString(String s, String s1) {
		return getProfileString(s, s1, "");
	}

	/**
	 * Este método comprueba si existe un campo dentro de una seccion
	 *
	 * @param sSeccion seccion en la que se busca el campo
	 * @param sCampo   campo que buscamos
	 * @return Devuelve true si el campo existe en la seccion indicada
	 */
	public synchronized boolean existProfile(String sSeccion, String sCampo) {
		try {
			Map map = (Map) m_sections.get(sSeccion);
			if (map != null) {
				Object obj = map.get(sCampo);
				if ((obj instanceof String) || (obj instanceof Integer))
					return true;
				else
					return false;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Este metodo obtiene el valor de un campo Stringen una
	 * seccion dentro de un fichero de configuracion.
	 *
	 * @param s  seccion.
	 * @param s1 campo.
	 * @param s2 valor por defecto.
	 * @return Este método devuelve el valor del campo
	 *         dentro del fichero de configuracion o el valor s2 en caso
	 *         contrario.
	 */
	public synchronized String getProfileString(String s, String s1, String s2) {
		try {
			String s3 = null;
			Integer s4 = null;
			Map map = (Map) m_sections.get(s);
			if (map != null) {
				Object obj = map.get(s1);
				if (obj instanceof String) {
					s3 = (String) map.get(s1);
				} else if (obj instanceof Integer) {
					s4 = (Integer) map.get(s1);
					s3 = s4.toString();
				}
			}
			if (s3 == null) {
				Properties p = new Properties();
				if (s1 != null) p.put("$PARAMETRO", s1);
				if (s != null) p.put("$SECCION", s);

				/*CFichTrazaAlerta.WriteInfoLog( CFichTrazaAlerta.SEVEWARNING, "multiChatHA",
										 "IniFile",
									  CAlertas.INI_AL_1,
										 "No se ha encontrado el parametro:"+s1+" en la seccion:"+s);*/
				System.out.println("[IniFile] No se ha encontrado el parametro:" + s1 + " en la seccion:" + s);
				return s2;
			} else
				return s3;
		} catch (Exception e) {
			System.out.println("Exception:" + e.toString());
			return null;
		}
	}

	/**
	 * Este metodo obtiene el valor de un campo int en una
	 * seccion dentro de un fichero de configuracion.
	 *
	 * @param s  seccion.
	 * @param s1 campo.
	 * @return Este método devuelve el valor del campo
	 *         dentro del fichero de configuracion o null en caso
	 *         contrario.
	 */
	public synchronized int getProfileInt(String s, String s1) {
		Integer integer = null;
		Map map = (Map) m_sections.get(s);
		if (map != null) {
			try {
				integer = (Integer) map.get(s1);
			} catch (Exception e) {
				String sParam = (String) map.get(s1);
				int iTamParam = sParam.length();
				if (iTamParam < 3)
					return 0;

				String sNumero = sParam.substring(0, iTamParam - 2);
				String sMedida = sParam.substring(iTamParam - 2);

				if ((sMedida != null) &&
						(sMedida.equalsIgnoreCase(HORAS) ||
						sMedida.equalsIgnoreCase(MINUTOS) ||
						sMedida.equalsIgnoreCase(SEGUNDOS) ||
						sMedida.equalsIgnoreCase(MILISEGUNDOS))) {
					integer = new Integer(Integer.parseInt(sNumero));
				}
			}
		}
		if (integer == null) {
			Properties p = new Properties();
			if (s1 != null) p.put("$PARAMETRO", s1);
			if (s != null) p.put("$SECCION", s);

			/*CFichTrazaAlerta.WriteWarningLog( CFichTrazaAlerta.SEVEWARNING, "multiChatHA",
								 "IniFile",
								CAlertas.INI_AL_1,
								 "No se ha encontrado el parametro:"+s1+" en la seccion:"+s);*/
			System.out.println("[IniFile] No se ha encontrado el parametro:" + s1 + " en la seccion:" + s);
			return 0;
		} else
			return integer.intValue();
	}

	/**
	 * Este metodo obtiene un vector con los valores de
	 * campos de una seccion.
	 *
	 * @param s seccion.
	 * @return Este método devuelve un vector con los valores
	 *         de los campos dentro de una seccion.
	 */
	public synchronized Vector getProfileSection(String s) {
		Vector vector = new Vector();
		Map map = (Map) m_sections.get(s);
		if (map != null) {
			StringBuffer stringbuffer;
			for (Enumeration enumeration = map.keys();
				 enumeration.hasMoreElements();
				 vector.addElement(stringbuffer.toString())) {
				String s1 = (String) enumeration.nextElement();
				stringbuffer = new StringBuffer(s1);
				stringbuffer.append("=");
				Object obj = map.get(s1);
				if (obj instanceof String)
					stringbuffer.append((String) obj);
				else if (obj instanceof Integer)
					stringbuffer.append(((Integer) obj).intValue());
			}

		}
		return vector;
	}

	/**
	 * Este metodo obtiene una tabla hash con los valores de
	 * campos de una seccion.
	 *
	 * @param s seccion.
	 * @return Este método devuelve una tabla hash con los valores
	 *         de los campos dentro de una seccion.
	 */
	public synchronized Hashtable getSection(String s) {
		Hashtable hash = new Hashtable();

		Map map = (Map) m_sections.get(s);
		if (map != null) {
			StringBuffer stringbuffer;
			for (Enumeration enumeration = map.keys();
				 enumeration.hasMoreElements();) {
				String s1 = (String) enumeration.nextElement();
				stringbuffer = new StringBuffer(s1);
				stringbuffer.append("=");
				Object obj = map.get(s1);
				if (obj instanceof String)
					stringbuffer.append((String) obj);
				else if (obj instanceof Integer)
					stringbuffer.append(((Integer) obj).intValue());

				// Añadimos el elemento.
				hash.put(s1, obj);
			}

		}
		return hash;
	}

	/**
	 * Este metodo obtiene un vector con los valores de
	 * campos de una seccion indicada por un identificador
	 * de seccion.
	 *
	 * @param i identificador de seccion.
	 * @return Este método devuelve un objeto map con
	 *         los valores de una seccion.
	 */
	public synchronized Hashtable getSection(int i) {
		Hashtable hash = new Hashtable();
		Enumeration enumeration = m_sections.elements();
		Map map = null;
		for (int j = 0; j < i; j++) {
			if (!enumeration.hasMoreElements())
				return null;
			map = (Map) enumeration.nextElement();
		}

		if (map != null) {
			StringBuffer stringbuffer;
			for (Enumeration enumeration2 = map.keys();
				 enumeration2.hasMoreElements();) {
				String s1 = (String) enumeration2.nextElement();
				stringbuffer = new StringBuffer(s1);
				stringbuffer.append("=");
				Object obj = map.get(s1);
				if (obj instanceof String)
					stringbuffer.append((String) obj);
				else if (obj instanceof Integer)
					stringbuffer.append(((Integer) obj).intValue());

				// Añadimos el elemento.
				hash.put(s1, obj);
			}

		}
		return hash;
	}

	/**
	 * Este metodo obtiene una tabla hash con los valores de
	 * campos de todas las secciones
	 * <p/>
	 * Los valores en las secciones no se pueden repetir.
	 *
	 * @return Este método devuelve una tabla hash con los valores
	 *         de todas las secciones.
	 */
	public synchronized Hashtable getAllSections() {
		Hashtable hashTodas = new Hashtable();

		if (m_sections != null) {

			//****************************
			// Las secciones empiezan en 1
			//****************************
			for (int i = 1; i <= m_sections.size(); i++) {
				Hashtable hashSection = getSection(i);
				for (Enumeration enumeration = hashSection.keys();
					 enumeration.hasMoreElements();) {
					String clave = (String) enumeration.nextElement();
					hashTodas.put(clave, hashSection.get(clave));
				}
			}
		}
		return hashTodas;
	}

	/**
	 * Este metodo obtiene un vector con los valores de
	 * campos de una seccion indicada por un identificador
	 * de seccion.
	 *
	 * @param i identificador de seccion.
	 * @return Este método devuelve un objeto map con
	 *         los valores de una seccion.
	 */
	public synchronized Map getProfileSection(int i) {
		Enumeration enumeration = m_sections.elements();
		Map map = null;
		for (int j = 0; j < i; j++) {
			if (!enumeration.hasMoreElements())
				return null;
			map = (Map) enumeration.nextElement();
		}

		return map;
	}

	/**
	 * Este metodo situa un valor de un campo string dentro de
	 * una seccion dentro un fichero de configuracion
	 *
	 * @param s  identificador de seccion.
	 * @param s1 identificador de campo.
	 * @param s2 identificador de valor.
	 * @return Este método devuelve un objeto map con
	 *         los valores de una seccion.
	 */
	public synchronized boolean putProfileString(String s, String s1, String s2) {
		Map map = (Map) m_sections.get(s);
		if (map == null) {
			m_sections.put(s, new Map());
			map = (Map) m_sections.get(s);
		}
		map.put(s1, s2);
		return true;
	}

	/**
	 * Este metodo situa un valor de un campo entero dentro de
	 * una seccion dentro un fichero de configuracion
	 *
	 * @param s  identificador de seccion.
	 * @param s1 identificador de campo.
	 * @param s2 identificador de valor.
	 * @return Este método devuelve true si el objeto
	 *         se ha podido insertar
	 */
	public synchronized boolean putProfileInt(String s, String s1, int i) {
		Map map = (Map) m_sections.get(s);
		if (map == null) {
			m_sections.put(s, new Map());
			map = (Map) m_sections.get(s);
		}
		map.put(s1, new Integer(i));
		return true;
	}

	/**
	 * Este metodo situa el contenido de un vector de campo:valor
	 * dentro de una seccion dentro un fichero de configuracion
	 *
	 * @param s      identificador de seccion.
	 * @param vector de campo:valor.
	 * @return Este método devuelve true si el objeto
	 *         se ha podido insertar
	 */
	public synchronized boolean putProfileSection(String s, Vector vector) {
		Map map = (Map) m_sections.get(s);
		if (map == null)
			map = new Map();
		for (Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();) {
			String s1 = (String) enumeration.nextElement();
			if (s1.indexOf(61) != -1) {
				String s2 = s1.substring(0, s1.indexOf(61));
				String s3 = s1.substring(s1.indexOf(61) + 1, s1.length());
				try {

					map.put(s2, new Integer(s3.trim()));
				} catch (NumberFormatException _ex) {
					map.put(s2, s3);
				}
			}
		}

		m_sections.put(s, map);
		return true;
	}

	/**
	 * Este metodo elimina una entrada del fichero
	 * de configuracion.
	 *
	 * @param s  seccion
	 * @param s1 valor a eliminar
	 * @return Este método no devuelve nada.
	 */
	public synchronized void removeEntry(String s, String s1) {
		Map map = (Map) m_sections.get(s);
		if (map == null) {
			return;
		} else {
			map.remove(s1);
			return;
		}
	}

	/**
	 * Este metodo elimina una seccion entera del fichero
	 * de configuracion.
	 *
	 * @param s seccion
	 * @return Este método no devuelve nada.
	 */
	public synchronized void removeSection(String s) {
		m_sections.remove(s);
	}


	/**
	 * Este metodo lee una seccion de un fichero de
	 * configuracion.
	 *
	 * @param linenumbeReader stream de lectura de linea.
	 * @param s               seccion.
	 * @return Este método devuelve la linea de la
	 *         seccion leida.
	 */
	protected synchronized String readSection(LineNumberReader linenumberreader, String s) {
		Vector vector = new Vector();
		String s1 = null;
		String s2 = null;
		do {
			try {
				while ((s1 = linenumberreader.readLine()) != null && s1.length() < 1) ;
			} catch (IOException _ex) {
			}
			if (s1 == null || s1.startsWith("[")) {
				if (s2 != null) {
					vector.removeElementAt(vector.size() - 1);
					vector.addElement(s2);
					s2 = null;
				}
				if (s != null && !vector.isEmpty())
					putProfileSection(s, vector);
				return s1;
			}
			if (s1.startsWith("_continue_")) {
				if (s2 == null)
					s2 = new String((String) vector.lastElement());
				s2 = s2.concat(" " + s1.substring(11));
			} else {
				if (s2 != null) {
					vector.removeElementAt(vector.size() - 1);
					vector.addElement(s2);
					s2 = null;
				}
				vector.addElement(s1);
			}
		} while (true);
	}

	/**
	 * Este metodo lee un stream del fichero de configuracion.
	 *
	 * @param inputStream stream de entrada.
	 * @return Este método no devuelve nada.
	 */
	public synchronized void readStream(InputStream inputstream) {
		LineNumberReader linenumberreader = new LineNumberReader(new InputStreamReader(inputstream));
		new Vector();
		String s = null;
		try {
			s = linenumberreader.readLine();
		} catch (IOException _ex) {
		}
		for (; s != null; s = readSection(linenumberreader, s))
			s = s.substring(1, s.length() - 1);

	}

	/**
	 * Este metodo escribe un stream en el fichero de configuracion.
	 *
	 * @param outputstream stream de salida.
	 * @return Este método no devuelve nada.
	 */
	public synchronized void writeStream(OutputStream outputstream) {
		PrintWriter printwriter = new PrintWriter(outputstream, true);
		for (Enumeration enumeration = m_sections.keys(); enumeration.hasMoreElements(); printwriter.println("")) {
			String s = (String) enumeration.nextElement();
			printwriter.println("[" + s + "]");
			Map map = (Map) m_sections.get(s);
			if (map == null)
				break;
			for (Enumeration enumeration1 = map.keys(); enumeration1.hasMoreElements();) {
				String s1 = (String) enumeration1.nextElement();
				if (map.get(s1) instanceof String) {
					String s2 = (String) map.get(s1);
					if (s2.length() >= 61) {
						printwriter.println(s1 + "=" + s2.substring(0, 71 - s1.length()));
						for (s2 = s2.substring(Math.min(71 - s1.length(), s2.length()), s2.length()); s2.length() > 0; s2 = s2.substring(Math.min(61, s2.length()), s2.length()))
							printwriter.println("_continue_=" + s2.substring(0, Math.min(61, s2.length())));

					} else {
						printwriter.println(s1 + "=" + map.get(s1));
					}
				} else {
					printwriter.println(s1 + "=" + map.get(s1));
				}
			}

		}

	}

	/**
	 * Secciones que componene el fichero de configuracion
	 */
	protected Map m_sections;
}
