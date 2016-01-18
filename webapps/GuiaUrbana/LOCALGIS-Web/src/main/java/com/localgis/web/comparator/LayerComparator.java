package com.localgis.web.comparator;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;

import com.localgis.web.openlayers.LayerOpenlayers;


public class LayerComparator implements Comparator{

	  public int compare(Object o1, Object o2) {
		  LayerOpenlayers layer1,layer2; 
          layer1 = (LayerOpenlayers) o1;
          layer2 = (LayerOpenlayers) o2;
          //Si devolvemos 0 se eliminan entradas duplicadas, lo evitamos
          if (layer1.getPosition().intValue()==layer2.getPosition().intValue())
        	  return 1;
          else if (layer1.getPosition().intValue()>layer2.getPosition().intValue())
        	  return -1;
          else
        	  return 1;
      }
}
