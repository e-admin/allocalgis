--Creo la capa de aceras
INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>aceraspmr</Name><UserStyle><Name>aceraspmr:_:default:aceraspmr</Name><Title>aceraspmr:_:default:aceraspmr</Title><Abstract>aceraspmr:_:default:aceraspmr</Abstract><FeatureTypeStyle><Name>aceraspmr</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>

');

INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','aceraspmr');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]aceraspmr');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]aceraspmr');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]aceraspmr');
INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]aceraspmr');

INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
		VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(select acl from layers where name='vias'),'aceraspmr',currval('seq_styles'),null,1,0,1);

INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),(select l.id_layerfamily from layerfamilies l,dictionary d  where l.id_name=d.id_vocablo and traduccion='Callejero'), 1);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
	VALUES ((select id_map from maps where "xml" like '%<mapName>Callejero</mapName>%' limit 1),(select l.id_layerfamily from layerfamilies l,dictionary d  where l.id_name=d.id_vocablo and traduccion='Callejero'),currval('seq_layers'),currval('seq_styles'),'aceraspmr:_:default:aceraspmr',true,0,0,true,true);		
		
CREATE TABLE aceraspmr
(
  id numeric(8,0) NOT NULL,
  "GEOMETRY" geometry,
  id_municipio numeric(5,0) NOT NULL,
  CONSTRAINT aceraspmr_pkey PRIMARY KEY (id),
  CONSTRAINT "$1" FOREIGN KEY (id_municipio)
      REFERENCES municipios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "$3" CHECK (geometrytype("GEOMETRY") = 'LINESTRING'::text OR "GEOMETRY" IS NULL)
);		
INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'aceraspmr',3);

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

INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Municipio');
INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);
		
		
INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
VALUES 
  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("aceraspmr"."GEOMETRY", ?T) AS "GEOMETRY","aceraspmr"."id","aceraspmr"."id_municipio" FROM "aceraspmr" WHERE "aceraspmr"."id_municipio" IN (?M)
','UPDATE "aceraspmr" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M WHERE "id"=?2
','INSERT INTO "aceraspmr" ("GEOMETRY","id","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M)
','DELETE FROM "aceraspmr" WHERE "id"=?2');

--Creo la tabla con los tipos de minusvalia


CREATE TABLE tipos_minusvalia (
    id integer NOT NULL,
    descripcion character varying(40),
    anchura_min_acera numeric(6,0),
    max_pendiente_transversal numeric(5,2),
    max_pendiente_longitudinal numeric(5,2),
    pavimento_irregular integer
);

INSERT INTO tipos_minusvalia (id, descripcion, anchura_min_acera, max_pendiente_transversal, max_pendiente_longitudinal, pavimento_irregular) VALUES (1, 'Restricción visual total', 100, 2.00, 6.00, 1);
INSERT INTO tipos_minusvalia (id, descripcion, anchura_min_acera, max_pendiente_transversal, max_pendiente_longitudinal, pavimento_irregular) VALUES (2, 'Restricción visual parcial', 100, 2.00, 6.00, 0);
INSERT INTO tipos_minusvalia (id, descripcion, anchura_min_acera, max_pendiente_transversal, max_pendiente_longitudinal, pavimento_irregular) VALUES (3, 'Utilización de silla de ruedas', 100, 4.00, 1.00, 1);



ALTER TABLE ONLY tipos_minusvalia
    ADD CONSTRAINT pk_tipos_minusvalia PRIMARY KEY (id);



insert into query_catalog (id,query) values('obtenerTiposMinusvalia','select * from tipos_minusvalia');

CREATE SEQUENCE seq_aceraspmr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE INDEX aceraspmr_spat_idx
  ON aceraspmr
  USING gist
  ("GEOMETRY");  
  
-- Annado nuevos campos para guardar las propiedades de las aceras
alter table network_street_properties add column irregular_pavement integer;
alter table network_street_properties add column transversal_slope numeric(6,2);
alter table network_street_properties add column longitudinal_slope numeric(6,2);
alter table network_street_properties add column width numeric(6,2);  