<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>Ayuda.- Aplicación de Registro de Expediente</title>

  <!-- maps -->
  <maps>
     <homeID>AlpToLocalGISIntro</homeID>
     <mapref location="AlpToLocalGISMap_es.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Tabla de Contenidos</label>
    <type>javax.help.TOCView</type>
    <data>AlpToLocalGISTOC_es.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Índice</label>
    <type>javax.help.IndexView</type>
    <data>AlpToLocalGISIndex_es.xml</data>
  </view>     

    <view>				<!-- Deseamos que se puedan realizar búsquedas -->
        <name>Search</name>
        <label>Búsqueda</label>			<!-- El tooltiptext -->
        <type>javax.help.SearchView</type>
        <data engine="com.sun.java.help.search.DefaultSearchEngine">JavaHelpSearch</data>
    </view>

</helpset>