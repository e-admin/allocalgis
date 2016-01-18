/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 * 
 *
 * $Id: URLStreamer.java,v 0.1 20041110 
 *
 * Copyright (C) 2004 Jan Ruzicka jan.ruzicka@vsb.cz
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

/**
 *
 * <p>Title: URLStreamer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Jan Ruzicka jan.ruzicka@vsb.cz
 * @version 0.1
 */

package cz.vsb.gisak.jump.plugin.arcims;

import java.util.*; //utils for collections
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
Sends data to the URL and gets answer
@author Ruzicka
*/
public class URLStreamer {
  /**Executes POST method on url. Sends postData. Returns result. */
  public String runPost(String[] postItems, String[] postValues, String url) {
    String postData= preparePostData(postItems, postValues);
    System.out.println(postData);
    String outputText = runPost(postData, url);
    return outputText;
  }
  public String runPost(String postData, String url) {
    BufferedReader in = null;
    String outputText = "";
    try {
      /** Makes URL */
      URL data = new URL(url);
      /** Creates URL connection to the URL*/
      URLConnection connection = data.openConnection();
      /** Disbale data cashing*/
      connection.setUseCaches(false);
      /** Enables sending the data*/
      connection.setDoOutput(true);

      /**
       This stream will be send to the URL
       512 - buffer size in bytes
      */
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);

      /** Second stream in connection to the first stream */
      PrintWriter out = new PrintWriter(byteStream,true);

      /** Sendig the data */
      out.print(postData);
      /** end of the writing. Empty the buffer */
      out.flush();

      /** Sending HTTP headers */
      //connection.setRequestMethod("POST");
      /** Data size */
      connection.setRequestProperty("Content-Length",String.valueOf(byteStream.size()));
      /** Content type */
      connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      //connection.setRequestMethod("POST");

      /** Real flush of the data to the URL connection */
      byteStream.writeTo(connection.getOutputStream());
      /** End of the data sending*/

      /** Begininig of data receeving*/
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String str;
      while((str = in.readLine())!=null) {
        outputText = outputText + str;
      }
      //connection.disconnect();
    }
    catch (java.net.MalformedURLException e) {
        System.out.println(e.getMessage());
    }
    catch (java.io.IOException e) {
        System.out.println(e.getMessage());
    }
    return outputText;

  }
  public String preparePostData(String[] postItems, String[] postValues) {
    String postData="";
    for(int i=0; i<postItems.length;i++) {
        /** Data encoding */
        if (i > 0) postData += "&";
        postData += URLEncoder.encode(postItems[i]) + "=" + URLEncoder.encode(postValues[i]);
    }
    System.out.println(postData);
    return postData;
  }
  public String runPostOnServerlet(String postData, String url) {
    String outputText = "";
    BufferedReader in = null;
    try {
      URL slURL = new URL(url);
      URLConnection connection = slURL.openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);
      PrintStream out = new PrintStream(connection.getOutputStream());
      out.print(postData);
      /** Begininig of data receeving*/
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String str;
      while((str = in.readLine())!=null) {
        outputText = outputText + str;
      }
    }
    catch (MalformedURLException e) {
        System.out.println(e.getMessage());
    }
    catch (IOException e) {
        System.out.println(e.getMessage());
    }
    return outputText;
  }
}


