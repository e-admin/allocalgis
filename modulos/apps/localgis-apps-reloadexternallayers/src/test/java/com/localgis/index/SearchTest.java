/**
 * SearchTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;





public class SearchTest {

	private static Logger logger = Logger.getLogger(SearchTest.class); 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
    	List result = new ArrayList();    	
    	IndexSearcher searcher=null;
    	IndexReader reader=null;

    	Analyzer an = new StandardAnalyzer(Version.LUCENE_35);    	
    	//Analyzer an = new SimpleAnalyzer(Version.LUCENE_35);    	

    	try {
    		//String termino="33001A04709008";
    		String termino="CERECEDA";
    		
    		String idEntidad="77";
    		String idLayerGeopista="16";
    		Query query = new QueryParser(Version.LUCENE_35, "searcheable", an).parse(termino.toUpperCase());
    		
			
			logger.debug("Query: " + query.toString());
			
			Directory indexDir = new NIOFSDirectory(new File("c:\\tmp\\localgis\\index\\"+idEntidad+"\\"+idLayerGeopista));
			
			if ((IndexReader.indexExists(indexDir))) {
				reader=IndexReader.open(indexDir);
				searcher = new IndexSearcher(reader);
				
				
				ScoreDoc[] hits;
				
				hits = searcher.search(query, 100).scoreDocs;
				
				
				
				logger.debug("Found " + hits.length + " hits.");

				for(ScoreDoc currentHit : hits) {
					int docId = currentHit.doc;
					Document d = searcher.doc(docId);
					//log.debug((i + 1) + ". " + d.get("idMensaje") + " - "
					//		+ d.get("asunto"));
					String name=d.get("name");
					logger.info("Encontrado elemento "+name+" :"+d.toString());
				
				}
			}
		} catch (CorruptIndexException e) {
			logger.error("Error CorruptIndexException: " + e.getMessage(), e);
		} catch (ParseException e) {
			logger.error("Error ParseException: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error IOException: " + e.getMessage(), e);
		}
    	finally{
    		try {
				if (searcher!=null)
					searcher.close();
				if (reader!=null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	

    	

	}

}
