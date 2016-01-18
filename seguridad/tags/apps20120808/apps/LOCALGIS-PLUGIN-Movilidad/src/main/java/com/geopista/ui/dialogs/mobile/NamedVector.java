package com.geopista.ui.dialogs.mobile;

import java.util.Vector;

class NamedVector extends Vector {
	  String name;

	  public NamedVector(String name) {
	    this.name = name;
	  }

	  public NamedVector(String name, Object elements[]) {
	    this.name = name;
	    for (int i = 0, n = elements.length; i < n; i++) {
	      add(elements[i]);
	    }
	  }

	  public String toString() {
	    return name;
	  }
	  
	  public String getName(){
		  return this.name;
	  }
	}
