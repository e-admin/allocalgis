/**
 * StringCompareUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
public class StringCompareUtil {
	
	/* replace multiple whitespaces between words with single blank */
    public static String itrim(String source) {
        return source.replaceAll("\\b\\s{2,}\\b", " ");
    }
    
    /* remove leading whitespace */
    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /* remove trailing whitespace */
    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

	
	private static double compareSubStrings(String str1, String str2) {
		ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
		ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
		int intersection = 0;
		int union = pairs1.size() + pairs2.size();
		for (int i=0; i<pairs1.size(); i++) {
			Object pair1=pairs1.get(i);
			for(int j=0; j<pairs2.size(); j++) {
				Object pair2=pairs2.get(j);
				if (pair1.equals(pair2)) {
					intersection++;
					pairs2.remove(j);
					break;
				}
			}
		}
		return (2.0*intersection)/union;
	}
	
	
	
	private static ArrayList wordLetterPairs(String str) {
		ArrayList allPairs = new ArrayList();
		// Tokenize the string and put the tokens/words into an array 
		String[] words = str.split("s");
		// For each word
		for (int w=0; w < words.length; w++) {
			// Find the pairs of characters
			String[] pairsInWord = letterPairs(words[w]);
			for (int p=0; p < pairsInWord.length; p++) {
				allPairs.add(pairsInWord[p]);
			}
		}
		return allPairs;
	}
	
	
	
	/** @return an array of adjacent letter pairs contained in the input string */
	private static String[] letterPairs(String str) {
		//int numPairs = str.length()-1;
		//String[] pairs = new String[numPairs];
		String str2 = "";
		PropertiesConfiguration forbiddenWords = null;
		str = str.replace(',', ' ');
		str = str.replace('.', ' ');
		str = str.replace('-', ' ');
		str = str.replace('(', ' ');
		str = str.replace(')', ' ');		

		try {
			forbiddenWords = new PropertiesConfiguration("forbiddenWords.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			System.err.println("It is not possible to open forbiddenWords.properties");
		}
		
		String[] stest = str.split(" ");
		
		
		
		//This block is to get out the words "LA", "EL", "DEL", "DE", "LOS" base of lots of errors. 
		// forbidden words are now included in the forbiddenWords.properties file
		for (int j=0; j<stest.length;j++){
			String sForbiddenWords=forbiddenWords.getString(stest[j]);
			if (sForbiddenWords==null){
				str2 += stest[j] + " ";
			}
		}
		
		
		// remove all unnecessary blank spaces
		str2 = itrim(ltrim(rtrim(str2)));
		
		int numPairs=0;
		if (str2.length()>0){
			numPairs = str2.length()-1;
		}
		String[] pairs = new String[numPairs];
		
		for (int i=0; i<numPairs; i++) {
			pairs[i] = str2.substring(i,i+2);
		}
		forbiddenWords = null;
		return pairs;
	}
	
	/**
	 * Method used to compare two strings and give a value that show their similarity. 
	 * 
	 * @param str1 Street 1 to be compared
	 * @param str2 Street 2 to be compared
	 * @return a value that show their equivalence. value =1 if equivalent. If
	 * not, the number returned can be used as a similarity value
	 * @throws Exception
	 */
	
	public static double compareStrings(String str1, String str2) throws Exception {
		PropertiesConfiguration dictionary = null;
		
		double db = 0;
		
		try {
			dictionary = new PropertiesConfiguration("dictionary.properties");
		} catch (ConfigurationException e) {
			System.err.println("It is not possible to open dictionary.properties file");
			throw e;
		}
		
		if (dictionary!=null){
			// wordsOrigin1 is an array containing each words of the first Parameter String
			String[] wordsOrigin1 = str1.split(" ");
			// wordsOrigin2 is an array containing each words of the second Parameter String
			String[] wordsOrigin2 = str2.split(" ");
			// The String sOriginal1 will contain each reconstruction of the first string
			String sOriginal1 = "";
			// The String sOriginal2 will contain each reconstruction of the second String
			String sOriginal2 = "";
			
			// We check in the dictionary if there are any equivalence for the first word
			// This is a temporary Array as it is missing the original Street type. That's why we 
			// construct a new String[] with the original Street type + all its equivalences. 
			String[] arlDictionary1Temp = dictionary.getStringArray(wordsOrigin1[0].toUpperCase());
			
			String[] arlDictionary1Definitive = new String[arlDictionary1Temp.length+1];
			//And then we reconstruct this array setting the first value to the original Street type
			arlDictionary1Definitive[0] = wordsOrigin1[0];
			
			for (int i=0; i<arlDictionary1Temp.length; i++){
				arlDictionary1Definitive[i+1] = arlDictionary1Temp[i];
			}
			
			// Now we do the same for the second street reference
			String[] arlDictionary2Temp = dictionary.getStringArray(wordsOrigin2[0].toUpperCase());
			
			String[] arlDictionary2Definitive = new String[arlDictionary2Temp.length+1];
			//And then we reconstruct this array setting the first value to the original Street type
			arlDictionary2Definitive[0] = wordsOrigin2[0];
			
			for (int i=0; i<arlDictionary2Temp.length; i++){
				arlDictionary2Definitive[i+1] = arlDictionary2Temp[i];
			}
		
			// Now we have all the different Street types possibilities in arlDictionaryXDefinitive arrays
			// it's time to compare streets. 
			for (int iStreetType1=0; iStreetType1<arlDictionary1Definitive.length; iStreetType1++){
				sOriginal1 = "";
				sOriginal1 += arlDictionary1Definitive[iStreetType1];
				
				for (int k=1; k<wordsOrigin1.length; k++){
					sOriginal1 += " " + wordsOrigin1[k];
				}
				//sOriginal1 has been reconstructed
				
				for (int iStreetType2=0; iStreetType2<arlDictionary2Definitive.length; iStreetType2++){
					sOriginal2 = "";
					sOriginal2 += arlDictionary2Definitive[iStreetType2];
					
					for (int l=1; l<wordsOrigin2.length; l++){
						sOriginal2 += " " + wordsOrigin2[l];
					}
					
					// sOriginal2 has been reconstructed. We can compare both Streets
					// if value recorded in db is lesser than the one returned, we 
					// increment it's value. 
					double dbTemp = compareSubStrings(sOriginal1, sOriginal2);
					if (dbTemp==1){
						dictionary = null;
						return dbTemp;
					}else if (db<dbTemp){
						db = dbTemp;	
					}
				}
			}
		}
		dictionary = null;
		return db;
	}
	
	/**
	 * Method that receive the name of the street and give back all the translations for it into 
	 * an array of Strings. This translation is only on a two way manner. 
	 * We are checking if any word is the key or the property in the traducciones.properties file
	 * 
	 * ej: if properties file has
	 * ORENSE=OURENSE
	 * ORENSE=GALICIA
	 * GALICIA=ESPAÑA
	 * and do a translation of OURENSE we will just have ORENSE and OURENSE
	 * 
	 * Thanks to the method translateStreetRecursive we can translate them totally which means
	 * that we will have GALICIA and ESPANA integrated into the possibilities
	 * 
	 * @param streetReference the name of the street to transalate
	 * @return an array of Strings which contains all the translations
	 * @throws Exception if we cannot open the translation file
	 */
	
	public static String[] translateStreet (String streetReference) throws Exception {
		PropertiesConfiguration traducciones = null;
		int iTranslationIndex=0;
		//This arraylist will contain the translations
		String[] arlStreetReferenceTranslation = new String[100];
		
		
		try {
			traducciones = new PropertiesConfiguration("traducciones.properties");
		} catch (ConfigurationException e) {
			System.err.println("Impossible to open traducciones.properties file");
			throw e;
		}
		
		if (traducciones!=null){
			// wordsOrigin1 is an array containing each words of the first Parameter String
			String[] wordsOrigin1 = streetReference.split(" ");
			
			// wordsOrigin1 has all the words of the first street. 
			// We need to do a loop to all those words and see if there is any translation. 
			
			// First we put the original Street into the array
			arlStreetReferenceTranslation[iTranslationIndex] = streetReference;
			iTranslationIndex++;
			
			for (int i=0; i<wordsOrigin1.length; i++){
				String sToCheck = wordsOrigin1[i]; 
				
				
				//This is to compare with keys in Traducciones.properties file
				String[] arlWordStreet1Translation = traducciones.getStringArray(sToCheck);
				if (arlWordStreet1Translation.length>0){
					//There is at least one translation for the word
					// We need to read all the array of translation and replace the translated word
					for (int x=0; x<arlWordStreet1Translation.length; x++){
						int iTranslationIndexTemp = iTranslationIndex;
						for (int j=0; j<iTranslationIndexTemp; j++){
							String strTemp = arlStreetReferenceTranslation[j];
							String[] arlStrTemp = strTemp.split(" ");
							arlStrTemp[i] = arlWordStreet1Translation[x];
							// We reconstruct the String and put back into the array of translations
							
							String strTemp2 = "";
							for (int k=0; k<arlStrTemp.length; k++){
								strTemp2 += arlStrTemp[k] + " ";
							}
							//we delete leading and trailing whitespace
							strTemp2 = strTemp2.trim();
							//Before adding the new String, we check it is not already existing
							// This is to avoid duplicating Strings
							boolean bExist = false;
							for (int h=0; h<iTranslationIndex; h++){
								if (arlStreetReferenceTranslation[h].equalsIgnoreCase(strTemp2)){
									bExist = true;
								}
							}
							if (!bExist){
								arlStreetReferenceTranslation[iTranslationIndex] = strTemp2;
								iTranslationIndex++;
							}
						}
					}
				}
				
				// Now we need to do same finding if word has a key
				// This is to compare with keys in Traducciones.properties file
				// Now we need to read all the properties and find if any of them match our String to Check
				Iterator itKeys = traducciones.getKeys();
				while (itKeys.hasNext()){
					String sTranslation = (String) itKeys.next();
					arlWordStreet1Translation = traducciones.getStringArray(sTranslation);
					
					for (int x=0; x<arlWordStreet1Translation.length; x++){
						if (arlWordStreet1Translation[x].equalsIgnoreCase(sToCheck)){
							// We need to add the translation into the array arlStreet1Translation
							// we need to reconstruct the original sentence, 
							// switch the translated word
							// and add it to the translation array
							
							int iTranslationIndexTemp = iTranslationIndex;
							for (int j=0; j<iTranslationIndexTemp; j++){
								String strTemp = arlStreetReferenceTranslation[j];
								String[] arlStrTemp = strTemp.split(" ");
								arlStrTemp[i] = sTranslation;
								// We reconstruct the String and put back into the array of translations
								
								String strTemp2 = "";
								for (int k=0; k<arlStrTemp.length; k++){
									strTemp2 += arlStrTemp[k] + " ";
								}
								//We delete leading and trailing whitespace
								strTemp2 = strTemp2.trim();
								
								//Before adding the new String, we check it is not already existing
								// This is to avoid duplicating Strings
								boolean bExist = false;
								for (int h=0; h<iTranslationIndex; h++){
									if (arlStreetReferenceTranslation[h].equalsIgnoreCase(strTemp2)){
										bExist = true;
									}
								}
								if (!bExist){
									arlStreetReferenceTranslation[iTranslationIndex] = strTemp2;
									iTranslationIndex++;
								}
							}
						}
					}
				}
			}
			
		}
		
		// We construct the array that we will send back. This array is just a copy of 
		// the temporary array arlStreetReferenceTranslation that has a precise size. 
		
		String[] arlStreetTranslation=new String[iTranslationIndex];
		for (int i=0; i<iTranslationIndex; i++){
			arlStreetTranslation[i] = arlStreetReferenceTranslation[i];
		}
		
		traducciones = null;
		return arlStreetTranslation;
	}
	
	
	/**
	 * Method that receive an array of Strings. This method translates all the Strings that are 
	 * recorded into this array
	 * 
	 * @param streetReference the name of the street to transalate
	 * @return an array of Strings which contains all the translations
	 * @throws Exception if we cannot open the translation file
	 */
	
	public static String[] translateStreets(String[] arlStreetsReferences) throws Exception {
		boolean bExist = false;
		int iTranslationsNb = arlStreetsReferences.length;
		String[] arlTempTranslations = new String[iTranslationsNb+100];
		String[] arlDefinitiveTranslations = null;
		
		try {
			//we fill the definitive arraylist of translations that we will give back
			for (int i=0; i<arlStreetsReferences.length; i++){
				arlTempTranslations[i] = new String(arlStreetsReferences[i]);
			}
			
			
			for (int i=0; i<arlStreetsReferences.length; i++){
				String streetToTranslate = arlStreetsReferences[i];
				String[] strAllTranslations = StringCompareUtil.translateStreet(streetToTranslate);
				
				if (strAllTranslations.length>1){
					//There is at least one translation
					for (int j=0; j<strAllTranslations.length; j++){
						bExist = false;
						for (int k=0; k<arlStreetsReferences.length; k++){
							//We have to check that the translation is not already existing
							// This allows us to avoid repetitions
							if (strAllTranslations[j].equalsIgnoreCase(arlStreetsReferences[k])){
								bExist = true;
							}
						}
						if (!bExist){
							//We need to add it to the array
							arlTempTranslations[iTranslationsNb] = new String(strAllTranslations[j]);
							iTranslationsNb++;
						}
					}
				}
			}
			
			// Now that it is finished, we construct the definitive arraylist and return it back
			arlDefinitiveTranslations = new String[iTranslationsNb];
			for (int i=0; i<iTranslationsNb; i++){
				arlDefinitiveTranslations[i] = new String(arlTempTranslations[i]);
			}
			
		}catch(Exception e){
			throw e;
		}
		
		return arlDefinitiveTranslations;
		
	}
	
	
	/**
	 * Method that receive a String. This method is calling recursively to the 
	 * translateStreets(arrayList) in order to check if we have all the different
	 * translations. 
	 * 
	 * @param streetReference the name of the street to transalate
	 * @return an array of Strings which contains all the translations
	 * @throws Exception if we cannot open the translation file
	 */
	
	public static String[] translateStreetRecursive(String streetReference) throws Exception {
		String[] arlAllTranslations = null;
		
		try{
			String[] arlStreets = new String[1];
			arlStreets[0] = streetReference;
			
			arlAllTranslations = StringCompareUtil.translateStreets(arlStreets);
	
			//There is at least one translation
			if (arlAllTranslations.length>1){
				while (arlAllTranslations.length < (((String[]) StringCompareUtil.translateStreets(arlAllTranslations)).length)){
					arlAllTranslations = StringCompareUtil.translateStreets(arlAllTranslations);
				}
			}
		}catch(Exception e){
			//an error has occured
			throw e;
		}
		
		return arlAllTranslations;
	}
	
}
