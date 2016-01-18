package com.geopista.reports.gui;

import java.awt.Frame;

import javax.swing.JLabel;

public class MapImageInteractiveHelpDialog extends javax.swing.JDialog {

    public MapImageInteractiveHelpDialog() {
        initComponents();
    }
    
    public MapImageInteractiveHelpDialog(Frame owner){
        super(owner);
        initComponents();
    }

    private void initComponents() {
        setTitle("Ayuda mapa interactivo");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);        

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 15));
        
        JLabel jLabelHelp = new JLabel();
        String text = "<html><h2>Informes interactivos</h2>" +
	    	"<p>Estos informes presentan datos e imagenes de mapas de un elemento seleccionado<br/>" +
	    	"sobre un mapa. Para confeccionar un informe de este tipo, siga los siguientes pasos:</p>" +
	    	"<ol><li>En el campo <i>Nombre del parámetro identificador</i>, establezca un nombre<br/>" +
	    	"para almacenar el identificador del elemento objeto del informe.</li>" +
	    	"<li>Modifique la consulta del informe para hacer referencia a dicho parámetro<br/>" +
	    	"Por ejemplo, si el parámetro se llama <i>id_param</i>:<br/>" +
	    	"<code>SELECT id, area FROM Parcelas<br/><b>WHERE id = $P{id_param}</b></code></li>" +
	    	"<li>Seleccione el mapa. Debe ser un mapa previamente publicado con carácter<br/>" +
	    	"privado en el servidor de mapas de LocalGIS.</li>" +
	    	"<li>Seleccione una capa de la lista sobre la cual desea ejecutar el informe.</li>" + 
	    	"<li>Seleccione el atributo <b>identificador</b> de la capa. Debe tener valor único<br/>" +
	    	"para cada elemento de la capa. El valor de este atributo en el elemento<br/>" +
	    	"será asignado al parámetro definido en el primer punto.</li>" +
        	"<li>Elija la escala de visualización del mapa en el informe.</li></ol></html>";
    	jLabelHelp.setText(text);
    	
    	add(jLabelHelp);
    	
    	pack();
	}

}
