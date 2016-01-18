/**
 * JoinTableDataSourceCSV.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package reso.jump.joinTable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georeference.beans.CsvRecordSet;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.io.ParseException;

/**
 * @author Olivier BEDEL && JVACA
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
public class JoinTableDataSourceCSV implements JoinTableDataSource {
	
	private ArrayList fieldNames = null;
	private ArrayList fieldTypes = null;
	private String filePath = null;
	private int fieldCount = 0;
	private String DEFAULT_DELEM = "\t";
	private String DELIMITATEURS = ";|\t"; //expression reguliere de qualification des delimitateurs de champs : tab ou ;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	public JoinTableDataSourceCSV(String filePath) {
		this.filePath = filePath;
		fieldNames = new ArrayList();
		readHeader();
	}
    public JoinTableDataSourceCSV() {
    }
	
	public ArrayList getFieldNames() {
		return fieldNames;
	}
	
	public ArrayList getFieldTypes() {
		return fieldTypes;
	}
		

	
	
	public Hashtable buildTable (int keyIndex) {
			FileReader fileReader;
			BufferedReader bufferedReader;
		
			Hashtable table = new Hashtable();	
			int nl=1;
			int nbCol;
			String s, line;
			String[] valeurs, val, aux;
			
			fieldTypes = new ArrayList();
			try {
				fileReader = new FileReader(filePath);
				FileInputStream fis = new FileInputStream(filePath);
				bufferedReader = new BufferedReader(new InputStreamReader(fis));
						
				 
				try {
					// passage de la premiere ligne
					line= bufferedReader.readLine();
					nl++;
					line= bufferedReader.readLine();
					nl++;
					if (line==null)
						throw (new Exception(I18NPlug.get(JoinTablePlugIn.name, "Empty file")));
					nl=1;
					while (line!=null) {
						// [OBEDEL]on encadre la chaine de la ligne pour que la fonction split 
						// prenne en compte les champs vides en debut et en fin de ligne 
						line = " " + DEFAULT_DELEM + line  + DEFAULT_DELEM + " ";
						aux = line.split(DELIMITATEURS);
						valeurs = new String[aux.length-2];
						nbCol = valeurs.length;
						
						// verification de la coherence du nombre de colonnes de l'entete et de la ligne 
						if ((nbCol)!=fieldCount)
						/*	// cas du delimitateur en fin de ligne
							if (nbCol==fieldCount-1 && line.substring(nbCol-1).matches(DELIMITATEURS)) {
								val = new String[fieldCount];
								val[fieldCount-1] = "" ;
								for (int i=0; i<fieldCount-1; i++)
									val[i] = valeurs[i];
								valeurs= val;  
							}
							else */
								throw (new Exception(I18NPlug.get(JoinTablePlugIn.name,"Field_problem_at_line_:") + nl));
						
						// on elimine les valeurs que l'on a rajoute en debut et en fin de ligne
						for (int i=0;i<valeurs.length;i++)
							valeurs[i]=aux[i+1];
						
						// analyse de la valeur de chaque colonne
						for (int i=0; i<nbCol; i++) {
							s = (String) valeurs[i]; 
							// mise a jour du type du champ
							if ((i+1)>fieldTypes.size())
								fieldTypes.add(i,typeOfData(s));
							else {
								AttributeType newFieldType = typeOfData(s);
								AttributeType fieldType = (AttributeType) fieldTypes.get(i);
								if 	(newFieldType!=fieldType)
								{
									if (newFieldType == AttributeType.STRING)
										fieldTypes.set(i,newFieldType);
									else if (fieldType!= AttributeType.STRING && newFieldType==AttributeType.DOUBLE)
										fieldTypes.set(i,newFieldType);
								}
							}
						}
						line= bufferedReader.readLine();
						nl++;
						//enregistrement de la ligne dans la table
						table.put(valeurs[keyIndex], valeurs);
					}
				}
				catch(Exception e) {
					String msg = I18NPlug.get(JoinTablePlugIn.name,"Error_while_reading_file") + filePath +" (" + e.getMessage() + ").";
					throw (new ParseException(msg));
				}		
				finally {
					bufferedReader.close(); 
					fileReader.close();
				}
			}
			catch (Exception e) {
				throw new IllegalStateException(e.getMessage());
			}
//	  obedel debug		
/*			AttributeType t;
			System.out.println(fieldTypes.size());
			for (int i=0; i<fieldTypes.size(); i++) {
				t = (AttributeType) fieldTypes.get(i); 
				System.out.println(t.toString() + " ");
	
		}*/	

		return table;
		}
    /**
     * Este metodo contrulle un hashMap con los valores y los atributos ordenados dentro de una clase CSVRECORDSET
     *
     */
    public Hashtable buildTableMultipleKey (int keyIndex) {
        FileReader fileReader;
        BufferedReader bufferedReader;
    
        Hashtable table = new Hashtable();  
        int nl=1;
        int nbCol;
        String s, line;
        String[] val;
        fieldTypes = new ArrayList();
        try {
            fileReader = new FileReader(filePath);
            FileInputStream fis = new FileInputStream(filePath);
            bufferedReader = new BufferedReader(new InputStreamReader(fis));
            try {
                line= bufferedReader.readLine();
                nl++;
                line= bufferedReader.readLine();
                nl++;
                if (line==null)
                    throw (new Exception(aplicacion.getApplicationContext().getI18nString("archivovacio")));
                
                nl=1;
                while (line!=null) {
                    line=line.trim();
                    val = line.split(DELIMITATEURS);
                   List valores =new ArrayList();
                    for(int j=0;j<val.length;j++)
                        valores.add(val[j]);
                    //Por cada linea tenemos que crear un recordset
                    List listRecordsetMaster=new ArrayList();
                    nbCol=valores.size();
                    if ((nbCol)!=fieldCount)
                        throw (new Exception(aplicacion.getApplicationContext().getI18nString("errorFileLine")+ " " + nl));
                    //en este bucle sacamos los distindtos tipos de datos que tenga una fila epero
                    for (int i=0; i<nbCol; i++) {
                        s = (String) valores.get(i); 
                        if ((i+1)>fieldTypes.size())
                            fieldTypes.add(i,typeOfData(s));
                        else{
                            AttributeType newFieldType = typeOfData(s);
                            AttributeType fieldType = (AttributeType) fieldTypes.get(i);
                            if  (!newFieldType.equals(fieldType))
                            {
                                //si el tipo de dato cambia es string
                                newFieldType = AttributeType.STRING;
                                fieldTypes.set(i,newFieldType);
                            }
                        }
                    }
                    if(table.size()!=0){
                        List listRecordsetstemp=(List)table.get(valores.get(keyIndex));
                        if(listRecordsetstemp!=null){
                            CsvRecordSet recodSet=new CsvRecordSet();
                            recodSet.setFieldValue(valores);
                            recodSet.setObjetDataSource(this);
                            listRecordsetstemp.add(recodSet);
                            table.put(valores.get(keyIndex), listRecordsetstemp);
                        }else{
                            List listRecordsets = new ArrayList();
                            CsvRecordSet recodSet=new CsvRecordSet();
                            recodSet.setFieldValue(valores);
                            recodSet.setObjetDataSource(this);
                            listRecordsets.add(recodSet);
                            table.put(valores.get(keyIndex), listRecordsets);
                        }
                    }else{
                        //la primera vez creo una lista y creo un recordset
                        List listRecordsets = new ArrayList();
                        CsvRecordSet recodSet=new CsvRecordSet();
                        //guardo los valores en el recordset
                        recodSet.setFieldValue(valores);
                        recodSet.setObjetDataSource(this);
                        //y guardo el recordset en la lista
                        listRecordsets.add(recodSet);
                        table.put(valores.get(keyIndex), listRecordsets);
                    }
                    line= bufferedReader.readLine();
                    nl++;
                }
            }
            catch(Exception e) {
                String msg = I18NPlug.get(JoinTablePlugIn.name,"Error_while_reading_file") + filePath +" (" + e.getMessage() + ").";
                throw (new ParseException(msg));
            }       
            finally {
                bufferedReader.close(); 
                fileReader.close();
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    return table;
    }

	private void readHeader() {
		FileReader fileReader;
		BufferedReader bufferedReader;
		String s,firstLine, champs[],secondLine;
		try {
			fileReader = new FileReader(filePath);
			FileInputStream fis = new FileInputStream(filePath);
			// non gestion des fichiers encodé en UTF-16
			bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			//bufferedReader = new BufferedReader(new InputStreamReader(fis));
			
			try {
				
				// boucle de chargement des nom de champs
				firstLine = bufferedReader.readLine();
                
				if (firstLine==null){
                    throw (new Exception(aplicacion.getApplicationContext().getI18nString("errorFileEmty")));
                }
                
				champs = firstLine.split(DELIMITATEURS);	
				
				// boucle de qualification unique des noms de colonne				
				for (int k=0; k<champs.length; k++) { 
					String suffixe, saux;
					int j,i;
					boolean nomUnique;
					
					s = champs[k];
					nomUnique = false;
					suffixe = "";
					j = 0;
					i = 1;
					while (!nomUnique) {
						nomUnique = true;
						j = 0;
						while (j<fieldNames.size()){
							saux = (String) fieldNames.get(j);
							nomUnique = nomUnique && !(s+suffixe).equalsIgnoreCase(saux); 
							j++;
						}
						if (!nomUnique) {
							suffixe = String.valueOf(i); 
							i++;
						}
					}
					
					fieldNames.add(s + suffixe);
					
				}
				
				//mise a jour de fieldCount
				fieldCount = fieldNames.size(); 
			}
			catch(Exception e) {
				String msg = filePath +"\n (" + e.getMessage() + ").";
				throw (new ParseException(msg));
			}		
			finally {
				bufferedReader.close(); 
				fileReader.close();
			}
		}
		catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}


	
	
	private AttributeType typeOfString(String s)
	{
		char firstChar, lastChar;
		AttributeType res = AttributeType.STRING;
		if (s.length()==0)
			res = AttributeType.INTEGER;
		else {
			firstChar = s.charAt(0);
			lastChar = s.charAt(s.length()-1);
			if ((firstChar=='0' && s.length()==1) || (firstChar>'0' && firstChar<='9') || firstChar == '-' || firstChar=='.' || firstChar==',') {
				if (s.indexOf('.')== -1 && s.indexOf(',')== -1)
					if (s.length()< 10)
						res = AttributeType.INTEGER;
					else
						res = AttributeType.DOUBLE;	// les nombres entiers de plus de 10 chiffres sont considérés comme des chaines de caracteres
				else
					res = AttributeType.DOUBLE;
			}
			else
				res = AttributeType.STRING;
		}
		return res; 
	}
    /**
     * esta es la funcion que dice que tipo de dato es en cada momento
     * @param s
     * @return
     */
    private AttributeType typeOfData(String s)
    {
        s=s.trim();
        AttributeType res = AttributeType.STRING;
        if (s.length()==0)
            res = AttributeType.STRING;
        else {
            try{
                //Lo primero que hacemos es intentarlo combertir a fecha

                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
                DateFormat d =sdf.getDateInstance();
                //DateFormat d = DateFormat.getDateInstance(DateFormat.MEDIUM);
                sdf.parse(s);
                //d.parse(s);
                //de no saltar la excepcion le pondriamos como fecha
                res = AttributeType.DATE;
            }catch(Exception e){
                try{
                    if (s.indexOf('.')== -1 && s.indexOf(',')== -1){
                        if (s.length()< 10){
                            new Integer(s);
                            res = AttributeType.INTEGER;
                        }else{
                            new Double(s);
                            res = AttributeType.DOUBLE; // les nombres entiers de plus de 10 chiffres sont considérés comme des chaines de caracteres
                        }
                    }else{
                        new Double(s);
                        res = AttributeType.DOUBLE;
                    }
                }catch(Exception f){
                    res = AttributeType.STRING;
                }
            }
            
        }
        return res; 
    }
}
