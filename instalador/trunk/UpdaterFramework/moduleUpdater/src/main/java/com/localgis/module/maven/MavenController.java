package com.localgis.module.maven;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MavenController
{

    private CommandLine cmdLine;
    private File workingDir;

    public void createWorkingDir()
    {
	if (this.cmdLine.hasOption("d"))
	    {
		String dir=this.cmdLine.getOptionValue("d");
		File dirPath= new File(dir);
		if (dirPath.exists())
		    {
			this.workingDir=dirPath;
		    }
		else
		    {
			throw new RuntimeException("Invalid working directory:"+dir);
		    }
	    }
    }

    public String processCLI(String[] args)
    {
	Options options = configureCLI();
	CommandLineParser parser = new GnuParser();
	    try {
	        this.cmdLine = parser.parse( options, args );
	        
	        
	        
	    }
	    catch (ParseException exp ) {
	        // oops, something went wrong
	        return ( "Parsing failed.  Reason: " + exp.getMessage() + "\n"+ generateHelp(options));
	    }
	    
	    
	return "ok";

    }

    private String generateHelp(Options options)
    {
	HelpFormatter formatter=new HelpFormatter();
	StringWriter out = new StringWriter();
	PrintWriter pw=new PrintWriter(out);
	formatter.printUsage(pw, 80, "LocalGIS Maven Updater", options);
	return out.toString();
    }

    private Options configureCLI()
    {
	Options options = new Options();
	Option dirOption = OptionBuilder.withArgName("workingdir").hasArg().withDescription("working directory").create("d");
	Option artifactOption = OptionBuilder.withArgName("artifactId").isRequired().hasArg().withDescription("Artifact for the update module").withLongOpt("artifact").create("a");
	Option versionOption = OptionBuilder.withArgName("version").isRequired().hasArg().withDescription("Version for the update module").withLongOpt("version").create("v");

	options.addOption(dirOption);
	options.addOption(artifactOption);
	options.addOption(versionOption);
	return options;
    }
}
/**
 * MavenController.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
