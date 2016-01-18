<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset   
PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>Ayuda Generador de Informes</title>

  <!-- maps -->
  <maps>
     <homeID>intro</homeID>
     <mapref location="ayuda.jhm"/>
  </maps>

  <!-- views -->
  <view mergetype="javax.help.UniteAppendMerge">
    <name>TOC</name>
    <label>Tabla de Contenido</label>
    <type>javax.help.TOCView</type>
    <data>ayudaTOC.xml</data>
  </view> 
  
  
  <!-- Esta es la sección que hay que poner para buscar palabras en la ayuda-->
  <view>
  	<name>Localizar</name>
  	<label>Búsquedas</label>
  	<type>javax.help.SearchView</type>
  	<data engine="com.sun.java.help.search.DefaultSearchEngine">JavaHelpSearch</data>
  </view>
  
  

    
</helpset>

