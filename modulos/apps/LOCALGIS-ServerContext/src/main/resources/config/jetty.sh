HOME="/usr/local/LocalGIS.III/jetty-distribution-8.1.9.v20130131/"

export JETTY_HOME=$HOME

RETVAL=$?

case "$1" in
  start)
        echo "  * Starting Jetty "
        cd $HOME
	 java -jar start.jar   
        ;;

esac

exit $RETVAL