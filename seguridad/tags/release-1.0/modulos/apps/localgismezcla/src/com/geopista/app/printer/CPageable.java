package com.geopista.app.printer;


import org.apache.log4j.Logger;
import java.awt.print.*;




public class CPageable {

   	public static void CGeneraPageable(java.awt.Image image) {


		//***************************************
		//** devuelve printJob, que es el objeto impresora por defecto del sistema
		//*************************************
		PrinterJob printJob = PrinterJob.getPrinterJob();

		//***************************************
		//** Mostramos primero el dialogo,antes de cualquier calculo,
		//** por si nos cambian alguna caracteristica de impresion
		//*************************************
		if (!printJob.printDialog()) {
			return;
		}


		//***************************************
		//** devuelve defaultPageFormat, un objeto con los atributos por defecto de la impresora printJob.
		//*************************************
		PageFormat defaultPageFormat = printJob.defaultPage();


		//***************************************
		//** Esto elimina los marjenes de la hoja por defecto
		//*************************************
		Paper paper = defaultPageFormat.getPaper();
		paper.setImageableArea(0, 0, defaultPageFormat.getWidth(), defaultPageFormat.getHeight());
		defaultPageFormat.setPaper(paper);

		//***************************************
		//** Configuramos la orientacion (antes de la modificacion de los marjenes)
		//*************************************
		//defaultPageFormat.setOrientation(PageFormat.LANDSCAPE);
		defaultPageFormat.setOrientation(PageFormat.PORTRAIT);




/*
		//***************************************
		//** muestra un dialogo para cambiar las propiedades de defaultPageFormat
		//*************************************
		PageFormat pageFormat=printJob.pageDialog(defaultPageFormat);
		System.out.println("pageFormat.getWidth(): "+pageFormat.getWidth());
		System.out.println("pageFormat.getHeight(): "+pageFormat.getHeight());
*/


		Book book = new Book();
		book.append(new CDocument(image), defaultPageFormat);
		printJob.setPageable(book);


		try {
			printJob.print();

		} catch (Exception ex) {
			System.out.println("[CPageable.CGeneraPageable()] Exception: " + ex.toString());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String _nombreficheroOutput = "file:///C:\\mapSpain.svg.png";
		System.out.println("[CPageable.main()] _nombreficheroOutput: " + _nombreficheroOutput);
		//CGeneraPageable(_nombreficheroOutput);
	}



}

