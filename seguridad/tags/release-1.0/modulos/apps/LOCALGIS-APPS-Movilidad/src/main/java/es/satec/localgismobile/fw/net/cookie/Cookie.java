package es.satec.localgismobile.fw.net.cookie;

import java.util.StringTokenizer;
import java.util.Vector;

public class Cookie {

	String comment = null;

	String domain = null;

	String maxAge = null;

	String name = null;

	String path = null;

	String value = null;

	Vector a = null;
	

	public Cookie(String strCookie) {
		a = new Vector();
		//strCookie=strCookie.toLowerCase();//Lo paso a minusculas
		parse(strCookie);// Al crear la cookie la parseo ya
	}

	public Cookie parse(String strCookie) {
		System.out.println("Parseando");
		StringTokenizer st = new StringTokenizer(strCookie);
		a.addElement(st.nextToken(";"));
		while (st.hasMoreTokens()) {
			a.addElement(st.nextToken(";"));
		}

		comment = get_parametro_cookie(" comment", 1);
		maxAge = get_parametro_cookie(" expires", 1);
		name = get_parametro_cookie("name", 0);
		path = get_parametro_cookie(" path", 1);
		value = get_parametro_cookie("value", 1);
		domain = get_parametro_cookie(" domain", 1);
		return null;
	}

	public String toString() {// Devolver un String a traves de los datos que
		// tienes de la cookie
		String temp = "";
		StringBuffer buffer_cookies = new StringBuffer();
		// buffer_cookies=buffer_cookies.append(getName()+"="+getValue()+getComment()+getMaxAge()+getDomain()+getPath());
		temp = getName();
		if (temp.length() > 0)
			buffer_cookies.append(temp + "=");
		temp = getValue();
		if (temp.length() > 0)
			buffer_cookies.append(temp);
		temp = getComment();
		if (temp.length() > 0)
			buffer_cookies.append("; Comment=" + temp);
		temp = getMaxAge();
		if (temp.length() > 0)
			buffer_cookies.append("; Expires=" + temp);
		temp = getDomain();
		if (temp.length() > 0)
			buffer_cookies.append("; Domain=" + temp);
		temp = getPath();
		if (temp.length() > 0)
			buffer_cookies.append("; Path=" + temp);

		String memoria_cookies = buffer_cookies.toString();
		return memoria_cookies;

	}

	/*
	 * Funcion que devuelve uno de los parametros del String que forma la cookie ,
	 * se le pasa el nombre de la etiqueta y si quieres su izquierda o derecha
	 */
	private String get_parametro_cookie(String nombre, int i_d) {
		String parametro = "";
		
		Vector temp = new Vector();
		if (nombre.equals("name")) {
			StringTokenizer stringtoke = new StringTokenizer((String) a
					.elementAt(0));
			temp.addElement(stringtoke.nextToken("="));
			return (String) temp.elementAt(0);
		}
		if (nombre.equals("value")) {
		/*Devuelvo todo el churro despues del primer igual hasta el punto y comma, 
		 * no parseo por igual pq puede haber varios iguales*/
		    String primera_posicion=(String) a.elementAt(0);
		    int indice_inicial=primera_posicion.indexOf("="); 
		    parametro=primera_posicion.substring(indice_inicial+1);
				return parametro;
			
			}
       
		for (int i = 0; i < a.size(); i++) {
			StringTokenizer stringtoke = new StringTokenizer((String) a
					.elementAt(i));
			temp.addElement(stringtoke.nextToken("="));// Faltaba esta linea y
														// no funcionaba en
														// 1.1.8 pero en 1.5 si
			while (stringtoke.hasMoreTokens()) {
				temp.addElement(stringtoke.nextToken("="));
			}
			String temporal=(String) temp.elementAt(0);
			temporal=temporal.toLowerCase();
			if (temporal.equals(nombre)) {
				parametro = (String) temp.elementAt(i_d);
				break;
			}
			temp.removeAllElements();
		}
		return parametro;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void main(String[] args) {
		 //String strCookie = "ELMUNDO_idusr=RUsPfn8AAAEAAFBVR3w-2839c762d487b53387d75b7fbe87c16f; expires=Mon, 02-Nov-09 09:44:30 GMT; path=/; domain=.elmundo.es";
		 String strCookie="JSESSIONID=87A1F6098B6F58055B0CBF0DFFE65CA7; Path=/";
		 //String url1 = " PREF=ID=03e05b0b19bc6ffd:TM=1162817549:LM=1162817549:S=cHX1rH9axWyXTBEg; expires=Sun, 17-Jan-2038 19:14:07 GMT; path=/; domain=.google.es";
		Cookie cookie = new Cookie(strCookie);
		System.out.println("LA FECHA ES: " + cookie.getMaxAge());
		System.out.println("El nombre es: " + cookie.getName());
		System.out.println("El value es: " + cookie.getValue());
		System.out.println("El dominio es: " + cookie.getDomain());
		System.out.println("El path es: " + cookie.getPath());
		//cookie.setValue("NUEVO VALOR");
		System.out.println("Cookie en forma de String =" + cookie.toString());

	}
}