package es.satec.localgismobile.ui.utils.impl;

 
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class W3CElementItemImpl extends ItemNode {

	
	Node node;
	public W3CElementItemImpl(String name) {
		super(name);
	}

	
	public W3CElementItemImpl(Node element, String name) {
		super(name);
		this.node  = element;
	}

	public void setContent(String value) {
		
		// Modifico el valor del nodo y meto la marca de que se ha cambiado
		node.setNodeValue(value);
		Node parent = node.getParentNode();
		if ( (parent != null) && (parent instanceof Element)) {
			((Element)( parent)).setAttribute("modified", "true");
		}
	}

}
