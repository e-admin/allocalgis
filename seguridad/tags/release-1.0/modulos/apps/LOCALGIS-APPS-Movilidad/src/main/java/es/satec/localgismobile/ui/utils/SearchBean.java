package es.satec.localgismobile.ui.utils;

/**Informacion a mostrar a la hora de hacer la busqueda**/

public class SearchBean {
	private String [] datosDouble = {"=","!=","<",">","<=",">=", "between"};
	//private String [] datosString = {"igual","diferente","empieza por","termina por","match","contiene","antes","despues"};
	//private String [] datosString = {"igual","diferente","contiene"};
	private String [] datosString = {"like"};
	
	public String[] getDatosDouble(){
		return this.datosDouble;
	}
	public String[] getDatosString(){
		return this.datosString;
	}
	
}
