/*
 * Obtiene todos los elementos de un formulario que tienen un estilo css determinado
 */
document.getElementsByClassName = function(cl) {
	var retnode = [];
	var myclass = new RegExp('\\b'+cl+'\\b');
	var elem = this.getElementsByTagName('*');
	for (var i = 0; i < elem.length; i++) {
		var classes = elem[i].className;
		if (myclass.test(classes)) {
			retnode.push(elem[i]);
		}
	}
	
	return retnode;
}; 

/*
 * Muestra/oculta los indicadores/categorías en árbol
 */
function mostrarOcultarArbore(trClassName, imgId, esPrimero, accionAbrir, accionCerrar,todos){
	var rutaOpen = "images/folder_open.png";
	var rutaClose = "images/folder_closed.png";
	
	var imagen = document.getElementById(imgId);
	
	if (esPrimero) {
		if ((imagen.src).indexOf(rutaClose) >= 0) {
			//imagen.src = rutaOpen;
			accionAbrir =true;
			accionCerrar=false;
		} else {
			//imagen.src = rutaClose;
			accionCerrar=true;
			accionAbrir=false;
		}
	}
	
	if (accionAbrir) {
		imagen.src = rutaOpen;
	} else if (accionCerrar) {
		imagen.src = rutaClose;
	}
	
	var elementos = document.getElementsByClassName(trClassName);
	
	if (elementos != null && elementos.length > 0) {
		
		for (var i = 0; i < elementos.length; i++) {
			
			if (accionAbrir) {
				elementos[i].style.display = '';
			} else if (accionCerrar) {
				elementos[i].style.display = 'none';
			}
			
			//if (elementos[i].style.display == 'none') {
			//	elementos[i].style.display = '';
			//} else {
			//	elementos[i].style.display = 'none';
			//}
			
			var vectorIdCategoria = (elementos[i].attributes["id"].value).split("Categoria");
			
			if (vectorIdCategoria.length == 2 && (accionCerrar||todos)) {
				mostrarOcultarArbore("idcategoriapadre-" + vectorIdCategoria[vectorIdCategoria.length -1] + "_tr", "idcategoria" + vectorIdCategoria[vectorIdCategoria.length -1] + "_img", false, accionAbrir, accionCerrar,todos);
			}
		}
	}
}