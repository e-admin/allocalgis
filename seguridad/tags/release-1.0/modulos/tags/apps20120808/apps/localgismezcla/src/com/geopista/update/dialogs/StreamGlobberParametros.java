package com.geopista.update.dialogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGlobberParametros extends Thread
{
    InputStream is;
    String type;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StreamGlobberParametros.class);


    public StreamGlobberParametros(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                logger.info(type + ">" + line);
            	System.out.println(type + ">" + line);
            }
        } catch (IOException ioe)
        {
        	ioe.printStackTrace();
		}
    }
}
