ALTER TABLE persona_juridico_fisica ADD COLUMN id_municipio numeric(5,0) DEFAULT 0;
INSERT INTO query_catalog(id, query, acl, idperm) VALUES('buscartipovia','SELECT tipovianormalizadocatastro, nombrecatastro FROM vias WHERE id=?',1,9205);

update dictionary set traduccion='ID de municipio' where id_vocablo=12004 and locale='es_ES';
update dictionary set traduccion='ID de municipi' where id_vocablo=12004 and locale='ca_ES';
update dictionary set traduccion='ID de municipio' where id_vocablo=12004 and locale='gl_ES';
update dictionary set traduccion='ID de municipi' where id_vocablo=12004 and locale='va_ES';

update query_catalog set query='SELECT DISTINCT parcelas.id,parcelas.referencia_catastral FROM parcelas, persona WHERE parcelas.referencia_catastral=persona.parcela_catastral and upper(persona.razon_social) like upper(?)' WHERE id='parcetituname';
update query_catalog set query='SELECT DISTINCT parcelas.id,parcelas.referencia_catastral FROM parcelas, persona WHERE parcelas.referencia_catastral=persona.parcela_catastral and upper(persona.razon_social) like upper(?)' WHERE id='parcesujename';
update query_catalog set query='SELECT DISTINCT parcelas.id,parcelas.referencia_catastral FROM parcelas, persona WHERE parcelas.referencia_catastral=persona.parcela_catastral and upper(persona.nif)like upper(?)' WHERE id='parcetitunif';
update query_catalog set query='SELECT DISTINCT parcelas.id,parcelas.referencia_catastral FROM parcelas, persona WHERE parcelas.referencia_catastral=persona.parcela_catastral and upper(persona.nif)like upper(?)' WHERE id='parcesujenif';

update queries set updatequery='update actividad_contaminante set "GEOMETRY"=(transform(GeometryFromText(text(?6),?S), ?T)), id_tipo_actividad=?2,id_razon_estudio=?3, num_administrativo=?4, asunto=?5 where actividad_contaminante.ID=?1' where id_layer=6666;