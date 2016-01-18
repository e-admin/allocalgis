/*
 * Created on 14-jul-2004
 *
 */
package com.geopista.ui.plugin.io.dgn.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.geopista.ui.plugin.io.dgn.impl.values.Value;
import com.geopista.ui.plugin.io.dgn.impl.values.ValueFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;



/**
 * @author Enxenio, SL
 */
public class DGNReader_Impl {
    private final int ID_FIELD_ID = 0;
    private final int ID_FIELD_ENTITY = 1;
    private final int ID_FIELD_LAYER = 2;
    private final int ID_FIELD_COLOR = 3;
    private final int ID_FIELD_WEIGHT = 4;
    private final int ID_FIELD_STYLE = 5;
    private final int ID_FIELD_HEIGHTTEXT = 6;
    private final int ID_FIELD_ROTATIONTEXT = 7;
    private final int ID_FIELD_TEXT = 8;
    private DGNReader m_dgnReader;
    private GeometryFactory _factory;
    private FeatureSchema _fs;
    private TreeMap _featureCollections;
    private HashMap _featureCollectionNames;
    
	public DGNReader_Impl(String dgnFileName, GeometryFactory factory) throws IOException, FileNotFoundException{
        m_dgnReader = new DGNReader(dgnFileName);
        _fs = new FeatureSchema();
        _fs.addAttribute("ID", AttributeType.STRING);
        _fs.addAttribute("ENTITY", AttributeType.STRING);
        _fs.addAttribute("LAYER", AttributeType.STRING);
        _fs.addAttribute("COLOR", AttributeType.STRING);
        _fs.addAttribute("WEIGHT", AttributeType.STRING);
        _fs.addAttribute("STYLE", AttributeType.STRING);
        _fs.addAttribute("HEIGHTTEXT", AttributeType.STRING);
        _fs.addAttribute("ROTATIONTEXT", AttributeType.STRING);
        _fs.addAttribute("TEXT", AttributeType.STRING);       
        _fs.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        _factory = factory;
	}
	
	public void readElements() throws IOException {
        Value[] auxRow = new Value[9];
        Value[] complexRow = new Value[9];

        _featureCollections = new TreeMap(Collections.reverseOrder());
        _featureCollectionNames = new HashMap();
        List outerBoundaries = null;
        List innerBoundaries = null;
        List lineStrings = null; 
        boolean bElementoCompuesto = false;
        boolean bInsideCell = false;
        boolean bEsPoligono = false;
        boolean elPoligonoAcabaDeEmpezar = false;
        boolean elPoligonoComenzoConUnAgujero = false;
        boolean estoyEsperandoUnBordeParaComenzarOtroPoligono = false;
        int complex_index_fill_color = -1;
        
        for (int id = 0; id < m_dgnReader.getNumEntities(); id++) {
            //System.out.println("Elemento " + id + " de " + m_dgnReader.getNumEntities());
            m_dgnReader.DGNGotoElement(id);
            DGNElemCore elemento = m_dgnReader.DGNReadElement();
            int nClass = 0;
            auxRow[ID_FIELD_HEIGHTTEXT] = ValueFactory.createValue(0);
            auxRow[ID_FIELD_ROTATIONTEXT] = ValueFactory.createValue(0);
            auxRow[ID_FIELD_TEXT] = ValueFactory.createNullValue();            
            if (elemento.properties != 0) {
                nClass = elemento.properties & DGNFileHeader.DGNPF_CLASS;
            }
            if ((elemento != null) && (elemento.deleted == 0) && (nClass == 0)) {
                // Si el elemento a tratar es un elemento con geometria
                if ((elemento.stype == DGNFileHeader.DGNST_MULTIPOINT) || (elemento.stype == DGNFileHeader.DGNST_ARC) || (elemento.stype == DGNFileHeader.DGNST_CELL_HEADER) || (elemento.stype == DGNFileHeader.DGNST_SHARED_CELL_DEFN) || (elemento.stype == DGNFileHeader.DGNST_COMPLEX_HEADER)) {
                    if (elemento.complex != 0) {
                    	// Si el elemento pertenece a un complejo
                        //bElementoCompuesto = true;
                    } else {
                    	// No es complejo
                    	// Si el elemento anterior fuera complejo, hay que crear el feature
                        if (bElementoCompuesto) {
                            if (!bInsideCell) {
                            	auxRow = complexRow;
                            }
                            if (complex_index_fill_color != -1) {
                                auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(complex_index_fill_color);
                            }
                            if (bEsPoligono) {
                                auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("MultiPolygon");
                                createMultiPolygonFeature(auxRow, outerBoundaries, innerBoundaries);
                                
                            }
                            else {
                                auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("MultiLineString");
                                createMultiLineStringFeature(auxRow, lineStrings);
                            }
                            //gvSIG añade como polilinea tambien
                        }
                        bElementoCompuesto = false;
                        bEsPoligono = false;
                        bInsideCell = false;
                    }
                }
            	switch (elemento.stype) {
                case DGNFileHeader.DGNST_SHARED_CELL_DEFN:
					bInsideCell = true;
					break;
                case DGNFileHeader.DGNST_CELL_HEADER:
					bInsideCell = true;
                	complex_index_fill_color = m_dgnReader.DGNGetShapeFillInfo(elemento);
                	break;
                case DGNFileHeader.DGNST_COMPLEX_HEADER:
                    bElementoCompuesto = true;
                    DGNElemComplexHeader psComplexHeader = (DGNElemComplexHeader) elemento;
                    complexRow[ID_FIELD_ID] = ValueFactory.createValue(elemento.element_id);
                    complexRow[ID_FIELD_LAYER] = ValueFactory.createValue(elemento.level);
                    complexRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
                    complexRow[ID_FIELD_WEIGHT] = ValueFactory.createValue(elemento.weight);
                    complexRow[ID_FIELD_STYLE] = ValueFactory.createValue(elemento.style);
                    complexRow[ID_FIELD_ENTITY] = ValueFactory.createValue("Complex");
                    complexRow[ID_FIELD_HEIGHTTEXT] = ValueFactory.createValue(0);
                    complexRow[ID_FIELD_ROTATIONTEXT] = ValueFactory.createValue(0);
                    complexRow[ID_FIELD_TEXT] = ValueFactory.createNullValue();
    
                    if (psComplexHeader.type == DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER) {
                        bEsPoligono = true;
                        elPoligonoAcabaDeEmpezar = true;
                        elPoligonoComenzoConUnAgujero = false;
                        estoyEsperandoUnBordeParaComenzarOtroPoligono = false;
                        // Si es un agujero, no conectamos con el anterior
                        //if ((psComplexHeader.properties & 0x8000) != 0) {
                        //    gotHoles = false;
                        //} else {
                        //    complex_index_fill_color = m_dgnReader.DGNGetShapeFillInfo(elemento);
                        //}
                        outerBoundaries = new ArrayList();
                        outerBoundaries.add(new ArrayList());
                        
                        innerBoundaries = new ArrayList();
                        List firstHole = new ArrayList();
                        firstHole.add(new ArrayList());
                        innerBoundaries.add(firstHole);
                    } else {
                        bEsPoligono = false;
                        lineStrings = new ArrayList(); 
                    }
                    break;
                case DGNFileHeader.DGNST_MULTIPOINT:
                    DGNElemMultiPoint psMultiPoint = (DGNElemMultiPoint) elemento;
                    auxRow[ID_FIELD_LAYER] = ValueFactory.createValue(elemento.level);
                    auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
                    auxRow[ID_FIELD_WEIGHT] = ValueFactory.createValue(elemento.weight);
                    auxRow[ID_FIELD_STYLE] = ValueFactory.createValue(elemento.style);
                    auxRow[ID_FIELD_ID] = ValueFactory.createValue(elemento.element_id);                    

                    if ((psMultiPoint.num_vertices == 2) && 
                    	(psMultiPoint.vertices[0].x == psMultiPoint.vertices[1].x) && 
						(psMultiPoint.vertices[0].y == psMultiPoint.vertices[1].y) &&
						(psMultiPoint.vertices[0].z == psMultiPoint.vertices[1].z)) {
                        auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("Point");                        
                        createPointFeature(auxRow, new Coordinate(psMultiPoint.vertices[0].x, psMultiPoint.vertices[0].y, psMultiPoint.vertices[0].z));
                    } else {
                        if (psMultiPoint.type == DGNFileHeader.DGNT_CURVE) {
                        	// Aplanamos las curvas
                        	psMultiPoint.num_vertices = psMultiPoint.num_vertices - 4;
                            for (int aux_n = 0; aux_n < psMultiPoint.num_vertices; aux_n++) {
                            	psMultiPoint.vertices[aux_n] = psMultiPoint.vertices[aux_n + 2];
                            }
                        }
                        List points = new ArrayList();
                        for (int i = 0; i < psMultiPoint.num_vertices; i++) {
                            points.add(new Coordinate(psMultiPoint.vertices[i].x, psMultiPoint.vertices[i].y, psMultiPoint.vertices[i].z));
                        }
                        if (!bElementoCompuesto) {
	                        if(psMultiPoint.type == DGNFileHeader.DGNT_SHAPE) {
	                        	// Es poligono
	                        	if (points.size() <=3) {
	                        		System.err.println("Invalid shape found in Design file");
	                        	}
	                        	else {
		                            // Miramos si tiene color de relleno
		                            if (elemento.attr_bytes > 0) {
		                                elemento.color = m_dgnReader.DGNGetShapeFillInfo(elemento);
		                                auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
		                            }
		                        	auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("Polygon");
		                        	createPolygonFeature(auxRow, points, null);
	                        	}
	                        }
	                        else {
	                        	//Es linestring
	                        	auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("LineString");
	                        	createLineFeature(auxRow, points);		                        	
	                        }	                        	
                        }
                        else {
                        	if (bEsPoligono) {
                                if ((psMultiPoint.properties & 0x8000) != 0) {
                                    if (elPoligonoAcabaDeEmpezar) {
                                        elPoligonoAcabaDeEmpezar = false;
                                        elPoligonoComenzoConUnAgujero = true;
                                        estoyEsperandoUnBordeParaComenzarOtroPoligono = false; 
                                    }                                    
                            		List aHole = (List)innerBoundaries.get(innerBoundaries.size()-1);
                            		List holeList = (List)innerBoundaries.get(innerBoundaries.size()-1);
                            		holeList.addAll(points);
                                    Coordinate firstPoint = (Coordinate)holeList.get(0);
                                    Coordinate lastPoint = (Coordinate)holeList.get(holeList.size()-1);
                                    if(firstPoint.equals(lastPoint)) {
                                        List newHoleList = new ArrayList();
                                        aHole.add(newHoleList);
                                    }
                                } else {
                                    if (elPoligonoAcabaDeEmpezar) {
                                        elPoligonoAcabaDeEmpezar = false;
                                        elPoligonoComenzoConUnAgujero = false;
                                        estoyEsperandoUnBordeParaComenzarOtroPoligono = false; 
                                        
                                    }                                    
                                    if (estoyEsperandoUnBordeParaComenzarOtroPoligono) {
                                        elPoligonoAcabaDeEmpezar = true;
                                        elPoligonoComenzoConUnAgujero = false;
                                        outerBoundaries.add(new ArrayList());
                                        List newHole = new ArrayList();
                                        newHole.add(new ArrayList());
                                        innerBoundaries.add(newHole);                                        
                                    }
                                	List outerBoundaryList = (List)outerBoundaries.get(outerBoundaries.size()-1);
                                	outerBoundaryList.addAll(points);
                                    Coordinate firstPoint = (Coordinate)outerBoundaryList.get(0);
                                    Coordinate lastPoint = (Coordinate)outerBoundaryList.get(outerBoundaryList.size()-1);
                                    if(firstPoint.equals(lastPoint)) {
                                        estoyEsperandoUnBordeParaComenzarOtroPoligono = true;
                                    }
                                }
                        	}
                        	else {
                        		lineStrings.add(_factory.createLineString((Coordinate[])points.toArray(new Coordinate[0])));                    		
                        	}
                        }
                    }
                    
                    break;
                case DGNFileHeader.DGNST_ARC:
                    // m_DgnReader.DGNDumpElement(m_DgnReader.getInfo(), elemento,"");
                    DGNElemArc psArc = (DGNElemArc) elemento;
    
                    // La definición de arco de MicroStation es distinta a la de Java.
                    // En el dgn el origin se entiende que es el centro del arco,
                    // y a la hora de crear un Arc2D las 2 primeras coordenadas son
                    // la esquina inferior izquierda del rectángulo que rodea al arco.
                    // 1.- Creamos la elipse sin rotación.
                    // 2.- Creamos el arco
                    // 3.- Rotamos el resultado
    
                    // System.out.println("Arco con primari axis: " + psArc.primary_axis +
                    //   " start angle: " + psArc.startang + " sweepang = " + psArc.sweepang);
                    //  System.out.println("secondaria axis: " + psArc.secondary_axis +
                    //                                    " rotation = " + psArc.rotation);
                    AffineTransform mT = AffineTransform.getRotateInstance(Math.toRadians(psArc.rotation), psArc.origin.x,psArc.origin.y);
    
                    // mT.preConcatenate(AffineTransform.getScaleInstance(100.0,100.0));
            		Arc2D.Double elArco = new Arc2D.Double(psArc.origin.x -
                            psArc.primary_axis,
                            psArc.origin.y - psArc.secondary_axis,
                            2.0 * psArc.primary_axis,
                            2.0 * psArc.secondary_axis, -psArc.startang,
                            -psArc.sweepang, Arc2D.OPEN);
                    List points = new ArrayList();
                    PathIterator iterator = new FlatteningPathIterator(elArco.getPathIterator(mT),Math.min(elArco.getWidth(), elArco.getHeight())/10000);
                    while (!iterator.isDone()) {
                    	double coords[] = new double[6];
                    	int type = iterator.currentSegment(coords);
                    	if (type == PathIterator.SEG_LINETO || type == PathIterator.SEG_MOVETO) {
                    		points.add(new Coordinate(coords[0], coords[1], 0));
                    	}
                    	iterator.next();
                    }
            		if (elemento.type == DGNFileHeader.DGNT_ELLIPSE) {
            			// The ellipse may not be closed due to precission problems
            			Coordinate firstPoint = (Coordinate)points.get(0);
            			Coordinate lastPoint = (Coordinate)points.get(points.size()-1);
            			if(!firstPoint.equals(lastPoint)) {
            				points.add(firstPoint);
            			}
            		}
                    auxRow[ID_FIELD_LAYER] = ValueFactory.createValue(elemento.level);
                    auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
                    auxRow[ID_FIELD_WEIGHT] = ValueFactory.createValue(elemento.weight);
                    auxRow[ID_FIELD_STYLE] = ValueFactory.createValue(elemento.style);
                    auxRow[ID_FIELD_ID] = ValueFactory.createValue(elemento.element_id);                    
                    if (!bElementoCompuesto) {
                        if (elemento.type == DGNFileHeader.DGNT_ELLIPSE) {
                        	// Es poligono
                        	if (points.size() <=3) {
                        		System.err.println("Invalid shape found in Design file");
                        	}
                        	else {
	                            // Miramos si tiene color de relleno
	                            if (elemento.attr_bytes > 0) {
	                                elemento.color = m_dgnReader.DGNGetShapeFillInfo(elemento);
	                                auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
	                            }
	                        	auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("ArcPolygon");
	                        	createPolygonFeature(auxRow, points, null);
                        	}
                        }
                        else {
                        	auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("ArcLineString");
                        	createLineFeature(auxRow, points);		                        	
                        }
                    }
                    else {
                    	if (bEsPoligono) {
                            if ((psArc.properties & 0x8000) != 0) {
                                if (elPoligonoAcabaDeEmpezar) {
                                    elPoligonoAcabaDeEmpezar = false;
                                    elPoligonoComenzoConUnAgujero = true;
                                    estoyEsperandoUnBordeParaComenzarOtroPoligono = false; 
                                }                                    
                        		List aHole = (List)innerBoundaries.get(innerBoundaries.size()-1);
                        		List holeList = (List)innerBoundaries.get(innerBoundaries.size()-1);
                        		holeList.addAll(points);
                                Coordinate firstPoint = (Coordinate)holeList.get(0);
                                Coordinate lastPoint = (Coordinate)holeList.get(holeList.size()-1);
                                if(firstPoint.equals(lastPoint)) {
                                    List newHoleList = new ArrayList();
                                    aHole.add(newHoleList);
                                }
                            } else {
                                if (elPoligonoAcabaDeEmpezar) {
                                    elPoligonoAcabaDeEmpezar = false;
                                    elPoligonoComenzoConUnAgujero = false;
                                    estoyEsperandoUnBordeParaComenzarOtroPoligono = false; 
                                    
                                }                                    
                                if (estoyEsperandoUnBordeParaComenzarOtroPoligono) {
                                    elPoligonoAcabaDeEmpezar = true;
                                    elPoligonoComenzoConUnAgujero = false;
                                    outerBoundaries.add(new ArrayList());
                                    List newHole = new ArrayList();
                                    newHole.add(new ArrayList());
                                    innerBoundaries.add(newHole);                                        
                                }
                            	List outerBoundaryList = (List)outerBoundaries.get(outerBoundaries.size()-1);
                            	outerBoundaryList.addAll(points);
                                Coordinate firstPoint = (Coordinate)outerBoundaryList.get(0);
                                Coordinate lastPoint = (Coordinate)outerBoundaryList.get(outerBoundaryList.size()-1);
                                if(firstPoint.equals(lastPoint)) {
                                    estoyEsperandoUnBordeParaComenzarOtroPoligono = true;
                                }
                            }
                    	}
                    }
    
                    // System.err.println("Entra un Arco");
                    break;
    
                case DGNFileHeader.DGNST_TEXT:
                    DGNElemText psText = (DGNElemText) elemento;
                    auxRow[ID_FIELD_ID] = ValueFactory.createValue(elemento.element_id);
                    auxRow[ID_FIELD_ENTITY] = ValueFactory.createValue("Text");
                    auxRow[ID_FIELD_LAYER] = ValueFactory.createValue(elemento.level);
                    auxRow[ID_FIELD_COLOR] = ValueFactory.createValue(elemento.color);
                    auxRow[ID_FIELD_WEIGHT] = ValueFactory.createValue(elemento.weight);
                    auxRow[ID_FIELD_STYLE] = ValueFactory.createValue(elemento.style);
                    auxRow[ID_FIELD_HEIGHTTEXT] = ValueFactory.createValue((float) psText.height_mult);
                    auxRow[ID_FIELD_ROTATIONTEXT] = ValueFactory.createValue(psText.rotation);
                    auxRow[ID_FIELD_TEXT] = ValueFactory.createValue(psText.string); // .trim();
                    createTextFeature(auxRow, new Coordinate(psText.origin.x, psText.origin.y, psText.origin.z));
                    break;
                } // switch
             }
        }
	}

	public Iterator getFeatureTypesIterator() {
   		Iterator result = null;
   		if (_featureCollections != null) {
            Collection featureCollections = _featureCollections.values();
            result = featureCollections.iterator(); 
   		}
   		return result;
   }
   
   public String getFeatureCollectionName(FeatureCollection fc) {
		String result = null;
   		if (_featureCollectionNames != null) {
   			result = (String)_featureCollectionNames.get(fc);
   		}
   		return result;
   }

    private Feature creaFeature(Value[] auxRow, Geometry g) {
        Feature result = new BasicFeature(_fs);
        result.setAttribute(ID_FIELD_ID, auxRow[ID_FIELD_ID].toString());
        result.setAttribute(ID_FIELD_ENTITY, auxRow[ID_FIELD_ENTITY].toString());
        result.setAttribute(ID_FIELD_LAYER, auxRow[ID_FIELD_LAYER].toString());
        result.setAttribute(ID_FIELD_COLOR, auxRow[ID_FIELD_COLOR].toString());
        result.setAttribute(ID_FIELD_WEIGHT, auxRow[ID_FIELD_WEIGHT].toString());
        result.setAttribute(ID_FIELD_STYLE, auxRow[ID_FIELD_STYLE].toString());
        result.setAttribute(ID_FIELD_HEIGHTTEXT, auxRow[ID_FIELD_HEIGHTTEXT].toString());
        result.setAttribute(ID_FIELD_ROTATIONTEXT, auxRow[ID_FIELD_ROTATIONTEXT].toString());
        result.setAttribute(ID_FIELD_TEXT, auxRow[ID_FIELD_TEXT].toString());
        result.setGeometry(g);
        return result;
    }
    
    private void createTextFeature(Value[] row, Coordinate point) {
    	Feature feature = creaFeature(row, _factory.createPoint(point));
    	addFeature(row[ID_FIELD_LAYER].toString(), feature);
    }

    private void createPointFeature(Value[] row, Coordinate point) {
    	Feature feature = creaFeature(row, _factory.createPoint(point));
    	addFeature(row[ID_FIELD_LAYER].toString(), feature);
    }

    private void createLineFeature(Value[] row, List line) {
    	LineString lineGeo = _factory.createLineString((Coordinate[])line.toArray(new Coordinate[0]));
    	Feature feature = creaFeature(row, lineGeo);
    	addFeature(row[ID_FIELD_LAYER].toString(), feature);
    }

    private void createPolygonFeature(Value[] row, List outerBoundary, List innerBoundaries) {
    	LinearRing outerBoundaryGeo = _factory.createLinearRing((Coordinate[])outerBoundary.toArray(new Coordinate[0]));
    	List innerBoundariesGeo = null;
    	Polygon polygonGeo = null;
    	if (innerBoundaries != null) {
	    	innerBoundariesGeo = new ArrayList();
	    	for (int i=0; i< innerBoundaries.size(); i++) {
	    		List innerBoundary = (List)innerBoundaries.get(i);
	    		innerBoundariesGeo.add(_factory.createLinearRing((Coordinate[])innerBoundary.toArray(new Coordinate[0])));
	    	}
	    	polygonGeo = _factory.createPolygon(outerBoundaryGeo, (LinearRing[])innerBoundariesGeo.toArray(new LinearRing[0]));
    	}
    	else {
    		polygonGeo = _factory.createPolygon(outerBoundaryGeo, null);
    	}
    	Feature feature = creaFeature(row, polygonGeo);
    	addFeature(row[ID_FIELD_LAYER].toString(), feature);
    }
    
    private void createMultiPolygonFeature(Value[] row, List outerBoundaries, List innerBoundaries) {
        List polygons = new ArrayList();
        for (int i=0; i< outerBoundaries.size(); i++) {
            List outerBoundaryList = (List)outerBoundaries.get(i);
            if (outerBoundaryList.size() <=3) {
                System.err.println("Invalid shape found in Design file");
            }
            else {
                Coordinate firstPoint = (Coordinate)outerBoundaryList.get(0);
                Coordinate lastPoint = (Coordinate)outerBoundaryList.get(outerBoundaryList.size()-1);
                if(!firstPoint.equals(lastPoint)) {
                    outerBoundaryList.add(firstPoint);
                }
                
            	LinearRing outerBoundaryGeo = _factory.createLinearRing((Coordinate[])outerBoundaryList.toArray(new Coordinate[0]));
                List innerBoundaryGeos = new ArrayList();
            	List holeList = (List)innerBoundaries.get(i);
                Iterator holeListIterator = holeList.iterator();
                while (holeListIterator.hasNext()) {
                    List aHoleList = (List)holeListIterator.next();
                    if (aHoleList.size()>0) {
                        innerBoundaryGeos.add(_factory.createLinearRing((Coordinate[])aHoleList.toArray(new Coordinate[0])));
                    }
                }
                if (innerBoundaryGeos.size() != 0) {
                    polygons.add(_factory.createPolygon(outerBoundaryGeo, (LinearRing[])innerBoundaryGeos.toArray(new LinearRing[0])));
                }
                else {
                    polygons.add(_factory.createPolygon(outerBoundaryGeo, null));
                }
            }
        }
    	MultiPolygon complexPolygon = _factory.createMultiPolygon((Polygon[])polygons.toArray(new Polygon[0]));
    	Feature feature = creaFeature(row, complexPolygon);
    	addFeature(row[ID_FIELD_LAYER].toString(), feature);
    }

    private void createMultiLineStringFeature(Value[] row, List lineStrings) {
		MultiLineString complexLine = _factory.createMultiLineString((LineString[])lineStrings.toArray(new LineString[0]));
		Feature feature = creaFeature(row, complexLine);
		addFeature(row[ID_FIELD_LAYER].toString(), feature);
	}
    
    
    private void addFeature(String level, Feature feature) {
        
        level = "00" + level;
        level = "Nivel " + level.substring(level.length()-2, level.length());
    	if (!_featureCollections.containsKey(level)) {
    		FeatureCollection newFC = new FeatureDataset(_fs);
    		_featureCollections.put(level, newFC);
    		_featureCollectionNames.put(newFC, level);
    	}
    	FeatureCollection fc = (FeatureCollection)_featureCollections.get(level);
    	fc.add(feature);
    }
}
