package ejemplosgeopista;

import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaMap;
import com.vividsolutions.jump.workbench.model.Task;

class GeopistaEjemplo
{
  public static void main(String[] args)
  {


    GeopistaFrame ejemplo = new GeopistaFrame();
    ejemplo.setTitle("ejemplo1. Original");
    GeopistaEditor editor1=ejemplo.getEditor();
    Task task1=editor1.getTask();
    
    GeopistaFrame ejemplo2 = new GeopistaFrame(task1);
    ejemplo2.setTitle("ejemplo2. copia");
   
   
    
    ejemplo2.setVisible(true);
    ejemplo.setVisible(true);
    ejemplo.initEditor();
  
    
  }
}

