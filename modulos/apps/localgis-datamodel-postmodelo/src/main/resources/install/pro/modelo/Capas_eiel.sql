
SELECT setval('public.seq_dictionary', (select max(ID_VOCABLO)::bigint from DICTIONARY), true);
SELECT setval('public.seq_layerfamilies', (select max(id_layerfamily)::bigint from layerfamilies), true);
SELECT setval('public.seq_styles', (select max(id_style)::bigint from styles), true);
SELECT setval('public.seq_acl', (select max(idacl)::bigint from acl), true);
SELECT setval('public.seq_layers', (select max(id_layer)::bigint from layers), true);
SELECT setval('public.seq_tables', (select max(id_table)::bigint from tables), true);
SELECT setval('public.seq_columns', (select max(id)::bigint from columns), true);
SELECT setval('public.seq_attributes', (select max(id)::bigint from attributes), true);
SELECT setval('public.seq_queries', (select max("id")::bigint from "queries"), true);





-- Al ejecutar este script comprobar que id tiene el usuario EIEL en la tabla iuseruserhdr

/*CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Mapa EIEL') THEN
	
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";*/

-- maps
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Mapa EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Mapa EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Mapa EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Mapa EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Mapa EIEL');
INSERT INTO maps (id_map,id_name,xml,image,id_entidad,projection_id,fecha_ins,fecha_mod) 
		VALUES (50,currval('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?>
<mapDescriptor><description></description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>UTM 30N ED50</mapProjection><mapName>EIEL</mapName></mapDescriptor>',null,0,null,null,null);


-- Familia: EIEL ABASTECIMIENTO
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL ABASTECIMIENTO');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL ABASTECIMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL ABASTECIMIENTO');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);


INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),0,0);

--Layer Captacion
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'), '<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CA</Name><UserStyle><Name>CA:_:EIEL: CA</Name><Title>CA:_:EIEL: CA</Title><Abstract>CA:_:EIEL: CA</Abstract><FeatureTypeStyle><Name>CA</Name><Rule><Name>Captación de agua</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#0099ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#6600cc</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>CA</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CA</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#330099</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Captacion');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);


INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Captación');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Captación');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Captación');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Captación');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Captación');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) ---Extended_form defines el panel con el formulario
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CA',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CA:_:EIEL: CA',true,0,0,true,true);	
			
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_ca',1);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Codi Ordre');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_ca',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

	
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_ca"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_abast_ca"."id","eiel_c_abast_ca"."id_municipio","eiel_c_abast_ca"."clave","eiel_c_abast_ca"."codprov","eiel_c_abast_ca"."codmunic","eiel_c_abast_ca"."orden_ca","eiel_c_abast_ca"."cota_z","eiel_c_abast_ca"."obra_ejec","eiel_c_abast_ca"."observ","eiel_c_abast_ca"."precision_dig" FROM "eiel_c_abast_ca" WHERE "eiel_c_abast_ca"."id_municipio" IN (?M)
','UPDATE "eiel_c_abast_ca" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"clave"=''CA'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_ca"=?7,"cota_z"=?8,"obra_ejec"=?9,"observ"=?10,"precision_dig"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_ca" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_ca","cota_z","obra_ejec","observ","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CA'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_abast_ca" WHERE "id"=?2');
	
--Layer Deposito
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>DE</Name><UserStyle><Name>DE:_:EIEL: DE</Name><Title>DE:_:EIEL: DE</Title><Abstract>DE:_:EIEL: DE</Abstract><FeatureTypeStyle><Name>DE</Name><Rule><Name>Depósito de agua</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#3333ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>DE</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>DE</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Deposito');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depósito');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depósito');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depósito');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depósito');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depósito');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'DE',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 2);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'DE:_:EIEL: DE',true,0,0,true,true);	
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_de',1);	

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometría');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_de',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_de"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_abast_de"."id","eiel_c_abast_de"."id_municipio","eiel_c_abast_de"."clave","eiel_c_abast_de"."codprov","eiel_c_abast_de"."codmunic","eiel_c_abast_de"."orden_de","eiel_c_abast_de"."cota_z","eiel_c_abast_de"."obra_ejec","eiel_c_abast_de"."precision_dig","eiel_c_abast_de"."observ" FROM "eiel_c_abast_de" WHERE "eiel_c_abast_de"."id_municipio" IN (?M)',
  'UPDATE "eiel_c_abast_de" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''DE'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_de"=?7,"cota_z"=?8,"obra_ejec"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_de" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_de","cota_z","obra_ejec","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''DE'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_abast_de" WHERE "id"=?2');
  

--Layer Tramos de Conduccion
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TCN</Name><UserStyle><Name>TCN:_:EIEL:TCN</Name><Title>TCN:_:EIEL:TCN</Title><Abstract>TCN:_:EIEL:TCN</Abstract><FeatureTypeStyle><Name>TCN</Name><Rule><Name>-</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.5</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Tramos de conducción</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.5</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Código Tramo</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Código Tramo</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#3333ff</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>8.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>TCN:_:Tramo_Conduccion</Name><Title>TCN:_:Tramo_Conduccion</Title><Abstract>TCN:_:Tramo_Conduccion</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#1d6e29</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#312837</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#f72b3c</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#477ec5</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tramos de Conduccion');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tramos de Conducción');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tramos de Conducción');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tramos de Conducción');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tramos de Conducción');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tramos de Conducción');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TCN',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 3);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TCN:_:EIEL:TCN',true,0,0,true,true);	
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_tcn',3);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Tramo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'tramo_cn',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Diámetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_diametro_abast'),'diametro',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_diametro_abast'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmi');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmi',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmf');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmf',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_tcn"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_abast_tcn"."id","eiel_c_abast_tcn"."id_municipio","eiel_c_abast_tcn"."clave","eiel_c_abast_tcn"."codprov","eiel_c_abast_tcn"."codmunic","eiel_c_abast_tcn"."tramo_cn","eiel_c_abast_tcn"."longitud","eiel_c_abast_tcn"."diametro","eiel_c_abast_tcn"."cota_z","eiel_c_abast_tcn"."obra_ejec","eiel_c_abast_tcn"."observ","eiel_c_abast_tcn"."precision_dig","eiel_c_abast_tcn"."pmi","eiel_c_abast_tcn"."pmf" FROM "eiel_c_abast_tcn" WHERE "eiel_c_abast_tcn"."id_municipio" in (?M)
','UPDATE "eiel_c_abast_tcn" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CN'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"tramo_cn"=?7,"longitud"=length(GeometryFromText(text(?1),?S)),"diametro"=?9,"cota_z"=?10,"obra_ejec"=?11,"observ"=?12,"precision_dig"=?13,"pmi"=?14,"pmf"=?15 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_tcn" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","tramo_cn","longitud","diametro","cota_z","obra_ejec","observ","precision_dig","pmi","pmf") VALUES(GeometryFromText(?1,?S),?PK,?M,''CN'',substr(?M,1,2),substr(?M,3,5),?7,length(GeometryFromText(text(?1),?S)),?9,?10,?11,?12,?13,?14,?15)
','DELETE FROM "eiel_c_abast_tcn" WHERE "id"=?2');
		

--Layer Tratamientos de Potabilizacion
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TP</Name><UserStyle><Name>TP:_:EIEL:TP</Name><Title>TP:_:EIEL:TP</Title><Abstract>TP:_:EIEL:TP</Abstract><FeatureTypeStyle><Name>TP</Name><Rule><Name>Punto de tratamiento</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>x</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000066</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>TP</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#cc00cc</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>10.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tratamientos de Potabilizacion');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tratamientos de Potabilización');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tratamientos de Potabilización');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tratamientos de Potabilización');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tratamientos de Potabilización');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tratamientos de Potabilización');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TP',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 4);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TP:_:EIEL:TP',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_tp',1);

		
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'orden_tp',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);	
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);
	

	

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_tp"."GEOMETRY", ?T) as "GEOMETRY","eiel_c_abast_tp"."id","eiel_c_abast_tp"."id_municipio","eiel_c_abast_tp"."clave","eiel_c_abast_tp"."codprov","eiel_c_abast_tp"."codmunic","eiel_c_abast_tp"."orden_tp","eiel_c_abast_tp"."cota_z","eiel_c_abast_tp"."obra_ejec","eiel_c_abast_tp"."precision_dig","eiel_c_abast_tp"."observ" FROM "eiel_c_abast_tp" WHERE "eiel_c_abast_tp"."id_municipio" in (?M) AND "eiel_c_abast_tp"."clave"<>''AN''
','UPDATE "eiel_c_abast_tp" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''TP'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_tp"=?7,"cota_z"=?8,"obra_ejec"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_tp" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_tp","cota_z","obra_ejec","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''TP'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_abast_tp" WHERE "id"=?2');

 
--Layer Red de distribucion domiciliaria	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>RD</Name><UserStyle><Name>RD:_:EIEL: RD</Name><Title>RD:_:EIEL: RD</Title><Abstract>RD:_:EIEL: RD</Abstract><FeatureTypeStyle><Name>RD</Name><Rule><Name>Red de distribución</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#3366ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>RD:_:rd</Name><Title>RD:_:rd</Title><Abstract>RD:_:rd</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#4db13f</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ee2435</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#5484d3</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#110a0b</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Red de distribucion domiciliaria');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Red de distribución domiciliaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Red de distribución domiciliaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va][va]Red de distribución domiciliaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Red de distribución domiciliaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Red de distribución domiciliaria');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'RD',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 5);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'RD:_:EIEL: RD',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_rd',3);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',4,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Tramo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),88,'tramo_rd',5,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),88,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Titular');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Titularidad'),'titular',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Titularidad'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Gestor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Gestión'),'gestor',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Gestión'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Material');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_material'),'material',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_material'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Diámetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_diametro_abast'),'diametro',null,null,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_diametro_abast'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Sistema de Transporte');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Sistema de Transporte');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Sistema de Transporte');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Sistema de Transporte');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Sistema de Transporte');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_sist_trans'),'sist_trans',2,0,0,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_sist_trans'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),18,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_inst',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),19,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),20,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10024,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),21,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10024,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_rd"."GEOMETRY",?T) as "GEOMETRY","eiel_c_abast_rd"."id","eiel_c_abast_rd"."id_municipio","eiel_c_abast_rd"."clave","eiel_c_abast_rd"."codprov","eiel_c_abast_rd"."codmunic","eiel_c_abast_rd"."codentidad","eiel_c_abast_rd"."codpoblamiento","eiel_c_abast_rd"."tramo_rd","eiel_c_abast_rd"."titular","eiel_c_abast_rd"."gestor","eiel_c_abast_rd"."estado","eiel_c_abast_rd"."material","eiel_c_abast_rd"."longitud","eiel_c_abast_rd"."diametro","eiel_c_abast_rd"."sist_trans","eiel_c_abast_rd"."cota_z","eiel_c_abast_rd"."obra_ejec","eiel_c_abast_rd"."fecha_inst","eiel_c_abast_rd"."precision_dig","eiel_c_abast_rd"."observ",precision_dig FROM "eiel_c_abast_rd" WHERE "eiel_c_abast_rd"."id_municipio" in (?M) AND "eiel_c_abast_rd"."clave"<>''AN''
','UPDATE "eiel_c_abast_rd" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''RD'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"tramo_rd"=?9,"titular"=?10,"gestor"=?11,"estado"=?12,"material"=?13,"longitud"=length(GeometryFromText(text(?1),?S)),"diametro"=?15,"sist_trans"=?16,"cota_z"=?17,"obra_ejec"=?18,"fecha_inst"=?19,"precision_dig"=?20,"observ"=?21 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_rd" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","tramo_rd","titular","gestor","estado","material","longitud","diametro","sist_trans","cota_z","obra_ejec","fecha_inst","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''RD'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13,length(GeometryFromText(text(?1),?S)),?15,?16,?17,?18,?19,?20,?21)
','DELETE FROM "eiel_c_abast_rd" WHERE "id"=?2'
);


--Layer Elemento puntual de abastecimiento	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>AR</Name><UserStyle><Name>AR:_:EIEL: AR</Name><Title>AR:_:EIEL: AR</Title><Abstract>AR:_:EIEL: AR</Abstract><FeatureTypeStyle><Name>AR</Name><Rule><Name>Arqueta de registro</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#66ccff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000099</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>7</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>AR</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Elemento puntual de abastecimiento');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Elemento puntual de abastecimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Elemento puntual de abastecimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Elemento puntual de abastecimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Elemento puntual de abastecimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Elemento puntual de abastecimiento');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'AR',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 6);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'AR:_:EIEL: AR',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_ar',1);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',4,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'orden_ar',4,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);



INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_ar"."GEOMETRY", ?T) as "GEOMETRY","eiel_c_abast_ar"."id","eiel_c_abast_ar"."id_municipio","eiel_c_abast_ar"."clave","eiel_c_abast_ar"."codprov","eiel_c_abast_ar"."codmunic","eiel_c_abast_ar"."codentidad","eiel_c_abast_ar"."codpoblamiento","eiel_c_abast_ar"."orden_ar","eiel_c_abast_ar"."estado","eiel_c_abast_ar"."cota_z","eiel_c_abast_ar"."obra_ejec","eiel_c_abast_ar"."precision_dig","eiel_c_abast_ar"."observ" FROM "eiel_c_abast_ar" WHERE "eiel_c_abast_ar"."id_municipio" in (?M) AND "eiel_c_abast_ar"."clave"<>''AN''
','UPDATE "eiel_c_abast_ar" SET  "GEOMETRY" =transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''AR'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_ar"=?9,"estado"=?10,"cota_z"=?11,"obra_ejec"=?12,"observ"=?13,"precision_dig"=?14 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_ar" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_ar","estado","cota_z","obra_ejec","observ","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''AR'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13,?14)
','DELETE FROM "eiel_c_abast_ar" WHERE "id"=?2');
		

--Layer Caseta de bombeo Abto
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CBA</Name><UserStyle><Name>CBA:_:EIEL:CBA</Name><Title>CBA:_:EIEL:CBA</Title><Abstract>CBA:_:EIEL:CBA</Abstract><FeatureTypeStyle><Name>CBA</Name><Rule><Name>0 a 25000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>triangle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ff9900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CBA</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Caseta bombeo CBA');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Caseta bombeo ABTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Caseta bombeo ABTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Caseta bombeo ABTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Caseta bombeo ABTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Caseta bombeo ABTO');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CBA',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 7);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CBA:_:EIEL: CBA',true,0,0,true,true);	
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_abast_cb',1);	

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometría');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_cb',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia del Motor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'pot_motor',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_abast_cb"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_abast_cb"."id","eiel_c_abast_cb"."id_municipio","eiel_c_abast_cb"."clave","eiel_c_abast_cb"."codprov","eiel_c_abast_cb"."codmunic","eiel_c_abast_cb"."orden_cb","eiel_c_abast_cb"."pot_motor","eiel_c_abast_cb"."estado","eiel_c_abast_cb"."cota_z","eiel_c_abast_cb"."obra_ejec","eiel_c_abast_cb"."precision_dig","eiel_c_abast_cb"."observ" FROM "eiel_c_abast_cb" WHERE "eiel_c_abast_cb"."id_municipio" IN (?M)',
  'UPDATE "eiel_c_abast_cb" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CB'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_cb"=?7,"pot_motor"=?8,"estado"=?9,"cota_z"=?10,"obra_ejec"=?11,"precision_dig"=?12,"observ"=?13 WHERE "id"=?2
','INSERT INTO "eiel_c_abast_cb" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_cb","pot_motor","estado","cota_z","obra_ejec","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CB'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13)
','DELETE FROM "eiel_c_abast_cb" WHERE "id"=?2');
		
-- Familia: EIEL SANEAMIENTO
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL SANEAMIENTO');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL SANEAMIENTO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL SANEAMIENTO');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),1,0);

--Layer Emisario

INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TEM</Name><UserStyle><Name>TEM:_:EIEL: TEM</Name><Title>TEM:_:EIEL: TEM</Title><Abstract>TEM:_:EIEL: TEM</Abstract><FeatureTypeStyle><Name>TEM</Name><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#666666</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>4.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Emisarios</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffff00</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Código Tramo</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>8.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.5</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>4.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Emisarios</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffff33</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>TEM:_:tem</Name><Title>TEM:_:tem</Title><Abstract>TEM:_:tem</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#5bc145</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#db4031</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#5088c0</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#08101a</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Emisario');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Emisario');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Emisario');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Emisario');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Emisario');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Emisario');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TEM',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TEM:_:EIEL: TEM',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_tem',3);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Tramo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'tramo_em',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud terrestre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud terrestre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud terrestre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud terrestre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud terrestre');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'long_terre',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud marítima');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud marítima');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud marítima');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud marítima');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud marítima');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'long_marit',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Diámetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_diametro_sanea'),'diametro',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_diametro_sanea'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmi');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmi',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmf');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmf',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_tem"."GEOMETRY", ?T) as "GEOMETRY","eiel_c_saneam_tem"."id","eiel_c_saneam_tem"."id_municipio","eiel_c_saneam_tem"."clave","eiel_c_saneam_tem"."codprov","eiel_c_saneam_tem"."codmunic","eiel_c_saneam_tem"."tramo_em","eiel_c_saneam_tem"."longitud","eiel_c_saneam_tem"."long_terre","eiel_c_saneam_tem"."long_marit","eiel_c_saneam_tem"."diametro","eiel_c_saneam_tem"."cota_z","eiel_c_saneam_tem"."obra_ejec","eiel_c_saneam_tem"."observ","eiel_c_saneam_tem"."pmi","eiel_c_saneam_tem"."pmf","eiel_c_saneam_tem"."precision_dig" FROM "eiel_c_saneam_tem" WHERE "eiel_c_saneam_tem"."id_municipio" in (?M) AND "eiel_c_saneam_tem"."clave"<>''AN''
','UPDATE "eiel_c_saneam_tem" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''EM'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"tramo_em"=?7,"longitud"=length(GeometryFromText(text(?1),?S)),"long_terre"=?9,"long_marit"=?10,"diametro"=?11,"cota_z"=?12,"obra_ejec"=?13,"observ"=?14,"pmi"=?15,"pmf"=?16,"precision_dig"=?17 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_tem" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","tramo_em","longitud","long_terre","long_marit","diametro","cota_z","obra_ejec","observ","pmi","pmf","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''EM'',substr(?M,1,2),substr(?M,3,5),?7,length(GeometryFromText(text(?1),?S)),?9,?10,?11,?12,?13,?14,?15,?16,?17)
','DELETE FROM "eiel_c_saneam_tem" WHERE "id"=?2
');
  

--Layer Colector
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TCL</Name><UserStyle><Name>TCL:_:EIEL: TCL</Name><Title>TCL:_:EIEL: TCL</Title><Abstract>TCL:_:EIEL: TCL</Abstract><FeatureTypeStyle><Name>TCL</Name><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Colectores</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffffcc</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Código Tramo</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>8.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.5</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.1</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Colectores</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.1</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffff99</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>TCL:_:tcl</Name><Title>TCL:_:tcl</Title><Abstract>TCL:_:tcl</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#328439</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#111718</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#e92a3b</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#2b84bd</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Colector');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Colector');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Colector');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Colector');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Colector');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Colector');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TCL',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 2);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TCL:_:EIEL: TCL',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_tcl',3);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Tramo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'tramo_cl',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Diámetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_diametro_sanea'),'diametro',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_diametro_sanea'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmi');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmi');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmi',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pmf');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pmf');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pmf',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);


		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_tcl"."GEOMETRY", ?T) as "GEOMETRY","eiel_c_saneam_tcl"."id","eiel_c_saneam_tcl"."id_municipio","eiel_c_saneam_tcl"."clave","eiel_c_saneam_tcl"."codprov","eiel_c_saneam_tcl"."codmunic","eiel_c_saneam_tcl"."tramo_cl","eiel_c_saneam_tcl"."longitud","eiel_c_saneam_tcl"."diametro","eiel_c_saneam_tcl"."cota_z","eiel_c_saneam_tcl"."obra_ejec","eiel_c_saneam_tcl"."observ","eiel_c_saneam_tcl"."pmi","eiel_c_saneam_tcl"."pmf","eiel_c_saneam_tcl"."precision_dig" FROM "eiel_c_saneam_tcl" WHERE "eiel_c_saneam_tcl"."id_municipio" in (?M) AND "eiel_c_saneam_tcl"."clave"<>''AN'' 
','UPDATE "eiel_c_saneam_tcl" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CL'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"tramo_cl"=?7,"longitud"=length(GeometryFromText(text(?1),?S)),"diametro"=?9,"cota_z"=?10,"obra_ejec"=?11,"observ"=?12,"pmi"=?13,"pmf"=?14,"precision_dig"=?15 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_tcl" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","tramo_cl","longitud","diametro","cota_z","obra_ejec","observ","pmi","pmf","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CL'',substr(?M,1,2),substr(?M,3,5),?7,length(GeometryFromText(text(?1),?S)),?9,?10,?11,?12,?13,?14,?15)
','DELETE FROM "eiel_c_saneam_tcl" WHERE "id"=?2
');
  

--Layer Depuradora	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>ED</Name><UserStyle><Name>ED:_:EIEL: ED</Name><Title>ED:_:EIEL: ED</Title><Abstract>ED:_:EIEL: ED</Abstract><FeatureTypeStyle><Name>ED</Name><Rule><Name>0 a 15000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#996600</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Depuradora');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depuradora');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depuradora');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depuradora');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depuradora');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depuradora');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'ED',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 3);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'ED:_:EIEL: ED',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_ed',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_ed',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_ed"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_saneam_ed"."id","eiel_c_saneam_ed"."id_municipio","eiel_c_saneam_ed"."clave","eiel_c_saneam_ed"."codprov","eiel_c_saneam_ed"."codmunic","eiel_c_saneam_ed"."orden_ed","eiel_c_saneam_ed"."precision_dig","eiel_c_saneam_ed"."observ" FROM eiel_c_saneam_ed WHERE "eiel_c_saneam_ed"."id_municipio" in (?M) AND "eiel_c_saneam_ed"."clave"<>''AN''
','UPDATE "eiel_c_saneam_ed" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''ED'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_ed"=?7,"precision_dig"=?8,"observ"=?9 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_ed" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_ed","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''ED'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9)
','DELETE FROM "eiel_c_saneam_ed" WHERE "id"=?2');
  
--Layer Red de ramales	

INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>RS</Name><UserStyle><Name>RS:_:EIEL: RS</Name><Title>RS:_:EIEL: RS</Title><Abstract>RS:_:EIEL: RS</Abstract><FeatureTypeStyle><Name>RS</Name><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#cccccc</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Red de saneamiento</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Tramo del ramal</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>6.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.5</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#cccccc</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Red de saneamiento</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>RS:_:rs</Name><Title>RS:_:rs</Title><Abstract>RS:_:rs</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#1e7935</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#142cb7</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0b1311</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#fb3d2f</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Red de ramales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Red de ramales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Red de ramales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Red de ramales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Red de ramales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Red de ramales');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'RS',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 4);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'RS:_:EIEL: RS',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_rs',3);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tramo del ramal');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tramo del ramal');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tramo del ramal');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tramo del ramal');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tramo del ramal');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),88,'tramo_rs',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),88,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Titular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Titular');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Titularidad'),'titular',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Titularidad'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Gestor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Gestión'),'gestor',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Gestión'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Material');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Material');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_material'),'material',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_material'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Sistema de impulsión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Sistema de impulsión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Sistema de impulsión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Sistema de impulsión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Sistema de impulsión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Sistema de impulsión'),'sist_impulsion',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Sistema de impulsión'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo de red interior');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo de red interior');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo de red interior');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo de red interior');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo de red interior');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_tipo_red_interior'),'tipo_red_interior',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_tipo_red_interior'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Diámetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Diámetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_diametro_sanea'),'diametro',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_diametro_sanea'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),18,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),19,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_inst',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),20,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),21,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),22,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_rs"."GEOMETRY", ?T) as "GEOMETRY","eiel_c_saneam_rs"."id","eiel_c_saneam_rs"."id_municipio","eiel_c_saneam_rs"."clave","eiel_c_saneam_rs"."codprov","eiel_c_saneam_rs"."codmunic","eiel_c_saneam_rs"."codentidad","eiel_c_saneam_rs"."codpoblamiento","eiel_c_saneam_rs"."tramo_rs","eiel_c_saneam_rs"."titular","eiel_c_saneam_rs"."gestor","eiel_c_saneam_rs"."estado","eiel_c_saneam_rs"."estado","eiel_c_saneam_rs"."material","eiel_c_saneam_rs"."sist_impulsion","eiel_c_saneam_rs"."tipo_red_interior","eiel_c_saneam_rs"."longitud","eiel_c_saneam_rs"."diametro","eiel_c_saneam_rs"."cota_z","eiel_c_saneam_rs"."obra_ejec","eiel_c_saneam_rs"."fecha_inst","eiel_c_saneam_rs"."observ","eiel_c_saneam_rs"."precision_dig" FROM "eiel_c_saneam_rs" WHERE "eiel_c_saneam_rs"."id_municipio" in (?M) AND "eiel_c_saneam_rs"."clave"<>''AN''
','UPDATE "eiel_c_saneam_rs" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''RS'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"tramo_rs"=?9,"titular"=?10,"gestor"=?11,"estado"=?12,"material"=?13,"sist_impulsion"=?14,"tipo_red_interior"=?15,"longitud"=length(GeometryFromText(text(?1),?S)),"diametro"=?17,"cota_z"=?18,"obra_ejec"=?19,"fecha_inst"=?20,"observ"=?21,"precision_dig"=?22 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_rs" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","tramo_rs","titular","gestor","estado","material","sist_impulsion","tipo_red_interior","longitud","diametro","cota_z","obra_ejec","fecha_inst","observ","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''RS'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13,?14,?15,length(GeometryFromText(text(?1),?S)),?17,?18,?19,?20,?21,?22)
','DELETE FROM "eiel_c_saneam_rs" WHERE "id"=?2');

--Layer Punto de vertido
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>PV</Name><UserStyle><Name>PV:_:EIEL:PV</Name><Title>PV:_:EIEL:PV</Title><Abstract>PV:_:EIEL:PV</Abstract><FeatureTypeStyle><Name>PV</Name><Rule><Name>0 a 25000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>triangle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ff9900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>PV</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Punto de vertido');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Punto de vertido');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Punto de vertido');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Punto de vertido');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Punto de vertido');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Punto de vertido');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'PV',currval('seq_styles'),null,1,0,1);

		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 5);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'PV:_:EIEL:PV',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_pv',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'orden_pv',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10001,'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10001,0);



INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_pv"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_saneam_pv"."id","eiel_c_saneam_pv"."id_municipio","eiel_c_saneam_pv"."clave","eiel_c_saneam_pv"."codprov","eiel_c_saneam_pv"."codmunic","eiel_c_saneam_pv"."orden_pv","eiel_c_saneam_pv"."cota_z","eiel_c_saneam_pv"."obra_ejec","eiel_c_saneam_pv"."precision_dig" FROM "eiel_c_saneam_pv" WHERE "eiel_c_saneam_pv"."id_municipio" in (?M) AND "eiel_c_saneam_pv"."clave"<>''AN'' 
','UPDATE "eiel_c_saneam_pv" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''PV'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_pv"=?7,"cota_z"=?8,"obra_ejec"=?9,"precision_dig"=?10 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_pv" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_pv","cota_z","obra_ejec","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''PV'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10)
','DELETE FROM "eiel_c_saneam_pv" WHERE "id"=?2');
  
  
--Layer Elementos puntuales	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>PR</Name><UserStyle><Name>PR:_:EIEL: PR</Name><Title>PR:_:EIEL: PR</Title><Abstract>PR:_:EIEL: PR</Abstract><FeatureTypeStyle><Rule><Name>Estado (B)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>6500.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#339900</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Estado (E)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>6500.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#000000</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>8</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Estado (M)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>6500.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ff3333</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>8</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Estado (R)</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>6500.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#6666ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>8</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>PR:_:EIEL: PR</Name><Title>PR:_:EIEL: PR</Title><Abstract>PR:_:EIEL: PR</Abstract><FeatureTypeStyle><Rule><Name>Pozo de registro</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#cccccc</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>8</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#cccccc</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Elementos puntuales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Elementos puntuales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Elementos puntuales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Elementos puntuales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Elementos puntuales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Elementos puntuales');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'PR',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 6);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'PR:_:EIEL: PR',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_pr',1);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',4,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_pr',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota Z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota Z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota Z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota Z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota Z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_inst',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);



INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_pr"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_saneam_pr"."id","eiel_c_saneam_pr"."id_municipio","eiel_c_saneam_pr"."clave","eiel_c_saneam_pr"."codprov","eiel_c_saneam_pr"."codmunic","eiel_c_saneam_pr"."codentidad","eiel_c_saneam_pr"."codpoblamiento","eiel_c_saneam_pr"."orden_pr","eiel_c_saneam_pr"."cota_z","eiel_c_saneam_pr"."obra_ejec","eiel_c_saneam_pr"."estado","eiel_c_saneam_pr"."precision_dig","eiel_c_saneam_pr"."fecha_inst" FROM "eiel_c_saneam_pr" WHERE "eiel_c_saneam_pr"."id_municipio" in (?M) AND "eiel_c_saneam_pr"."clave"<>''AN'' 
','UPDATE "eiel_c_saneam_pr" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''PR'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_pr"=?9,"cota_z"=?10,"obra_ejec"=?11,"estado"=?12,"precision_dig"=?13,"fecha_inst"=?14 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_pr" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_pr","cota_z","obra_ejec","estado","precision_dig","fecha_inst") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''PR'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13,?14)
','DELETE FROM "eiel_c_saneam_pr" WHERE "id"=?2');
  

--Layer Caseta de bombeo Sato
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CBS</Name><UserStyle><Name>CBS:_:EIEL:CBS</Name><Title>CBS:_:EIEL:CBS</Title><Abstract>CBS:_:EIEL:CBS</Abstract><FeatureTypeStyle><Name>CBS</Name><Rule><Name>0 a 25000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>triangle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ff9900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CBS</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Caseta bombeo CBS');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Caseta bombeo SATO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Caseta bombeo SATO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Caseta bombeo SATO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Caseta bombeo SATO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Caseta bombeo SATO');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CBS',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 7);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CBS:_:EIEL: CBS',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_cb',1);	

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometría');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_cb',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia del Motor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'pot_motor',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_cb"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_saneam_cb"."id","eiel_c_saneam_cb"."id_municipio","eiel_c_saneam_cb"."clave","eiel_c_saneam_cb"."codprov","eiel_c_saneam_cb"."codmunic","eiel_c_saneam_cb"."orden_cb","eiel_c_saneam_cb"."pot_motor","eiel_c_saneam_cb"."estado","eiel_c_saneam_cb"."cota_z","eiel_c_saneam_cb"."obra_ejec","eiel_c_saneam_cb"."precision_dig","eiel_c_saneam_cb"."observ" FROM "eiel_c_saneam_cb" WHERE "eiel_c_saneam_cb"."id_municipio" IN (?M)',
  'UPDATE "eiel_c_saneam_cb" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CB'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_cb"=?7,"pot_motor"=?8,"estado"=?9,"cota_z"=?10,"obra_ejec"=?11,"precision_dig"=?12,"observ"=?13 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_cb" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_cb","pot_motor","estado","cota_z","obra_ejec","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CB'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13)
','DELETE FROM "eiel_c_saneam_cb" WHERE "id"=?2');


--Layer Aliviaderos
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>LI</Name><UserStyle><Name>LI:_:EIEL:LI</Name><Title>LI:_:EIEL:LI</Title><Abstract>LI:_:EIEL:LI</Abstract><FeatureTypeStyle><Name>LI</Name><Rule><Name>0 a 25000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>triangle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ff9900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>LI</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Aliviaderos');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Aliviaderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Aliviaderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Aliviaderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Aliviaderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Aliviaderos');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'LI',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 8);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'LI:_:EIEL: LI',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_saneam_ali',1);	

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometría');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometría');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Acumulación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Acumulación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Acumulación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Acumulación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Acumulación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Acumulación Aliviadero'),'acumulacion',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia del Motor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia del Motor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'pot_motor',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cota_z');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cota_z');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'cota_z',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_saneam_ali"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_saneam_ali"."id","eiel_c_saneam_ali"."id_municipio","eiel_c_saneam_ali"."clave","eiel_c_saneam_ali"."codprov","eiel_c_saneam_ali"."codmunic","eiel_c_saneam_ali"."acumulacion","eiel_c_saneam_ali"."pot_motor","eiel_c_saneam_ali"."estado","eiel_c_saneam_ali"."cota_z","eiel_c_saneam_ali"."obra_ejec","eiel_c_saneam_ali"."precision_dig","eiel_c_saneam_ali"."observ" FROM "eiel_c_saneam_ali" WHERE "eiel_c_saneam_ali"."id_municipio" IN (?M)',
  'UPDATE "eiel_c_saneam_ali" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''LI'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"acumulacion"=?7,"pot_motor"=?8,"estado"=?9,"cota_z"=?10,"obra_ejec"=?11,"precision_dig"=?12,"observ"=?13 WHERE "id"=?2
','INSERT INTO "eiel_c_saneam_ali" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","acumulacion","pot_motor","estado","cota_z","obra_ejec","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''LI'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11,?12,?13)
','DELETE FROM "eiel_c_saneam_ali" WHERE "id"=?2');



-- Familia: EIEL EQUIPAMIENTOS
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL EQUIPAMIENTOS');


INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL EQUIPAMIENTOS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL EQUIPAMIENTOS');

INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),2,0);

--Layer Instalacion deportiva
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>ID</Name><UserStyle><Name>ID:_:EIEL: ID</Name><Title>ID:_:EIEL: ID</Title><Abstract>ID:_:EIEL: ID</Abstract><FeatureTypeStyle><Rule><Name>Instalación deportiva</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>ID</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#0e0d0e</CssParameter></Fill><Stroke><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>2.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>ID</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-weight'>bold</CssParameter><CssParameter name='font-size'>10.0</CssParameter><CssParameter name='font-style'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>ID:_:default:ID</Name><Title>ID:_:default:ID</Title><Abstract>ID:_:default:ID</Abstract><FeatureTypeStyle><Name>ID</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#6600cc</CssParameter></Fill><Stroke><CssParameter name='stroke'>#ff0000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter><CssParameter name='stroke-width'><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>ID</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink='http://www.w3.org/1999/xlink' xlink:type='simple' xlink:href='file:iconlib/instalaciones_deportivas.png'/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-weight'>normal</CssParameter><CssParameter name='font-style'>normal</CssParameter><CssParameter name='font-size'>11.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Instalacion deportiva');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Instalación deportiva');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Instalación deportiva');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Instalación deportiva');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Instalación deportiva');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Instalación deportiva');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'ID',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 13);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'ID:_:EIEL: ID',true,0,0,true,true);	
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_id',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_id',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_id"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_id"."id","eiel_c_id"."id_municipio","eiel_c_id"."clave","eiel_c_id"."codprov","eiel_c_id"."codmunic","eiel_c_id"."codentidad","eiel_c_id"."codpoblamiento","eiel_c_id"."orden_id","eiel_c_id"."precision_dig","eiel_c_id"."observ" FROM "eiel_c_id"  WHERE "eiel_c_id"."id_municipio" in (?M) AND "eiel_c_id"."clave"<>''AN''
','UPDATE "eiel_c_id" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''ID'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_id"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_id" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_id","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''ID'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_id" WHERE "id"=?2');
  

--Layer Centros culturales o de esparcimiento	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CU</Name><UserStyle><Name>CU:_:default:CU</Name><Title>CU:_:default:CU</Title><Abstract>CU:_:default:CU</Abstract><FeatureTypeStyle><Name>CU</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/centros_culturales.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>CU:_:EIEL: CU</Name><Title>CU:_:EIEL: CU</Title><Abstract>CU:_:EIEL: CU</Abstract><FeatureTypeStyle><Rule><Name>Centro cultural</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#de9a3e</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#c59531</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>CU</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CU</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#d58d3b</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros culturales o de esparcimiento');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros culturales o de esparcimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros culturales o de esparcimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros culturales o de esparcimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros culturales o de esparcimiento');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros culturales o de esparcimiento');
INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CU',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 14);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CU:_:default:CU',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_cu',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_cu',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_cu"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_cu"."id","eiel_c_cu"."id_municipio","eiel_c_cu"."clave","eiel_c_cu"."codprov","eiel_c_cu"."codmunic","eiel_c_cu"."codentidad","eiel_c_cu"."codpoblamiento","eiel_c_cu"."orden_cu","eiel_c_cu"."precision_dig","eiel_c_cu"."observ" FROM "eiel_c_cu"  WHERE "eiel_c_cu"."id_municipio" in (?M) AND "eiel_c_cu"."clave"<>''AN''
','UPDATE "eiel_c_cu" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CU'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_cu"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_cu" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_cu","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CU'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_cu" WHERE "id"=?2');

 
--Layer Parques jardines y áreas naturales
		
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>PJ</Name><UserStyle><Name>PJ:_:default:PJ</Name><Title>PJ:_:default:PJ</Title><Abstract>PJ:_:default:PJ</Abstract><FeatureTypeStyle><Name>PJ</Name><Rule><Name>PJ</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/parque_municipal.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>PJ:_:EIEL: PJ</Name><Title>PJ:_:EIEL: PJ</Title><Abstract>PJ:_:EIEL: PJ</Abstract><FeatureTypeStyle><Rule><Name>Parques y jardines</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#368531</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#3c8a27</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>PJ</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>PJ</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#1f821a</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parques jardines y áreas naturales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parques jardines y áreas naturales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Parques jardines y áreas naturales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Parques jardines y áreas naturales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Parques jardines y áreas naturales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Parques jardines y áreas naturales');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'PJ',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 15);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'PJ:_:EIEL: PJ',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_pj',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_pj',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_pj"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_pj"."id","eiel_c_pj"."id_municipio","eiel_c_pj"."clave","eiel_c_pj"."codprov","eiel_c_pj"."codmunic","eiel_c_pj"."codentidad","eiel_c_pj"."codpoblamiento","eiel_c_pj"."orden_pj","eiel_c_pj"."precision_dig","eiel_c_pj"."observ" FROM "eiel_c_pj" WHERE "eiel_c_pj"."id_municipio" in (?M) AND "eiel_c_pj"."clave"<>''AN''
','UPDATE "eiel_c_pj" SET "GEOMETRY"=transform("eiel_c_pj"."GEOMETRY" , ?T),"id"=?2,"id_municipio"=?M,"clave"=''PJ'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_pj"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_pj" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_pj","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''PJ'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_pj" WHERE "id"=?2');
  

--Layer Lonjas, mercados y recintos feriales
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>LM</Name><UserStyle><Name>LM:_:default:LM</Name><Title>LM:_:default:LM</Title><Abstract>LM:_:default:LM</Abstract><FeatureTypeStyle><Name>LM</Name><Rule><Name>LM</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/mercados.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>LM:_:EIEL: LM</Name><Title>LM:_:EIEL: LM</Title><Abstract>LM:_:EIEL: LM</Abstract><FeatureTypeStyle><Rule><Name>Lonjas y mercados</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#05efe6</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#07f5ef</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>LM</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#0feaf9</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
		
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Lonjas, mercados y recintos feriales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Lonjas, mercados y recintos feriales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Lonjas, mercados y recintos feriales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Lonjas, mercados y recintos feriales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Lonjas, mercados y recintos feriales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Lonjas, mercados y recintos feriales');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'LM',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 16);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'LM:_:default:LM',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_lm',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_lm',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_lm"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_lm"."id","eiel_c_lm"."id_municipio","eiel_c_lm"."clave","eiel_c_lm"."codprov","eiel_c_lm"."codmunic","eiel_c_lm"."codentidad","eiel_c_lm"."codpoblamiento","eiel_c_lm"."orden_lm","eiel_c_lm"."precision_dig","eiel_c_lm"."observ" FROM "eiel_c_lm" WHERE "eiel_c_lm"."id_municipio" in (?M) AND "eiel_c_lm"."clave"<>''AN''
','UPDATE "eiel_c_lm" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''LM'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_lm"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_lm" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_lm","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''LM'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_lm" WHERE "id"=?2');
  

--Layer Mataderos
		
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>MT</Name><UserStyle><Name>MT:_:default:MT</Name><Title>MT:_:default:MT</Title><Abstract>MT:_:default:MT</Abstract><FeatureTypeStyle><Name>MT</Name><Rule><Name>MT</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>MT:_:EIEL: MT</Name><Title>MT:_:EIEL: MT</Title><Abstract>MT:_:EIEL: MT</Abstract><FeatureTypeStyle><Rule><Name>Matadero</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#4abef0</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#3895db</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>MT</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Geometria</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#629ced</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Mataderos');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Mataderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Mataderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Mataderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Mataderos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Mataderos');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'MT',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 17);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'MT:_:EIEL: MT',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_mt',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_mt',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_mt"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_mt"."id","eiel_c_mt"."id_municipio","eiel_c_mt"."clave","eiel_c_mt"."codprov","eiel_c_mt"."codmunic","eiel_c_mt"."codentidad","eiel_c_mt"."codpoblamiento","eiel_c_mt"."orden_mt","eiel_c_mt"."precision_dig","eiel_c_mt"."observ" FROM "eiel_c_mt"  WHERE "eiel_c_mt"."id_municipio" in (?M) AND "eiel_c_mt"."clave"<>''AN''
','UPDATE "eiel_c_mt" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''MT'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_mt"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_mt" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_mt","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''MT'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_mt" WHERE "id"=?2');

--Layer Cementerios	

INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CE</Name><UserStyle><Name>CE:_:default:CE</Name><Title>CE:_:default:CE</Title><Abstract>CE:_:default:CE</Abstract><FeatureTypeStyle><Name>CE</Name><Rule><Name>CE</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>CE:_:EIEL: CE</Name><Title>CE:_:EIEL: CE</Title><Abstract>CE:_:EIEL: CE</Abstract><FeatureTypeStyle><Rule><Name>Cementerio</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#9a9292</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#908787</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>CE</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CE</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#817a7a</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Cementerios');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cementerios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cementerios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cementerios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cementerios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cementerios');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CE',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 18);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CE:_:EIEL: CE',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_ce',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_ce',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);



INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_ce"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_ce"."id","eiel_c_ce"."id_municipio","eiel_c_ce"."clave","eiel_c_ce"."codprov","eiel_c_ce"."codmunic","eiel_c_ce"."codentidad","eiel_c_ce"."codpoblamiento","eiel_c_ce"."orden_ce","eiel_c_ce"."precision_dig","eiel_c_ce"."observ" FROM "eiel_c_ce" WHERE "eiel_c_ce"."id_municipio" in (?M) AND "eiel_c_ce"."clave"<>''AN''
','UPDATE "eiel_c_ce" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CE'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_ce"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_ce" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_ce","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CE'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_ce" WHERE "id"=?2');

--Layer Tanatorios
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TN</Name><UserStyle><Name>TN:_:default:TN</Name><Title>TN:_:default:TN</Title><Abstract>TN:_:default:TN</Abstract><FeatureTypeStyle><Name>TN</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>TN:_:EIEL: TN</Name><Title>TN:_:EIEL: TN</Title><Abstract>TN:_:EIEL: TN</Abstract><FeatureTypeStyle><Rule><Name>Tanatorio</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>TA</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#da137e</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#cc067d</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>TA</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>TA</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#dc0976</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tanatorios');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tanatorios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tanatorios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tanatorios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tanatorios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tanatorios');


INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TN',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 19);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TN:_:EIEL: TN',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_ta',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_ta',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_ta"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_ta"."id","eiel_c_ta"."id_municipio","eiel_c_ta"."clave","eiel_c_ta"."codprov","eiel_c_ta"."codmunic","eiel_c_ta"."codentidad","eiel_c_ta"."codpoblamiento","eiel_c_ta"."orden_ta","eiel_c_ta"."precision_dig","eiel_c_ta"."observ" FROM "eiel_c_ta" WHERE "eiel_c_ta"."id_municipio" in (?M) AND "eiel_c_ta"."clave"<>''AN''
','UPDATE "eiel_c_ta" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''TA'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_ta"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_ta" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_ta","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''TA'',substr(?M,1,2),substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_ta" WHERE "id"=?2');
  

	--Layer Centros Sanitarios	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>SA</Name><UserStyle><Name>SA:_:default:SA</Name><Title>SA:_:default:SA</Title><Abstract>SA:_:default:SA</Abstract><FeatureTypeStyle><Name>SA</Name><Rule><Name>SA</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/centros_sanitarios.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>SA:_:EIEL: SA</Name><Title>SA:_:EIEL: SA</Title><Abstract>SA:_:EIEL: SA</Abstract><FeatureTypeStyle><Rule><Name>Centro sanitario</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#883294</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#8f339f</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>SA</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>SA</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#a01699</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros Sanitarios');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros Sanitarios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros Sanitarios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros Sanitarios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros Sanitarios');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros Sanitarios');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'SA',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 20);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'SA:_:default:SA',true,0,0,true,true);	
			
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_sa',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_sa',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_sa"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_sa"."id","eiel_c_sa"."id_municipio","eiel_c_sa"."clave","eiel_c_sa"."codprov","eiel_c_sa"."codmunic","eiel_c_sa"."codentidad","eiel_c_sa"."codpoblamiento","eiel_c_sa"."orden_sa","eiel_c_sa"."precision_dig","eiel_c_sa"."observ" FROM "eiel_c_sa" WHERE "eiel_c_sa"."id_municipio" in (?M) AND "eiel_c_sa"."clave"<>''AN''
','UPDATE "eiel_c_sa" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''SA'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_sa"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_sa" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_sa","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''SA'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_sa" WHERE "id"=?2');


--Layer Centros Asistenciales	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>ASL</Name><UserStyle><Name>ASL:_:default:ASL</Name><Title>ASL:_:default:ASL</Title><Abstract>ASL:_:default:ASL</Abstract><FeatureTypeStyle><Name>ASL</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/centros_asistenciales.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>11.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>ASL:_:EIEL: AS</Name><Title>ASL:_:EIEL: AS</Title><Abstract>ASL:_:EIEL: AS</Abstract><FeatureTypeStyle><Rule><Name>Centro asistencial</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>AS</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#dd0c18</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#db1c20</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>AS</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>ASL</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#dc0f16</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros Asistenciales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros Asistenciales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros Asistenciales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros Asistenciales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros Asistenciales');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros Asistenciales');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'ASL',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 21);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'ASL:_:default:ASL',true,0,0,true,true);	
				
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_as',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_as',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_as"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_as"."id","eiel_c_as"."id_municipio","eiel_c_as"."clave","eiel_c_as"."codprov","eiel_c_as"."codmunic","eiel_c_as"."codentidad","eiel_c_as"."codpoblamiento","eiel_c_as"."orden_as","eiel_c_as"."precision_dig","eiel_c_as"."observ" FROM "eiel_c_as" WHERE "eiel_c_as"."id_municipio" in (?M) AND "eiel_c_as"."clave"<>''AN''
','UPDATE "eiel_c_as" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''AS'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_as"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_as" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_as","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''AS'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_as" WHERE "id"=?2');
  

--Layer Centros de enseñanza
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EN</Name><UserStyle><Name>EN:_:default:EN</Name><Title>EN:_:default:EN</Title><Abstract>EN:_:default:EN</Abstract><FeatureTypeStyle><Name>EN</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/centros_de_ensenanza.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>EN:_:EIEL: EN</Name><Title>EN:_:EIEL: EN</Title><Abstract>EN:_:EIEL: EN</Abstract><FeatureTypeStyle><Rule><Name>Centro de enseñanza</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#f7d35e</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#edbb55</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>EN</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>EN</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#f2c451</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros de enseñanza');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros de enseñanza');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros de enseñanza');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros de enseñanza');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros de enseñanza');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros de enseñanza');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'EN',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 22);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EN:_:default:EN',true,0,0,true,true);	
				
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_en',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_en',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_en"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_en"."id","eiel_c_en"."id_municipio","eiel_c_en"."clave","eiel_c_en"."codprov","eiel_c_en"."codmunic","eiel_c_en"."codentidad","eiel_c_en"."codpoblamiento","eiel_c_en"."orden_en","eiel_c_en"."precision_dig","eiel_c_en"."observ" FROM "eiel_c_en" WHERE "eiel_c_en"."id_municipio" in (?M) AND "eiel_c_en"."clave"<>''AN''
','UPDATE "eiel_c_en" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''EN'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_en"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_en" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_en","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''EN'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_en" WHERE "id"=?2');
  

--Layer Centro de extincion de incendios y protección civil		
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>IP</Name><UserStyle><Name>IP:_:default:IP</Name><Title>IP:_:default:IP</Title><Abstract>IP:_:default:IP</Abstract><FeatureTypeStyle><Name>IP</Name><Rule><Name>IP</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>IP:_:EIEL: IP</Name><Title>IP:_:EIEL: IP</Title><Abstract>IP:_:EIEL: IP</Abstract><FeatureTypeStyle><Rule><Name>Protección civil</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#f9f70c</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#fdfa0d</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>IP</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Geometria</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#f8f509</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centro de protección civil');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centro de extincion de incendios y protección civil');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centro de extincion de incendios y protección civil');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centro de extincion de incendios y protección civil');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centro de extincion de incendios y protección civil');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centro de extincion de incendios y protección civil');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'IP',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 23);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'IP:_:EIEL: IP',true,0,0,true,true);	
				
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_ip',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_ip',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_ip"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_ip"."id","eiel_c_ip"."id_municipio","eiel_c_ip"."clave","eiel_c_ip"."codprov","eiel_c_ip"."codmunic","eiel_c_ip"."codentidad","eiel_c_ip"."codpoblamiento","eiel_c_ip"."orden_ip","eiel_c_ip"."precision_dig","eiel_c_ip"."observ" FROM "eiel_c_ip" WHERE "eiel_c_ip"."id_municipio" in (?M) AND "eiel_c_ip"."clave"<>''AN''
','UPDATE "eiel_c_ip" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''IP'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_ip"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_ip" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_ip","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''IP'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_ip" WHERE "id"=?2');
  

--Layer Casas consistoriales y otros equipamientos de titularidad municipal
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CC</Name><UserStyle><Name>CC:_:default:CC</Name><Title>CC:_:default:CC</Title><Abstract>CC:_:default:CC</Abstract><FeatureTypeStyle><Name>CC</Name><Rule><Name>CC</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink=''http://www.w3.org/1999/xlink'' xlink:type=''simple'' xlink:href=''file:iconlib/casas_consistoriales.png''/><Format>image/png</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>15</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>CC:_:EIEL: CC</Name><Title>CC:_:EIEL: CC</Title><Abstract>CC:_:EIEL: CC</Abstract><FeatureTypeStyle><Rule><Name>Casa consistorial</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CC</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#7bd55c</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#85c358</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>CC</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CC</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#66cc00</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
		
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Casas consistoriales');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Casas consistoriales y otros equipamientos de titularidad municipal');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Casas consistoriales y otros equipamientos de titularidad municipal');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Casas consistoriales y otros equipamientos de titularidad municipal');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Casas consistoriales y otros equipamientos de titularidad municipal');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Casas consistoriales y otros equipamientos de titularidad municipal');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CC',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 24);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CC:_:default:CC',true,0,0,true,true);	
				
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_cc',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_cc',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_cc"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_cc"."id","eiel_c_cc"."id_municipio","eiel_c_cc"."clave","eiel_c_cc"."codprov","eiel_c_cc"."codmunic","eiel_c_cc"."codentidad","eiel_c_cc"."codpoblamiento","eiel_c_cc"."orden_cc","eiel_c_cc"."precision_dig","eiel_c_cc"."observ" FROM "eiel_c_cc" WHERE "eiel_c_cc"."id_municipio" in (?M) AND "eiel_c_cc"."clave"<>''AN''
','UPDATE "eiel_c_cc" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''CC'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_cc"=?9,"precision_dig"=?10,"observ"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_cc" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_cc","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''CC'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_cc" WHERE "id"=?2');
  

--Layer Edificios de titularidad publica sin uso	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>SU</Name><UserStyle><Name>SU:_:EIEL: SU</Name><Title>SU:_:EIEL: SU</Title><Abstract>SU:_:EIEL: SU</Abstract><FeatureTypeStyle><Name>SU</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>SU:_:EIEL: SU</Name><Title>SU:_:EIEL: SU</Title><Abstract>SU:_:EIEL: SU</Abstract><FeatureTypeStyle><Rule><Name>Edificios sin uso</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#180aed</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#1009f7</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>SU</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Geometria</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#0a14fb</CssParameter><CssParameter name=''font-family''>Verdana</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');
	
INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Edificios de titularidad publica sin uso');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Edificios de titularidad pública sin uso');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Edificios de titularidad pública sin uso');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Edificios de titularidad pública sin uso');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Edificios de titularidad pública sin uso');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Edificios de titularidad pública sin uso');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'SU',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 25);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'SU:_:EIEL: SU',true,0,0,true,true);	
				
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_su',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_su',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_su"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_su"."id","eiel_c_su"."id_municipio","eiel_c_su"."clave","eiel_c_su"."codprov","eiel_c_su"."codmunic","eiel_c_su"."codentidad","eiel_c_su"."codpoblamiento","eiel_c_su"."orden_su","eiel_c_su"."observ","eiel_c_su"."precision_dig" FROM "eiel_c_su" WHERE "eiel_c_su"."id_municipio" in (?M) AND "eiel_c_su"."clave"<>''AN''
','UPDATE "eiel_c_su" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''SU'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?7,"codpoblamiento"=?8,"orden_su"=?9,"observ"=?10,"precision_dig"=?11 WHERE "id"=?2
','INSERT INTO "eiel_c_su" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_su","observ","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''SU'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9,?10,?11)
','DELETE FROM "eiel_c_su" WHERE "id"=?2');
  



-- Familia: EIEL INFRAESTRUCTURA VIARIA
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL INFRAESTRUCTURA VIARIA');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL INFRAESTRUCTURA VIARIA');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),3,0);

--Layer Infraestructura viaria

INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TU</Name><UserStyle><Name>TU:_:default:TU</Name><Title>TU:_:default:TU</Title><Abstract>TU:_:default:TU</Abstract><FeatureTypeStyle><Name>TU</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Infraestructura viaria');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Infraestructura viaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Infraestructura viaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Infraestructura viaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Infraestructura viaria');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Infraestructura viaria');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TU',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 26);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TU:_:default:TU',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_redviaria_tu',3);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Tramo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Tramo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'tramo_vu',5,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Tipo Red Viaria'),'tipo',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Tipo Red Viaria'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Denominación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Denominación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Denominación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Denominación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Denominación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10036,'denominacion',200,0,0,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Ancho');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'ancho',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Material del firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Material del firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Material del firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Material del firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Material del firme');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Firme Red Viaria'),'firme',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Firme Red Viaria'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10030,'longitud',6,2,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10030,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Superficie');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'superficie',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Viviendas afectadas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Viviendas afectadas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Viviendas afectadas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Viviendas afectadas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Viviendas afectadas');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'viviendas_afec',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de actuación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de actuación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de actuación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de actuación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de actuación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_actuacion',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),18,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),19,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),20,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','ttggss');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]ttggss');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]ttggss');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]ttggss');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]ttggss');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10041,'ttggss',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),21,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10041,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Vía');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Vía');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Vía');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Vía');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Vía');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'via',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),22,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Número y Símbolo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Número y Símbolo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Número y Símbolo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Número y Símbolo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Número y Símbolo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'numsymbol',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),23,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Mapa');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Mapa');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Mapa');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Mapa');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Mapa');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10030,'mapa',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),24,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10030,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_redviaria_tu"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_redviaria_tu"."id","eiel_c_redviaria_tu"."id_municipio","eiel_c_redviaria_tu"."clave","eiel_c_redviaria_tu"."codprov","eiel_c_redviaria_tu"."codmunic","eiel_c_redviaria_tu"."codentidad","eiel_c_redviaria_tu"."codpoblamiento","eiel_c_redviaria_tu"."tramo_vu","eiel_c_redviaria_tu"."tipo","eiel_c_redviaria_tu"."denominacion","eiel_c_redviaria_tu"."ancho","eiel_c_redviaria_tu"."estado","eiel_c_redviaria_tu"."firme","eiel_c_redviaria_tu"."longitud","eiel_c_redviaria_tu"."superficie","eiel_c_redviaria_tu"."viviendas_afec","eiel_c_redviaria_tu"."fecha_actuacion","eiel_c_redviaria_tu"."obra_ejec","eiel_c_redviaria_tu"."observ","eiel_c_redviaria_tu"."ttggss","eiel_c_redviaria_tu"."via","eiel_c_redviaria_tu"."numsymbol","eiel_c_redviaria_tu"."mapa" FROM "eiel_c_redviaria_tu"  WHERE "eiel_c_redviaria_tu"."id_municipio" in (?M) 
','UPDATE "eiel_c_redviaria_tu" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=?4,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"tramo_vu"=?9,"tipo"=?10,"denominacion"=?11,"ancho"=?12,"estado"=?13,"firme"=?14,"longitud"=length(GeometryFromText(text(?1),?S)),"superficie"=?16,"viviendas_afec"=?17,"fecha_actuacion"=?18,"obra_ejec"=?19,"observ"=?20,"ttggss"=?21,"via"=?22,"numsymbol"=?23,"mapa"=?24 WHERE "id"=?2
','INSERT INTO "eiel_c_redviaria_tu" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","tramo_vu","tipo","denominacion","ancho","estado","firme","longitud","superficie","viviendas_afec","fecha_actuacion","obra_ejec","observ","ttggss","via","numsymbol","mapa") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,substr(?M,1,2),?6,?7,?8,?9,?10,?11,?12,?13,?14,length(GeometryFromText(text(?1),?S)),?16,?17,?18,?19,?20,?21,?22,?23,?24)
','DELETE FROM "eiel_c_redviaria_tu" WHERE "id"=?2');
  


-- Familia: EIEL RED DE CARRETERAS

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL RED DE CARRETERAS');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL RED DE CARRETERAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL RED DE CARRETERAS');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),4,0);

--Layer Tramos de carretera
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>carreteras</Name><UserStyle><Name>carreteras:_:default:carreteras</Name><Title>carreteras:_:default:carreteras</Title><Abstract>carreteras:_:default:carreteras</Abstract><FeatureTypeStyle><Name>carreteras</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tramos de carretera');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tramos de carretera');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tramos de carretera');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tramos de carretera');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tramos de carretera');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tramos de carretera');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'carreteras',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 27);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'carreteras:_:default:carreteras',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_tramos_carreteras',3);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código carretera');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código carretera');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código carretera');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código carretera');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código carretera');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),89,'cod_carrt',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),89,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Punto kilométrico inicial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Punto kilométrico inicial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Punto kilométrico inicial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Punto kilométrico inicial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Punto kilométrico inicial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pki',6,3,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Punto kilométrico final');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Punto kilométrico final');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Punto kilométrico final');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Punto kilométrico final');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Punto kilométrico final');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'pkf',6,3,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Gestor');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Gestor');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Gestor Tramos C'),'gestor',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Gestor Tramos C'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Señaliza');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Señaliza');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Señaliza');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Señaliza');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Señaliza');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Señaliza'),'senaliza',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Señaliza'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Firme');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Firme');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Firme Tramos C'),'firme',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Firme Tramos C'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado Tramos C'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado Tramos C'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Ancho');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Ancho');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10019,'ancho',4,1,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10019,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'longitud',6,3,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,false);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Paso a nivel');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Paso a nivel');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Paso a nivel');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Paso a nivel');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Paso a nivel');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10021,'paso_nivel',6,3,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10021,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Dimensionamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Dimensionamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Dimensionamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Dimensionamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Dimensionamiento');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_dimensiona'),'dimensiona',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_dimensiona'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Sinuosidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Sinuosidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Sinuosidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Sinuosidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Sinuosidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_muy_sinuoso'),'muy_sinuoso',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_muy_sinuoso'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Pendiente excesiva');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Pendiente excesiva');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Pendiente excesiva');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Pendiente excesiva');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Pendiente excesiva');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_pte_excesiva'),'pte_excesiva',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),18,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_pte_excesiva'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Frecuente extrechamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Frecuente extrechamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Frecuente extrechamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Frecuente extrechamiento');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Frecuente extrechamiento');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_fre_estrech'),'fre_estrech',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),19,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_fre_estrech'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Superficie');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Superficie');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10023,'superficie',8,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),20,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10023,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),21,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Toma de datos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Toma de datos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Toma de datos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Toma de datos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Toma de datos');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'toma_dato',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),22,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10002,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de actualización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de actualización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de actualización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de actualización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de actualización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_act',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),23,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha desuso');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha desuso');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha desuso');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha desuso');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha desuso');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_desuso',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),24,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),25,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);





INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_tramos_carreteras"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_tramos_carreteras"."id","eiel_c_tramos_carreteras"."id_municipio","eiel_c_tramos_carreteras"."codprov","eiel_c_tramos_carreteras"."codmunic","eiel_c_tramos_carreteras"."cod_carrt","eiel_c_tramos_carreteras"."pki","eiel_c_tramos_carreteras"."pkf","eiel_c_tramos_carreteras"."gestor","eiel_c_tramos_carreteras"."senaliza","eiel_c_tramos_carreteras"."firme","eiel_c_tramos_carreteras"."estado","eiel_c_tramos_carreteras"."ancho","eiel_c_tramos_carreteras"."longitud","eiel_c_tramos_carreteras"."paso_nivel","eiel_c_tramos_carreteras"."dimensiona","eiel_c_tramos_carreteras"."muy_sinuoso","eiel_c_tramos_carreteras"."pte_excesiva","eiel_c_tramos_carreteras"."fre_estrech","eiel_c_tramos_carreteras"."superficie","eiel_c_tramos_carreteras"."superficie","eiel_c_tramos_carreteras"."obra_ejec","eiel_c_tramos_carreteras"."toma_dato","eiel_c_tramos_carreteras"."fecha_act","eiel_c_tramos_carreteras"."fecha_desuso","eiel_c_tramos_carreteras"."observ" FROM "eiel_c_tramos_carreteras" WHERE "eiel_c_tramos_carreteras"."id_municipio" in (?M)
','UPDATE "eiel_c_tramos_carreteras" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"cod_carrt"=?6,"pki"=?7,"pkf"=?8,"gestor"=?9,"senaliza"=?10,"firme"=?11,"estado"=?12,"ancho"=?13,"longitud"=length(GeometryFromText(text(?1),?S)),"paso_nivel"=?15,"dimensiona"=?16,"muy_sinuoso"=?17,"pte_excesiva"=?18,"fre_estrech"=?19,"superficie"=?20,"obra_ejec"=?21,"toma_dato"=?22,"fecha_act"=?23,"fecha_desuso"=?24,"observ"=?25 WHERE "id"=?2
','INSERT INTO "eiel_c_tramos_carreteras" ("GEOMETRY","id","id_municipio","codprov","codmunic","cod_carrt","pki","pkf","gestor","senaliza","firme","estado","ancho","longitud","paso_nivel","dimensiona","muy_sinuoso","pte_excesiva","fre_estrech","superficie","obra_ejec","toma_dato","fecha_act","fecha_desuso","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12,?13,length(GeometryFromText(text(?1),?S)),?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25)
','DELETE FROM "eiel_c_tramos_carreteras" WHERE "id"=?2');
  

-- Familia: EIEL ALUMBRADO
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL ALUMBRADO');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL ALUMBRADO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL ALUMBRADO');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),5,0);

--Layer Cuadro de mando
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CMP</Name><UserStyle><Name>CMP:_:EIEL: CMP</Name><Title>CMP:_:EIEL: CMP</Title><Abstract>CMP:_:EIEL: CMP</Abstract><FeatureTypeStyle><Name>CMP</Name><Rule><Name>Cuadro de medida de protección</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffff99</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Cuadro de mando');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cuadro de mando');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CMP',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 28);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CMP:_:EIEL: CMP',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_alum_cmp',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia instalada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia instalada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia instalada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia instalada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia instalada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'potencia_inst',null,0,null,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia contratada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia contratada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia contratada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia contratada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia contratada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'potencia_contratada',null,0,null,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Número circuitos');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10016,'n_circuitos',2,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10016,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Contador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Contador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Contador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Contador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Contador');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_contador'),'contador',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_contador'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);		
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_cmp',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_inst',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado de Revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de revisión'),'estado_revision',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de revisión'),0);


		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_alum_cmp"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_alum_cmp"."id","eiel_c_alum_cmp"."id_municipio","eiel_c_alum_cmp"."codprov","eiel_c_alum_cmp"."codmunic","eiel_c_alum_cmp"."potencia_inst","eiel_c_alum_cmp"."potencia_contratada","eiel_c_alum_cmp"."n_circuitos","eiel_c_alum_cmp"."contador","eiel_c_alum_cmp"."estado","eiel_c_alum_cmp"."obra_ejec","eiel_c_alum_cmp"."observ","eiel_c_alum_cmp"."precision_dig","eiel_c_alum_cmp"."orden_cmp","eiel_c_alum_cmp"."fecha_inst","eiel_c_alum_cmp"."fecha_revision","eiel_c_alum_cmp"."estado_revision" FROM "eiel_c_alum_cmp" WHERE "eiel_c_alum_cmp"."id_municipio" in (?M)
','UPDATE "eiel_c_alum_cmp" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"potencia_inst"=?6,"potencia_contratada"=?7,"n_circuitos"=?8,"contador"=?9,"estado"=?10,"obra_ejec"=?11,"observ"=?12,"precision_dig"=?13,"orden_cmp"=?14,"fecha_inst"=?15,"fecha_revision"=?16,"estado_revision"=?17 WHERE "id"=?2
','INSERT INTO "eiel_c_alum_cmp" ("GEOMETRY","id","id_municipio","codprov","codmunic","potencia_inst","potencia_contratada","n_circuitos","contador","estado","obra_ejec","observ","precision_dig","orden_cmp","fecha_inst","fecha_revision","estado_revision") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2), substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17)
','DELETE FROM "eiel_c_alum_cmp" WHERE "id"=?2');
  

--Layer Estabilizador
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>eea</Name><UserStyle><Name>eea:_:default:eea</Name><Title>eea:_:default:eea</Title><Abstract>eea:_:default:eea</Abstract><FeatureTypeStyle><Name>eea</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Estabilizador');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Estabilizador');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Estabilizador');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'eea',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 29);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'eea:_:default:eea',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_alum_eea',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'orden_eea',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Número circuitos');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Número circuitos');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10016,'n_circuitos',2,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10016,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estabilizador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estabilizador');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estabilizador');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'estabilizador',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'potencia',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado de Revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de revisión'),'estado_revision',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de revisión'),0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_alum_eea"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_alum_eea"."id","eiel_c_alum_eea"."id_municipio","eiel_c_alum_eea"."codprov","eiel_c_alum_eea"."codmunic","eiel_c_alum_eea"."orden_eea","eiel_c_alum_eea"."n_circuitos","eiel_c_alum_eea"."estabilizador","eiel_c_alum_eea"."estado","eiel_c_alum_eea"."potencia","eiel_c_alum_eea"."obra_ejec","eiel_c_alum_eea"."precision_dig","eiel_c_alum_eea"."observ","eiel_c_alum_eea"."fecha_revision","eiel_c_alum_eea"."estado_revision" FROM "eiel_c_alum_eea" WHERE "eiel_c_alum_eea"."id_municipio" in (?M) 
','UPDATE "eiel_c_alum_eea" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_eea"=?6,"n_circuitos"=?7,"estabilizador"=?8,"estado"=?9,"potencia"=?10,"obra_ejec"=?11,"precision_dig"=?12,"observ"=?13,"fecha_revision"=?14,"estado_revision"=?15 WHERE "id"=?2
','INSERT INTO "eiel_c_alum_eea" ("GEOMETRY","id","id_municipio","codprov","codmunic","orden_eea","n_circuitos","estabilizador","estado","potencia","obra_ejec","precision_dig","observ","fecha_revision","estado_revision") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15)
','DELETE FROM "eiel_c_alum_eea" WHERE "id"=?2');

--Layer Punto de luz
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>PL</Name><UserStyle><Name>PL:_:EIEL: PL</Name><Title>PL:_:EIEL: PL</Title><Abstract>PL:_:EIEL: PL</Abstract><FeatureTypeStyle><Name>PL</Name><Rule><Name>Punto de luz</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffff00</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Punto de luz</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffff00</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#663300</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>PL:_:pl</Name><Title>PL:_:pl</Title><Abstract>PL:_:pl</Abstract><FeatureTypeStyle><Rule><Name>regla temática 0</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>B</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#1f721f</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#3f8b31</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>8.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>regla temática 0</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>E</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#080f03</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>regla temática 1</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>M</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#e22229</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>regla temática 2</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Estado</ogc:PropertyName><ogc:Literal>R</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#3797cd</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>10</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Punto de luz');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Punto de luz');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Punto de luz');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Punto de luz');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Punto de luz');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Punto de luz');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'PL',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 30);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'PL:_:EIEL: PL',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_alum_pl',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_pl',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Sistema eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Sistema eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Sistema eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Sistema eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Sistema eficiencia energética');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_sist_eficiencia_pl'),'sist_eficiencia_pl',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_sist_eficiencia_pl'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Reductor de flujo en la luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Reductor de flujo en la luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Reductor de flujo en la luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Reductor de flujo en la luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Reductor de flujo en la luminaria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Reductor de flujo en la luminaria'),'ah_ener_rfl',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Reductor de flujo en la luminaria'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Reductor de flujo en la instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Reductor de flujo en la instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Reductor de flujo en la instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Reductor de flujo en la instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Reductor de flujo en la instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Reductor de flujo en la instalación'),'ah_ener_rfi',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Reductor de flujo en la instalación'),0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de conservación'),'estado',1,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de conservación'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Potencia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Potencia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'potencia',null,1,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo lampara');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo lampara');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo lampara');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo lampara');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo lampara');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_tipo_lampara'),'tipo_lampara',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_tipo_lampara'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo apoyo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo apoyo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo apoyo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo apoyo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo apoyo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_tipo_apoyo'),'tipo_apoyo',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_tipo_apoyo'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de instalación');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de instalación');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_inst',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),18,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),19,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Número lamparas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Número lamparas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Número lamparas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Número lamparas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Número lamparas');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'n_lamparas',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),20,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Altura');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Altura');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Altura');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Altura');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Altura');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10022,'altura',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),21,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10022,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo luminaria');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo luminaria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10001,'tipo_luminaria',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),22,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Obra ejecutada'),'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),23,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Obra ejecutada'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Eficiencia energética');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Eficiencia energética');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'orden_eea',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),24,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10002,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Cuadro de mando');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Cuadro de mando');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'orden_cmp',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),25,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10002,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_alum_pl"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_alum_pl"."id","eiel_c_alum_pl"."id_municipio","eiel_c_alum_pl"."clave","eiel_c_alum_pl"."codprov","eiel_c_alum_pl"."codmunic","eiel_c_alum_pl"."codentidad","eiel_c_alum_pl"."codpoblamiento","eiel_c_alum_pl"."orden_pl","eiel_c_alum_pl"."sist_eficiencia_pl","eiel_c_alum_pl"."ah_ener_rfl","eiel_c_alum_pl"."ah_ener_rfi","eiel_c_alum_pl"."estado","eiel_c_alum_pl"."potencia","eiel_c_alum_pl"."tipo_lampara","eiel_c_alum_pl"."tipo_apoyo","eiel_c_alum_pl"."fecha_inst","eiel_c_alum_pl"."observ","eiel_c_alum_pl"."precision_dig","eiel_c_alum_pl"."n_lamparas","eiel_c_alum_pl"."altura","eiel_c_alum_pl"."tipo_luminaria","eiel_c_alum_pl"."obra_ejec","eiel_c_alum_pl"."orden_eea","eiel_c_alum_pl"."orden_cmp" FROM "eiel_c_alum_pl" WHERE "eiel_c_alum_pl"."id_municipio" in (?M) AND "eiel_c_alum_pl"."clave"<>''AN''
','UPDATE "eiel_c_alum_pl" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=?4,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"orden_pl"=?9,"sist_eficiencia_pl"=?10,"ah_ener_rfl"=?11,"ah_ener_rfi"=?12,"estado"=?13,"potencia"=?14,"tipo_lampara"=?15,"tipo_apoyo"=?16,"fecha_inst"=?17,"observ"=?18,"precision_dig"=?19,"n_lamparas"=?20,"altura"=?21,"tipo_luminaria"=?22,"obra_ejec"=?23,"orden_eea"=?24,"orden_cmp"=?25 WHERE "id"=?2
','INSERT INTO "eiel_c_alum_pl" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","codentidad","codpoblamiento","orden_pl","sist_eficiencia_pl","ah_ener_rfl","ah_ener_rfi","estado","potencia","tipo_lampara","tipo_apoyo","fecha_inst","observ","precision_dig","n_lamparas","altura","tipo_luminaria","obra_ejec","orden_eea","orden_cmp") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25)  
','DELETE FROM "eiel_c_alum_pl" WHERE "id"=?2');
  

-- Familia: EIEL VERTEDERO
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL VERTEDERO');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL VERTEDERO');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),6,0);

--Layer Vertedero
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>VT</Name><UserStyle><Name>VT:_:default:VT</Name><Title>VT:_:default:VT</Title><Abstract>VT:_:default:VT</Abstract><FeatureTypeStyle><Name>VT</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ece9d8</CssParameter></Fill><Stroke><CssParameter name='stroke'>#ece9d8</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>VT:_:EIEL: VT</Name><Title>VT:_:EIEL: VT</Title><Abstract>VT:_:EIEL: VT</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ffffff</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#ffff33</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>0 a 15000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ffffff</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>VT</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-weight'>bold</CssParameter><CssParameter name='font-style'>normal</CssParameter><CssParameter name='font-size'>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Vertedero');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Vertedero');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Vertedero');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Vertedero');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Vertedero');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Vertedero');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'VT',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 31);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'VT:_:default:VT',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_vt',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_vt',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);		


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_vt"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_vt"."id","eiel_c_vt"."id_municipio","eiel_c_vt"."clave","eiel_c_vt"."codprov","eiel_c_vt"."codmunic","eiel_c_vt"."orden_vt","eiel_c_vt"."precision_dig","eiel_c_vt"."observ" FROM "eiel_c_vt" WHERE "eiel_c_vt"."id_municipio" in (?M) AND "eiel_c_vt"."clave"<>''AN''
','UPDATE "eiel_c_vt" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"clave"=''VT'',"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"orden_vt"=?7,"precision_dig"=?8,"observ"=?9 WHERE "id"=?2
','INSERT INTO "eiel_c_vt" ("GEOMETRY","id","id_municipio","clave","codprov","codmunic","orden_vt","precision_dig","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,''VT'',substr(?M,1,2), substr(?M,3,5),?7,?8,?9)
','DELETE FROM "eiel_c_vt" WHERE "id"=?2');
  

-- Familia: EIEL PARROQUIAS
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL PARROQUIAS');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL VERTEDERO');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL PARROQUIAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL PARROQUIAS');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),7,0);

--Layer Parroquias
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>parroquias</Name><UserStyle><Name>default:parroquias</Name><Title>default:parroquias</Title><Abstract>default:parroquias</Abstract><FeatureTypeStyle><Name>parroquias</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parroquias');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parroquias');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Parroquias');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Parroquias');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Parroquias');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Parroquias');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'parroquias',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 32);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:parroquias',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_parroquias',11);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Id parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Id parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Id parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Id parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Id parroquia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),116,'id_parroquia',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),116,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre parroquia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre parroquia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10036,'nombre_parroquia',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_parroquias"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_parroquias"."id","eiel_c_parroquias"."id_municipio","eiel_c_parroquias"."codprov","eiel_c_parroquias"."codmunic","eiel_c_parroquias"."id_parroquia","eiel_c_parroquias"."nombre_parroquia","eiel_c_parroquias"."fecha_revision","eiel_c_parroquias"."observ" FROM "eiel_c_parroquias" WHERE "eiel_c_parroquias"."id_municipio" in (?M)
','UPDATE "eiel_c_parroquias" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"id_parroquia"=?6,"nombre_parroquia"=?7,"fecha_revision"=?8,"observ"=?9 WHERE "id"=?2
','INSERT INTO "eiel_c_parroquias" ("GEOMETRY","id","id_municipio","codprov","codmunic","id_parroquia","nombre_parroquia","fecha_revision","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9)
','DELETE FROM "eiel_c_parroquias" WHERE "id"=?2');

  


-- Familia: EIEL EDIFICACIONES SINGULARES

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL EDIFICACIONES SINGULARES');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL EDIFICACIONES SINGULARES');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL EDIFICACIONES SINGULARES');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),8,0);

--Layer Edificaciones singulares
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>edificiosing</Name><UserStyle><Name>default:edificiosing</Name><Title>default:edificiosing</Title><Abstract>default:edificiosing</Abstract><FeatureTypeStyle><Name>edificiosing</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Edificaciones singulares');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Edificaciones singulares');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Edificaciones singulares');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Edificaciones singulares');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Edificaciones singulares');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Edificaciones singulares');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'edificiosing',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 33);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:edificiosing',true,0,0,true,true);	
		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_edificiosing',1);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Id elemento singular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Id elemento singular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Id elemento singular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Id elemento singular');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Id elemento singular');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),116,'id_elemsing',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),116,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'clave',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_el',3,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Tipo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Tipo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10001,'tipo',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10036,'nombre',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Obra ejecutada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Obra ejecutada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10001,'obra_ejec',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),82,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado de Revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de revisión'),'estado_revision',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),15,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de revisión'),0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),16,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Precisión digitalización');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Precisión digitalización');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_precision_dig'),'precision_dig',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),17,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_precision_dig'),0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_edificiosing"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_edificiosing"."id","eiel_c_edificiosing"."id_municipio","eiel_c_edificiosing"."codprov","eiel_c_edificiosing"."codmunic","eiel_c_edificiosing"."id_elemsing","eiel_c_edificiosing"."clave","eiel_c_edificiosing"."codentidad","eiel_c_edificiosing"."codpoblamiento","eiel_c_edificiosing"."orden_el","eiel_c_edificiosing"."tipo","eiel_c_edificiosing"."nombre","eiel_c_edificiosing"."obra_ejec","eiel_c_edificiosing"."fecha_revision","eiel_c_edificiosing"."estado_revision","eiel_c_edificiosing"."observ","eiel_c_edificiosing"."precision_dig" FROM "eiel_c_edificiosing" WHERE "eiel_c_edificiosing"."id_municipio" in (?M)
','UPDATE "eiel_c_edificiosing" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"id_elemsing"=?6,"clave"=?7,"codentidad"=?8,"codpoblamiento"=?9,"orden_el"=?10,"tipo"=?11,"nombre"=?12,"obra_ejec"=?13,"fecha_revision"=?14,"estado_revision"=?15,"observ"=?16,"precision_dig"=?17 WHERE "id"=?2
','INSERT INTO "eiel_c_edificiosing" ("GEOMETRY","id","id_municipio","codprov","codmunic","id_elemsing","clave","codentidad","codpoblamiento","orden_el","tipo","nombre","obra_ejec","fecha_revision","estado_revision","observ","precision_dig") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17)
','DELETE FROM "eiel_c_edificiosing" WHERE "id"=?2');
  



-- OTRAS CAPAS EIEL
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL OTRAS CAPAS');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]EIEL OTRAS CAPAS');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]EIEL OTRAS CAPAS');
INSERT INTO layerfamilies (id_layerfamily,id_name,id_description,validator) VALUES (nextval('seq_layerfamilies'),SELECT currval('seq_dictionary') from seq_dictionary,currval('seq_dictionary'),null);

INSERT INTO maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) VALUES (50,currval('seq_layerfamilies'),9,0);

--Layer Comarcas EIEL
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>CO</Name><UserStyle><Name>CO:_:default:CO</Name><Title>CO:_:default:CO</Title><Abstract>CO:_:default:CO</Abstract><FeatureTypeStyle><Name>CO</Name><Rule><Name>Desde 100.000 hasta 250.000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>100000.0</MinScaleDenominator><MaxScaleDenominator>250000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>0.0</CssParameter><CssParameter name='fill'>#ffffff</CssParameter></Fill><Stroke><CssParameter name='stroke-dasharray'>8.0,8.0</CssParameter><CssParameter name='stroke'>#996600</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-opacity'>0.86</CssParameter><CssParameter name='stroke-width'><ogc:Literal>3.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Nombre Comarca (Desde 250.000 hasta 500.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>250000.0</MinScaleDenominator><MaxScaleDenominator>500000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-weight'>bold</CssParameter><CssParameter name='font-size'>10.0</CssParameter><CssParameter name='font-style'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Nombre Comarca (Desde 500.000 hasta 800.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>500000.0</MinScaleDenominator><MaxScaleDenominator>800000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-weight'>bold</CssParameter><CssParameter name='font-size'>8.0</CssParameter><CssParameter name='font-style'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Nombre Comarca (Desde 800.000 hasta 1.100.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>800000.0</MinScaleDenominator><MaxScaleDenominator>1100000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name='font-family'>Arial</CssParameter><CssParameter name='font-color'>#000000</CssParameter><CssParameter name='font-weight'>bold</CssParameter><CssParameter name='font-size'>6.0</CssParameter><CssParameter name='font-style'>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Comarcas EIEL');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Comarcas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Comarcas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Comarcas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Comarcas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Comarcas EIEL');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'CO',currval('seq_styles'),null,1,0,1);
	
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 34);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'CO:_:default:CO',true,0,0,true,true);	
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_comarcas',5);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);		
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código comarca');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codcomarca',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre comarca');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),93,'nombre_comarca',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),93,0);	
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Hectáreas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Hectáreas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Hectáreas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Hectáreas');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Hectáreas');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'hectareas',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Perímetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Perímetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Perímetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Perímetro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Perímetro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'perimetro',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código municipio capital 1');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código municipio capital 1');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código municipio capital 1');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código municipio capital 1');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código municipio capital 1');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic_capital1',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código municipio capital 2');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código municipio capital 2');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código municipio capital 2');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código municipio capital 2');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código municipio capital 2');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic_capital2',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);	
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);		
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Huso UTM');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10003,'uso_utm',5,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10003,0);


INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_comarcas"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_comarcas"."id", "eiel_c_comarcas"."id_municipio","eiel_c_comarcas"."codcomarca","eiel_c_comarcas"."nombre_comarca","eiel_c_comarcas"."hectareas","eiel_c_comarcas"."perimetro","eiel_c_comarcas"."codmunic_capital1","eiel_c_comarcas"."codmunic_capital2","eiel_c_comarcas"."fecha_revision","eiel_c_comarcas"."observ","eiel_c_comarcas"."uso_utm" FROM "eiel_c_comarcas" WHERE "eiel_c_comarcas"."id_municipio" in (?M)',NULL,NULL,NULL);

--Layer Municipios EIEL	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TTMM</Name><UserStyle><Name>TTMM:_:default:TTMM</Name><Title>TTMM:_:default:TTMM</Title><Abstract>TTMM:_:default:TTMM</Abstract><FeatureTypeStyle><Name>TTMM</Name><Rule><Name>Desde 250.000 hasta 1.100.000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>250001.0</MinScaleDenominator><MaxScaleDenominator>1100000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>0.0</CssParameter><CssParameter name='fill'>#ffffff</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke'>#666666</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Municipios EIEL');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios EIEL');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TTMM',currval('seq_styles'),null,1,0,1);
		
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 35);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TTMM:_:default:TTMM',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_municipios',11);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre Oficial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),93,'nombre_oficial',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),93,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código catastro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'cod_catastro',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10002,0);	
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Huso UTM');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10003,'uso_utm',5,null,0,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10003,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Id Comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Id Comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Id Comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Id Comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Id Comarca');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'id_comarca',null,null,null,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);
	
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_municipios"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_municipios"."id", "eiel_c_municipios"."id_municipio","eiel_c_municipios"."codprov","eiel_c_municipios"."nombre_oficial","eiel_c_municipios"."cod_catastro","eiel_c_municipios"."codmunic","eiel_c_municipios"."uso_utm","eiel_c_municipios"."id_comarca"  FROM "eiel_c_municipios" WHERE "eiel_c_municipios"."id_municipio" in (?M)',NULL,NULL,NULL);


--Layer Municipios EIEL puntos
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>TTMMpunto</Name><UserStyle><Name>TTMMpunto:_:default:TTMMpunto</Name><Title>TTMMpunto:_:default:TTMMpunto</Title><Abstract>TTMMpunto:_:default:TTMMpunto</Abstract><FeatureTypeStyle><Name>TTMMpunto</Name><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>250000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000033</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>14.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Municipios EIEL puntos');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios EIEL puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios EIEL puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios EIEL puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios EIEL puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios EIEL puntos');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'TTMMpunto',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 36);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'TTMMpunto:_:default:TTMMpunto',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_municipios_puntos',1);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre Oficial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10008,'nombre_oficial',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10008,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código catastro');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código catastro');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10002,'cod_catastro',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10002,0);	
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Id comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Id comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Id comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Id comarca');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Id comarca');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'id_comarca',null,null,null,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);	
	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Huso UTM');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Huso UTM');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10003,'huso_utm',null,5,0,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10003,0);
			
		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_municipios_puntos"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_municipios_puntos"."id","eiel_c_municipios_puntos"."id_municipio","eiel_c_municipios_puntos"."codprov","eiel_c_municipios_puntos"."codmunic","eiel_c_municipios_puntos"."nombre_oficial","eiel_c_municipios_puntos"."cod_catastro","eiel_c_municipios_puntos"."id_comarca","eiel_c_municipios_puntos"."huso_utm" FROM "eiel_c_municipios_puntos" WHERE "eiel_c_municipios_puntos"."id_municipio" in (?M)
','UPDATE "eiel_c_municipios_puntos" SET "GEOMETRY"=GeometryFromText(?1,?S),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"nombre_oficial"=?6,"cod_catastro"=?7,"id_comarca"=?8,"huso_utm"=?9 WHERE "id"=?2
','INSERT INTO "eiel_c_municipios_puntos" ("GEOMETRY","id","id_municipio","codprov","codmunic","nombre_oficial","cod_catastro","id_comarca","huso_utm") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9)
','DELETE FROM "eiel_c_municipios_puntos" WHERE "id"=?2');
  

--Layer Nucleos Poblacion EIEL	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>NP</Name><UserStyle><Name>NP:_:EIEL: NP</Name><Title>NP:_:EIEL: NP</Title><Abstract>NP:_:EIEL: NP</Abstract><FeatureTypeStyle><Name>NP</Name><Rule><Name>Núcleo urbano</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.26</CssParameter><CssParameter name=''fill''>#9966ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#9900cc</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>-</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>5000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Núcleo urbano</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>5000000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.3</CssParameter><CssParameter name=''fill''>#cc99ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>-</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>5000.0</MinScaleDenominator><MaxScaleDenominator>5000000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>11.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>1.0</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Nucleos Poblacion EIEL');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos Población EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos Población EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos Población EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos Población EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos Población EIEL');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'NP',currval('seq_styles'),null,1,0,1);
	
INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 37);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'NP:_:EIEL: NP',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_nucleo_poblacion',5);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre Oficial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),93,'nombre_oficial',null,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),93,0);	
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de alta');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_alta',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado de Revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de revisión'),'estado_revision',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de revisión'),0);

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_nucleo_poblacion"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_nucleo_poblacion"."id","eiel_c_nucleo_poblacion"."id_municipio","eiel_c_nucleo_poblacion"."codprov","eiel_c_nucleo_poblacion"."codmunic","eiel_c_nucleo_poblacion"."codentidad","eiel_c_nucleo_poblacion"."nombre_oficial","eiel_c_nucleo_poblacion"."codpoblamiento","eiel_c_nucleo_poblacion"."fecha_alta","eiel_c_nucleo_poblacion"."observ","eiel_c_nucleo_poblacion"."fecha_revision","eiel_c_nucleo_poblacion"."estado_revision" FROM "eiel_c_nucleo_poblacion" WHERE "eiel_c_nucleo_poblacion"."id_municipio" in (?M)
','UPDATE "eiel_c_nucleo_poblacion" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?6,"codpoblamiento"=?7,"nombre_oficial"=?8,"fecha_alta"=?9,"observ"=?10,"fecha_revision"=?11,"estado_revision"=?12 WHERE "id"=?2
','INSERT INTO "eiel_c_nucleo_poblacion" ("GEOMETRY","id","id_municipio","codprov","codmunic","codentidad","codpoblamiento","nombre_oficial","fecha_alta","observ","fecha_revision","estado_revision") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12)
','DELETE FROM "eiel_c_nucleo_poblacion" WHERE "id"=?2');
  

--Layer Nucleos Poblacion EIEL Puntos
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>Nucleos</Name><UserStyle><Name>Nucleos:_:EIEL: Nucleos</Name><Title>Nucleos:_:EIEL: Nucleos</Title><Abstract>Nucleos:_:EIEL: Nucleos</Abstract><FeatureTypeStyle><Name>Nucleos</Name><Rule><Name>Nombre Capital Comarca (Desde 250.000 hasta 500.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>250000.0</MinScaleDenominator><MaxScaleDenominator>500000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>8.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Nombre Capital Comarca (Desde 500.000 hasta 800.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>500000.0</MinScaleDenominator><MaxScaleDenominator>800000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>6.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Nombre Capital Comarca (Desde 800.000 hasta 1.100.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>800000.0</MinScaleDenominator><MaxScaleDenominator>1100000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre Oficial</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>6.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Nucleos Poblacion EIEL Puntos');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos Población EIEL Puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos Población EIEL Puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos Población EIEL Puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos Población EIEL Puntos');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos Población EIEL Puntos');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'Nucleos',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 38);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'Nucleos:_:EIEL: Nucleos',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_nucleos_puntos',1);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE Núcleo');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE Núcleo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codpoblamiento',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre Oficial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),93,'nombre_oficial',50,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),93,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de alta');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de alta');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_alta',null,null,null,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10008,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10008,0);

	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Fecha de revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Fecha de revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10025,'fecha_revision',null,8,0,4);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10025,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Estado de Revisión');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Estado de Revisión');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),(select id from domains where name='eiel_Estado de revisión'),'estado_revision',null,0,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),(select id from domains where name='eiel_Estado de revisión'),0);

	
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_nucleos_puntos"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_nucleos_puntos"."id","eiel_c_nucleos_puntos"."id_municipio","eiel_c_nucleos_puntos"."codprov","eiel_c_nucleos_puntos"."codmunic","eiel_c_nucleos_puntos"."codentidad","eiel_c_nucleos_puntos"."nombre_oficial","eiel_c_nucleos_puntos"."codpoblamiento","eiel_c_nucleos_puntos"."fecha_alta","eiel_c_nucleos_puntos"."observ","eiel_c_nucleos_puntos"."fecha_revision","eiel_c_nucleos_puntos"."estado_revision" FROM "eiel_c_nucleos_puntos" WHERE "eiel_c_nucleos_puntos"."id_municipio" in (?M) AND "eiel_c_nucleos_puntos"."codprov"<>''AN''
','UPDATE "eiel_c_nucleo_poblacion" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?6,"codpoblamiento"=?7,"nombre_oficial"=?8,"fecha_alta"=?9,"observ"=?10,"fecha_revision"=?11,"estado_revision"=?12 WHERE "id"=?2
','INSERT INTO "eiel_c_nucleo_poblacion" ("GEOMETRY","id","id_municipio","codprov","codmunic","codentidad","codpoblamiento","nombre_oficial","fecha_alta","observ","fecha_revision","estado_revision") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12)
','DELETE FROM "eiel_c_nucleo_poblacion" WHERE "id"=?2');
  

--Layer Parcelas EIEL	
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>parcelas_eiel</Name><UserStyle><Name>parcelas_eiel:_:EIEL: parcelas_eiel</Name><Title>parcelas_eiel:_:EIEL: parcelas_eiel</Title><Abstract>parcelas_eiel:_:EIEL: parcelas_eiel</Abstract><FeatureTypeStyle><Name>parcelas_eiel</Name><Rule><Name>Parcelas urbanas</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.25</CssParameter><CssParameter name=''fill''>#ffffcc</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#333300</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parcelas EIEL');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parcelas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Parcelas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Parcelas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Parcelas EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Parcelas EIEL');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'parcelas_eiel',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'parcelas_eiel:_:EIEL: parcelas_eiel',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_parcelas',5);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmunic',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE entidad');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE entidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),87,'codentidad',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),6,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),87,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código catastral manzana');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código INE manzana');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código INE manzana');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código INE manzana');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código INE manzana');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codmasa',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),7,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código catastral parcela');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código catastral parcela');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código catastral parcela');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código catastral parcela');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código catastral parcela');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'codparcela',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),8,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);	
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Letra asociada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Letra asociada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Letra asociada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Letra asociada');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Letra asociada');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10000,'num_pol_dup',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),9,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10000,0);	


INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Número de policia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Número de policia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Número de policia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Número de policia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Número de policia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10037,'num_policia',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),10,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10037,0);	
		
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Observaciones');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Observaciones');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10014,'observ',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),11,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10014,0);
	
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','Codi Ordre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','Código Orden');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','Código Orden');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),86,'orden_infr',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),12,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),86,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Clave');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Clave');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10001,'clave_infr',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),13,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10001,0);	

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre Oficial');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre Oficial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),99,'nombre_oficial',100,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),14,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),99,0);	

INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_parcelas"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_parcelas"."id","eiel_c_parcelas"."id_municipio","eiel_c_parcelas"."codprov","eiel_c_parcelas"."codmunic","eiel_c_parcelas"."codentidad","eiel_c_parcelas"."codmasa","eiel_c_parcelas"."codparcela","eiel_c_parcelas"."num_pol_dup","eiel_c_parcelas"."num_policia","eiel_c_parcelas"."observ","eiel_c_parcelas"."orden_infr","eiel_c_parcelas"."clave_infr","eiel_c_parcelas"."nombre_oficial" FROM "eiel_c_parcelas" WHERE "eiel_c_parcelas"."id_municipio" in (?M)
','UPDATE "eiel_c_parcelas" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M,1,2),"codmunic"=substr(?M,3,5),"codentidad"=?6,"codmasa"=?7,"codparcela"=?8,"num_pol_dup"=?9,"num_policia"=?10,"observ"=?11,"orden_infr"=?12,"clave_infr"=?13,"nombre_oficial"=?14 WHERE "id"=?2
','INSERT INTO "eiel_c_parcelas" ("GEOMETRY","id","id_municipio","codprov","codmunic","codentidad","codmasa","codparcela","num_pol_dup","num_policia","observ","orden_infr","clave_infr","nombre_oficial") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M,1,2),substr(?M,3,5),?6,?7,?8,?9,?10,?11,?12,?13,?14)
','DELETE FROM "eiel_c_parcelas" WHERE "id"=?2
');

  
--Layer Provincias EIEL
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>Provincia</Name><UserStyle><Name>Provincia:_:default:Provincia</Name><Title>Provincia:_:default:Provincia</Title><Abstract>Provincia:_:default:Provincia</Abstract><FeatureTypeStyle><Name>Provincia</Name><Rule><Name>Provincia (Desde 0 hasta 250.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>250000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Provincia (Desde 250.000 hasta 500.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>250000.0</MinScaleDenominator><MaxScaleDenominator>500000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Nombre prov. (Desde 250.000 hasta 500.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>250000.0</MinScaleDenominator><MaxScaleDenominator>500000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>11.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Provincia (Desde 500.000 hasta 800.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>500000.0</MinScaleDenominator><MaxScaleDenominator>800000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#669900</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Nombre prov. (Desde 500.000 hasta 800.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>500000.0</MinScaleDenominator><MaxScaleDenominator>800000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>8.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Provincia (Desde 800.000 hasta 1.100.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>800000.0</MinScaleDenominator><MaxScaleDenominator>1100000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#669900</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Nombre prov. (Desde 800.000 hasta 1.100.000)</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>800000.0</MinScaleDenominator><MaxScaleDenominator>1100000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>6.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>-10.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>Provincia:_:Obras_Provincia</Name><Title>Provincia:_:Obras_Provincia</Title><Abstract>Provincia:_:Obras_Provincia</Abstract><FeatureTypeStyle><Rule><Name>Limite provincia</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>700000.0</MinScaleDenominator><MaxScaleDenominator>4000000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.4</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>700000.0</MinScaleDenominator><MaxScaleDenominator>4000000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>11.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.5</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Limite provincia</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>4000000.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>0.4</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>Provincia:_:Obras_comarca</Name><Title>Provincia:_:Obras_comarca</Title><Abstract>Provincia:_:Obras_comarca</Abstract><FeatureTypeStyle><Rule><Name>Nombre</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>700000.0</MinScaleDenominator><MaxScaleDenominator>4000000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Nombre</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>11.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.5</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Provincias EIEL');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Provincias EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Provincias EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Provincias EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Provincias EIEL');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Provincias EIEL');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,currval('seq_acl'),'Provincia',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES (50,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'Provincia:_:default:Provincia',true,0,0,true,true);		
		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'eiel_c_provincia',5);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Código provincia');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Código provincia');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),85,'codprov',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),3,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),85,0);

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10008,'nombre',2,null,null,3);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),4,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10008,0);		

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]IdMunicip');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]IdMunicip');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);
		
		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("eiel_c_provincia"."GEOMETRY" , ?T) as "GEOMETRY","eiel_c_provincia"."id","eiel_c_provincia"."id" as id,"eiel_c_provincia"."codprov","eiel_c_provincia"."nombre","eiel_c_provincia"."id_municipio" FROM "eiel_c_provincia" WHERE "eiel_c_provincia"."id_municipio" in (?M)
','UPDATE "eiel_c_provincia" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"codprov"=?3,"nombre"=?4,"id_municipio"=?M WHERE "id"=?2
','INSERT INTO "eiel_c_provincia" ("GEOMETRY","id","codprov","nombre","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?M)
','DELETE FROM "eiel_c_provincia" WHERE "id"=?2');
  




		
		
