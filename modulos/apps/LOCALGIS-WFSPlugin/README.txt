WFSPlugin - a plugin to retrieve WFS-layer version 1.0
------------------------------------------------------

This plugin offers the ability to include WFS-layer into OpenJump.
OGC WFS version 1.0 is currently supported with this plugin.

I - Install

  a - get the sources

      anonymous CVS access:

        cvs -d:pserver:anonymous@jump-pilot.cvs.sourceforge.net:/cvsroot/jump-pilot login

        cvs -z3
        -d:pserver:anonymous@jump-pilot.cvs.sourceforge.net:/cvsroot/jump-pilot co -P WFSPlugin


      developer CVS access:

        export CVS_RSH=ssh

        cvs -z3 -d:ext:developername@jump-pilot.cvs.sourceforge.net:/cvsroot/jump-pilot co -P WFSPlugin

        (where developername is the respective login name)

  b - build
     
      in a Unix (alike) environment

        build the plugin:

        cd etc
        ant dist
        
        That should create a dist directory containing the jar file of
        the plugin e.g:

        cd ..
        ls dist

        wfsplugin-<YYYYMMDD>.jar

  c - using the plugin

      there are actually two ways 

      - for development (unix alike) and as standalone generic
        WFS-client:

        ant run -Dargs=http://demo.intevation.de/geoserver/wfs

      - the cooked jar version (unix alike):

        * Make sure you have built the plugin as described in b

        * Copy the jar-file into the ext/lib directory of your OpenJUMP 
          installation (for additional jar-files please refer to the 
          INSTALL.txt).

-----------------------------------------------------------------------------------
Development of the WFSPlugin was sponsored by the Finnish Agency for Rural Affairs.

